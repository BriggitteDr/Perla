package com.example.elperlanegra.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elperlanegra.R;
import com.example.elperlanegra.adaptadores.CategoryAdapter;
import com.example.elperlanegra.adaptadores.EspecialidadesAdapter;
import com.example.elperlanegra.adaptadores.MenuDiarioAdapter;
import com.example.elperlanegra.adaptadores.VerTodoAdapter;
import com.example.elperlanegra.databinding.FragmentHomeBinding;
import com.example.elperlanegra.modelos.CategoryModel;
import com.example.elperlanegra.modelos.EspecialidadesModel;
import com.example.elperlanegra.modelos.MenuDiarioModel;
import com.example.elperlanegra.modelos.VerTodoModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    ScrollView scrollView;
    ProgressBar home_pb;

    //////SEARCH BAR VIEW/////////
    EditText search_box;
    private List<VerTodoModel> verTodoModelList;
    private RecyclerView rv_search;
    private VerTodoAdapter verTodoAdapter;

    //RECYCLERVIEW
    RecyclerView menudiarioRec, categoryRec, especialidadesRec;

    //FIREBASE
    FirebaseFirestore db;

    //POPULAR ITEMS
    List<MenuDiarioModel> menuDiarioModelList;
    MenuDiarioAdapter menuDiarioAdapter;

    //CATEGORY ITEMS
    List<CategoryModel> categoryModelList;
    CategoryAdapter categoryAdapter;

    //DESAYUNO ITEMS
    List<EspecialidadesModel> especialidadesModelList;
    EspecialidadesAdapter especialidadesAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        db = FirebaseFirestore.getInstance();
        menudiarioRec = root.findViewById(R.id.pop_rec);
        categoryRec = root.findViewById(R.id.cat_rec);
        especialidadesRec = root.findViewById(R.id.desayuno_rec);
        scrollView = root.findViewById(R.id.sv_home1);
        home_pb = root.findViewById(R.id.home_pb);

        home_pb.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        //Menú Diario items
        menudiarioRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        menuDiarioModelList = new ArrayList<>();
        menuDiarioAdapter = new MenuDiarioAdapter(getActivity(), menuDiarioModelList);
        menudiarioRec.setAdapter(menuDiarioAdapter);
        db.collection("MenuDiario")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                MenuDiarioModel menuDiarioModel = document.toObject(MenuDiarioModel.class);
                                menuDiarioModelList.add(menuDiarioModel);
                                menuDiarioAdapter.notifyDataSetChanged();

                                home_pb.setVisibility(View.GONE);
                                scrollView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //Category items
        categoryRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        categoryModelList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getActivity(), categoryModelList);
        categoryRec.setAdapter(categoryAdapter);

        db.collection("Categorias")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CategoryModel categoryModel = document.toObject(CategoryModel.class);
                                categoryModelList.add(categoryModel);
                                categoryAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //Especialidades items
        especialidadesRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        especialidadesModelList = new ArrayList<>();
        especialidadesAdapter = new EspecialidadesAdapter(getActivity(), especialidadesModelList);
        especialidadesRec.setAdapter(especialidadesAdapter);

        db.collection("Especialidades")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                EspecialidadesModel especialidadesModel = document.toObject(EspecialidadesModel.class);
                                especialidadesModelList.add(especialidadesModel);
                                especialidadesAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //////SEARCH BAR VIEW/////////
        rv_search = root.findViewById(R.id.search_bar_rv);
        search_box = root.findViewById(R.id.search_box);
        verTodoModelList = new ArrayList<>();
        verTodoAdapter = new VerTodoAdapter(getContext(), verTodoModelList);
        rv_search.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_search.setAdapter(verTodoAdapter);
        rv_search.setHasFixedSize(true);
        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().isEmpty()){
                    verTodoModelList.clear();
                    verTodoAdapter.notifyDataSetChanged();
                } else {
                    searchProduct(s.toString());
                    //String searchText = s.toString().toLowerCase();
                    //searchProduct(searchText);
                }
            }
        });
        return root;
    }

    private void searchProduct(String name) {
        if (!name.isEmpty()){
            String searchText = name.toLowerCase(); // convertirtodo a minúsculas
            db.collection("AllProducts").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null){
                                verTodoModelList.clear();
                                verTodoAdapter.notifyDataSetChanged();
                                for (DocumentSnapshot doc : task.getResult().getDocuments()){
                                    String nombre = doc.getString("nombre"); // obtener el campo "nombre" del documento
                                    if (nombre != null && nombre.toLowerCase().contains(searchText)) { // buscar coincidencias
                                        VerTodoModel verTodoModel = doc.toObject(VerTodoModel.class);
                                        verTodoModelList.add(verTodoModel);
                                        verTodoAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    });
        }
    }

}