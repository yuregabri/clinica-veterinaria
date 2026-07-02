package com.clinica.gui;

import com.clinica.model.*;
import com.clinica.service.SistemaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsultaPanel extends JPanel {
    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final DefaultTableModel tableModel;
    private final JTextField txtDataHora = new JTextField(12);
    private final JComboBox<String> cmbAnimal = new JComboBox<>();
    private final JComboBox<String> cmbVeterinario = new JComboBox<>();

    public ConsultaPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(new String[]{"ID", "Data/Hora", "Animal", "Veterinario", "Diagnostico"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"Data (dd/MM/yyyy HH:mm):", "Animal:", "Veterinario:"};
        JComponent[] fields = {txtDataHora, cmbAnimal, cmbVeterinario};
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

                String animalStr = (String) cmbAnimal.getSelectedItem();
                if (animalStr == null || animalStr.equals("Nenhum")) {
                    JOptionPane.showMessageDialog(this, "Selecione um animal!");
                    return;
                }
                int idAnimal = Integer.parseInt(animalStr.split(" - ")[0]);
                Animal animal = SistemaService.findAnimalById(idAnimal);
                if (animal == null) { JOptionPane.showMessageDialog(this, "Animal nao encontrado!"); return; }

                String vetStr = (String) cmbVeterinario.getSelectedItem();
                if (vetStr == null || vetStr.equals("Nenhum")) {
                    JOptionPane.showMessageDialog(this, "Selecione um veterinario!");
                    return;
                }
                int idVet = Integer.parseInt(vetStr.split(" - ")[0]);
                Veterinario vet = SistemaService.findVeterinarioById(idVet);
                if (vet == null) { JOptionPane.showMessageDialog(this, "Veterinario nao encontrado!"); return; }

                SistemaService.cadastrarConsulta(data, animal, vet);
                limparCampos();
                atualizarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Data invalida! Use dd/MM/yyyy HH:mm");
            }
        });

        btnLimpar.addActionListener(e -> limparCampos());

        btnAtualizar.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione uma consulta!"); return; }
            int id = (int) tableModel.getValueAt(row, 0);
            Consulta c = SistemaService.findConsultaById(id);
            if (c != null) {
                try {
                    LocalDateTime data = LocalDateTime.parse(txtDataHora.getText(), fmt);
                    String animalStr = (String) cmbAnimal.getSelectedItem();
                    if (animalStr == null || animalStr.equals("Nenhum")) {
                        JOptionPane.showMessageDialog(this, "Selecione um animal!");
                        return;
                    }
                    int idAnimal = Integer.parseInt(animalStr.split(" - ")[0]);
                    Animal animal = SistemaService.findAnimalById(idAnimal);
                    if (animal == null) { JOptionPane.showMessageDialog(this, "Animal nao encontrado!"); return; }

                    String vetStr = (String) cmbVeterinario.getSelectedItem();
                    if (vetStr == null || vetStr.equals("Nenhum")) {
                        JOptionPane.showMessageDialog(this, "Selecione um veterinario!");
                        return;
                    }
                    int idVet = Integer.parseInt(vetStr.split(" - ")[0]);
                    Veterinario vet = SistemaService.findVeterinarioById(idVet);
                    if (vet == null) { JOptionPane.showMessageDialog(this, "Veterinario nao encontrado!"); return; }

                    c.setDataHora(data);
                    c.setAnimal(animal);
                    c.setVeterinario(vet);
                    limparCampos();
                    atualizarTabela();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Data invalida! Use dd/MM/yyyy HH:mm");
                }
            }
        });

        btnRemover.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione uma consulta!"); return; }
            int id = (int) tableModel.getValueAt(row, 0);
            SistemaService.removerConsulta(id);
            limparCampos();
            atualizarTabela();
        });

        atualizarTabela();
    }

    public void atualizarCombos() {
        cmbAnimal.removeAllItems();
        cmbAnimal.addItem("Nenhum");
        SistemaService.listarAnimais().forEach(a -> cmbAnimal.addItem(a.getId() + " - " + a.getNome()));

        cmbVeterinario.removeAllItems();
        cmbVeterinario.addItem("Nenhum");
        SistemaService.listarVeterinarios().forEach(v -> cmbVeterinario.addItem(v.getId() + " - " + v.getNome()));
    }

    public void atualizarTabela() {
        tableModel.setRowCount(0);
        SistemaService.listarConsultas().forEach(c ->
                tableModel.addRow(new Object[]{c.getId(), c.getDataHora().format(fmt),
                        c.getAnimal().getNome(), c.getVeterinario().getNome(),
                        c.getDiagnostico() != null ? c.getDiagnostico().getNomeDoenca() : "-"}));
    }

    private void limparCampos() {
        txtDataHora.setText("");
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) atualizarCombos();
    }
}
