package com.example.ipet.crud;

public class Pet {
    private String name;
    private String raca;

    public Pet() {
        // Construtor vazio necessário para o Firebase
    }

    public Pet(String name, String raca) {
        this.name = name;
        this.raca = raca;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }
}
