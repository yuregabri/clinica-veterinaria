package com.clinica.model;

public class Vacina implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nome;
    private String fabricante;
    private int duracaoMeses;

    public Vacina(int id, String nome, String fabricante, int duracaoMeses) {
        this.id = id;
        this.nome = nome;
        this.fabricante = fabricante;
        this.duracaoMeses = duracaoMeses;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getFabricante() { return fabricante; }
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }

    public int getDuracaoMeses() { return duracaoMeses; }
    public void setDuracaoMeses(int duracaoMeses) { this.duracaoMeses = duracaoMeses; }

    @Override
    public String toString() {
        return "Vacina{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", fabricante='" + fabricante + '\'' +
                ", duracaoMeses=" + duracaoMeses +
                '}';
    }
}