package com.example.ipet.activities;

public class Usuario {

    private String id;
    private String nome;
    private String email;
    private String dataNascimento;
    private int pontuacao;
    private int contasResolvidas;
    private long tempo1;
    private long tempo2;
    private long tempo3;

    // Construtor vazio para o Firestore
    public Usuario() {
    }

    // Construtor com parâmetros
    public Usuario(String id, String nome, String email, String dataNascimento, int pontuacao,
                   int contasResolvidas, long tempo1, long tempo2, long tempo3) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.pontuacao = pontuacao;
        this.contasResolvidas = contasResolvidas;
        this.tempo1 = tempo1;
        this.tempo2 = tempo2;
        this.tempo3 = tempo3;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public int getContasResolvidas() {
        return contasResolvidas;
    }

    public void setContasResolvidas(int contasResolvidas) {
        this.contasResolvidas = contasResolvidas;
    }

    public long getTempo1() {
        return tempo1;
    }

    public void setTempo1(long tempo1) {
        this.tempo1 = tempo1;
    }

    public long getTempo2() {
        return tempo2;
    }

    public void setTempo2(long tempo2) {
        this.tempo2 = tempo2;
    }

    public long getTempo3() {
        return tempo3;
    }

    public void setTempo3(long tempo3) {
        this.tempo3 = tempo3;
    }
}
