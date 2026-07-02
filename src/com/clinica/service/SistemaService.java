package com.clinica.service;

import com.clinica.model.*;
import com.clinica.util.GeradorId;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SistemaService {
    private static final String ARQUIVO_DADOS = "clinica_dados.dat";

    private static final List<Tutor> tutores = new ArrayList<>();
    private static final List<Veterinario> veterinarios = new ArrayList<>();
    private static final List<Funcionario> funcionarios = new ArrayList<>();
    private static final List<Animal> animais = new ArrayList<>();
    private static final List<Consulta> consultas = new ArrayList<>();
    private static final List<Diagnostico> diagnosticos = new ArrayList<>();
    private static final List<Vacina> vacinas = new ArrayList<>();
    private static final List<RegistroVacina> registrosVacina = new ArrayList<>();
    private static final List<Produto> produtos = new ArrayList<>();
    private static final List<Venda> vendas = new ArrayList<>();
    private static final List<BanhoTosa> banhoTosas = new ArrayList<>();
    private static final List<AgendamentoRetorno> agendamentos = new ArrayList<>();

    // --- Persistencia ---

    @SuppressWarnings("unchecked")
    public static void carregarDados() {
        File file = new File(ARQUIVO_DADOS);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            tutores.clear();
            tutores.addAll((List<Tutor>) ois.readObject());
            veterinarios.clear();
            veterinarios.addAll((List<Veterinario>) ois.readObject());
            funcionarios.clear();
            funcionarios.addAll((List<Funcionario>) ois.readObject());
            animais.clear();
            animais.addAll((List<Animal>) ois.readObject());
            consultas.clear();
            consultas.addAll((List<Consulta>) ois.readObject());
            diagnosticos.clear();
            diagnosticos.addAll((List<Diagnostico>) ois.readObject());
            vacinas.clear();
            vacinas.addAll((List<Vacina>) ois.readObject());
            registrosVacina.clear();
            registrosVacina.addAll((List<RegistroVacina>) ois.readObject());
            produtos.clear();
            produtos.addAll((List<Produto>) ois.readObject());
            vendas.clear();
            vendas.addAll((List<Venda>) ois.readObject());
            banhoTosas.clear();
            banhoTosas.addAll((List<BanhoTosa>) ois.readObject());
            agendamentos.clear();
            agendamentos.addAll((List<AgendamentoRetorno>) ois.readObject());
            GeradorId.setContador((int) ois.readObject());
        } catch (Exception e) {
            System.out.println("Erro ao carregar dados: " + e.getMessage());
        }
    }

    public static void salvarDados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS))) {
            oos.writeObject(tutores);
            oos.writeObject(veterinarios);
            oos.writeObject(funcionarios);
            oos.writeObject(animais);
            oos.writeObject(consultas);
            oos.writeObject(diagnosticos);
            oos.writeObject(vacinas);
            oos.writeObject(registrosVacina);
            oos.writeObject(produtos);
            oos.writeObject(vendas);
            oos.writeObject(banhoTosas);
            oos.writeObject(agendamentos);
            oos.writeObject(GeradorId.getContador());
        } catch (Exception e) {
            System.out.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    // --- Metodos de cadastro ---

    public static Tutor cadastrarTutor(String nome, String telefone, String email, String endereco) {
        Tutor t = new Tutor(GeradorId.proximoId(), nome, telefone, email, endereco);
        tutores.add(t);
        return t;
    }

    public static Veterinario cadastrarVeterinario(String nome, String telefone, String email, String crm, String especialidade) {
        Veterinario v = new Veterinario(GeradorId.proximoId(), nome, telefone, email, crm, especialidade);
        veterinarios.add(v);
        return v;
    }

    public static Funcionario cadastrarFuncionario(String nome, String telefone, String email,
                                                   String matricula, String cargo, LocalDateTime dataContratacao) {
        Funcionario f = new Funcionario(GeradorId.proximoId(), nome, telefone, email, matricula, cargo, dataContratacao);
        funcionarios.add(f);
        return f;
    }

    public static Animal cadastrarAnimal(String nome, Especie especie, String raca, int idade, double peso, Tutor tutor) {
        Animal a = new Animal(GeradorId.proximoId(), nome, especie, raca, idade, peso);
        a.setTutor(tutor);
        tutor.adicionarAnimal(a);
        animais.add(a);
        return a;
    }

    public static Consulta cadastrarConsulta(LocalDateTime dataHora, Animal animal, Veterinario veterinario) {
        Consulta c = new Consulta(GeradorId.proximoId(), dataHora, animal, veterinario);
        animal.adicionarConsulta(c);
        veterinario.adicionarConsulta(c);
        consultas.add(c);
        return c;
    }

    public static Diagnostico cadastrarDiagnostico(String nomeDoenca, String descricao) {
        Diagnostico d = new Diagnostico(GeradorId.proximoId(), nomeDoenca, descricao);
        diagnosticos.add(d);
        return d;
    }

    public static Vacina cadastrarVacina(String nome, String fabricante, int duracaoMeses) {
        Vacina v = new Vacina(GeradorId.proximoId(), nome, fabricante, duracaoMeses);
        vacinas.add(v);
        return v;
    }

    public static RegistroVacina cadastrarRegistroVacina(LocalDateTime dataAplicacao, LocalDateTime dataProximaDose,
                                                         Vacina vacina, Animal animal) {
        RegistroVacina rv = new RegistroVacina(GeradorId.proximoId(), dataAplicacao, dataProximaDose, vacina);
        animal.adicionarRegistroVacina(rv);
        registrosVacina.add(rv);
        return rv;
    }

    public static Produto cadastrarProduto(String nome, String descricao, double preco, int quantidade, String categoria) {
        Produto p = new Produto(GeradorId.proximoId(), nome, descricao, preco, quantidade, categoria);
        produtos.add(p);
        return p;
    }

    public static Venda cadastrarVenda(LocalDateTime dataHora, Funcionario funcionario, Tutor tutor) {
        Venda v = new Venda(GeradorId.proximoId(), dataHora, funcionario, tutor);
        funcionario.adicionarVenda(v);
        vendas.add(v);
        return v;
    }

    public static BanhoTosa cadastrarBanhoTosa(LocalDateTime dataHora, String tipoServico, double preco,
                                               AppointmentStatus status, Animal animal, Funcionario funcionario) {
        BanhoTosa bt = new BanhoTosa(GeradorId.proximoId(), dataHora, tipoServico, preco, status);
        animal.adicionarServicoBanhoTosa(bt);
        funcionario.adicionarServicoBanhoTosa(bt);
        banhoTosas.add(bt);
        return bt;
    }

    public static AgendamentoRetorno cadastrarAgendamentoRetorno(LocalDateTime dataHoraAgendada, String motivo, Consulta consulta) {
        AgendamentoRetorno ar = new AgendamentoRetorno(GeradorId.proximoId(), dataHoraAgendada, motivo, consulta);
        agendamentos.add(ar);
        return ar;
    }

    // --- Metodos de listagem ---

    public static List<Tutor> listarTutores() { return new ArrayList<>(tutores); }
    public static List<Veterinario> listarVeterinarios() { return new ArrayList<>(veterinarios); }
    public static List<Funcionario> listarFuncionarios() { return new ArrayList<>(funcionarios); }
    public static List<Animal> listarAnimais() { return new ArrayList<>(animais); }
    public static List<Consulta> listarConsultas() { return new ArrayList<>(consultas); }
    public static List<Diagnostico> listarDiagnosticos() { return new ArrayList<>(diagnosticos); }
    public static List<Vacina> listarVacinas() { return new ArrayList<>(vacinas); }
    public static List<RegistroVacina> listarRegistrosVacina() { return new ArrayList<>(registrosVacina); }
    public static List<Produto> listarProdutos() { return new ArrayList<>(produtos); }
    public static List<Venda> listarVendas() { return new ArrayList<>(vendas); }
    public static List<BanhoTosa> listarBanhoTosas() { return new ArrayList<>(banhoTosas); }
    public static List<AgendamentoRetorno> listarAgendamentos() { return new ArrayList<>(agendamentos); }

    // --- Metodos de remocao ---

    public static boolean removerTutor(int id) {
        return tutores.removeIf(t -> t.getId() == id);
    }

    public static boolean removerAnimal(int id) {
        return animais.removeIf(a -> a.getId() == id);
    }

    public static boolean removerVeterinario(int id) {
        return veterinarios.removeIf(v -> v.getId() == id);
    }

    public static boolean removerFuncionario(int id) {
        return funcionarios.removeIf(f -> f.getId() == id);
    }

    public static boolean removerConsulta(int id) {
        return consultas.removeIf(c -> c.getId() == id);
    }

    public static boolean removerBanhoTosa(int id) {
        return banhoTosas.removeIf(b -> b.getId() == id);
    }

    public static boolean removerAgendamento(int id) {
        return agendamentos.removeIf(a -> a.getId() == id);
    }

    public static boolean removerProduto(int id) {
        return produtos.removeIf(p -> p.getId() == id);
    }

    public static boolean removerRegistroVacina(int id) {
        return registrosVacina.removeIf(r -> r.getId() == id);
    }

    // --- Metodos de busca por ID ---

    public static Tutor findTutorById(int id) {
        return tutores.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    public static Veterinario findVeterinarioById(int id) {
        return veterinarios.stream().filter(v -> v.getId() == id).findFirst().orElse(null);
    }

    public static Funcionario findFuncionarioById(int id) {
        return funcionarios.stream().filter(f -> f.getId() == id).findFirst().orElse(null);
    }

    public static Animal findAnimalById(int id) {
        return animais.stream().filter(a -> a.getId() == id).findFirst().orElse(null);
    }

    public static Consulta findConsultaById(int id) {
        return consultas.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    public static Diagnostico findDiagnosticoById(int id) {
        return diagnosticos.stream().filter(d -> d.getId() == id).findFirst().orElse(null);
    }

    public static Vacina findVacinaById(int id) {
        return vacinas.stream().filter(v -> v.getId() == id).findFirst().orElse(null);
    }

    public static Produto findProdutoById(int id) {
        return produtos.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    public static BanhoTosa findBanhoTosaById(int id) {
        return banhoTosas.stream().filter(b -> b.getId() == id).findFirst().orElse(null);
    }

    public static AgendamentoRetorno findAgendamentoById(int id) {
        return agendamentos.stream().filter(a -> a.getId() == id).findFirst().orElse(null);
    }

    public static RegistroVacina findRegistroVacinaById(int id) {
        return registrosVacina.stream().filter(r -> r.getId() == id).findFirst().orElse(null);
    }
}
