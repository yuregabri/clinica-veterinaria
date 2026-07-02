package com.clinica.util;

public class GeradorId implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private static int contador = 1;

    public static int proximoId() {
        return contador++;
    }

    public static void resetar() {
        contador = 1;
    }

    public static int getContador() {
        return contador;
    }

    public static void setContador(int valor) {
        contador = valor;
    }
}
