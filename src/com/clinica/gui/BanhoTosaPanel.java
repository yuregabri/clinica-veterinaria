package com.clinica.gui;

import com.clinica.model.*;
import com.clinica.service.SistemaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BanhoTosaPanel extends JPanel {
    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final DefaultTableModel tableModel;
    private final JTextField txtDataHora = new JTextField(12);
    private final JTextField txtTipoServico = new JTextField(12);
    private final JTextField txtPreco = new JTextField(8);
    private final JComboBox<AppointmentStatus> cmbStatus = new JComboBox<>(AppointmentStatus.values());
    private final JComboBox<String> cmbAnimal = new JComboBox<>();
    private final JComboBox<String> cmbFuncionario = new JComboBox<>();

    public BanhoTosaPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(new String[]{"ID", "Data/Hora", "Servico", "Preco", "Status", "Animal", "Funcionario"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"Data (dd/MM/yyyy HH:mm):", "Tipo Servico:", "Preco:", "Status:", "Animal:", "Funcionario:"};
        JComponent[] fields = {txtDataHora, txtTipoServico, txtPreco, cmbStatus, cmbAnimal, cmbFuncionario};
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            formPanel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            formPanel.add(fields[i], gbc);
        }

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
                double preco = Double.parseDouble(txtPreco.getText());

                String animalStr = (String) cmbAnimal.getSelectedItem();
                if (animalStr == null || animalStr.equals("Nenhum")) {
                    JOptionPane.showMessageDialog(this, "Selecione um animal!");
                    return;
                }
                int idAnimal = Integer.parseInt(animalStr.split(" - ")[0]);
                Animal animal = SistemaService.findAnimalById(idAnimal);
                if (animal == null) { JOptionPane.showMessageDialog(this, "Animal nao encontrado!"); return; }

                String funcStr = (String) cmbFuncionario.getSelectedItem();
                if (funcStr == null || funcStr.equals("Nenhum")) {
                    JOptionPane.showMessageDialog(this, "Selecione um funcionario!");
                    return;
                }
                int idFunc = Integer.parseInt(funcStr.split(" - ")[0]);
                Funcionario func = SistemaService.findFuncionarioById(idFunc);
                if (func == null) { JOptionPane.showMessageDialog(this, "Funcionario nao encontrado!"); return; }

                SistemaService.cadastrarBanhoTosa(data, txtTipoServico.getText(), preco,
                        (AppointmentStatus) cmbStatus.getSelectedItem(), animal, func);
                limparCampos();
                atualizarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Data ou preco invalido!");
            }
        });

        btnLimpar.addActionListener(e -> limparCampos());

        btnAtualizar.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um servico de banho/tosa!"); return; }
            int id = (int) tableModel.getValueAt(row, 0);
            BanhoTosa bt = SistemaService.findBanhoTosaById(id);
            if (bt != null) {
                try {
                    LocalDateTime data = LocalDateTime.parse(txtDataHora.getText(), fmt);
                    double preco = Double.parseDouble(txtPreco.getText());
                    bt.setDataHora(data);
                    bt.setTiposServico(txtTipoServico.getText());
                    bt.setPreco(preco);
                    bt.setStatus((AppointmentStatus) cmbStatus.getSelectedItem());
                    limparCampos();
                    atualizarTabela();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Data ou preco invalido!");
                }
            }
        });

        btnRemover.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um servico de banho/tosa!"); return; }
            int id = (int) tableModel.getValueAt(row, 0);
            SistemaService.removerBanhoTosa(id);
            limparCampos();
            atualizarTabela();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtDataHora.setText(String.valueOf(tableModel.getValueAt(row, 1)));
                txtTipoServico.setText(String.valueOf(tableModel.getValueAt(row, 2)));
                txtPreco.setText(String.valueOf(tableModel.getValueAt(row, 3)).replace("R$ ", ""));
            }
        });

        atualizarTabela();
    }

    public void atualizarCombos() {
        cmbAnimal.removeAllItems();
        cmbAnimal.addItem("Nenhum");
        SistemaService.listarAnimais().forEach(a -> cmbAnimal.addItem(a.getId() + " - " + a.getNome()));

        cmbFuncionario.removeAllItems();
        cmbFuncionario.addItem("Nenhum");
        SistemaService.listarFuncionarios().forEach(f -> cmbFuncionario.addItem(f.getId() + " - " + f.getNome()));
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        SistemaService.listarBanhoTosas().forEach(b ->
                tableModel.addRow(new Object[]{b.getId(), b.getDataHora().format(fmt),
                        b.getTiposServico(), "R$ " + String.format("%.2f", b.getPreco()),
                        b.getStatus(), b.getAnimal().getNome(), b.getFuncionario().getNome()}));
    }

    private void limparCampos() {
        txtDataHora.setText(""); txtTipoServico.setText(""); txtPreco.setText("");
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) atualizarCombos();
    }
}
