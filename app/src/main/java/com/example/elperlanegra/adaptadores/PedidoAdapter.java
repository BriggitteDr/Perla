package com.example.elperlanegra.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elperlanegra.DetallePedidoActivity;
import com.example.elperlanegra.R;
import com.example.elperlanegra.modelos.PedidoModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>{

    Context context;
    List<PedidoModel> pedidoModelList;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    public PedidoAdapter(Context context, List<PedidoModel> pedidoModelList) {
        this.context = context;
        this.pedidoModelList = pedidoModelList;
    }

    @NonNull
    @Override
    public PedidoAdapter.PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PedidoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pedidos_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoAdapter.PedidoViewHolder holder, int position) {

        holder.id.setText(pedidoModelList.get(position).getIdPedido());
        holder.estado.setText(pedidoModelList.get(position).getEstado());
        holder.fecha.setText(pedidoModelList.get(position).getFecha());
        holder.hora.setText(pedidoModelList.get(position).getHora());
        holder.monto.setText(pedidoModelList.get(position).getMontoTotal());
        holder.detalles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentdetalles = new Intent(context, DetallePedidoActivity.class);
                intentdetalles.putExtra("pedidoId", pedidoModelList.get(position).getIdPedido());
                context.startActivity(intentdetalles);
            }
        });


    }

    @Override
    public int getItemCount() {
        return pedidoModelList.size();
    }

    public class PedidoViewHolder extends RecyclerView.ViewHolder {

        TextView id, fecha, estado, hora, monto, detalles;
        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.pedido_id);
            fecha = itemView.findViewById(R.id.pedido_fecha);
            estado = itemView.findViewById(R.id.pedido_estado);
            hora = itemView.findViewById(R.id.pedido_hora);
            monto = itemView.findViewById(R.id.pedido_monto);
            detalles = itemView.findViewById(R.id.detalles_pedido);
        }
    }
}
