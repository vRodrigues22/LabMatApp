package com.example.ipet.activities;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ipet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DailyActivity extends AppCompatActivity {

    private TextView equationText, scoreText, hintText;
    private EditText answerInput;
    private Button btnResponder, btnDica;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference documentReference;
    private ArrayList<String> equations;
    private ArrayList<Integer> answers;
    private ArrayList<String> hints;
    private int currentScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        equationText = findViewById(R.id.equationTextDaily);
        scoreText = findViewById(R.id.scoreTextDaily);
        hintText = findViewById(R.id.hintDaily);
        answerInput = findViewById(R.id.answerInputDaily);
        btnResponder = findViewById(R.id.calcbtnDaily);
        btnDica = findViewById(R.id.hintDaily);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            documentReference = db.collection("usuarios").document(userId);

            // Carregar dados do Firestore
            carregarDadosUsuario();

            // Configurar as equações diárias
            configurarEquacoes();

            // Exibir equação do dia
            updateQuestion();

            // Botão de responder
            btnResponder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verificarResposta();
                }
            });

            // Botão de dica
            btnDica.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exibirDica();
                }
            });
        } else {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
        }
    }

    private void carregarDadosUsuario() {
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Long pontos = document.getLong("pontuacao");
                        currentScore = (pontos != null) ? pontos.intValue() : 0;
                        scoreText.setText("Pontuação: " + currentScore);
                    } else {
                        Toast.makeText(DailyActivity.this, "Dados do usuário não encontrados", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DailyActivity.this, "Erro ao carregar dados", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void configurarEquacoes() {
        equations = new ArrayList<>();
        answers = new ArrayList<>();
        hints = new ArrayList<>();

        equations.add("Equação 1: 108 / 9 + 5²");
        answers.add(37);
        hints.add("Dica: Resolva a potência primeiro, depois a divisão e soma.");

        equations.add("Equação 3: 12³");
        answers.add(1728);
        hints.add("Dica: 12 elevado a 3 é 12 multiplicado por si mesmo três vezes.");

        equations.add("Equação 4: 90 ÷ 3");
        answers.add(30);
        hints.add("Dica: Simples divisão.");

        equations.add("Equação 5: 3x + 9 = 15");
        answers.add(2);
        hints.add("Dica: Resolva para x isolando a variável.");
    }

    private void updateQuestion() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // Começa em domingo como 0
        if (dayOfWeek >= 0 && dayOfWeek < equations.size()) {
            equationText.setText(equations.get(dayOfWeek));
            btnResponder.setEnabled(true);
            btnResponder.setText("Responder");
        } else {
            equationText.setText("Nenhum desafio para hoje.");
            btnResponder.setEnabled(false);
        }
    }

    private void verificarResposta() {
        String respostaStr = answerInput.getText().toString();
        if (!respostaStr.isEmpty()) {
            int resposta = Integer.parseInt(respostaStr);
            Calendar calendar = Calendar.getInstance();
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

            if (resposta == answers.get(dayOfWeek)) {
                // Incrementa a pontuação
                currentScore++;
                scoreText.setText("Pontuação: " + currentScore);
                atualizarPontuacaoFirestore();

                // Desativa o botão após resposta correta
                btnResponder.setText("Respondido");
                btnResponder.setEnabled(false);
                Toast.makeText(this, "Resposta correta!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Resposta incorreta. Tente novamente!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Por favor, insira uma resposta.", Toast.LENGTH_SHORT).show();
        }
    }

    private void exibirDica() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek >= 0 && dayOfWeek < hints.size()) {
            Toast.makeText(this, hints.get(dayOfWeek), Toast.LENGTH_LONG).show();
        }
    }

    private void atualizarPontuacaoFirestore() {
        Map<String, Object> dadosAtualizados = new HashMap<>();
        dadosAtualizados.put("pontuacao", currentScore);

        documentReference.update(dadosAtualizados).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(DailyActivity.this, "Erro ao atualizar pontuação", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
