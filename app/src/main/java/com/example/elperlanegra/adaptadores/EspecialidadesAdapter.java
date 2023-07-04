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
import com.example.elperlanegra.modelos.EspecialidadesModel;

import java.util.List;

public class EspecialidadesAdapter extends RecyclerView.Adapter<EspecialidadesAdapter.ViewHolder> {

        Context context;
        List<EspecialidadesModel> desayunoModelList;

public EspecialidadesAdapter(Context context, List<EspecialidadesModel> desayunoModelList) {
        this.context = context;
        this.desayunoModelList = desayunoModelList;
        }

@NonNull
@Override
public EspecialidadesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.especialidades_item, parent, false));
        }

@Override
public void onBindViewHolder(@NonNull EspecialidadesAdapter.ViewHolder holder, int position) {

        Glide.with(context).load(desayunoModelList.get(position).getImg_url()).into(holder.desayunoImg);
        holder.nombre.setText(desayunoModelList.get(position).getNombre());
        holder.descripcion.setText(desayunoModelList.get(position).getDescripcion());
        holder.rating.setText(desayunoModelList.get(position).getRating());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Intent intentPP = new Intent(context, VerTodoActivity.class);
        intentPP.putExtra("tipo", desayunoModelList.get(position).getTipo());
        context.startActivity(intentPP);
        }
        });
        }

@Override
public int getItemCount() {
        return desayunoModelList.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder {

    ImageView desayunoImg;
    TextView nombre, descripcion, rating;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        desayunoImg = itemView.findViewById(R.id.esp_img);
        nombre = itemView.findViewById(R.id.nombre_esp);
        descripcion = itemView.findViewById(R.id.esp_desc);
        rating = itemView.findViewById(R.id.esp_rating);
    }
}
}
