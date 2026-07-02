package com.clinica.model;

public class Produto implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nome;
    private String descricao;
    private double preco;
    private int quantidadeEstoque;
    private String categoria;

    public Produto(int id, String nome, String descricao, double preco, int quantidadeEstoque, String categoria) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        this.categoria = categoria;
    }

    public boolean isEmEstoque() {
        return quantidadeEstoque > 0;
    }

    public void reduzirEstoque(int quantidade) {
        if (quantidade > 0 && quantidade <= quantidadeEstoque) {
            this.quantidadeEstoque -= quantidade;
        } else {
            throw new IllegalArgumentException("Quantidade inválida ou insuficiente em estoque.");
        }
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    public int getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(int quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                ", estoque=" + quantidadeEstoque +
                '}';
    }
}