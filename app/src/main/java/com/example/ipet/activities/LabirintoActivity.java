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

public class LabirintoActivity extends AppCompatActivity {

        private TextView problemText;
        private EditText answerInput;
        private Button submitButton;
        private GridLayout labyrinthGrid;
        private int correctAnswer = 15; // Exemplo de resposta correta

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_labirinto);

            // Inicializar os elementos do layout
            problemText = findViewById(R.id.problem_text);
            answerInput = findViewById(R.id.answer_input);
            submitButton = findViewById(R.id.submit_button);
            labyrinthGrid = findViewById(R.id.labyrinth_grid);

            // Configurar o problema de matemática
            setMathProblem();

            // Ação para o botão de envio
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswer();
                }
            });
        }

        // Define um problema de matemática
        private void setMathProblem() {
            problemText.setText("Qual é 10 + 5?");
            correctAnswer = 15; // Exemplo de resposta
        }

        // Verifica se a resposta está correta
        private void checkAnswer() {
            String userAnswer = answerInput.getText().toString();
            if (!userAnswer.isEmpty()) {
                int answer = Integer.parseInt(userAnswer);
                if (answer == correctAnswer) {
                    Toast.makeText(this, "Correto! Continue!", Toast.LENGTH_SHORT).show();
                    // Aqui você pode atualizar o labirinto, mover o personagem, etc.
                } else {
                    Toast.makeText(this, "Resposta Errada. Tente novamente.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Insira uma resposta.", Toast.LENGTH_SHORT).show();
            }
        }
    }

