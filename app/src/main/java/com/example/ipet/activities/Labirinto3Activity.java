package com.example.ipet.activities;

import android.os.Bundle;
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

public class Labirinto3Activity extends AppCompatActivity{

    private TextView equationText, scoreText, hintText;
    private EditText answerInput;
    private Button submitButton, hintButton;
    private int currentEquationIndex = 0;
    private int score = 0;
    private int currentPlayerPosition = 0;
    private static final int TOTAL_POSITIONS = 25;
    private static final int ADVANCE_STEPS = 2;
    private List<String> equations = new ArrayList<>();
    private List<String> hints = new ArrayList<>();
    private List<Integer> answers = new ArrayList<>(); // respostas corretas para validação
    private FirebaseFirestore db;
    private DocumentReference documentReference;
    private FirebaseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labirinto2);

        equationText = findViewById(R.id.equationText);
        scoreText = findViewById(R.id.scoreText);
        hintText = findViewById(R.id.hintText);
        answerInput = findViewById(R.id.answerInput);
        submitButton = findViewById(R.id.calcbtn);
        hintButton = findViewById(R.id.hintButton);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        equations.add("Equação :(6² + 24) ÷ 3");
        answers.add(16);
        hints.add("Dica: Calcule a potência primeiro, some, depois divida.");

        equations.add("Equação : 200 ÷ 4 + 30");
        answers.add(80);
        hints.add("Dica: Divida e depois some.");

        equations.add("Equação : 5³ - 7 * 3");
        answers.add(98);
        hints.add("Dica: Potência primeiro, depois a multiplicação e subtração.");

        equations.add("Equação :  (20 + 10) * 2");
        answers.add(60);
        hints.add("Dica: Resolva o parêntese e depois multiplique.");

        equations.add("Equação :  18x - 9 = 63");
        answers.add(4);
        hints.add("Dica: Isole x para resolver.");

        equations.add("Equação :  90 ÷ (3 + 6)");
        answers.add(10);
        hints.add("Dica: Resolva o parêntese primeiro, depois divida.");

        equations.add("Equação : (12 + 8) * (3² - 5)");
        answers.add(80);
        hints.add("Dica: Siga a ordem dos parênteses, depois calcule a potência e finalize com as operações.");

        updateQuestion();

        updatePlayerPosition(currentPlayerPosition, R.drawable.square_player);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userAnswer = answerInput.getText().toString();
                if (userAnswer.isEmpty()) {
                    Toast.makeText(Labirinto3Activity.this, "Por favor, insira uma resposta", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verificar resposta
                int answer = Integer.parseInt(userAnswer);
                if (answer == answers.get(currentEquationIndex)) {
                    score+= 10; // Incrementa a pontuação
                    Toast.makeText(Labirinto3Activity.this, "Correto! Pontuação: " + score, Toast.LENGTH_SHORT).show();
                    movePlayer();
                    currentEquationIndex++;
                    if (currentEquationIndex < equations.size()) {
                        updateQuestion();
                    } else {
                        // Atualiza o scoreText antes de salvar
                        scoreText.setText("Pontuação: " + score);
                        // Aqui chamamos saveScoreToFirestore e emitimos o aviso
                        saveScoreToFirestore();
                        Toast.makeText(Labirinto3Activity.this, "Você completou a Etapa 1! Pontuação total: " + score, Toast.LENGTH_LONG).show();
                        // Bloqueia o botão de enviar resposta
                        submitButton.setEnabled(false);
                        hintButton.setEnabled(false);
                        answerInput.setEnabled(false);
                    }
                } else {
                    Toast.makeText(Labirinto3Activity.this, "Resposta incorreta, tente novamente", Toast.LENGTH_SHORT).show();
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

    private void movePlayer() {
        if (currentPlayerPosition >= TOTAL_POSITIONS - 1) {
            // Aqui emite o aviso quando o jogador chega ao final
            Toast.makeText(this, "Parabéns! Você completou o labirinto!", Toast.LENGTH_LONG).show();
            saveScoreToFirestore();
            return;
        }

        // Atualiza a posição anterior do jogador com a imagem da célula vazia
        updatePlayerPosition(currentPlayerPosition, R.drawable.cell_image);

        int row = currentPlayerPosition / 5; // Linha atual
        int nextPosition;

        // Define o movimento com base na linha atual (zigue-zague)
        if (row % 2 == 0) {
            // Em linhas pares, mova para a direita
            nextPosition = currentPlayerPosition + ADVANCE_STEPS;
        } else {
            // Em linhas ímpares, mova para a esquerda
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

    private void updatePlayerPosition(int position, int drawableId) {
        int row = position / 5; // 5 colunas
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

    // Atualiza a questão e a dica
    private void updateQuestion() {
        equationText.setText(equations.get(currentEquationIndex));
        hintText.setText(""); // Limpa a dica ao mostrar uma nova questão
        scoreText.setText("Pontuação: " + score);
    }

    // Salva a pontuação no Firestore
    private void saveScoreToFirestore() {
        // Primeiro, vamos recuperar a pontuação atual do usuário
        db.collection("usuarios").document(user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        int currentScore = 0;

                        // Verifica se o documento existe e se a pontuação está presente
                        if (document.exists() && document.contains("pontuacao")) {
                            currentScore = document.getLong("pontuacao").intValue(); // Obtém a pontuação atual
                        }

                        // Soma a pontuação atual com a nova pontuação
                        int newScore = currentScore + score;

                        // Prepara o novo valor para atualizar no Firestore
                        Map<String, Object> update = new HashMap<>();
                        update.put("pontuacao", newScore);

                        // Atualiza o Firestore com a nova pontuação
                        db.collection("usuarios").document(user.getUid())
                                .update(update)
                                .addOnCompleteListener(updateTask -> {
                                    if (updateTask.isSuccessful()) {
                                        Toast.makeText(Labirinto3Activity.this, "Pontuação salva com sucesso!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Labirinto3Activity.this, "Erro ao salvar pontuação", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(Labirinto3Activity.this, "Erro ao recuperar pontuação atual", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
