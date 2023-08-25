package com.alejandrafajardo.apilocal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button listar, Adicionar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listar=findViewById(R.id.listar);
        Adicionar=findViewById(R.id.Adicionar);
        listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intento=new Intent(MainActivity.this,Activity_listar.class);
                startActivity(intento);
            }
        });

        Adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intento=new Intent(MainActivity.this,FormularioActivity.class);
                startActivity(intento);
            }
        });
    }
}