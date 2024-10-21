package com.example.ipet.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ipet.R;
import java.util.Random;

public class LabirintoActivity extends AppCompatActivity {

    private GridLayout labyrinthGrid;
    private TextView problemText;
    private EditText answerInput;
    private Button submitAnswer;
    private TextView congratulationsText;
    private int playerPosition = 0; // Posição inicial do jogador no labirinto
    private int[][] mathProblems; // Problemas de matemática para cada célula
    private int goalPosition = 24; // Posição do ponto de chegada
    private int gridSize = 5; // Tamanho do grid (5x5)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labirinto);

        labyrinthGrid = findViewById(R.id.labyrinth_grid);
        problemText = findViewById(R.id.problem_text);
        answerInput = findViewById(R.id.answer_input);
        submitAnswer = findViewById(R.id.submit_answer);
        congratulationsText = findViewById(R.id.congratulations_text);

        // Inicializa problemas matemáticos
        mathProblems = generateMathProblems(gridSize * gridSize); // 25 células

        // Inicializa o labirinto
        setupLabyrinth();

        // Configura botões de movimento
        setupMovementButtons();
    }

    private void setupLabyrinth() {
        // Cria as células do labirinto
        for (int i = 0; i < gridSize * gridSize; i++) {
            ImageView cell = new ImageView(this);
            cell.setLayoutParams(new GridLayout.LayoutParams());

            // Define as imagens para os muros e caminhos
            if (i == playerPosition) {
                cell.setImageResource(R.drawable.character_icon); // Coloca o personagem na posição inicial
            } else if (i == goalPosition) {
                cell.setImageResource(R.drawable.goal_icon); // Ponto de chegada
            } else {
                // Define se a célula é um muro ou caminho livre
                if (isPathFree(i)) {
                    cell.setImageResource(R.drawable.path_icon);
                } else {
                    cell.setImageResource(R.drawable.wall_icon);
                }
            }

            labyrinthGrid.addView(cell);
        }
    }

    private boolean isPathFree(int position) {
        // Exemplo de lógica simples para definir muros e caminhos
        return position % 2 == 0; // Caminhos livres em posições pares
    }

    private void setupMovementButtons() {
        Button moveUp = findViewById(R.id.move_up);
        Button moveDown = findViewById(R.id.move_down);
        Button moveLeft = findViewById(R.id.move_left);
        Button moveRight = findViewById(R.id.move_right);

        moveUp.setOnClickListener(v -> movePlayer(-gridSize)); // Mover para cima
        moveDown.setOnClickListener(v -> movePlayer(gridSize)); // Mover para baixo
        moveLeft.setOnClickListener(v -> movePlayer(-1)); // Mover para a esquerda
        moveRight.setOnClickListener(v -> movePlayer(1)); // Mover para a direita

        // Botão de envio de resposta
        submitAnswer.setOnClickListener(v -> checkAnswer());
    }

    private void movePlayer(int offset) {
        int newPosition = playerPosition + offset;

        // Checa se o movimento é válido
        if (isMoveValid(newPosition)) {
            // Atualiza a posição do jogador
            updatePlayerPosition(newPosition);
            presentMathProblem(newPosition);
        } else {
            Toast.makeText(this, "Movimento inválido! Resolva o problema matemático para passar.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isMoveValid(int newPosition) {
        // Verifica se o movimento está dentro dos limites do labirinto
        if (newPosition < 0 || newPosition >= gridSize * gridSize) {
            return false;
        }

        // Checa se a célula é um caminho livre ou o objetivo
        return isPathFree(newPosition) || (newPosition == goalPosition && playerPosition != goalPosition);
    }

    private void updatePlayerPosition(int newPosition) {
        // Atualiza a imagem do personagem na posição do labirinto
        ImageView oldCell = (ImageView) labyrinthGrid.getChildAt(playerPosition);
        oldCell.setImageResource(isPathFree(playerPosition) ? R.drawable.path_icon : R.drawable.wall_icon); // Restaura a imagem da célula anterior

        playerPosition = newPosition; // Atualiza a posição do jogador
        ImageView newCell = (ImageView) labyrinthGrid.getChildAt(playerPosition);
        newCell.setImageResource(R.drawable.character_icon); // Coloca o personagem na nova posição

        // Verifica se o jogador chegou ao objetivo
        if (playerPosition == goalPosition) {
            congratulationsText.setVisibility(View.VISIBLE);
            congratulationsText.setText("Parabéns! Você completou o labirinto!");
        } else {
            congratulationsText.setVisibility(View.GONE); // Esconde a mensagem de parabéns
        }
    }

    private void presentMathProblem(int position) {
        if (!isPathFree(position)) {
            int[] problem = mathProblems[position];
            String question = generateQuestion(problem);
            problemText.setText(question);
            playerPosition = position; // Atualiza a posição do jogador após responder
        }
    }

    private int[][] generateMathProblems(int size) {
        Random random = new Random();
        int[][] problems = new int[size][3]; // [operando1, operando2, operação]
        for (int i = 0; i < size; i++) {
            int operando1 = random.nextInt(10) + 1;
            int operando2 = random.nextInt(10) + 1;
            int operation = random.nextInt(4); // 0 = soma, 1 = subtração, 2 = multiplicação, 3 = divisão
            problems[i][0] = operando1;
            problems[i][1] = operando2;
            problems[i][2] = operation;
        }
        return problems;
    }

    private String generateQuestion(int[] problem) {
        switch (problem[2]) {
            case 0:
                return problem[0] + " + " + problem[1];
            case 1:
                return problem[0] + " - " + problem[1];
            case 2:
                return problem[0] + " * " + problem[1];
            case 3:
                return problem[0] + " / " + problem[1];
            default:
                return "";
        }
    }

    private void checkAnswer() {
        int[] problem = mathProblems[playerPosition];
        int correctAnswer = solveProblem(problem);

        try {
            int userAnswer = Integer.parseInt(answerInput.getText().toString());

            if (userAnswer == correctAnswer) {
                Toast.makeText(this, "Resposta correta!", Toast.LENGTH_SHORT).show();
                movePlayerAfterAnswer(); // Move o jogador após responder corretamente
            } else {
                Toast.makeText(this, "Resposta incorreta! Tente novamente.", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Por favor, insira um número válido!", Toast.LENGTH_SHORT).show();
        }
    }

    private void movePlayerAfterAnswer() {
        // Permite que o jogador se mova para a próxima posição se a resposta estiver correta
        // Aqui você pode definir como o jogador avança após resolver o problema
        int nextPosition = playerPosition + 1; // Apenas como exemplo, você pode definir a lógica de movimento
        if (isMoveValid(nextPosition)) {
            updatePlayerPosition(nextPosition);
        }
    }

    private int solveProblem(int[] problem) {
        switch (problem[2]) {
            case 0:
                return problem[0] + problem[1];
            case 1:
                return problem[0] - problem[1];
            case 2:
                return problem[0] * problem[1];
            case 3:
                return problem[0] / problem[1];
            default:
                return 0;
        }
    }
}
