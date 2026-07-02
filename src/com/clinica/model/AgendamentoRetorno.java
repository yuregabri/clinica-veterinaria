package com.clinica.model;

import java.time.LocalDateTime;

public class AgendamentoRetorno implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private LocalDateTime dataHoraAgendada;
    private String motivo;
    private Consulta consulta;

    public AgendamentoRetorno(int id, LocalDateTime dataHoraAgendada, String motivo, Consulta consulta) {
        this.id = id;
        this.dataHoraAgendada = dataHoraAgendada;
        this.motivo = motivo;
        this.consulta = consulta;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getDataHoraAgendada() { return dataHoraAgendada; }
    public void setDataHoraAgendada(LocalDateTime dataHoraAgendada) { this.dataHoraAgendada = dataHoraAgendada; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public Consulta getConsulta() { return consulta; }
    public void setConsulta(Consulta consulta) { this.consulta = consulta; }

    @Override
    public String toString() {
        return "AgendamentoRetorno{" +
                "id=" + id +
                ", dataHoraAgendada=" + dataHoraAgendada +
                ", motivo='" + motivo + '\'' +
                ", consulta=" + (consulta != null ? consulta.getId() : "null") +
                '}';
    }
}