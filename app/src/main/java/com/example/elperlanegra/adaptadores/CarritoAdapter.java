package com.example.elperlanegra.adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.elperlanegra.R;
import com.example.elperlanegra.modelos.CarritoModel;
import com.example.elperlanegra.ui.carrito.CarritoFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolder> {

    Context context;
    List<CarritoModel> carritoModelList;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    double precioT = 0;
    private CarritoFragment carritoFragment;

    public CarritoAdapter(Context context, List<CarritoModel> carritoModelList, CarritoFragment carritoFragment) {
        this.context = context;
        this.carritoModelList = carritoModelList;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        this.carritoFragment = carritoFragment;
    }

    @NonNull
    @Override
    public CarritoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.carrito_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(context).load(carritoModelList.get(position).getImg()).into(holder.carritoProd);

        holder.nombre.setText(carritoModelList.get(position).getNombre());
        holder.precio.setText(carritoModelList.get(position).getPrecio());
        holder.fecha.setText(carritoModelList.get(position).getFecha());
        holder.hora.setText(carritoModelList.get(position).getHora());
        holder.cantidad.setText(carritoModelList.get(position).getCantidad());
        holder.precioTotal.setText(carritoModelList.get(position).getPrecioTotal() + "");

        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("Carrito").document(auth.getCurrentUser().getUid())
                        .collection("Cart")
                        .document(carritoModelList.get(position).getDocumentId())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    carritoModelList.remove(carritoModelList.get(position));
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "¡¡ÍTEM ELIMINADO!!", Toast.LENGTH_SHORT).show();
                                    carritoFragment.calcularMontoTotal(carritoModelList);

                                } else {
                                    Toast.makeText(context, "ERROR: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        /*////Mostrar precio total en el fragmento Carrito/////
        precioT = precioT + carritoModelList.get(position).getPrecioTotal();
        Intent intentPrecio = new Intent("MiPrecioTotal");
        intentPrecio.putExtra("montoTotal", precioT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intentPrecio);*/



    }



    @Override
    public int getItemCount() {
        return carritoModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, precio, fecha, hora, cantidad, precioTotal;
        ImageView carritoProd, deleteItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre_prodcarrito);
            precio = itemView.findViewById(R.id.precio_prodcarrito);
            fecha = itemView.findViewById(R.id.fecha_prodcarrito);
            hora = itemView.findViewById(R.id.hora_prodcarrito);
            cantidad = itemView.findViewById(R.id.cant_prodcarrito);
            precioTotal = itemView.findViewById(R.id.total_prodcarrito);
            carritoProd = itemView.findViewById(R.id.carritoImag);
            deleteItem = itemView.findViewById(R.id.delete_item);
        }
    }
}
