package com.example.ipet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ipet.R;


public class MainActivity extends AppCompatActivity {



    private Button buttonClientes;
    private Button buttonParceiros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        buttonClientes = findViewById(R.id.buttonclientes);
        buttonParceiros = findViewById(R.id.button2);

        buttonClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClientesActivity.class);
                startActivity(intent);
            }
        });

        buttonParceiros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Direcionar para a atividade ParceirosActivity
        //        Intent intent = new Intent(MainActivity.this, ParceirosActivity.class);
          //      startActivity(intent);
           }
        });










    }
}