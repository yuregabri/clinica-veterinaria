package com.clinica.gui;

import com.clinica.model.*;
import com.clinica.service.SistemaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AnimalPanel extends JPanel {
    private final DefaultTableModel tableModel;
    private final JTextField txtNome = new JTextField(12);
    private final JComboBox<Especie> cmbEspecie = new JComboBox<>(Especie.values());
    private final JTextField txtRaca = new JTextField(12);
    private final JTextField txtIdade = new JTextField(5);
    private final JTextField txtPeso = new JTextField(5);
    private final JComboBox<String> cmbTutor = new JComboBox<>();

    public AnimalPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "Especie", "Raca", "Idade", "Peso", "Tutor"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"Nome:", "Especie:", "Raca:", "Idade:", "Peso:", "Tutor:"};
        JComponent[] fields = {txtNome, cmbEspecie, txtRaca, txtIdade, txtPeso, cmbTutor};
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
                String tutorStr = (String) cmbTutor.getSelectedItem();
                if (tutorStr == null || tutorStr.equals("Nenhum")) {
                    JOptionPane.showMessageDialog(this, "Selecione um tutor!");
                    return;
                }
                int idTutor = Integer.parseInt(tutorStr.split(" - ")[0]);
                Tutor tutor = SistemaService.findTutorById(idTutor);
                if (tutor == null) { JOptionPane.showMessageDialog(this, "Tutor nao encontrado!"); return; }

                SistemaService.cadastrarAnimal(
                        txtNome.getText(),
                        (Especie) cmbEspecie.getSelectedItem(),
                        txtRaca.getText(),
                        Integer.parseInt(txtIdade.getText()),
                        Double.parseDouble(txtPeso.getText()),
                        tutor
                );
                limparCampos();
                atualizarTabela();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Idade e Peso devem ser numeros!");
            }
        });

        btnAtualizar.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um animal!"); return; }
            int id = (int) tableModel.getValueAt(row, 0);
            Animal a = SistemaService.findAnimalById(id);
            if (a != null) {
                try {
                    String tutorStr = (String) cmbTutor.getSelectedItem();
                    Tutor tutor = null;
                    if (tutorStr != null && !tutorStr.equals("Nenhum")) {
                        int idTutor = Integer.parseInt(tutorStr.split(" - ")[0]);
                        tutor = SistemaService.findTutorById(idTutor);
                    }
                    a.setNome(txtNome.getText());
                    a.setEspecie((Especie) cmbEspecie.getSelectedItem());
                    a.setRaca(txtRaca.getText());
                    a.setIdade(Integer.parseInt(txtIdade.getText()));
                    a.setPeso(Double.parseDouble(txtPeso.getText()));
                    if (tutor != null) a.setTutor(tutor);
                    limparCampos();
                    atualizarTabela();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Idade e Peso devem ser numeros!");
                }
            }
        });

        btnRemover.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um animal!"); return; }
            int id = (int) tableModel.getValueAt(row, 0);
            SistemaService.removerAnimal(id);
            limparCampos();
            atualizarTabela();
        });

        btnLimpar.addActionListener(e -> limparCampos());

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtNome.setText(String.valueOf(tableModel.getValueAt(row, 1)));
                txtRaca.setText(String.valueOf(tableModel.getValueAt(row, 3)));
                txtIdade.setText(String.valueOf(tableModel.getValueAt(row, 4)));
                txtPeso.setText(String.valueOf(tableModel.getValueAt(row, 5)).replace("R$ ", ""));
            }
        });

        atualizarTabela();
    }

    public void atualizarTabela() {
        tableModel.setRowCount(0);
        SistemaService.listarAnimais().forEach(a ->
                tableModel.addRow(new Object[]{a.getId(), a.getNome(), a.getEspecie(),
                        a.getRaca(), a.getIdade(), a.getPeso(),
                        a.getTutor() != null ? a.getTutor().getNome() : ""}));
    }

    public void atualizarComboTutor() {
        cmbTutor.removeAllItems();
        cmbTutor.addItem("Nenhum");
        SistemaService.listarTutores().forEach(t -> cmbTutor.addItem(t.getId() + " - " + t.getNome()));
    }

    private void limparCampos() {
        txtNome.setText(""); txtRaca.setText(""); txtIdade.setText(""); txtPeso.setText("");
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) atualizarComboTutor();
    }
}
