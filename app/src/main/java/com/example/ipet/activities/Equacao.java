package com.example.ipet.activities;

public class Equacao {
    private String equacao;
    private String resposta;
    private String dica;
    private String id;

    public Equacao(String equacao, String resposta, String dica, String id) {
        this.equacao = equacao;
        this.resposta = resposta;
        this.dica = dica;
        this.id = id;
    }

    public String getEquacao() {
        return equacao;
    }

    public String getResposta() {
        return resposta;
    }

    public String getDica() {
        return dica;
    }

    public String getId() {
        return id;
    }
}
