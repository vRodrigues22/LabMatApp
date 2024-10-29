package com.example.ipet.activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.ipet.R;

public class LabirintoActivity extends AppCompatActivity {

    private jogo jogo;
    private GridLayout gridLayout;
    private celula_activity[][] grid;
    private int linhas;
    private int colunas;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labirinto);

        int linhas = 10;
        int colunas = 10;
        jogo = new jogo (linhas, colunas);

        gridLayout = findViewById(R.id.gridLayout);
        atualizarLabirinto();

        Button btnCalcularPontuacao = findViewById(R.id.btnCalcularPontuacao);
        btnCalcularPontuacao.setOnClickListener(v -> {
            int pontuacaoFinal = pontuação_activity .calcularPontuacaoFinal();
            Toast.makeText(this, "Pontuação Final: " + pontuacaoFinal, Toast.LENGTH_LONG).show();
        });

           }

    // Métodos para retornar as dimensões do labirinto
    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }

    public LabirintoActivity (int linhas, int colunas) {
        this.linhas = linhas;
        this.colunas = colunas;
        grid = new celula_activity[linhas][colunas];
        inicializarLabirinto();
    }

    private void inicializarLabirinto() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                boolean parede = (i == 0 || j == 0 || i == linhas - 1 || j == colunas - 1); // Bordas como parede
                boolean objetivo = (i == linhas - 2 && j == colunas - 2); // Objetivo na penúltima célula
                grid[i][j] = new celula_activity (parede, objetivo);
            }
        }
    }

    public celula_activity getCelula(int x, int y) {
        return grid[x][y];
    }

    private void atualizarLabirinto() {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            Button cell = (Button) gridLayout.getChildAt(i);
            int row = i / jogo.getLabirinto().getColunas();
            int col = i % jogo.getLabirinto().getColunas();

            if (jogo.getLabirinto().getCelula(row, col).isParede()) {
                cell.setBackgroundColor(Color.BLACK);
            } else if (jogo.getLabirinto().getCelula(row, col).isObjetivo()) {
                cell.setBackgroundColor(Color.GREEN);
            } else {
                cell.setBackgroundColor(Color.WHITE);
            }
        }
        // Atualiza a posição do personagem
        int personagemIndex = jogo.getPersonagem().getPosX() * jogo.getLabirinto().getColunas() + jogo.getPersonagem().getPosY();
        Button playerCell = (Button) gridLayout.getChildAt(personagemIndex);
        playerCell.setBackgroundColor(Color.RED);
    }

    @SuppressLint("NonConstantResourceId")
    public void moverPersonagem (View view) {
        switch (view.getId()) {
            case R.id.buttonCima:
                jogo.moverPersonagem("CIMA");
                break;
            case R.id.buttonBaixo:
                jogo.moverPersonagem("BAIXO");
                break;
            case R.id.buttonEsquerda:
                jogo.moverPersonagem("ESQUERDA");
                break;
            case R.id.buttonDireita:
                jogo.moverPersonagem("DIREITA");
                break;
        }
        atualizarLabirinto();
        if (jogo.verificarVitoria()) {
            Toast.makeText(this, "Você venceu!", Toast.LENGTH_SHORT).show();
        }
    }


}


