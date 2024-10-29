package com.example.ipet.activities;

public class jogo {

    private LabirintoActivity labirinto;
    private personagem_activity personagem;

    public jogo(int linhas, int colunas) {
        labirinto = new LabirintoActivity(linhas, colunas);
        personagem = new personagem_activity(1, 1, labirinto);
    }

    public void moverPersonagem(String direcao) {
        switch (direcao) {
            case "CIMA":
                personagem.mover(-1, 0);
                break;
            case "BAIXO":
                personagem.mover(1, 0);
                break;
            case "ESQUERDA":
                personagem.mover(0, -1);
                break;
            case "DIREITA":
                personagem.mover(0, 1);
                break;
        }
    }

    public boolean verificarVitoria() {
        return personagem.atingiuObjetivo();
    }

    public LabirintoActivity getLabirinto() {
        return labirinto;
    }

    public personagem_activity getPersonagem() {
        return personagem;
    }
}
