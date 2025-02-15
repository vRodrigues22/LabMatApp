package com.example.ipet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ipet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EquacoesActivity extends AppCompatActivity {

    private Button btnRegistrar, btnVisualizar;
    private EditText editEquacao, editResposta, editDica;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equacoes);

        // Inicialização dos componentes
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnRegistrar = findViewById(R.id.button);
        btnVisualizar = findViewById(R.id.button3); // Botão "Visualizar"
        editEquacao = findViewById(R.id.textView6);
        editResposta = findViewById(R.id.textView14);
        editDica = findViewById(R.id.textView15);

        // Configuração do botão "Registrar"
        btnRegistrar.setOnClickListener(v -> registrarEquacao());

        // Configuração do botão "Visualizar"
        btnVisualizar.setOnClickListener(v -> {
            Intent intent = new Intent(EquacoesActivity.this, EquacaoviewActivity.class);
            startActivity(intent);  // Inicia a EquacaoviewActivity
        });
    }

    // Metodo para registrar a equação
    private void registrarEquacao() {
        final String equacao = editEquacao.getText().toString();
        final String resposta = editResposta.getText().toString();
        final String dica = editDica.getText().toString();

        // Verificar se os campos estão preenchidos
        if (equacao.isEmpty() || resposta.isEmpty() || dica.isEmpty()) {
            Toast.makeText(EquacoesActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Criação de um novo documento no Firestore com os dados da equação
        Map<String, Object> equacaoData = new HashMap<>();
        equacaoData.put("equacao", equacao);
        equacaoData.put("resposta", resposta);
        equacaoData.put("dica", dica);

        // Adicionar a equação no Firestore
        db.collection("equacoes")
                .add(equacaoData)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sucesso ao registrar a equação
                        Toast.makeText(EquacoesActivity.this, "Equação registrada com sucesso", Toast.LENGTH_SHORT).show();
                    } else {
                        // Erro ao registrar a equação
                        Toast.makeText(EquacoesActivity.this, "Erro ao registrar equação", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
