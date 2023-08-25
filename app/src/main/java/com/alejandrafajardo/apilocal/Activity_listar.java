package com.alejandrafajardo.apilocal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alejandrafajardo.apilocal.Adaptador.Adaptador_Productos;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import cz.msebera.android.httpclient.Header;

public class Activity_listar extends AppCompatActivity {
    RecyclerView contenedor;
    Button volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);
        volver=findViewById(R.id.volver);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intento=new Intent(Activity_listar.this,MainActivity.class);
                startActivity(intento);
            }
        });
        contenedor=findViewById(R.id.contenedor);
        contenedor.setLayoutManager(new LinearLayoutManager(this));

        //Conectarse a la base de datos
        AsyncHttpClient httpClient=new AsyncHttpClient();
        httpClient.get(UrlServidor.UrlListar, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String respuesta=new String(responseBody);
                try {
                    cargarDatos(statusCode, respuesta);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(Activity_listar.this, "onSucces, SI hay conexion", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    cargarDatos(statusCode, "Error de conexion");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(Activity_listar.this, "onFailure, NO hay conexion", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void cargarDatos(int statusCode, String respuesta) throws JSONException {
        if (statusCode == 200) {
            // Si hay conexion
            // Log.e("datos", respuesta.toString());
            //JSONObject miJsonObj= new JSONObject(respuesta);
            JSONArray miJsonArray=new JSONArray(respuesta);

            // Ahora coloco los datos en el Listview, recycler
            // Crear el constructor del adaptador en un nuevo java class y creo el adaptador aqui
            Adaptador_Productos miAdaptador= new Adaptador_Productos(this,miJsonArray);
            contenedor.setAdapter(miAdaptador);
        }else{
            Toast.makeText(this, ""+respuesta, Toast.LENGTH_SHORT).show();
        }
    }

}