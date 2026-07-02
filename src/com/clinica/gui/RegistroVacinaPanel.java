package com.clinica.gui;

import com.clinica.model.*;
import com.clinica.service.SistemaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegistroVacinaPanel extends JPanel {
    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final DefaultTableModel tableModel;
    private final JTextField txtDataAplicacao = new JTextField(12);
    private final JTextField txtDataProxima = new JTextField(12);
    private final JComboBox<String> cmbVacina = new JComboBox<>();
    private final JComboBox<String> cmbAnimal = new JComboBox<>();

    public RegistroVacinaPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(new String[]{"ID", "Aplicacao", "Proxima Dose", "Vacina", "Animal", "Vencida?"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"Aplicacao (dd/MM/yyyy HH:mm):", "Proxima Dose:", "Vacina:", "Animal:"};
        JComponent[] fields = {txtDataAplicacao, txtDataProxima, cmbVacina, cmbAnimal};
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            formPanel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            formPanel.add(fields[i], gbc);
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnRegistrar = new JButton("Registrar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnRemover = new JButton("Remover");
        JButton btnLimpar = new JButton("Limpar");
        buttonPanel.add(btnRegistrar);
        buttonPanel.add(btnAtualizar);
        buttonPanel.add(btnRemover);
        buttonPanel.add(btnLimpar);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        btnRegistrar.addActionListener(e -> {
            try {
                LocalDateTime dataAplicacao = LocalDateTime.parse(txtDataAplicacao.getText(), fmt);
                LocalDateTime dataProxima = LocalDateTime.parse(txtDataProxima.getText(), fmt);

                String vacinaStr = (String) cmbVacina.getSelectedItem();
                if (vacinaStr == null || vacinaStr.equals("Nenhuma")) {
                    JOptionPane.showMessageDialog(this, "Selecione uma vacina!");
                    return;
                }
                int idVacina = Integer.parseInt(vacinaStr.split(" - ")[0]);
                Vacina vacina = SistemaService.findVacinaById(idVacina);
                if (vacina == null) { JOptionPane.showMessageDialog(this, "Vacina nao encontrada!"); return; }

                String animalStr = (String) cmbAnimal.getSelectedItem();
                if (animalStr == null || animalStr.equals("Nenhum")) {
                    JOptionPane.showMessageDialog(this, "Selecione um animal!");
                    return;
                }
                int idAnimal = Integer.parseInt(animalStr.split(" - ")[0]);
                Animal animal = SistemaService.findAnimalById(idAnimal);
                if (animal == null) { JOptionPane.showMessageDialog(this, "Animal nao encontrado!"); return; }

                SistemaService.cadastrarRegistroVacina(dataAplicacao, dataProxima, vacina, animal);
                limparCampos();
                atualizarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Data invalida! Use dd/MM/yyyy HH:mm");
            }
        });

        btnLimpar.addActionListener(e -> limparCampos());

        btnAtualizar.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um registro de vacina!"); return; }
            int id = (int) tableModel.getValueAt(row, 0);
            RegistroVacina rv = SistemaService.findRegistroVacinaById(id);
            if (rv != null) {
                try {
                    LocalDateTime dataAplicacao = LocalDateTime.parse(txtDataAplicacao.getText(), fmt);
                    LocalDateTime dataProxima = LocalDateTime.parse(txtDataProxima.getText(), fmt);
                    rv.setDataAplicacao(dataAplicacao);
                    rv.setDataProximaDose(dataProxima);
                    limparCampos();
                    atualizarTabela();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Data invalida! Use dd/MM/yyyy HH:mm");
                }
            }
        });

        btnRemover.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um registro de vacina!"); return; }
            int id = (int) tableModel.getValueAt(row, 0);
            SistemaService.removerRegistroVacina(id);
            limparCampos();
            atualizarTabela();
        });

        atualizarTabela();
    }

    public void atualizarCombos() {
        cmbVacina.removeAllItems();
        cmbVacina.addItem("Nenhuma");
        SistemaService.listarVacinas().forEach(v -> cmbVacina.addItem(v.getId() + " - " + v.getNome()));

        cmbAnimal.removeAllItems();
        cmbAnimal.addItem("Nenhum");
        SistemaService.listarAnimais().forEach(a -> cmbAnimal.addItem(a.getId() + " - " + a.getNome()));
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        SistemaService.listarRegistrosVacina().forEach(r ->
                tableModel.addRow(new Object[]{r.getId(), r.getDataAplicacao().format(fmt),
                        r.getDataProximaDose().format(fmt),
                        r.getVacina().getNome(),
                        r.getAnimal() != null ? r.getAnimal().getNome() : "-",
                        r.isVencida() ? "SIM" : "NAO"}));
    }

    private void limparCampos() {
        txtDataAplicacao.setText(""); txtDataProxima.setText("");
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) atualizarCombos();
    }
}
