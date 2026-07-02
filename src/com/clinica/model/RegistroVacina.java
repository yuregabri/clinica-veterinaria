package com.clinica.model;

import java.time.LocalDateTime;

public class RegistroVacina implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private LocalDateTime dataAplicacao;
    private LocalDateTime dataProximaDose;
    private Vacina vacina;
    private Animal animal;

    public RegistroVacina(int id, LocalDateTime dataAplicacao, LocalDateTime dataProximaDose, Vacina vacina) {
        this.id = id;
        this.dataAplicacao = dataAplicacao;
        this.dataProximaDose = dataProximaDose;
        this.vacina = vacina;
    }

    public boolean isVencida() {
        return LocalDateTime.now().isAfter(dataProximaDose);
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getDataAplicacao() { return dataAplicacao; }
    public void setDataAplicacao(LocalDateTime dataAplicacao) { this.dataAplicacao = dataAplicacao; }

    public LocalDateTime getDataProximaDose() { return dataProximaDose; }
    public void setDataProximaDose(LocalDateTime dataProximaDose) { this.dataProximaDose = dataProximaDose; }

    public Vacina getVacina() { return vacina; }
    public void setVacina(Vacina vacina) { this.vacina = vacina; }

    public Animal getAnimal() { return animal; }
    public void setAnimal(Animal animal) { this.animal = animal; }

    @Override
    public String toString() {
        return "RegistroVacina{" +
                "id=" + id +
                ", dataAplicacao=" + dataAplicacao +
                ", vacina=" + (vacina != null ? vacina.getNome() : "null") +
                ", vencida=" + isVencida() +
                '}';
    }
}