package com.clinica.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venda implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private LocalDateTime dataHora;
    private List<Produto> produtos;
    private Funcionario funcionario;
    private Tutor tutor;
    private double valorTotal;

    public Venda(int id, LocalDateTime dataHora, Funcionario funcionario, Tutor tutor) {
        this.id = id;
        this.dataHora = dataHora;
        this.funcionario = funcionario;
        this.tutor = tutor;
        this.produtos = new ArrayList<>();
        this.valorTotal = 0.0;
    }

    public void adicionarProduto(Produto produto) {
        produtos.add(produto);
        calcularTotal(); // recalcula sempre que adiciona
    }

    public void calcularTotal() {
        valorTotal = produtos.stream().mapToDouble(Produto::getPreco).sum();
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public List<Produto> getProdutos() { return produtos; }
    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
        calcularTotal();
    }

    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }

    public Tutor getTutor() { return tutor; }
    public void setTutor(Tutor tutor) { this.tutor = tutor; }

    public double getValorTotal() { return valorTotal; }
    // Não há setter para valorTotal, pois é calculado automaticamente

    @Override
    public String toString() {
        return "Venda{" +
                "id=" + id +
                ", dataHora=" + dataHora +
                ", tutor=" + (tutor != null ? tutor.getNome() : "null") +
                ", valorTotal=" + valorTotal +
                '}';
    }
}