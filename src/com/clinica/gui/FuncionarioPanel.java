package com.clinica.gui;

import com.clinica.model.Funcionario;
import com.clinica.service.SistemaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FuncionarioPanel extends JPanel {
    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final DefaultTableModel tableModel;
    private final JTextField txtNome = new JTextField(12);
    private final JTextField txtTelefone = new JTextField(12);
    private final JTextField txtEmail = new JTextField(12);
    private final JTextField txtMatricula = new JTextField(12);
    private final JTextField txtCargo = new JTextField(12);
    private final JTextField txtDataContratacao = new JTextField(12);

    public FuncionarioPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "Telefone", "Email", "Matricula", "Cargo", "Data Contratacao"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"Nome:", "Telefone:", "Email:", "Matricula:", "Cargo:", "Data (dd/MM/yyyy HH:mm):"};
        JTextField[] fields = {txtNome, txtTelefone, txtEmail, txtMatricula, txtCargo, txtDataContratacao};
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            formPanel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            formPanel.add(fields[i], gbc);
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnRemover = new JButton("Remover");
        JButton btnLimpar = new JButton("Limpar");
        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnAtualizar);
        buttonPanel.add(btnRemover);
        buttonPanel.add(btnLimpar);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        btnAdicionar.addActionListener(e -> {
            if (txtNome.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome e obrigatorio!");
                return;
            }
            try {
                LocalDateTime data = LocalDateTime.parse(txtDataContratacao.getText(), fmt);
                SistemaService.cadastrarFuncionario(txtNome.getText(), txtTelefone.getText(),
                        txtEmail.getText(), txtMatricula.getText(), txtCargo.getText(), data);
                limparCampos();
                atualizarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Formato de data invalido! Use dd/MM/yyyy HH:mm");
            }
        });

        btnLimpar.addActionListener(e -> limparCampos());

        btnAtualizar.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um funcionario!"); return; }
            int id = (int) tableModel.getValueAt(row, 0);
            Funcionario f = SistemaService.findFuncionarioById(id);
            if (f != null) {
                try {
                    LocalDateTime data = LocalDateTime.parse(txtDataContratacao.getText(), fmt);
                    f.setNome(txtNome.getText());
                    f.setTelefone(txtTelefone.getText());
                    f.setEmail(txtEmail.getText());
                    f.setMatricula(txtMatricula.getText());
                    f.setCargo(txtCargo.getText());
                    f.setDataContratacao(data);
                    limparCampos();
                    atualizarTabela();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Formato de data invalido! Use dd/MM/yyyy HH:mm");
                }
            }
        });

        btnRemover.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um funcionario!"); return; }
            int id = (int) tableModel.getValueAt(row, 0);
            SistemaService.removerFuncionario(id);
            limparCampos();
            atualizarTabela();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtNome.setText(String.valueOf(tableModel.getValueAt(row, 1)));
                txtTelefone.setText(String.valueOf(tableModel.getValueAt(row, 2)));
                txtEmail.setText(String.valueOf(tableModel.getValueAt(row, 3)));
                txtMatricula.setText(String.valueOf(tableModel.getValueAt(row, 4)));
                txtCargo.setText(String.valueOf(tableModel.getValueAt(row, 5)));
                txtDataContratacao.setText(String.valueOf(tableModel.getValueAt(row, 6)));
            }
        });

        atualizarTabela();
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        SistemaService.listarFuncionarios().forEach(f ->
                tableModel.addRow(new Object[]{f.getId(), f.getNome(), f.getTelefone(),
                        f.getEmail(), f.getMatricula(), f.getCargo(),
                        f.getDataContratacao().format(fmt)}));
    }

    private void limparCampos() {
        txtNome.setText(""); txtTelefone.setText(""); txtEmail.setText("");
        txtMatricula.setText(""); txtCargo.setText(""); txtDataContratacao.setText("");
    }
}
