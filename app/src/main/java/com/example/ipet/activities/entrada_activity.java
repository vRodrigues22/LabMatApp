package com.example.ipet.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ipet.R;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

public class entrada_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableEdgeToEdge();
        setContentView(R.layout.activity_entrada);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent telaTemp = new Intent(entrada_activity.this, ClientesActivity.class);

                startActivity(telaTemp);
                finish();
            }
        }, 3000); // Atraso de 3000 milissegundos (3 segundos)
    }

    private void enableEdgeToEdge() {
    }
}

