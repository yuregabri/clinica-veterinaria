package com.clinica.gui;

import com.clinica.model.Produto;
import com.clinica.service.SistemaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProdutoPanel extends JPanel {
    private final DefaultTableModel tableModel;
    private final JTextField txtNome = new JTextField(12);
    private final JTextField txtDescricao = new JTextField(12);
    private final JTextField txtPreco = new JTextField(8);
    private final JTextField txtQuantidade = new JTextField(8);
    private final JTextField txtCategoria = new JTextField(12);

    public ProdutoPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "Descricao", "Preco", "Estoque", "Categoria"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"Nome:", "Descricao:", "Preco:", "Estoque:", "Categoria:"};
        JComponent[] fields = {txtNome, txtDescricao, txtPreco, txtQuantidade, txtCategoria};
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
                SistemaService.cadastrarProduto(txtNome.getText(), txtDescricao.getText(),
                        Double.parseDouble(txtPreco.getText()),
                        Integer.parseInt(txtQuantidade.getText()),
                        txtCategoria.getText());
                limparCampos();
                atualizarTabela();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Preco e Estoque devem ser numeros!");
            }
        });

        btnLimpar.addActionListener(e -> limparCampos());

        btnAtualizar.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um produto!"); return; }
            int id = (int) tableModel.getValueAt(row, 0);
            Produto p = SistemaService.findProdutoById(id);
            if (p != null) {
                try {
                    p.setNome(txtNome.getText());
                    p.setDescricao(txtDescricao.getText());
                    p.setPreco(Double.parseDouble(txtPreco.getText()));
                    p.setQuantidadeEstoque(Integer.parseInt(txtQuantidade.getText()));
                    p.setCategoria(txtCategoria.getText());
                    limparCampos();
                    atualizarTabela();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Preco e Estoque devem ser numeros!");
                }
            }
        });

        btnRemover.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um produto!"); return; }
            int id = (int) tableModel.getValueAt(row, 0);
            SistemaService.removerProduto(id);
            limparCampos();
            atualizarTabela();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtNome.setText(String.valueOf(tableModel.getValueAt(row, 1)));
                txtDescricao.setText(String.valueOf(tableModel.getValueAt(row, 2)));
                txtPreco.setText(String.valueOf(tableModel.getValueAt(row, 3)));
                txtQuantidade.setText(String.valueOf(tableModel.getValueAt(row, 4)));
                txtCategoria.setText(String.valueOf(tableModel.getValueAt(row, 5)));
            }
        });

        atualizarTabela();
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        SistemaService.listarProdutos().forEach(p ->
                tableModel.addRow(new Object[]{p.getId(), p.getNome(), p.getDescricao(),
                        "R$ " + String.format("%.2f", p.getPreco()),
                        p.getQuantidadeEstoque(), p.getCategoria()}));
    }

    private void limparCampos() {
        txtNome.setText(""); txtDescricao.setText(""); txtPreco.setText("");
        txtQuantidade.setText(""); txtCategoria.setText("");
    }
}
