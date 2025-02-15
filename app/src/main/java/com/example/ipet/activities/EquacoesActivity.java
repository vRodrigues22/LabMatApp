package com.example.ipet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ipet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class EquacoesActivity extends AppCompatActivity {

    private Button btnRegistrar;
    private EditText editEquacao, editResposta, editDica;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    //private FirebaseReference equacoesRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equacoes);

        // Inicialização dos componentes
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnRegistrar = findViewById(R.id.button);
        editEquacao = findViewById(R.id.textView6);
        editResposta = findViewById(R.id.textView14);
        editDica = findViewById(R.id.textView15);

        // Configuração do botão de registrar
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarEquacao();
            }
        });
    }

    private void registrarEquacao() {
        final String equacao = editEquacao.getText().toString();
        final String resposta = editResposta.getText().toString();
        final String dica = editDica.getText().toString();

        // Verifique se os campos não estão vazios
        if (equacao.isEmpty() || resposta.isEmpty() || dica.isEmpty()) {
            Toast.makeText(EquacoesActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Criar um novo documento para registrar a equação, resposta e dica no Firestore
        Map<String, Object> equacaoData = new HashMap<>();
        equacaoData.put("equacao", equacao);
        equacaoData.put("resposta", resposta);
        equacaoData.put("dica", dica);

        // Adicionar a equação no Firestore
        db.collection("equacoes")
                .add(equacaoData) // Adiciona os dados da equação
                .addOnCompleteListener(EquacoesActivity.this, new OnCompleteListener<   DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            // Documento adicionado com sucesso
                            Toast.makeText(EquacoesActivity.this, "Equação registrada com sucesso", Toast.LENGTH_SHORT).show();
                            // Redirecionar para a próxima atividade ou tela
                            Intent intent = new Intent(EquacoesActivity.this, ClientesActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Falha ao adicionar a equação
                            Toast.makeText(EquacoesActivity.this, "Erro ao registrar equação", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}