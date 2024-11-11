package com.example.ipet.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ipet.R;


public class MainActivity extends AppCompatActivity {

    private Button buttonNivel1;
    private Button buttonNivel2;
    private Button buttonNivel3;
    private Button perfil;
    private Button daily;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonNivel1 = findViewById(R.id.buttonivel1);
        buttonNivel2 = findViewById(R.id.buttonnivel2);
        buttonNivel3 = findViewById(R.id.buttonnivel3);
        perfil = findViewById(R.id.perfil);
        daily = findViewById(R.id.dailyButton);

        buttonNivel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LabyrinthActivity.class);
                startActivity(intent);
            }
        });

        buttonNivel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Labirinto2Activity.class);
                startActivity(intent);
            }
        });

        buttonNivel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Labirinto2Activity.class);
                startActivity(intent);
            }
        });

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V){
                Intent intent = new Intent(MainActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });

        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V){
                Intent intent = new Intent(MainActivity.this, DailyActivity.class);
                startActivity(intent);
            }
        });
    }
}