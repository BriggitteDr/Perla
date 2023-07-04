package com.example.elperlanegra.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.elperlanegra.DetallesActivity;
import com.example.elperlanegra.R;
import com.example.elperlanegra.modelos.VerTodoModel;

import java.util.List;

public class VerTodoAdapter extends RecyclerView.Adapter<VerTodoAdapter.ViewHolder>{
    Context context;
    List<VerTodoModel> verTodoModelList;

    public VerTodoAdapter(Context context, List<VerTodoModel> verTodoModelList) {
        this.context = context;
        this.verTodoModelList = verTodoModelList;
    }

    @NonNull
    @Override
    public VerTodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.vertodo_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VerTodoAdapter.ViewHolder holder, int position) {

        Glide.with(context).load(verTodoModelList.get(position).getImg_url()).into(holder.productImg);
        holder.nombre.setText(verTodoModelList.get(position).getNombre());
        holder.descripcion.setText(verTodoModelList.get(position).getDescripcion());
        holder.rating.setText(verTodoModelList.get(position).getRating());
        holder.precio.setText(verTodoModelList.get(position).getPrecio()+"");

        if (verTodoModelList.get(position).getTipo().equals("fresco")){
            holder.precio.setText(verTodoModelList.get(position).getPrecio() + "/Lb");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intendD = new Intent(context, DetallesActivity.class);
                intendD.putExtra("detalle", verTodoModelList.get(position));
                context.startActivity(intendD);
            }
        });
    }

    @Override
    public int getItemCount() {
        return verTodoModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImg;
        TextView nombre, rating, descripcion, precio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImg = itemView.findViewById(R.id.viewall_img);
            nombre = itemView.findViewById(R.id.nombre_producto);
            descripcion = itemView.findViewById(R.id.descripcion_producto);
            rating = itemView.findViewById(R.id.rating_producto);
            precio = itemView.findViewById(R.id.precio_producto);
        }
    }
}
