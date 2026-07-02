package com.clinica.gui;

import com.clinica.model.Vacina;
import com.clinica.service.SistemaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VacinaPanel extends JPanel {
    private final DefaultTableModel tableModel;
    private final JTextField txtNome = new JTextField(15);
    private final JTextField txtFabricante = new JTextField(15);
    private final JTextField txtDuracao = new JTextField(8);

    public VacinaPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "Fabricante", "Duracao (meses)"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"Nome:", "Fabricante:", "Duracao (meses):"};
        JTextField[] fields = {txtNome, txtFabricante, txtDuracao};
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            formPanel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            formPanel.add(fields[i], gbc);
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnLimpar = new JButton("Limpar");
        buttonPanel.add(btnAdicionar);
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
                SistemaService.cadastrarVacina(txtNome.getText(), txtFabricante.getText(),
                        Integer.parseInt(txtDuracao.getText()));
                limparCampos();
                atualizarTabela();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Duracao deve ser um numero inteiro!");
            }
        });

        btnLimpar.addActionListener(e -> limparCampos());

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtNome.setText(String.valueOf(tableModel.getValueAt(row, 1)));
                txtFabricante.setText(String.valueOf(tableModel.getValueAt(row, 2)));
                txtDuracao.setText(String.valueOf(tableModel.getValueAt(row, 3)));
            }
        });

        atualizarTabela();
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        SistemaService.listarVacinas().forEach(v ->
                tableModel.addRow(new Object[]{v.getId(), v.getNome(), v.getFabricante(), v.getDuracaoMeses()}));
    }

    private void limparCampos() {
        txtNome.setText(""); txtFabricante.setText(""); txtDuracao.setText("");
    }
}
