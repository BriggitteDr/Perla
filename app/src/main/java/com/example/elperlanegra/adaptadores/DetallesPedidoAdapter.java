package com.example.elperlanegra.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elperlanegra.DetallePedidoActivity;
import com.example.elperlanegra.R;
import com.example.elperlanegra.modelos.DetallesPedidoModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class DetallesPedidoAdapter extends RecyclerView.Adapter<DetallesPedidoAdapter.ViewHolder>{

    Context context;
    List<DetallesPedidoModel> detallesPedidoModelList;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    double precioT = 0;

    DetallePedidoActivity detallePedidoActivity;

    public DetallesPedidoAdapter(Context context, List<DetallesPedidoModel> detallesPedidoModelList) {
        this.context = context;
        this.detallesPedidoModelList = detallesPedidoModelList;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        this.detallePedidoActivity = detallePedidoActivity;
    }

    @NonNull
    @Override
    public DetallesPedidoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.productlist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DetallesPedidoAdapter.ViewHolder holder, int position) {

        holder.nombre.setText(detallesPedidoModelList.get(position).getNombre());
        holder.pUnit.setText(detallesPedidoModelList.get(position).getPrecio());
        holder.cant.setText(detallesPedidoModelList.get(position).getCantidad());
        double precioTotal = detallesPedidoModelList.get(position).getPrecioTotal();
        String precioTotalConSigno = "$" + precioTotal;
        holder.pTotal.setText(precioTotalConSigno);
    }

    @Override
    public int getItemCount() {
        return detallesPedidoModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, pUnit, cant, pTotal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.nombre_detalles);
            pUnit = itemView.findViewById(R.id.precioUnit_detalles);
            cant = itemView.findViewById(R.id.cant_detalles);
            pTotal = itemView.findViewById(R.id.total_detalles);
        }
    }
}
