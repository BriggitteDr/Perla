package com.example.elperlanegra;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.elperlanegra.adaptadores.DetallesPedidoAdapter;
import com.example.elperlanegra.modelos.DetallesPedidoModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DetallePedidoActivity extends AppCompatActivity {

    Toolbar detalles;

    FirebaseFirestore firestore;

    RecyclerView detallespedidos_rec;
    DetallesPedidoAdapter detallesPedidoAdapter;

    TextView overTotalAmount;

    List<DetallesPedidoModel> listaProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pedido);

        Toolbar detalles = findViewById(R.id.tb_detalles_pedido);
        setSupportActionBar(detalles);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        firestore = FirebaseFirestore.getInstance();
        overTotalAmount = findViewById(R.id.montototal_detalles);

        String idPedido = getIntent().getStringExtra("pedidoId");
        detallespedidos_rec = findViewById(R.id.detallepedido_rec);
        detallespedidos_rec.setLayoutManager(new LinearLayoutManager(this));

        listaProductos = new ArrayList<>();
        detallesPedidoAdapter = new DetallesPedidoAdapter(this, listaProductos);
        detallespedidos_rec.setAdapter(detallesPedidoAdapter);

        firestore.collection("Pedidos")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Ordenes")
                .whereEqualTo("idPedido", idPedido)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            DetallesPedidoModel detallesPedidoModel = documentSnapshot.toObject(DetallesPedidoModel.class);
                            listaProductos.add(detallesPedidoModel);
                        }
                        detallesPedidoAdapter.notifyDataSetChanged();
                        calcularMontoTotal(listaProductos);
                    } else {
                        Log.e("DetallePedidoActivity", "Error al obtener los productos del pedido", task.getException());
                    }
                });
    }

    public void calcularMontoTotal(List<DetallesPedidoModel> detallesPedidoModels) {
        double montoTotal = 0.0;
        for(DetallesPedidoModel detallesPedidoModel : detallesPedidoModels){
            montoTotal += detallesPedidoModel.getPrecioTotal();
        }
        //overTotalAmount.setText("Monto Total: " + montoTotal);
        String formattedTotalAmount = String.format("%.2f", montoTotal);
        overTotalAmount.setText(formattedTotalAmount);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}