package com.example.ipet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ipet.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EquacoesActivity extends AppCompatActivity {

    private Button btnRegistrar, btnVisualizar;
    private EditText editEquacao, editResposta, editDica;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equacoes);

        // Inicializa os componentes
        db = FirebaseFirestore.getInstance(); // Inicializa a instância do Firestore
        btnRegistrar = findViewById(R.id.button);
        btnVisualizar = findViewById(R.id.button3);
        editEquacao = findViewById(R.id.textView6);
        editResposta = findViewById(R.id.textView14);
        editDica = findViewById(R.id.textView15);

        // Configura o botão para registrar a equação
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarEquacao(); // Chama o metodo que salva a equação
            }
        });

        // Configura o botão para visualizar as equações
        btnVisualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visualizarEquacoes(); // Chama o metodo que exibe as equações cadastradas
            }
        });
    }

    // Metodo para registrar as equações no Firestore
    private void registrarEquacao() {
        final String equacao = editEquacao.getText().toString();
        final String resposta = editResposta.getText().toString();
        final String dica = editDica.getText().toString();

        // Verifica se os campos não estão vazios
        if (equacao.isEmpty() || resposta.isEmpty() || dica.isEmpty()) {
            Toast.makeText(EquacoesActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cria um Map para armazenar os dados da equação
        Map<String, Object> equacaoData = new HashMap<>();
        equacaoData.put("equacao", equacao);
        equacaoData.put("resposta", resposta);
        equacaoData.put("dica", dica);

        // Adiciona os dados da equação na coleção "equacoes"
        db.collection("equacoes")
                .add(equacaoData) // Adiciona um novo documento com os dados da equação
                .addOnCompleteListener(EquacoesActivity.this, task -> {
                    if (task.isSuccessful()) {
                        // Sucesso ao adicionar a equação
                        Toast.makeText(EquacoesActivity.this, "Equação registrada com sucesso", Toast.LENGTH_SHORT).show();
                    } else {
                        // Falha ao adicionar a equação
                        Toast.makeText(EquacoesActivity.this, "Erro ao registrar equação", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Metodo para visualizar as equações cadastradas
    private void visualizarEquacoes() {
        Intent intent = new Intent(EquacoesActivity.this, EquacaoviewActivity.class);
        startActivity(intent);
    }
}
