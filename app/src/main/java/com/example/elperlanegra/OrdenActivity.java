package com.example.elperlanegra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.elperlanegra.modelos.CarritoModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class OrdenActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore firestore;

    Toolbar toolbarOrden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orden);

        toolbarOrden = findViewById(R.id.tb_orden);
        setSupportActionBar(toolbarOrden);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        List<CarritoModel> list = (ArrayList<CarritoModel>) getIntent().getSerializableExtra("itemList");

        if (list != null &&list.size() > 0){
            String idPedido = (String) generateRandomId(6);
            for (CarritoModel model : list) {
                final HashMap<String, Object> cartMap = new HashMap<>();


                cartMap.put("idPedido", idPedido);
                cartMap.put("nombre", model.getNombre());
                cartMap.put("precio", model.getPrecio());
                cartMap.put("fecha", model.getFecha());
                //cartMap.put("hora", model.getHora());
                cartMap.put("cantidad", model.getCantidad());
                cartMap.put("precioTotal", model.getPrecioTotal());
                cartMap.put("img", model.getImg());

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String horaActual = dateFormat.format(calendar.getTime());

                cartMap.put("hora", horaActual);


                firestore.collection("Pedidos").document(auth.getCurrentUser().getUid())
                        .collection("Ordenes").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    String pedidoId = task.getResult().getId();

                                    firestore.collection("Pedidos")
                                            .document(auth.getCurrentUser().getUid())
                                            .collection("Ordenes")
                                            .document(pedidoId)
                                            .update("idPedido", idPedido)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(OrdenActivity.this, "PEDIDO REALIZADO CON ÉXITO", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(OrdenActivity.this, "Error al asignar ID al pedido", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(OrdenActivity.this, "Error al guardar el pedido", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }

    private Object generateRandomId(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        String characters = "0123456789";

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
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