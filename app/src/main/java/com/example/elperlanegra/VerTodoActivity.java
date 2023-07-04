package com.example.elperlanegra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.elperlanegra.adaptadores.VerTodoAdapter;
import com.example.elperlanegra.modelos.VerTodoModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VerTodoActivity extends AppCompatActivity {

    FirebaseFirestore firestore;

    RecyclerView verTodorec;
    VerTodoAdapter verTodoAdapter;
    List<VerTodoModel> verTodoModelList;

    ProgressBar progressBar;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_todo);

        Toolbar toolbar = findViewById(R.id.tb_vertodo);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.pb_vertodo);
        progressBar.setVisibility(View.VISIBLE);

        firestore = FirebaseFirestore.getInstance();

        String type = getIntent().getStringExtra("tipo");
        //String type = String.valueOf(getIntent().getStringArrayListExtra("tipo"));
        verTodorec = findViewById(R.id.vertodo_rec);
        verTodorec.setVisibility(View.GONE);
        verTodorec.setLayoutManager(new LinearLayoutManager(this));

        verTodoModelList = new ArrayList<>();
        verTodoAdapter = new VerTodoAdapter(this, verTodoModelList);
        verTodorec.setAdapter(verTodoAdapter);

        ///MENU DIARIO PRODUCTS////
        /////////Desayunos//////////////
        if (type.equalsIgnoreCase("desayuno")){
            firestore.collection("AllProducts").whereEqualTo("tipo", "desayuno").get().addOnCompleteListener(task -> {
                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                    VerTodoModel verTodoModel = documentSnapshot.toObject(VerTodoModel.class);
                    verTodoModelList.add(verTodoModel);
                    verTodoAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    verTodorec.setVisibility(View.VISIBLE);
                }
            });
        }

        /////////Almuerzos//////////////
        if (type.equalsIgnoreCase("almuerzo")){
            firestore.collection("AllProducts").whereEqualTo("tipo", "almuerzo").get().addOnCompleteListener(task -> {
                for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()) {
                    VerTodoModel verTodoModel = documentSnapshot.toObject(VerTodoModel.class);
                    verTodoModelList.add(verTodoModel);
                    verTodoAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    verTodorec.setVisibility(View.VISIBLE);
                }
            });
        }

        ///////CATEGORÍAS///////
        /////////Bebidas//////////////
        if (type.equalsIgnoreCase("bebida")){
            firestore.collection("AllProducts").whereEqualTo("tipo", "bebida").get().addOnCompleteListener(task -> {
                for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()) {
                    VerTodoModel verTodoModel = documentSnapshot.toObject(VerTodoModel.class);
                    verTodoModelList.add(verTodoModel);
                    verTodoAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    verTodorec.setVisibility(View.VISIBLE);
                }
            });
        }

        /////////Mariscos//////////////
        if (type.equalsIgnoreCase("marisco")){
            firestore.collection("AllProducts").whereEqualTo("tipo", "marisco").get().addOnCompleteListener(task -> {
                for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()) {
                    VerTodoModel verTodoModel = documentSnapshot.toObject(VerTodoModel.class);
                    verTodoModelList.add(verTodoModel);
                    verTodoAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    verTodorec.setVisibility(View.VISIBLE);
                }
            });
        }

        /////////Mariscos Frescos//////////////
        if (type.equalsIgnoreCase("fresco")){
            firestore.collection("AllProducts").whereEqualTo("tipo", "fresco").get().addOnCompleteListener(task -> {
                for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()) {
                    VerTodoModel verTodoModel = documentSnapshot.toObject(VerTodoModel.class);
                    verTodoModelList.add(verTodoModel);
                    verTodoAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    verTodorec.setVisibility(View.VISIBLE);
                }
            });
        }

        /////////Picadas//////////////
        if (type.equalsIgnoreCase("picada")){
            firestore.collection("AllProducts").whereEqualTo("tipo", "picada").get().addOnCompleteListener(task -> {
                for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()) {
                    VerTodoModel verTodoModel = documentSnapshot.toObject(VerTodoModel.class);
                    verTodoModelList.add(verTodoModel);
                    verTodoAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    verTodorec.setVisibility(View.VISIBLE);
                }
            });
        }

        /////////Parrilladas//////////////
        if (type.equalsIgnoreCase("parrillada")){
            firestore.collection("AllProducts").whereEqualTo("tipo", "parrillada").get().addOnCompleteListener(task -> {
                for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()) {
                    VerTodoModel verTodoModel = documentSnapshot.toObject(VerTodoModel.class);
                    verTodoModelList.add(verTodoModel);
                    verTodoAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    verTodorec.setVisibility(View.VISIBLE);
                }
            });
        }

        /////////Hamburguesas//////////////
        if (type.equalsIgnoreCase("hamburguesa")){
            firestore.collection("AllProducts").whereEqualTo("tipo", "hamburguesa").get().addOnCompleteListener(task -> {
                for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()) {
                    VerTodoModel verTodoModel = documentSnapshot.toObject(VerTodoModel.class);
                    verTodoModelList.add(verTodoModel);
                    verTodoAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    verTodorec.setVisibility(View.VISIBLE);
                }
            });
        }

        ////////ESPECIALIDADES////////
        /////////Especialidades//////////////
        if (type.equalsIgnoreCase("especialidad")){
            firestore.collection("AllProducts").whereEqualTo("tipo", "especialidad").get().addOnCompleteListener(task -> {
                for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()) {
                    VerTodoModel verTodoModel = documentSnapshot.toObject(VerTodoModel.class);
                    verTodoModelList.add(verTodoModel);
                    verTodoAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    verTodorec.setVisibility(View.VISIBLE);
                }
            });
        }

        /////////Económicos//////////////
        if (type.equalsIgnoreCase("economico")){
            firestore.collection("AllProducts").whereEqualTo("tipo", "economico").get().addOnCompleteListener(task -> {
                for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()) {
                    VerTodoModel verTodoModel = documentSnapshot.toObject(VerTodoModel.class);
                    verTodoModelList.add(verTodoModel);
                    verTodoAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    verTodorec.setVisibility(View.VISIBLE);
                }
            });
        }


    }
}