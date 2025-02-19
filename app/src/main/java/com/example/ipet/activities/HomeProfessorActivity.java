package com.example.ipet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ipet.R;

public class HomeProfessorActivity extends AppCompatActivity {

    private Button buttonEquacoes, buttonJogo, buttonTurma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_professor);

        // Inicializando os botões
        buttonEquacoes = findViewById(R.id.button2);  // Botão "Equações"
        buttonJogo = findViewById(R.id.button4);      // Botão "Jogo"
        buttonTurma = findViewById(R.id.button);      // Botão "Turma"

        // Configurando o listener de clique para o botão de Equações
        buttonEquacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quando clicado, redireciona para a EquacoesActivity
                Intent intent = new Intent(HomeProfessorActivity.this, EquacoesActivity.class);
                startActivity(intent);
            }
        });

        // Configurando o listener de clique para o botão de Jogo
        buttonJogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quando clicado, redireciona para a MainActivity (Jogo)
                Intent intent = new Intent(HomeProfessorActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Configurando o listener de clique para o botão de Turma
        buttonTurma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quando clicado, redireciona para a TurmaActivity
                Intent intent = new Intent(HomeProfessorActivity.this, TurmaActivity.class);
                startActivity(intent);
            }
        });
    }
}
