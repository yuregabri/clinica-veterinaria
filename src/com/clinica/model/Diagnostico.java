package com.clinica.model;

public class Diagnostico implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nomeDoenca;
    private String descricao;

    public Diagnostico(int id, String nomeDoenca, String descricao) {
        this.id = id;
        this.nomeDoenca = nomeDoenca;
        this.descricao = descricao;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNomeDoenca() { return nomeDoenca; }
    public void setNomeDoenca(String nomeDoenca) { this.nomeDoenca = nomeDoenca; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    @Override
    public String toString() {
        return "Diagnostico{" +
                "id=" + id +
                ", nomeDoenca='" + nomeDoenca + '\'' +
                '}';
    }
}