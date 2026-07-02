package com.clinica.model;

import java.time.LocalDateTime;

public class Consulta implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private LocalDateTime dataHora;
    private Animal animal;
    private Veterinario veterinario;
    private Diagnostico diagnostico;
    private String observacoes;

    public Consulta(int id, LocalDateTime dataHora, Animal animal, Veterinario veterinario) {
        this.id = id;
        this.dataHora = dataHora;
        this.animal = animal;
        this.veterinario = veterinario;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public Animal getAnimal() { return animal; }
    public void setAnimal(Animal animal) { this.animal = animal; }

    public Veterinario getVeterinario() { return veterinario; }
    public void setVeterinario(Veterinario veterinario) { this.veterinario = veterinario; }

    public Diagnostico getDiagnostico() { return diagnostico; }
    public void setDiagnostico(Diagnostico diagnostico) { this.diagnostico = diagnostico; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    @Override
    public String toString() {
        return "Consulta{" +
                "id=" + id +
                ", dataHora=" + dataHora +
                ", animal=" + (animal != null ? animal.getNome() : "null") +
                ", veterinario=" + (veterinario != null ? veterinario.getNome() : "null") +
                ", diagnostico=" + (diagnostico != null ? diagnostico.getNomeDoenca() : "null") +
                '}';
    }
}