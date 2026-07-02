package com.clinica.gui;

import com.clinica.model.Tutor;
import com.clinica.service.SistemaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TutorPanel extends JPanel {
    private final DefaultTableModel tableModel;
    private final JTextField txtNome = new JTextField(15);
    private final JTextField txtTelefone = new JTextField(15);
    private final JTextField txtEmail = new JTextField(15);
    private final JTextField txtEndereco = new JTextField(15);

    public TutorPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "Telefone", "Email", "Endereco"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"Nome:", "Telefone:", "Email:", "Endereco:"};
        JTextField[] fields = {txtNome, txtTelefone, txtEmail, txtEndereco};
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
            SistemaService.cadastrarTutor(txtNome.getText(), txtTelefone.getText(),
                    txtEmail.getText(), txtEndereco.getText());
            limparCampos();
            atualizarTabela();
        });

        btnAtualizar.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um tutor!"); return; }
            int id = (int) tableModel.getValueAt(row, 0);
            Tutor t = SistemaService.findTutorById(id);
            if (t != null) {
                t.setNome(txtNome.getText());
                t.setTelefone(txtTelefone.getText());
                t.setEmail(txtEmail.getText());
                t.setEndereco(txtEndereco.getText());
                limparCampos();
                atualizarTabela();
            }
        });

        btnRemover.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um tutor!"); return; }
            int id = (int) tableModel.getValueAt(row, 0);
            SistemaService.removerTutor(id);
            limparCampos();
            atualizarTabela();
        });

        btnLimpar.addActionListener(e -> limparCampos());

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtNome.setText(String.valueOf(tableModel.getValueAt(row, 1)));
                txtTelefone.setText(String.valueOf(tableModel.getValueAt(row, 2)));
                txtEmail.setText(String.valueOf(tableModel.getValueAt(row, 3)));
                txtEndereco.setText(String.valueOf(tableModel.getValueAt(row, 4)));
            }
        });

        atualizarTabela();
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        SistemaService.listarTutores().forEach(t ->
                tableModel.addRow(new Object[]{t.getId(), t.getNome(), t.getTelefone(), t.getEmail(), t.getEndereco()}));
    }

    private void limparCampos() {
        txtNome.setText(""); txtTelefone.setText(""); txtEmail.setText(""); txtEndereco.setText("");
    }
}
