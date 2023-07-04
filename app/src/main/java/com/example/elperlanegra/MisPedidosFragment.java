package com.example.elperlanegra;

import android.content.Context;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.elperlanegra.adaptadores.PedidoAdapter;
import com.example.elperlanegra.modelos.PedidoModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MisPedidosFragment extends Fragment {

    RecyclerView rv_order;
    PedidoAdapter pedidoAdapter;
    List<PedidoModel> pedidoModelList;

    //////////FIREBASE//////
    FirebaseFirestore firestore;

    ///////////VARIOS//////////
    ConstraintLayout empty;
    ConstraintLayout nonEmpty;
    ProgressBar progressBar;

    public MisPedidosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_mis_pedidos, container, false);

        ///////PROGRESS BAR
        progressBar = root.findViewById(R.id.pb_pedidos);


        ///RECYCLERVIEW////////
        rv_order = root.findViewById(R.id.rv_order);
        rv_order.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_order.setVisibility(View.GONE);

        Context context = requireContext();
        pedidoModelList = new ArrayList<>();
        pedidoAdapter = new PedidoAdapter(context, pedidoModelList);
        rv_order.setAdapter(pedidoAdapter);

        empty = root.findViewById(R.id.constraint3);
        nonEmpty = root.findViewById(R.id.constraint4);

        //databaseReference = FirebaseDatabase.getInstance().getReference().child("Pedidos");
        firestore = FirebaseFirestore.getInstance();

        firestore.collection("Pedidos")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Ordenes")
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        // Ha ocurrido un error al obtener los datos de Firestore
                        Log.e("MisPedidosFragment", "Error al obtener los pedidos", e);
                        return;
                    }

                    pedidoModelList.clear();


                    if (querySnapshot != null){
                        Map<String, Double> pedidoMap = new HashMap<>();
                        //double montoTotalPedido = 0.0;
                        for (DocumentSnapshot document : querySnapshot.getDocuments()){
                            PedidoModel pedido = document.toObject(PedidoModel.class);

                            String idPedido = pedido.getIdPedido();

                            /////////////
                            double montoTotalPedido = pedidoMap.getOrDefault(idPedido, 0.0);
                            montoTotalPedido += pedido.getPrecioTotal();
                            pedidoMap.put(idPedido, montoTotalPedido);

                            /*pedido.setMontoTotal(String.valueOf(montoTotalPedido));
                            pedidoModelList.add(pedido);*/
                            //pedidoMap.put(idPedido, pedido);

                            //montoTotalPedido += pedido.getPrecioTotal();

                        }


                        for (Map.Entry<String, Double> entry : pedidoMap.entrySet()) {
                            String idPedido = entry.getKey();
                            double montoTotalPedido = entry.getValue();

                            String montoTotalFormateado = String.format("%.2f", montoTotalPedido);

                            PedidoModel pedido = new PedidoModel();
                            pedido.setIdPedido(idPedido);
                            pedido.setMontoTotal(montoTotalFormateado);

                            //String.valueOf(montoTotalPedido)
                            pedido.setEstado("PAGADO");

                            // Recuperar la información de fecha, hora y estado del primer documento con el ID de pedido correspondiente
                            DocumentSnapshot firstDocument = querySnapshot.getDocuments().stream()
                                    .filter(document -> document.getString("idPedido").equals(idPedido))
                                    .findFirst()
                                    .orElse(null);

                            if (firstDocument != null) {
                                pedido.setFecha(firstDocument.getString("fecha"));
                                pedido.setHora(firstDocument.getString("hora"));
                            }
                            pedidoModelList.add(pedido);
                        }

                        //pedidoModelList.addAll(pedidoMap.values());
                        pedidoAdapter.notifyDataSetChanged();

                        /*// Asignar el montoTotal al último pedido
                        if (!pedidoModelList.isEmpty()) {
                            PedidoModel ultimoPedido = pedidoModelList.get(pedidoModelList.size() - 1);
                            ultimoPedido.setMontoTotal(String.valueOf(montoTotalPedido));
                        }*/
                    }

                    if (pedidoModelList.isEmpty()) {
                        empty.setVisibility(View.VISIBLE);
                        nonEmpty.setVisibility(View.GONE);
                        rv_order.setVisibility(View.GONE);
                    } else {
                        empty.setVisibility(View.GONE);
                        nonEmpty.setVisibility(View.VISIBLE);
                        rv_order.setVisibility(View.VISIBLE);
                    }

                    progressBar.setVisibility(View.GONE);

                });
        return root;
    }
}