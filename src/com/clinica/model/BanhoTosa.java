package com.clinica.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BanhoTosa implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private LocalDateTime dataHora;
    private String tiposServico;
    private double preco;
    private AppointmentStatus status;
    private Animal animal;
    private Funcionario funcionario;
    private List<Produto> produtosUsados;

    public BanhoTosa(int id, LocalDateTime dataHora, String tiposServico, double preco, AppointmentStatus status) {
        this.id = id;
        this.dataHora = dataHora;
        this.tiposServico = tiposServico;
        this.preco = preco;
        this.status = status;
        this.produtosUsados = new ArrayList<>();
    }

    // Métodos do diagrama (implementados com lógica vazia, pois não há listas correspondentes)
    public void adicionarRegistroVacina(RegistroVacina registro) {
        // Este método não faz sentido em BanhoTosa, mas está no diagrama.
        // Vamos apenas logar ou ignorar.
        System.out.println("Método adicionarRegistroVacina chamado em BanhoTosa (ignorado).");
    }

    public void adicionarServicoBanhoTosa(BanhoTosa servico) {
        // Também sem sentido. Ignorar.
        System.out.println("Método adicionarServicoBanhoTosa chamado em BanhoTosa (ignorado).");
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public String getTiposServico() { return tiposServico; }
    public void setTiposServico(String tiposServico) { this.tiposServico = tiposServico; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }

    public Animal getAnimal() { return animal; }
    public void setAnimal(Animal animal) { this.animal = animal; }

    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }

    public List<Produto> getProdutosUsados() { return produtosUsados; }
    public void setProdutosUsados(List<Produto> produtosUsados) { this.produtosUsados = produtosUsados; }

    @Override
    public String toString() {
        return "BanhoTosa{" +
                "id=" + id +
                ", dataHora=" + dataHora +
                ", tipo='" + tiposServico + '\'' +
                ", preco=" + preco +
                ", status=" + status +
                ", animal=" + (animal != null ? animal.getNome() : "null") +
                ", funcionario=" + (funcionario != null ? funcionario.getNome() : "null") +
                '}';
    }
}