package com.example.elperlanegra.ui.carrito;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elperlanegra.OrdenActivity;
import com.example.elperlanegra.R;
import com.example.elperlanegra.adaptadores.CarritoAdapter;
import com.example.elperlanegra.modelos.CarritoModel;
import com.example.elperlanegra.modelos.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class CarritoFragment extends Fragment {

    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    DatabaseReference dbUsuarios;

    RecyclerView rv_carrito;
    ConstraintLayout empty;
    CarritoAdapter carritoAdapter;
    List<CarritoModel> carritoModelList;
    TextView overTotalAmount;
    Button payNow;
    UserModel modelUser;
    ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_carrito, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        progressBar = root.findViewById(R.id.pb_carrito);
        progressBar.setVisibility(View.VISIBLE);

        /*currentUser = auth.getCurrentUser();
        dbUsuarios = FirebaseDatabase.getInstance().getReference().child("Users");*/

        rv_carrito = root.findViewById(R.id.rv_carrito);
        rv_carrito.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_carrito.setVisibility(View.GONE);

        empty = root.findViewById(R.id.constraint1);

        payNow = root.findViewById(R.id.pay_btn);

        overTotalAmount = root.findViewById(R.id.totalamount);

        /*LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(mMessageReceiver, new IntentFilter("MiPrecioTotal"));*/

        carritoModelList = new ArrayList<>();
        //carritoAdapter = new CarritoAdapter(getActivity(), carritoModelList);
        CarritoAdapter carritoAdapter = new CarritoAdapter(getActivity(), carritoModelList, this);

        rv_carrito.setAdapter(carritoAdapter);

        db.collection("Carrito").document(auth.getCurrentUser().getUid())
                .collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {

                                String documentId = documentSnapshot.getId();

                                CarritoModel carritoModel = documentSnapshot.toObject(CarritoModel.class);

                                carritoModel.setDocumentId(documentId);
                                carritoModelList.add(carritoModel);
                                carritoAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                rv_carrito.setVisibility(View.VISIBLE);
                            }

                            calcularMontoTotal(carritoModelList);
                        }
                    }
                });


        progressBar.setVisibility(View.GONE);

        ///////PARA MOSTRAR EL CONSTRAINT LAYOUT CUANDO NO HAY ITEMS
        carritoAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (carritoAdapter.getItemCount() == 0) {
                    empty.setVisibility(View.VISIBLE);
                    payNow.setEnabled(false);

                } else {
                    empty.setVisibility(View.GONE);
                    payNow.setEnabled(true);
                    payNow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intentO = new Intent(getContext(), OrdenActivity.class);
                            intentO.putExtra("itemList", (Serializable) carritoModelList);
                            startActivity(intentO);
                        }
                    });
                }
            }
        });

        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!payNow.isEnabled()) {
                    Toast.makeText(getContext(), "El botón está deshabilitado", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }

    @SuppressLint("SetTextI18n")
    public void calcularMontoTotal(List<CarritoModel> carritoModelList) {

        double montoTotal = 0.0;
        for(CarritoModel carritoModel : carritoModelList){
            montoTotal += carritoModel.getPrecioTotal();
        }
        //overTotalAmount.setText("Monto Total: " + montoTotal);
        @SuppressLint("DefaultLocale") String formattedTotalAmount = String.format("%.2f", montoTotal);
        overTotalAmount.setText("Monto Total: " + formattedTotalAmount);
    }


}