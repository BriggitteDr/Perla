package com.example.elperlanegra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.elperlanegra.modelos.VerTodoModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class DetallesActivity extends AppCompatActivity {

    ImageView detailedImg, addItem, removeItem, carritoImg;
    TextView nombre, precio, descripcion, rating, cantidad;

    Button addTocart;

    int cantTotal = 1;
    double totalPrice = 0;

    Toolbar toolbarDetalles;
    VerTodoModel verTodoModel = null;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        toolbarDetalles = findViewById(R.id.tb_detalles);
        setSupportActionBar(toolbarDetalles);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        final Object objeto = getIntent().getSerializableExtra("detalle");
        if (objeto instanceof VerTodoModel){
            verTodoModel = (VerTodoModel) objeto;
        }

        detailedImg = findViewById(R.id.detallesImg);

        addItem = findViewById(R.id.add_item);
        removeItem = findViewById(R.id.quitar_item);
        cantidad = findViewById(R.id.number_item);

        precio = findViewById(R.id.precio_detalles);
        descripcion = findViewById(R.id.descrip_detalles);
        rating = findViewById(R.id.rating_detalles);
        nombre = findViewById(R.id.nombre_detalle);
        carritoImg = findViewById(R.id.carritoImag);



        if (verTodoModel != null){
            Glide.with(getApplicationContext()).load(verTodoModel.getImg_url()).into(detailedImg);
            nombre.setText(verTodoModel.getNombre());
            rating.setText(verTodoModel.getRating());
            descripcion.setText(verTodoModel.getDescripcion());
            precio.setText("$"+ (double) verTodoModel.getPrecio() + "");

            totalPrice = verTodoModel.getPrecio() * cantTotal;

        }

        addTocart = findViewById(R.id.addtocart_bt);


        /////////PARA BOTÓN AGREGAR AL CARRITO DE DETALLES//////////////
        addTocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addedToCart();
            }
        });

        //////PARA BOTONES + Y - DE DETALLES///////////
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cantTotal < 10){
                    cantTotal++;
                    cantidad.setText(String.valueOf(cantTotal));
                    totalPrice = verTodoModel.getPrecio() * cantTotal;
                }
            }
        });

        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cantTotal > 1){
                    cantTotal--;
                    cantidad.setText(String.valueOf(cantTotal));
                    totalPrice = verTodoModel.getPrecio() * cantTotal;
                }
            }
        });
    }

    ////////MÉTODO PARA AGREGAR AL CARRITO/////////
    private void addedToCart() {
        String fecha;
        Calendar calendarFecha = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat fechaActual = new SimpleDateFormat("dd/MM/yyyy");
        fecha = fechaActual.format(calendarFecha.getTime());

        /*SimpleDateFormat horaActual = new SimpleDateFormat("hh:mm:ss a");
        hora = horaActual.format(calendarFecha.getTime());*/ ///no sale la hora de Ecuador
        ///Si sale la hora de Ecuador
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        TimeZone timeZone = TimeZone.getTimeZone("America/Guayaquil");
        dateFormat.setTimeZone(timeZone);
        String hora = dateFormat.format(new Date());

        final HashMap<String, Object> cartMap = new HashMap<>();

        cartMap.put("nombre", verTodoModel.getNombre());
        cartMap.put("precio", precio.getText().toString());
        cartMap.put("fecha", fecha);
        cartMap.put("hora", hora);
        cartMap.put("cantidad", cantidad.getText().toString());
        cartMap.put("precioTotal", totalPrice);
        cartMap.put("img", verTodoModel.getImg_url());

        firestore.collection("Carrito").document(auth.getCurrentUser().getUid())
                .collection("Cart").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetallesActivity.this, "AGREGADO AL CARRITO", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                });
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