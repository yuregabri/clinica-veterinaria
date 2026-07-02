package com.clinica.gui;

import com.clinica.model.*;
import com.clinica.service.SistemaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AgendamentoRetornoPanel extends JPanel {
    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final DefaultTableModel tableModel;
    private final JTextField txtDataHora = new JTextField(12);
    private final JTextField txtMotivo = new JTextField(20);
    private final JComboBox<String> cmbConsulta = new JComboBox<>();

    public AgendamentoRetornoPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(new String[]{"ID", "Data Agendada", "Motivo", "Consulta"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        formPanel.add(new JLabel("Data (dd/MM/yyyy HH:mm):"), gbc);
        gbc.gridx = 1; formPanel.add(txtDataHora, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Motivo:"), gbc);
        gbc.gridx = 1; formPanel.add(txtMotivo, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Consulta:"), gbc);
        gbc.gridx = 1; formPanel.add(cmbConsulta, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnAgendar = new JButton("Agendar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnRemover = new JButton("Remover");
        JButton btnLimpar = new JButton("Limpar");
        buttonPanel.add(btnAgendar);
        buttonPanel.add(btnAtualizar);
        buttonPanel.add(btnRemover);
        buttonPanel.add(btnLimpar);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        btnAgendar.addActionListener(e -> {
            try {
                LocalDateTime data = LocalDateTime.parse(txtDataHora.getText(), fmt);

                String consultaStr = (String) cmbConsulta.getSelectedItem();
                if (consultaStr == null || consultaStr.equals("Nenhuma")) {
                    JOptionPane.showMessageDialog(this, "Selecione uma consulta!");
                    return;
                }
                int idConsulta = Integer.parseInt(consultaStr.split(" - ")[0]);
                Consulta c = SistemaService.findConsultaById(idConsulta);
                if (c == null) { JOptionPane.showMessageDialog(this, "Consulta nao encontrada!"); return; }

                SistemaService.cadastrarAgendamentoRetorno(data, txtMotivo.getText(), c);
                limparCampos();
                atualizarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Data invalida! Use dd/MM/yyyy HH:mm");
            }
        });

        btnLimpar.addActionListener(e -> limparCampos());

        btnAtualizar.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um agendamento!"); return; }
            int id = (int) tableModel.getValueAt(row, 0);
            AgendamentoRetorno ar = SistemaService.findAgendamentoById(id);
            if (ar != null) {
                try {
                    LocalDateTime data = LocalDateTime.parse(txtDataHora.getText(), fmt);
                    String consultaStr = (String) cmbConsulta.getSelectedItem();
                    if (consultaStr == null || consultaStr.equals("Nenhuma")) {
                        JOptionPane.showMessageDialog(this, "Selecione uma consulta!");
                        return;
                    }
                    int idConsulta = Integer.parseInt(consultaStr.split(" - ")[0]);
                    Consulta c = SistemaService.findConsultaById(idConsulta);
                    if (c == null) { JOptionPane.showMessageDialog(this, "Consulta nao encontrada!"); return; }

                    ar.setDataHoraAgendada(data);
                    ar.setMotivo(txtMotivo.getText());
                    ar.setConsulta(c);
                    limparCampos();
                    atualizarTabela();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Data invalida! Use dd/MM/yyyy HH:mm");
                }
            }
        });

        btnRemover.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um agendamento!"); return; }
            int id = (int) tableModel.getValueAt(row, 0);
            SistemaService.removerAgendamento(id);
            limparCampos();
            atualizarTabela();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtDataHora.setText(String.valueOf(tableModel.getValueAt(row, 1)));
                txtMotivo.setText(String.valueOf(tableModel.getValueAt(row, 2)));
            }
        });

        atualizarTabela();
    }

    public void atualizarComboConsulta() {
        cmbConsulta.removeAllItems();
        cmbConsulta.addItem("Nenhuma");
        SistemaService.listarConsultas().forEach(c ->
                cmbConsulta.addItem(c.getId() + " - Animal: " + c.getAnimal().getNome() + ", Vet: " + c.getVeterinario().getNome()));
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        SistemaService.listarAgendamentos().forEach(a ->
                tableModel.addRow(new Object[]{a.getId(), a.getDataHoraAgendada().format(fmt),
                        a.getMotivo(), a.getConsulta().getId()}));
    }

    private void limparCampos() {
        txtDataHora.setText(""); txtMotivo.setText("");
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) atualizarComboConsulta();
    }
}
