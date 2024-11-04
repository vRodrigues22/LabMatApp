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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LabyrinthActivity extends AppCompatActivity {

    private TextView equationText, scoreText, hintText;
    private EditText answerInput;
    private Button submitButton, hintButton;
    private int currentEquationIndex = 0;
    private int score = 0;
    private int currentPlayerPosition = 0;
    private List<String> equations = new ArrayList<>();
    private List<String> hints = new ArrayList<>();
    private List<Integer> answers = new ArrayList<>(); // respostas corretas para validação
    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labyrinth);

        equationText = findViewById(R.id.equationText);
        scoreText = findViewById(R.id.scoreText);
        hintText = findViewById(R.id.hintText);
        answerInput = findViewById(R.id.answerInput);
        submitButton = findViewById(R.id.calcbtn);
        hintButton = findViewById(R.id.hintButton);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

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

        updateQuestion();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userAnswer = answerInput.getText().toString();
                if (userAnswer.isEmpty()) {
                    Toast.makeText(LabyrinthActivity.this, "Por favor, insira uma resposta", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verificar resposta
                int answer = Integer.parseInt(userAnswer);
                if (answer == answers.get(currentEquationIndex)) {
                    score++;
                    Toast.makeText(LabyrinthActivity.this, "Correto! Pontuação: " + score, Toast.LENGTH_SHORT).show();
                    movePlayer();
                    currentEquationIndex++;
                    if (currentEquationIndex < equations.size()) {
                        updateQuestion();
                    } else {
                        saveScoreToFirestore();
                    }
                } else {
                    Toast.makeText(LabyrinthActivity.this, "Resposta incorreta, tente novamente", Toast.LENGTH_SHORT).show();
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
        ImageView previousPosition = findViewById(getResources().getIdentifier("mazePosition" + currentPlayerPosition, "id", getPackageName()));
        previousPosition.setImageResource(R.drawable.cell_image); // Volta ao estado da célula padrão

        currentPlayerPosition++;
        if (currentPlayerPosition >= 25) {
            currentPlayerPosition = 24;
            Toast.makeText(this, "Parabéns! Você completou o labirinto!", Toast.LENGTH_LONG).show();
            saveScoreToFirestore(); // Salva pontuação quando o labirinto é completado
            return;
        }

        // Atualiza a imagem da nova posição com o jogador
        ImageView currentPosition = findViewById(getResources().getIdentifier("mazePosition" + currentPlayerPosition, "id", getPackageName()));
        currentPosition.setImageResource(R.drawable.square_player); // Exibe o jogador na nova posição
    }


    // Atualiza a questão e a dica
    private void updateQuestion() {
        equationText.setText(equations.get(currentEquationIndex));
        hintText.setText(""); // Limpa a dica ao mostrar uma nova questão
        scoreText.setText("Pontuação: " + score);
    }

    // Salva a pontuação no Firestore
    private void saveScoreToFirestore() {
        Map<String, Object> update = new HashMap<>();
        update.put("pontuacao", score);

        db.collection("usuarios").document(user.getUid())
                .update(update)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LabyrinthActivity.this, "Pontuação salva com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LabyrinthActivity.this, "Erro ao salvar pontuação", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
