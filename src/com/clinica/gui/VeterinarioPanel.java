package com.clinica.gui;

import com.clinica.model.Veterinario;
import com.clinica.service.SistemaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VeterinarioPanel extends JPanel {
    private final DefaultTableModel tableModel;
    private final JTextField txtNome = new JTextField(15);
    private final JTextField txtTelefone = new JTextField(15);
    private final JTextField txtEmail = new JTextField(15);
    private final JTextField txtCrm = new JTextField(15);
    private final JTextField txtEspecialidade = new JTextField(15);

    public VeterinarioPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "Telefone", "Email", "CRM", "Especialidade"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"Nome:", "Telefone:", "Email:", "CRM:", "Especialidade:"};
        JTextField[] fields = {txtNome, txtTelefone, txtEmail, txtCrm, txtEspecialidade};
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
            if (txtNome.getText().isEmpty() || txtCrm.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome e CRM sao obrigatorios!");
                return;
            }
            SistemaService.cadastrarVeterinario(txtNome.getText(), txtTelefone.getText(),
                    txtEmail.getText(), txtCrm.getText(), txtEspecialidade.getText());
            limparCampos();
            atualizarTabela();
        });

        btnLimpar.addActionListener(e -> limparCampos());

        btnAtualizar.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um veterinario!"); return; }
            int id = (int) tableModel.getValueAt(row, 0);
            Veterinario v = SistemaService.findVeterinarioById(id);
            if (v != null) {
                v.setNome(txtNome.getText());
                v.setTelefone(txtTelefone.getText());
                v.setEmail(txtEmail.getText());
                v.setCrm(txtCrm.getText());
                v.setEspecialidade(txtEspecialidade.getText());
                limparCampos();
                atualizarTabela();
            }
        });

        btnRemover.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um veterinario!"); return; }
            int id = (int) tableModel.getValueAt(row, 0);
            SistemaService.removerVeterinario(id);
            limparCampos();
            atualizarTabela();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtNome.setText(String.valueOf(tableModel.getValueAt(row, 1)));
                txtTelefone.setText(String.valueOf(tableModel.getValueAt(row, 2)));
                txtEmail.setText(String.valueOf(tableModel.getValueAt(row, 3)));
                txtCrm.setText(String.valueOf(tableModel.getValueAt(row, 4)));
                txtEspecialidade.setText(String.valueOf(tableModel.getValueAt(row, 5)));
            }
        });

        atualizarTabela();
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        SistemaService.listarVeterinarios().forEach(v ->
                tableModel.addRow(new Object[]{v.getId(), v.getNome(), v.getTelefone(),
                        v.getEmail(), v.getCrm(), v.getEspecialidade()}));
    }

    private void limparCampos() {
        txtNome.setText(""); txtTelefone.setText(""); txtEmail.setText("");
        txtCrm.setText(""); txtEspecialidade.setText("");
    }
}
