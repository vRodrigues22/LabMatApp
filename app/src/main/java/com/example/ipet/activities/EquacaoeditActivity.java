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

public class EquacaoeditActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText editEquacao, editResposta, editDica;
    private Button btnSave, btnDelete;
    private String equacaoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equacaoedit);

        // Inicializa os componentes
        db = FirebaseFirestore.getInstance();
        editEquacao = findViewById(R.id.editEquacao);
        editResposta = findViewById(R.id.editResposta);
        editDica = findViewById(R.id.editDica);
        btnSave = findViewById(R.id.btnSave);

        // Recebe o ID da equação da Intent
        equacaoId = getIntent().getStringExtra("equacao_id");

        // Carregar a equação para edição
        carregarEquacao();

        // Configura o clique no botão "Salvar"
        btnSave.setOnClickListener(v -> {
            salvarEquacao();
        });

    }

    private void carregarEquacao() {
        db.collection("equacoes").document(equacaoId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String equacao = documentSnapshot.getString("equacao");
                        String resposta = documentSnapshot.getString("resposta");
                        String dica = documentSnapshot.getString("dica");

                        // Preenche os campos com os dados da equação
                        editEquacao.setText(equacao);
                        editResposta.setText(resposta);
                        editDica.setText(dica);
                    } else {
                        Toast.makeText(EquacaoeditActivity.this, "Equação não encontrada", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EquacaoeditActivity.this, "Erro ao carregar a equação", Toast.LENGTH_SHORT).show();
                });
    }

    private void salvarEquacao() {
        String novaEquacao = editEquacao.getText().toString();
        String novaResposta = editResposta.getText().toString();
        String novaDica = editDica.getText().toString();

        if (novaEquacao.isEmpty() || novaResposta.isEmpty() || novaDica.isEmpty()) {
            Toast.makeText(EquacaoeditActivity.this, "Preencha todos os campos para salvar!", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("equacoes").document(equacaoId)
                .update("equacao", novaEquacao, "resposta", novaResposta, "dica", novaDica)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EquacaoeditActivity.this, "Equação atualizada com sucesso", Toast.LENGTH_SHORT).show();
                        finish();  // Fecha a tela de edição
                    } else {
                        Toast.makeText(EquacaoeditActivity.this, "Erro ao atualizar equação", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
