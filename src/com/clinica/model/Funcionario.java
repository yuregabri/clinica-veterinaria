package com.clinica.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Funcionario extends Pessoas {
    private String matricula;
    private String cargo;
    private LocalDateTime dataContratacao;
    private List<Venda> vendas;
    private List<BanhoTosa> servicosBanhoTosa;

    public Funcionario(int id, String nome, String telefone, String email,
                       String matricula, String cargo, LocalDateTime dataContratacao) {
        super(id, nome, telefone, email);
        this.matricula = matricula;
        this.cargo = cargo;
        this.dataContratacao = dataContratacao;
        this.vendas = new ArrayList<>();
        this.servicosBanhoTosa = new ArrayList<>();
    }

    public void adicionarVenda(Venda venda) {
        vendas.add(venda);
        venda.setFuncionario(this);
    }

    public void adicionarServicoBanhoTosa(BanhoTosa servico) {
        servicosBanhoTosa.add(servico);
        servico.setFuncionario(this);
    }

    public double calcularSalarioMensal() {
        // Cálculo simplificado: baseado no cargo
        switch (cargo.toLowerCase()) {
            case "gerente": return 5000.0;
            case "atendente": return 2500.0;
            case "tosador": return 2200.0;
            default: return 2000.0;
        }
    }

    // Getters e Setters
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public LocalDateTime getDataContratacao() { return dataContratacao; }
    public void setDataContratacao(LocalDateTime dataContratacao) { this.dataContratacao = dataContratacao; }

    public List<Venda> getVendas() { return vendas; }
    public void setVendas(List<Venda> vendas) { this.vendas = vendas; }

    public List<BanhoTosa> getServicosBanhoTosa() { return servicosBanhoTosa; }
    public void setServicosBanhoTosa(List<BanhoTosa> servicosBanhoTosa) { this.servicosBanhoTosa = servicosBanhoTosa; }

    @Override
    public String toString() {
        return "Funcionario{" +
                "id=" + getId() +
                ", nome='" + getNome() + '\'' +
                ", matricula='" + matricula + '\'' +
                ", cargo='" + cargo + '\'' +
                '}';
    }
}