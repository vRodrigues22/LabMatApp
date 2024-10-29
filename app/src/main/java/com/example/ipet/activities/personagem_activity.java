package com.example.ipet.activities;

public class personagem_activity {

    private int posX;
    private int posY;
    private LabirintoActivity labirinto;

    public personagem_activity(int startX, int startY, LabirintoActivity labirinto) {
        this.posX = startX;
        this.posY = startY;
        this.labirinto = labirinto;
    }

    public void mover(int deltaX, int deltaY) {
        int novoX = posX + deltaX;
        int novoY = posY + deltaY;

        if (novoX >= 0 && novoX < labirinto.getLinhas() &&
                novoY >= 0 && novoY < labirinto.getColunas() &&
                !labirinto.getCelula(novoX, novoY).isParede()) {
            posX = novoX;
            posY = novoY;
        }
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public boolean atingiuObjetivo() {
        return labirinto.getCelula(posX, posY).isObjetivo();
    }

}
