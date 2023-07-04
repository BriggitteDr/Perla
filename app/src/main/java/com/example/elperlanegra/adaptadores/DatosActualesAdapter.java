package com.example.elperlanegra.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elperlanegra.R;
import com.example.elperlanegra.modelos.UserModel;

import java.util.List;

public class DatosActualesAdapter extends RecyclerView.Adapter<DatosActualesAdapter.ViewHolder>{

    Context context;
    List<UserModel> userModelList;

    public DatosActualesAdapter(Context context, List<UserModel> userModelList) {
        this.context = context;
        this.userModelList = userModelList;
    }

    @NonNull
    @Override
    public DatosActualesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.datosactuales_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DatosActualesAdapter.ViewHolder holder, int position) {

        holder.nombre.setText(userModelList.get(position).getNombreAp());
        holder.direccion.setText(userModelList.get(position).getDireccion());
        holder.correo.setText(userModelList.get(position).getEmail());
        holder.telefono.setText(userModelList.get(position).getTelefono());
    }

    int  numItems = 1;
    @Override
    public int getItemCount() {
        if(userModelList.size()<numItems){
            return userModelList.size();
        }
        return numItems;
        //return userModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nombre,direccion,telefono,correo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.datos_nombre_rec);
            direccion = itemView.findViewById(R.id.datos_direccion_rec);
            correo = itemView.findViewById(R.id.datos_correo_rec);
            telefono = itemView.findViewById(R.id.datos_telefono_rec);
        }
    }
}
