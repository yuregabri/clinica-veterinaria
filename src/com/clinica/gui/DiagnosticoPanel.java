package com.clinica.gui;

import com.clinica.model.*;
import com.clinica.service.SistemaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DiagnosticoPanel extends JPanel {
    private final DefaultTableModel tableModel;
    private final JTextField txtNomeDoenca = new JTextField(15);
    private final JTextField txtDescricao = new JTextField(20);
    private final JComboBox<String> cmbConsulta = new JComboBox<>();

    public DiagnosticoPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(new String[]{"ID", "Doenca", "Descricao", "Consulta"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        formPanel.add(new JLabel("Doenca:"), gbc);
        gbc.gridx = 1; formPanel.add(txtNomeDoenca, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Descricao:"), gbc);
        gbc.gridx = 1; formPanel.add(txtDescricao, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Consulta:"), gbc);
        gbc.gridx = 1; formPanel.add(cmbConsulta, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnRegistrar = new JButton("Registrar");
        JButton btnLimpar = new JButton("Limpar");
        buttonPanel.add(btnRegistrar);
        buttonPanel.add(btnLimpar);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        btnRegistrar.addActionListener(e -> {
            if (txtNomeDoenca.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome da doenca e obrigatorio!");
                return;
            }
            Diagnostico d = SistemaService.cadastrarDiagnostico(txtNomeDoenca.getText(), txtDescricao.getText());

            String consultaStr = (String) cmbConsulta.getSelectedItem();
            if (consultaStr != null && !consultaStr.equals("Nenhuma")) {
                int idConsulta = Integer.parseInt(consultaStr.split(" - ")[0]);
                Consulta c = SistemaService.findConsultaById(idConsulta);
                if (c != null) c.setDiagnostico(d);
            }

            limparCampos();
            atualizarTabela();
        });

        btnLimpar.addActionListener(e -> limparCampos());

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtNomeDoenca.setText(String.valueOf(tableModel.getValueAt(row, 1)));
                txtDescricao.setText(String.valueOf(tableModel.getValueAt(row, 2)));
            }
        });

        atualizarTabela();
    }

    public void atualizarComboConsulta() {
        cmbConsulta.removeAllItems();
        cmbConsulta.addItem("Nenhuma");
        SistemaService.listarConsultas().forEach(c ->
                cmbConsulta.addItem(c.getId() + " - Animal: " + c.getAnimal().getNome()));
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        SistemaService.listarDiagnosticos().forEach(d -> {
            String consulta = "-";
            for (Consulta c : SistemaService.listarConsultas()) {
                if (c.getDiagnostico() != null && c.getDiagnostico().getId() == d.getId()) {
                    consulta = "ID " + c.getId();
                    break;
                }
            }
            tableModel.addRow(new Object[]{d.getId(), d.getNomeDoenca(), d.getDescricao(), consulta});
        });
    }

    private void limparCampos() {
        txtNomeDoenca.setText(""); txtDescricao.setText("");
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) atualizarComboConsulta();
    }
}
