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

        // Habilita o modo Edge to Edge (você precisará implementar ou chamar essa função)
        enableEdgeToEdge();

        // Define o layout da activity
        setContentView(R.layout.activity_entrada);

        // Usando Handler para executar a intenção após 3 segundos
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Cria a intenção para iniciar MainActivityHome
                Intent telaTemp = new Intent(entrada_activity.this, ClientesActivity.class);

                // Inicia a nova Activity
                startActivity(telaTemp);

                // Finaliza a Activity atual
                finish();
            }
        }, 3000); // Atraso de 3000 milissegundos (3 segundos)
    }

    // Método para habilitar Edge to Edge (você deve implementá-lo)
    private void enableEdgeToEdge() {
        // Implementação para habilitar o modo Edge to Edge
    }
}

