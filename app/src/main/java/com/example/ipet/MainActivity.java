package com.example.ipet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

// entendi
// da pra programar em java por aqui , mas java é um pouco mais complicado,mas vai fazer diferença? classe repetida tende a dar bug
// eu apaguei a classe vou testar pra ver se some o erro
// Classe Main para testar o código
public class MainActivity {
    public static void main(String[] args) {
        // Criando objetos da classe Categoria
        Categoria trabalho = new Categoria("Trabalho");
        Categoria pessoal = new Categoria("Pessoal");

        // Criando tarefas urgentes e com prazo flexível
        TarefaUrgente tarefa1 = new TarefaUrgente("Finalizar relatório", 5, trabalho);
        TarefaComPrazoFlexivel tarefa2 = new TarefaComPrazoFlexivel("Planejar férias", 3, pessoal);

        // Imprimindo as tarefas
        System.out.println(tarefa1.toString());
        System.out.println(tarefa2.toString());

        // Enviando notificação da tarefa urgente
        tarefa1.enviarNotificacao();
    }
}
// Interface Notificacao
// Esta interface define um contrato para qualquer classe que deseje implementar o envio de notificações.
// Ela possui um metodo abstrato `enviarNotificacao`, que deve ser implementado pelas classes que implementarem esta interface.
// O metodo serve para enviar notificações personalizadas de tarefas que necessitam de alertas.
interface Notificacao {
    // Metodo para enviar uma notificação
    void enviarNotificacao();
}

// Classe Categoria
// A classe `Categoria` é usada para agrupar tarefas sob uma mesma categoria, como "Trabalho" ou "Pessoal".
// Ela tem um único atributo, `nome`, que representa o nome da categoria, além de métodos para obter e modificar esse nome.
class Categoria {
    private String nome;

    // Construtor que recebe o nome da categoria.
    public Categoria(String nome) {
        this.nome = nome;
    }

    // Metodo para obter o nome da categoria.
    public String getNome() {
        return nome;
    }

    // Metodo para modificar o nome da categoria.
    public void setNome(String nome) {
        this.nome = nome;
    }
}

// Classe Tarefa
// A classe `Tarefa` serve como a base para representar qualquer tipo de tarefa, com uma descrição, uma prioridade e uma categoria.
// Ela valida a prioridade para garantir que seja um valor entre os limites estabelecidos (definidos como constantes).
// As subclasses podem sobrescrever o metodo `getTipoTarefa` para fornecer informações adicionais sobre o tipo de tarefa.
class Tarefa {
    private String descricao; // Descrição da tarefa
    private int prioridade;   // Nível de prioridade da tarefa (1 a 5)
    private Categoria categoria; // Categoria da tarefa (ex.: Trabalho, Pessoal)

    // Constantes para definir os limites da prioridade permitida
    private static final int PRIORIDADE_MIN = 1;
    private static final int PRIORIDADE_MAX = 5;

    // Construtor que inicializa a tarefa com uma descrição, prioridade e categoria.
    // Ele também valida se a prioridade está dentro dos limites definidos.
    public Tarefa(String descricao, int prioridade, Categoria categoria) {
        if (!isPrioridadeValida(prioridade)) {
            throw new IllegalArgumentException("Prioridade inválida. Deve ser entre " + PRIORIDADE_MIN + " e " + PRIORIDADE_MAX + ".");
        }
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.categoria = categoria;
    }

    // Metodo privado que valida se a prioridade está entre os limites permitidos.
    private boolean isPrioridadeValida(int prioridade) {
        return prioridade >= PRIORIDADE_MIN && prioridade <= PRIORIDADE_MAX;
    }

    // Metodo para obter a descrição da tarefa.
    public String getDescricao() {
        return descricao;
    }

    // Metodo para obter a categoria da tarefa.
    public Categoria getCategoria() {
        return categoria;
    }

    // Metodo protegido que pode ser sobrescrito pelas subclasses para indicar o tipo da tarefa.
    // Por padrão, ele retorna uma string vazia, mas subclasses como `TarefaUrgente` e `TarefaComPrazoFlexivel` irão sobrescrevê-lo.
    protected String getTipoTarefa() {
        return "";
    }

    // Metodo que retorna uma representação textual da tarefa, incluindo sua descrição, prioridade e categoria.
    // O tipo da tarefa também é incluído (caso uma subclasse tenha sobrescrito `getTipoTarefa`).
    @Override
    public String toString() {
        return descricao + " - Prioridade: " + prioridade + " - Categoria: " + categoria.getNome() + getTipoTarefa();
    }
}

// Classe TarefaUrgente
// Esta classe herda de `Tarefa` e representa uma tarefa urgente, implementando também a interface `Notificacao`.
// O metodo `getTipoTarefa` é sobrescrito para indicar que a tarefa é urgente.
// Também implementa o metodo `enviarNotificacao`, que é responsável por enviar uma notificação informando que a tarefa é urgente.
class TarefaUrgente extends Tarefa implements Notificacao {

    // Construtor que chama o construtor da classe base `Tarefa` para inicializar a tarefa urgente.
    public TarefaUrgente(String descricao, int prioridade, Categoria categoria) {
        super(descricao, prioridade, categoria);
    }

    // Sobrescreve o metodo `getTipoTarefa` para retornar "(Urgente)", indicando que essa é uma tarefa urgente.
    @Override
    protected String getTipoTarefa() {
        return " (Urgente)";
    }

    // Implementação do metodo `enviarNotificacao` da interface `Notificacao`.
    // Este metodo imprime uma mensagem alertando que a tarefa urgente precisa de atenção imediata.
    @Override
    public void enviarNotificacao() {
        System.out.println("Notificação: A tarefa '" + getDescricao() + "' é urgente e precisa de atenção imediata!");
    }
}

// Classe TarefaComPrazoFlexivel
// Esta classe herda de `Tarefa` e representa uma tarefa com prazo flexível.
// O metodo `getTipoTarefa` é sobrescrito para indicar que a tarefa tem um prazo flexível.
class TarefaComPrazoFlexivel extends Tarefa {

    // Construtor que chama o construtor da classe base `Tarefa` para inicializar a tarefa com prazo flexível.
    public TarefaComPrazoFlexivel(String descricao, int prioridade, Categoria categoria) {
        super(descricao, prioridade, categoria);
    }

    // Sobrescreve o metodo `getTipoTarefa` para retornar "(Prazo Flexível)", indicando que a tarefa não tem um prazo fixo.
    @Override
    protected String getTipoTarefa() {
        return " (Prazo Flexível)";
    }
}
