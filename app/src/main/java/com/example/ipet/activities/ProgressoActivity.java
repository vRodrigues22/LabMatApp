package com.example.ipet.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ipet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProgressoActivity extends AppCompatActivity {

    private TextView txtPontuacao, txtContasResolvidas;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progresso);

        txtPontuacao = findViewById(R.id.txtpontuação);
        txtContasResolvidas = findViewById(R.id.txtconta);

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        carregarDadosProgresso();
    }

    private void carregarDadosProgresso() {
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DocumentReference userRef = db.collection("usuarios").document(userId);
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Obtém a pontuação e contas resolvidas do documento
                        long pontuacao = document.getLong("pontuacao").longValue();
                        long contasResolvidas = document.getLong("contasResolvidas").longValue(); // Substitua "contasResolvidas" pelo nome do campo no Firestore

                        // Atualiza os TextViews com os valores
                        txtPontuacao.setText("Pontuação: " + pontuacao);
                        txtContasResolvidas.setText("Contas Resolvidas: " + contasResolvidas);
                    } else {
                        Toast.makeText(ProgressoActivity.this, "Usuário não encontrado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProgressoActivity.this, "Erro ao carregar dados de progresso", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}