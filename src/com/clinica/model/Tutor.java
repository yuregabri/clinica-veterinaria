package com.clinica.model;

import java.util.ArrayList;
import java.util.List;

public class Tutor extends Pessoas {
    private String endereco;
    private List<Animal> animais;

    public Tutor(int id, String nome, String telefone, String email, String endereco) {
        super(id, nome, telefone, email);
        this.endereco = endereco;
        this.animais = new ArrayList<>();
    }

    public void adicionarAnimal(Animal animal) {
        animais.add(animal);
        animal.setTutor(this); // atualiza a referência inversa
    }

    // Getters e Setters
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public List<Animal> getAnimais() { return animais; }
    public void setAnimais(List<Animal> animais) { this.animais = animais; }

    @Override
    public String toString() {
        return "Tutor{" +
                "id=" + getId() +
                ", nome='" + getNome() + '\'' +
                ", endereco='" + endereco + '\'' +
                '}';
    }
}