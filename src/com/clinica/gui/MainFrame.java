package com.clinica.gui;

import com.clinica.service.SistemaService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Sistema de Gerenciamento - Clinica Veterinaria");
        setSize(1000, 650);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        SistemaService.carregarDados();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Tutores", new TutorPanel());
        tabbedPane.addTab("Animais", new AnimalPanel());
        tabbedPane.addTab("Veterinarios", new VeterinarioPanel());
        tabbedPane.addTab("Funcionarios", new FuncionarioPanel());
        tabbedPane.addTab("Produtos", new ProdutoPanel());
        tabbedPane.addTab("Vacinas", new VacinaPanel());
        tabbedPane.addTab("Consultas", new ConsultaPanel());
        tabbedPane.addTab("Diagnosticos", new DiagnosticoPanel());
        tabbedPane.addTab("Reg. Vacina", new RegistroVacinaPanel());
        tabbedPane.addTab("Banho/Tosa", new BanhoTosaPanel());
        tabbedPane.addTab("Vendas", new VendaPanel());
        tabbedPane.addTab("Retornos", new AgendamentoRetornoPanel());

        add(tabbedPane);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SistemaService.salvarDados();
                dispose();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new MainFrame().setVisible(true);
        });
    }
}
