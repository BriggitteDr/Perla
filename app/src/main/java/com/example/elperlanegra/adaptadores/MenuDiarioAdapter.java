package com.example.elperlanegra.adaptadores;

import android.annotation.SuppressLint;
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
import com.example.elperlanegra.R;
import com.example.elperlanegra.VerTodoActivity;
import com.example.elperlanegra.modelos.MenuDiarioModel;

import java.util.List;

public class MenuDiarioAdapter extends RecyclerView.Adapter<MenuDiarioAdapter.ViewHolder> {

    private Context context;
    private List<MenuDiarioModel> menuDiarioModelList;

    public MenuDiarioAdapter(Context context, List<MenuDiarioModel> menuDiarioModelList) {
        this.context = context;
        this.menuDiarioModelList = menuDiarioModelList;
    }

    @NonNull
    @Override
    public MenuDiarioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menudiario_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MenuDiarioAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(context).load(menuDiarioModelList.get(position).getImg_url()).into(holder.popImg);
        holder.nombre.setText(menuDiarioModelList.get(position).getNombre());
        holder.descripcion.setText(menuDiarioModelList.get(position).getDescripcion());
        holder.rating.setText(menuDiarioModelList.get(position).getRating());
        holder.promo.setText(menuDiarioModelList.get(position).getPromo());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPP = new Intent(context, VerTodoActivity.class);
                intentPP.putExtra("tipo", menuDiarioModelList.get(position).getTipo());
                context.startActivity(intentPP);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuDiarioModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView popImg;
        TextView nombre, descripcion, rating, promo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            popImg = itemView.findViewById(R.id.men_img);
            nombre = itemView.findViewById(R.id.men_name);
            descripcion = itemView.findViewById(R.id.men_desc);
            rating = itemView.findViewById(R.id.men_rating);
            promo = itemView.findViewById(R.id.men_promo);
        }
    }
}
