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

    private TextView txtPontuacao, txtContasResolvidas, txtTempo1, txtTempo2, txtTempo3;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progresso);

        txtPontuacao = findViewById(R.id.txtpontuacao);
        txtContasResolvidas = findViewById(R.id.txtconta);
        txtTempo1 = findViewById(R.id.txttempo1);
        txtTempo2 = findViewById(R.id.txttempo2);
        txtTempo3 = findViewById(R.id.txttempo3);

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
                        // Obtém a pontuação, contas resolvidas e tempos das etapas
                        long pontuacao = document.getLong("pontuacao") != null ? document.getLong("pontuacao") : 0;
                        long contasResolvidas = document.getLong("contasResolvidas") != null ? document.getLong("contasResolvidas") : 0;
                        long tempo1 = document.getLong("tempo1") != null ? document.getLong("tempo1") : 0;
                        long tempo2 = document.getLong("tempo2") != null ? document.getLong("tempo2") : 0;
                        long tempo3 = document.getLong("tempo3") != null ? document.getLong("tempo3") : 0;

                        // Atualiza os TextViews com os valores
                        txtPontuacao.setText("Pontuação: " + pontuacao);
                        txtContasResolvidas.setText("Contas Resolvidas: " + contasResolvidas);
                        txtTempo1.setText("Tempo do Nível 1: " + tempo1 + "s");
                        txtTempo2.setText("Tempo do Nível 2: " + tempo2 + "s");
                        txtTempo3.setText("Tempo do Nível 3: " + tempo3 + "s");
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
