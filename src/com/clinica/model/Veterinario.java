package com.clinica.model;

import java.util.ArrayList;
import java.util.List;

public class Veterinario extends Pessoas {
    private String crm;
    private String especialidade;
    private List<Consulta> consultas;

    public Veterinario(int id, String nome, String telefone, String email, String crm, String especialidade) {
        super(id, nome, telefone, email);
        this.crm = crm;
        this.especialidade = especialidade;
        this.consultas = new ArrayList<>();
    }

    public void adicionarConsulta(Consulta consulta) {
        consultas.add(consulta);
    }

    // Getters e Setters
    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

    public List<Consulta> getConsultas() { return consultas; }
    public void setConsultas(List<Consulta> consultas) { this.consultas = consultas; }

    @Override
    public String toString() {
        return "Veterinario{" +
                "id=" + getId() +
                ", nome='" + getNome() + '\'' +
                ", crm='" + crm + '\'' +
                ", especialidade='" + especialidade + '\'' +
                '}';
    }
}