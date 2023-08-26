package com.alejandrafajardo.apilocal;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FormularioActivity extends AppCompatActivity {
    Button Foto,enviar;
    ImageView imageView;
    EditText Nombre, Codigo, Precio, desc;
    String directorioFoto;
    Bitmap bitmap;
    Spinner spnMarca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        Foto=findViewById(R.id.Foto);
        imageView=findViewById(R.id.imageView);
        enviar=findViewById(R.id.enviar);
        Nombre=findViewById(R.id.Nombre);
        Codigo=findViewById(R.id.codigo);
        Precio=findViewById(R.id.Precio);
        desc=findViewById(R.id.desc);
        spnMarca=findViewById(R.id.spnMarca);

        Foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCamara();
            }
        });

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarDatos();
            }
        });
        consultarMaras();

    }
    private void consultarMaras(){
        AsyncHttpClient httpClient=new AsyncHttpClient();
        httpClient.get("http://192.168.172.1/tienda/wservicio/marcas/listar.php", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String respuesta=new String(responseBody);
                try {
                    JSONArray jsonArray= new JSONArray(respuesta);
                    List listaMarcas= new ArrayList<>();
                    for (int i=0;i<jsonArray.length();i++){
                        Marca miMarca=new Marca();
                        miMarca.setId(Integer.parseInt(jsonArray.getJSONObject(i).get("id").toString()));
                        miMarca.setNombre((String)jsonArray.getJSONObject(i).get("nombre"));
                        listaMarcas.add(miMarca);
                    }
                    ArrayAdapter arrayAdapter=new ArrayAdapter<>(FormularioActivity.this, android.R.layout.simple_dropdown_item_1line, listaMarcas);
                    spnMarca.setAdapter(arrayAdapter);
                }catch (JSONException e){
                    Toast.makeText(FormularioActivity.this, "Error en  el JSON", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(FormularioActivity.this, "Fallo en la conexion", Toast.LENGTH_SHORT).show();
            }
            });
        }

        private void enviarDatos(){
        AsyncHttpClient httpClient=new AsyncHttpClient();
        //capturar datps ára enviar al servidor
            RequestParams datosEnviar= new RequestParams();
            datosEnviar.put("codigo",Codigo.getText().toString());
            datosEnviar.put("nombre",Nombre.getText().toString());
            datosEnviar.put("vrUnitario",Precio.getText().toString());
            datosEnviar.put("descrpcion",desc.getText().toString());
            datosEnviar.put("idMarca",spnMarca.getSelectedItemId());
            //Imagen
            String imagen=imagenAstring(bitmap);
            datosEnviar.put("imagen",imagen);

            httpClient.post("http://192.168.172.1/tienda/wservicio/insertar.php", datosEnviar, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Toast.makeText(FormularioActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(FormularioActivity.this, "Fallo de conexion", Toast.LENGTH_SHORT).show();
                }
            });
        }
        private String imagenAstring(Bitmap bitmap){
            ByteArrayOutputStream arrayOutputStream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, arrayOutputStream);
            byte[] imgByte=arrayOutputStream.toByteArray();
            String imgString= Base64.encodeToString(imgByte,Base64.DEFAULT);
            return imgString;
        }

    private void abrirCamara() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imgFile = null;
        try {
            imgFile = crearImagen();
        }catch (IOException e){
            Log.e("Error File ", e.getMessage());
            Toast.makeText(this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if (imgFile != null){
            Uri uri = FileProvider.getUriForFile(this, "com.alejandrafajardo.apilocal", imgFile);
            i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            camaraLauncher.launch(i);
        }
    }
    ActivityResultLauncher<Intent> camaraLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            //validar con requestsCode y resultCode
            //Bundle bundle=result.getData().getExtras();
            //Bitmap miImagen=(Bitmap) bundle.get("data");
            bitmap= BitmapFactory.decodeFile(directorioFoto);
            imageView.setImageBitmap(bitmap);
        }
    });
    //metodo para crear file
    public File crearImagen() throws IOException {
        File dirAlmacena=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String imgFileFecha= new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgNameFile="JPG_"+imgFileFecha;
        File imagen=File.createTempFile(imgNameFile,".jpg", dirAlmacena);
        directorioFoto=imagen.getAbsolutePath();

        return imagen;
    }
}