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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LabyrinthActivity extends AppCompatActivity {

    // Elementos da interface (UI)
    private TextView equationText, scoreText, hintText, timeText;  // TextViews para equação, pontuação e dica
    private EditText answerInput;  // Campo para o usuário digitar a resposta
    private Button submitButton, hintButton;  // Botões para enviar a resposta e exibir a dica
    private int currentEquationIndex = 0;  // Índice da equação atual
    private int score = 0;  // Pontuação do jogador
    private int currentPlayerPosition = 0;  // Posição do jogador no labirinto
    private static final int TOTAL_POSITIONS = 25;  // Total de posições no labirinto (5x5)
    private static final int ADVANCE_STEPS = 6;  // Quantos passos o jogador avança a cada acerto
    private List<String> equations = new ArrayList<>();  // Lista das equações
    private List<String> hints = new ArrayList<>();  // Lista das dicas
    private List<Integer> answers = new ArrayList<>();  // Respostas corretas para validação
    private FirebaseFirestore db;  // Instância do Firestore para interagir com o banco de dados
    private FirebaseUser user;  // Usuário atual (obtido a partir do FirebaseAuth)

    private long startTime = 0;  // Armazena o tempo inicial
    private long elapsedTime = 0; // Armazena o tempo já decorrido
    private CountDownTimer countDownTimer;  // Cronômetro

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labyrinth);

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

        // Recupera o tempo da fase anterior
        getPreviousTime();

        // Preenche as listas de equações, respostas e dicas
        equations.add("Equação : 108 / 9 + 5²");
        answers.add(37);
        hints.add("Dica: Resolva a potência primeiro, depois a divisão e soma.");

        equations.add("Equação : 12³");
        answers.add(1728);
        hints.add("Dica: 12 elevado a 3 é 12 multiplicado por si mesmo três vezes.");

        equations.add("Equação : 72 ÷ 8");
        answers.add(9);
        hints.add("Dica: Divida 72 em 8 partes iguais.");

        // Atualiza a primeira questão e a posição do jogador
        updateQuestion();
        updatePlayerPosition(currentPlayerPosition, R.drawable.square_player);

        // Começa a contagem do tempo
        startTimer();

        // Evento de clique no botão de submissão (resposta)
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lê a resposta inserida pelo usuário
                String userAnswer = answerInput.getText().toString();

                // Verifica se a resposta foi preenchida
                if (userAnswer.isEmpty()) {
                    Toast.makeText(LabyrinthActivity.this, "Por favor, insira uma resposta", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verifica se a resposta está correta
                int answer = Integer.parseInt(userAnswer);
                if (answer == answers.get(currentEquationIndex)) {
                    score += 10;  // Incrementa a pontuação
                    Toast.makeText(LabyrinthActivity.this, "Correto! Pontuação: " + score, Toast.LENGTH_SHORT).show();
                    movePlayer();  // Move o jogador para a próxima posição no labirinto
                    currentEquationIndex++;  // Avança para a próxima equação

                    // Incrementa o número de contas resolvidas do usuário no Firestore
                    DocumentReference userRef = db.collection("usuarios").document(user.getUid());
                    userRef.update("contasResolvidas", FieldValue.increment(1))
                            .addOnSuccessListener(aVoid -> {
                                // Sucesso ao atualizar contas resolvidas
                            })
                            .addOnFailureListener(e -> {
                                // Erro ao atualizar contas resolvidas
                                Toast.makeText(LabyrinthActivity.this, "Erro ao salvar progresso", Toast.LENGTH_SHORT).show();
                            });

                    // Se todas as equações foram resolvidas, avança para a próxima fase
                    if (currentEquationIndex < equations.size()) {
                        updateQuestion();  // Atualiza a próxima equação
                    } else {
                        // Exibe a pontuação total ao terminar o jogo
                        scoreText.setText("Pontuação: " + score);

                        // Salva a pontuação do jogador no Firestore
                        saveScoreToFirestore();

                        // Exibe a mensagem de conclusão
                        Toast.makeText(LabyrinthActivity.this, "Você completou a Etapa 1! Pontuação total: " + score, Toast.LENGTH_LONG).show();

                        // Desabilita os botões de interação
                        submitButton.setEnabled(false);
                        hintButton.setEnabled(false);
                        answerInput.setEnabled(false);

                        // Direciona o jogador para a próxima fase
                        startActivity(new Intent(LabyrinthActivity.this, Labirinto2Activity.class));
                        finish();  // Finaliza a atividade atual para não retornar ao nível 1
                    }
                } else {
                    Toast.makeText(LabyrinthActivity.this, "Resposta incorreta, tente novamente", Toast.LENGTH_SHORT).show();
                }

                // Limpa o campo de resposta após a tentativa
                answerInput.setText("");
            }
        });

        // Evento de clique no botão de dica
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintText.setText(hints.get(currentEquationIndex));  // Exibe a dica da equação atual
            }
        });
    }

    // Função para iniciar o cronômetro

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
        userData.put("tempo1", elapsedTime); // Salva o tempo da fase 1

        db.collection("usuarios").document(user.getUid())
                .update(userData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(LabyrinthActivity.this, "Pontuação e Tempo salvos com sucesso!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LabyrinthActivity.this, "Erro ao salvar dados", Toast.LENGTH_SHORT).show();
                });
    }


    // Função para mover o jogador no labirinto
    private void movePlayer() {
        if (currentPlayerPosition >= TOTAL_POSITIONS - 1) {
            // Se o jogador chegar ao final do labirinto, exibe uma mensagem de vitória
            Toast.makeText(this, "Parabéns! Você completou o labirinto!", Toast.LENGTH_LONG).show();
            saveScoreToFirestore();  // Salva a pontuação ao completar o labirinto
            return;
        }

        // Atualiza a posição anterior do jogador com a célula vazia
        updatePlayerPosition(currentPlayerPosition, R.drawable.cell_image);

        int row = currentPlayerPosition / 5;  // Linha atual
        int nextPosition;

        // Define o movimento com base na linha atual (zigue-zague)
        if (row % 2 == 0) {
            nextPosition = currentPlayerPosition + ADVANCE_STEPS;
        } else {
            nextPosition = currentPlayerPosition - ADVANCE_STEPS;
        }

        // Verifica se atingimos o fim da linha e passa para a próxima linha, se necessário
        if ((row % 2 == 0 && nextPosition % 5 == 0) || (row % 2 != 0 && nextPosition < row * 5)) {
            nextPosition = currentPlayerPosition + (5 - (currentPlayerPosition % 5));
        }

        // Ajusta a posição para garantir que o jogador não ultrapasse os limites do labirinto
        if (nextPosition >= TOTAL_POSITIONS) {
            nextPosition = TOTAL_POSITIONS - 1;
        } else if (nextPosition < 0) {
            nextPosition = 0;
        }

        // Atualiza a nova posição do jogador com a imagem do jogador
        currentPlayerPosition = nextPosition;
        updatePlayerPosition(currentPlayerPosition, R.drawable.square_player);
    }

    // Função para atualizar a posição do jogador no labirinto
    private void updatePlayerPosition(int position, int drawableId) {
        int row = position / 5;  // 5 colunas
        int col = position % 5;

        if (row % 2 != 0) {
            col = 4 - col;  // Inverte a direção de movimento nas linhas ímpares
        }

        String cellId = "mazePosition" + (row * 5 + col);  // Calcula o ID da célula correspondente
        int resID = getResources().getIdentifier(cellId, "id", getPackageName());

        ImageView cell = findViewById(resID);  // Obtém a célula da grid
        if (cell != null) {
            cell.setImageResource(drawableId);  // Atualiza a imagem da célula
        }
    }

    // Função para atualizar a equação que está sendo resolvida
    private void updateQuestion() {
        equationText.setText(equations.get(currentEquationIndex));  // Exibe a equação atual
        hintText.setText("");  // Limpa a dica ao mostrar uma nova questão
        scoreText.setText("Pontuação: " + score);  // Exibe a pontuação atual
    }


}
