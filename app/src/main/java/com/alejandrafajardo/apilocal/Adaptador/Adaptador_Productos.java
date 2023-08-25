package com.alejandrafajardo.apilocal.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alejandrafajardo.apilocal.R;
import com.alejandrafajardo.apilocal.UrlServidor;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

public class Adaptador_Productos extends RecyclerView.Adapter<Adaptador_Productos.ViewHolder> {

    JSONArray jsonArray;
    Context context;
    public Adaptador_Productos(Context context, JSONArray jsonArray){
        this.jsonArray=jsonArray; this.context=context;
    }
    @NonNull
    @Override
    public Adaptador_Productos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Colocar los datos del Json en cada elemento o vista
        try {
            holder.Id.setText(jsonArray.getJSONObject(position).getString("id"));
            holder.Nombre.setText(jsonArray.getJSONObject(position).getString("nombre"));
            holder.vrUnitario.setText(jsonArray.getJSONObject(position).getString("vrUnitario"));

            //holder.Descripcion.setText(jsonArray.getJSONObject(position).getString("descripcion"));
            try {
                String descripcion = jsonArray.getJSONObject(position).getString("descripcion");
                if (descripcion != null) {
                    holder.Descripcion.setText(descripcion);
                } else {
                    // Manejar el caso en el que la descripción es nula
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Manejar la excepción de JSON en caso de que ocurra un error
            }

            // Imagen
            //holder.ivImagen.setImageResource(R.mipmap.ic_launcher);
            String url= UrlServidor.UrlResProdu +jsonArray.getJSONObject(position).get("imagen").toString();
            Picasso.with(context).load(url).into(holder.Imagen);
        } catch (JSONException e) {
            // Error porque no se pudo obtener el Json
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Atributos
        TextView Id, Nombre, vrUnitario, Descripcion;
        ImageView Imagen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Relacion de los atributos con la vista
            Id=itemView.findViewById(R.id.Id);
            Nombre=itemView.findViewById(R.id.Nombre);
            vrUnitario=itemView.findViewById(R.id.vrUnitario);
            Imagen=itemView.findViewById(R.id.Imagen);
            Descripcion=itemView.findViewById(R.id.Descripcion);
        }
    }
}
