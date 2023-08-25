package com.alejandrafajardo.apilocal;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormularioActivity extends AppCompatActivity {
    Button Foto;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        Foto=findViewById(R.id.Foto);
        imageView=findViewById(R.id.imageView);
        Foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCamara();
            }
        });

    }
    private void abrirCamara(){
        Intent intento=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imgFile=null;
        try{
            imgFile=crearImagen();
        }catch (IOException e){
            Log.e("Error File",e.getMessage());
        }


        camaraLauncher.launch(intento);
    }
    ActivityResultLauncher<Intent> camaraLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            //validar con requestsCode y resultCode
            Bundle bundle=result.getData().getExtras();
            Bitmap miImagen=(Bitmap) bundle.get("data");
            imageView.setImageBitmap(miImagen);
        }
    });
    //metodo para crear file
    public File crearImagen() throws IOException {
        File dirAlmacena=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String imgFileFecha= new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgNameFile="JPG_"+imgFileFecha;
        File imagen=File.createTempFile(imgNameFile,".jpg", dirAlmacena);

        return imagen;
    }
}