package com.example.ipet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ipet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Labirinto2Activity extends AppCompatActivity {

    private TextView equationText, scoreText, hintText, timeText;
    private EditText answerInput;
    private Button submitButton, hintButton;
    private int currentEquationIndex = 0;
    private int score = 0;
    private int currentPlayerPosition = 0;
    private static final int TOTAL_POSITIONS = 25;
    private static final int ADVANCE_STEPS = 3;
    private List<String> equations = new ArrayList<>();
    private List<String> hints = new ArrayList<>();
    private List<Integer> answers = new ArrayList<>(); // respostas corretas para validação
    private FirebaseFirestore db;
    private FirebaseUser user;

    private long startTime = 0;  // Armazena o tempo inicial
    private long elapsedTime = 0; // Armazena o tempo já decorrido
    private CountDownTimer countDownTimer;  // Cronômetro

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labirinto2);

        // Inicializa os componentes da UI
        equationText = findViewById(R.id.equationText);
        scoreText = findViewById(R.id.scoreText);
        hintText = findViewById(R.id.hintText);
        timeText = findViewById(R.id.timeText);  // Inicializa o TextView de tempo
        answerInput = findViewById(R.id.answerInput);
        submitButton = findViewById(R.id.calcbtn);
        hintButton = findViewById(R.id.hintButton);

        // Inicializa Firestore e o usuário autenticado
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        // Preenche as listas de equações, respostas e dicas
        equations.add("Equação : 25 * 2 + 4³");
        answers.add(89);
        hints.add("Dica: Resolva a potência primeiro, depois a multiplicação e a adição.");

        equations.add("Equação :144 ÷ 12");
        answers.add(12);
        hints.add("Dica: Divisão simples.");

        equations.add("Equação : 7x + 15 = 50");
        answers.add(5);
        hints.add("Dica: Isole x.");

        equations.add("Equação :  3 * (15 + 5)");
        answers.add(60);
        hints.add("Dica: Resolva o parêntese primeiro, depois multiplique.");

        equations.add("Equação : 64 ÷ 8 + 4 * 3");
        answers.add(28);
        hints.add("Dica: Siga a ordem: divisão, multiplicação e, por fim, adição.");

        // Recupera o tempo da fase anterior
        getPreviousTime();

        updateQuestion();

        updatePlayerPosition(currentPlayerPosition, R.drawable.square_player);

        // Evento de clique no botão de submissão (resposta)
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userAnswer = answerInput.getText().toString();
                if (userAnswer.isEmpty()) {
                    Toast.makeText(Labirinto2Activity.this, "Por favor, insira uma resposta", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verificar resposta
                int answer = Integer.parseInt(userAnswer);
                if (answer == answers.get(currentEquationIndex)) {
                    score += 10; // Incrementa a pontuação
                    Toast.makeText(Labirinto2Activity.this, "Correto! Pontuação: " + score, Toast.LENGTH_SHORT).show();
                    movePlayer();
                    currentEquationIndex++;
                    if (currentEquationIndex < equations.size()) {
                        updateQuestion();
                    } else {
                        // Atualiza o scoreText antes de salvar
                        scoreText.setText("Pontuação: " + score);
                        saveScoreToFirestore();
                        Toast.makeText(Labirinto2Activity.this, "Você completou a Etapa 2! Pontuação total: " + score, Toast.LENGTH_LONG).show();
                        // Redireciona para o Labirinto 3 automaticamente
                        redirectToLabirinto3();
                    }
                } else {
                    Toast.makeText(Labirinto2Activity.this, "Resposta incorreta, tente novamente", Toast.LENGTH_SHORT).show();
                }

                answerInput.setText("");
            }
        });

        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintText.setText(hints.get(currentEquationIndex));
            }
        });
    }


    private void startTimer() {
        startTime = System.currentTimeMillis() - elapsedTime;
        countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
                timeText.setText("Tempo: " + elapsedTime + "s");
            }

            @Override
            public void onFinish() {
                // Ação quando o cronômetro acabar
            }
        };
        countDownTimer.start();
    }

    private void getPreviousTime() {
        db.collection("usuarios").document(user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists() && document.contains("tempo1")) {
                            elapsedTime = document.getLong("tempo1");
                        }
                        startTimer();
                    } else {
                        startTimer();
                    }
                });
    }

    private void saveScoreToFirestore() {
        Map<String, Object> userData = new HashMap<>();
        userData.put("pontuacao", score);
        userData.put("tempo2", elapsedTime);  // Salva o tempo da fase 2

        db.collection("usuarios").document(user.getUid())
                .update(userData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(Labirinto2Activity.this, "Pontuação e Tempo salvos com sucesso!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Labirinto2Activity.this, "Erro ao salvar dados", Toast.LENGTH_SHORT).show();
                });
    }

    private void movePlayer() {
        if (currentPlayerPosition >= TOTAL_POSITIONS - 1) {
            Toast.makeText(this, "Parabéns! Você completou o labirinto!", Toast.LENGTH_LONG).show();
            saveScoreToFirestore();
            return;
        }

        updatePlayerPosition(currentPlayerPosition, R.drawable.cell_image);

        int row = currentPlayerPosition / 5;
        int nextPosition;

        if (row % 2 == 0) {
            nextPosition = currentPlayerPosition + ADVANCE_STEPS;
        } else {
            nextPosition = currentPlayerPosition - ADVANCE_STEPS;
        }

        if ((row % 2 == 0 && nextPosition % 5 == 0) || (row % 2 != 0 && nextPosition < row * 5)) {
            nextPosition = currentPlayerPosition + (5 - (currentPlayerPosition % 5));
        }

        if (nextPosition >= TOTAL_POSITIONS) {
            nextPosition = TOTAL_POSITIONS - 1;
        } else if (nextPosition < 0) {
            nextPosition = 0;
        }

        currentPlayerPosition = nextPosition;
        updatePlayerPosition(currentPlayerPosition, R.drawable.square_player);
    }

    private void updatePlayerPosition(int position, int drawableId) {
        int row = position / 5;
        int col = position % 5;

        if (row % 2 != 0) {
            col = 4 - col;
        }

        String cellId = "mazePosition" + (row * 5 + col);
        int resID = getResources().getIdentifier(cellId, "id", getPackageName());

        ImageView cell = findViewById(resID);
        if (cell != null) {
            cell.setImageResource(drawableId);
        }
    }

    private void updateQuestion() {
        equationText.setText(equations.get(currentEquationIndex));
        hintText.setText("");
        scoreText.setText("Pontuação: " + score);
    }

    // Função para redirecionar para a terceira fase (Labirinto3Activity)
    private void redirectToLabirinto3() {
        // Chama a próxima fase automaticamente após completar o labirinto
        startActivity(new Intent(Labirinto2Activity.this, Labirinto3Activity.class));
        finish();  // Finaliza a atividade atual para não retornar ao nível 2
    }
}
