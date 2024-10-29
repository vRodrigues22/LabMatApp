package com.example.ipet.activities;

public class celula_activity {

    private boolean parede;
    private boolean objetivo;
    private calculo_activity calculo; // Adiciona cálculo na célula

    public celula_activity (boolean parede, boolean objetivo) {
        this.parede = parede;
        this.objetivo = objetivo;
        this.calculo = null; // Inicialmente sem cálculo
    }

    public void adicionarCalculo() {
        this.calculo = new calculo_activity();
    }

    public boolean isParede() {
        return parede;
    }

    public boolean isObjetivo() {
        return objetivo;
    }

    public calculo_activity getCalculo() {
        return calculo;
    }

    public boolean temCalculo() {
        return calculo != null;
    }
}
