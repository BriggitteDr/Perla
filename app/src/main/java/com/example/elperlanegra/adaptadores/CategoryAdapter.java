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
import com.example.elperlanegra.R;
import com.example.elperlanegra.VerTodoActivity;
import com.example.elperlanegra.modelos.CategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Context context;
    List<CategoryModel> categoryModelList;

    public CategoryAdapter(Context context, List<CategoryModel> categoryModelList) {
        this.context = context;
        this.categoryModelList = categoryModelList;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {

        Glide.with(context).load(categoryModelList.get(position).getImg_url()).into(holder.catImg);
        holder.nombre.setText(categoryModelList.get(position).getNombre());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPP = new Intent(context, VerTodoActivity.class);
                intentPP.putExtra("tipo", categoryModelList.get(position).getTipo());
                context.startActivity(intentPP);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView catImg;
        TextView nombre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            catImg = itemView.findViewById(R.id.categ_img);
            nombre = itemView.findViewById(R.id.categ_name);
        }
    }
}
