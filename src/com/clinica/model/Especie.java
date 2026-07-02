package com.clinica.model;

public enum Especie {
    CACHORRO("Cachorro"),
    GATO("Gato");

    private String descricao;

    Especie(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}