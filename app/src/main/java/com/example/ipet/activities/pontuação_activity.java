package com.example.ipet.activities;

public class pontuação_activity {

    private static int acertos;

    public pontuação_activity () {
        this.acertos = 0;
    }

    // Adiciona acerto ao contador
    public void adicionarAcerto() {
        acertos++;
    }

    // Calcula a pontuação final com base nos acertos
    public static int calcularPontuacaoFinal() {
        return acertos * 10; // Exemplo: cada acerto vale 10 pontos
    }

    // Reseta a pontuação ao iniciar uma nova partida
    public void resetar() {
        acertos = 0;
    }

    public int getAcertos() {
        return acertos;
    }

}
