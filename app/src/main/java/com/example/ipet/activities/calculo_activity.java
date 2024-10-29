package com.example.ipet.activities;

import java.util.Random;

public class calculo_activity {

    private int numero1;
    private int numero2;
    private char operador;
    private int respostaCorreta;

    public calculo_activity () {
        gerarCalculo();
    }

    private void gerarCalculo() {
        Random random = new Random();
        numero1 = random.nextInt(10) + 1; // Número entre 1 e 10
        numero2 = random.nextInt(10) + 1;

        int operadorEscolhido = random.nextInt(4);
        switch (operadorEscolhido) {
            case 0:
                operador = '+';
                respostaCorreta = numero1 + numero2;
                break;
            case 1:
                operador = '-';
                respostaCorreta = numero1 - numero2;
                break;
            case 2:
                operador = '*';
                respostaCorreta = numero1 * numero2;
                break;
            case 3:
                operador = '/';
                respostaCorreta = numero1 / numero2;
                break;
        }
    }

    public String getPergunta() {
        return numero1 + " " + operador + " " + numero2 + " = ?";
    }

    public boolean verificarResposta(int resposta) {
        return resposta == respostaCorreta;
    }

}
