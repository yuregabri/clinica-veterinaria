package com.clinica.model;

import java.util.ArrayList;
import java.util.List;

public class Animal implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nome;
    private Especie especie;
    private String raca;
    private int idade;
    private double peso;
    private Tutor tutor;
    private List<Consulta> consultas;
    private List<RegistroVacina> registrosVacina;
    private List<BanhoTosa> servicosBanhoTosa;

    public Animal(int id, String nome, Especie especie, String raca, int idade, double peso) {
        this.id = id;
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
        this.idade = idade;
        this.peso = peso;
        this.consultas = new ArrayList<>();
        this.registrosVacina = new ArrayList<>();
        this.servicosBanhoTosa = new ArrayList<>();
    }

    public void adicionarConsulta(Consulta consulta) {
        consultas.add(consulta);
        consulta.setAnimal(this);
    }

    public void adicionarRegistroVacina(RegistroVacina registro) {
        registrosVacina.add(registro);
        registro.setAnimal(this);
    }

    public void adicionarServicoBanhoTosa(BanhoTosa servico) {
        servicosBanhoTosa.add(servico);
        servico.setAnimal(this);
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Especie getEspecie() { return especie; }
    public void setEspecie(Especie especie) { this.especie = especie; }

    public String getRaca() { return raca; }
    public void setRaca(String raca) { this.raca = raca; }

    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    public Tutor getTutor() { return tutor; }
    public void setTutor(Tutor tutor) { this.tutor = tutor; }

    public List<Consulta> getConsultas() { return consultas; }
    public void setConsultas(List<Consulta> consultas) { this.consultas = consultas; }

    public List<RegistroVacina> getRegistrosVacina() { return registrosVacina; }
    public void setRegistrosVacina(List<RegistroVacina> registrosVacina) { this.registrosVacina = registrosVacina; }

    public List<BanhoTosa> getServicosBanhoTosa() { return servicosBanhoTosa; }
    public void setServicosBanhoTosa(List<BanhoTosa> servicosBanhoTosa) { this.servicosBanhoTosa = servicosBanhoTosa; }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", especie=" + especie +
                ", raca='" + raca + '\'' +
                ", tutor=" + (tutor != null ? tutor.getNome() : "null") +
                '}';
    }
}