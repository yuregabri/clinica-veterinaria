package com.clinica.gui;

import com.clinica.model.*;
import com.clinica.service.SistemaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VendaPanel extends JPanel {
    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final DefaultTableModel tableModel;
    private final DefaultTableModel produtosTableModel;
    private final JComboBox<String> cmbFuncionario = new JComboBox<>();
    private final JComboBox<String> cmbTutor = new JComboBox<>();
    private final JComboBox<String> cmbProduto = new JComboBox<>();
    private final JTextField txtQuantidade = new JTextField(5);
    private final JLabel lblTotal = new JLabel("Total: R$ 0.00");
    private Venda vendaAtual;

    public VendaPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(new String[]{"ID", "Data/Hora", "Funcionario", "Tutor", "Total"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);

        produtosTableModel = new DefaultTableModel(new String[]{"Produto", "Preco"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tableProdutos = new JTable(produtosTableModel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(table), new JScrollPane(tableProdutos));
        splitPane.setDividerLocation(200);
        add(splitPane, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Funcionario:"), gbc);
        gbc.gridx = 1; formPanel.add(cmbFuncionario, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Tutor:"), gbc);
        gbc.gridx = 1; formPanel.add(cmbTutor, gbc);

        JPanel produtoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        produtoPanel.add(new JLabel("Produto:"));
        produtoPanel.add(cmbProduto);
        produtoPanel.add(new JLabel("Qtd:"));
        produtoPanel.add(txtQuantidade);
        JButton btnAddProduto = new JButton("+");
        produtoPanel.add(btnAddProduto);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnNovaVenda = new JButton("Nova Venda");
        JButton btnFinalizar = new JButton("Finalizar Venda");
        JButton btnLimpar = new JButton("Limpar");
        buttonPanel.add(btnNovaVenda);
        buttonPanel.add(btnFinalizar);
        buttonPanel.add(btnLimpar);
        buttonPanel.add(lblTotal);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.NORTH);
        topPanel.add(produtoPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        btnNovaVenda.addActionListener(e -> {
            try {
                String funcStr = (String) cmbFuncionario.getSelectedItem();
                String tutorStr = (String) cmbTutor.getSelectedItem();
                if (funcStr == null || funcStr.equals("Nenhum") || tutorStr == null || tutorStr.equals("Nenhum")) {
                    JOptionPane.showMessageDialog(this, "Selecione funcionario e tutor!");
                    return;
                }
                int idFunc = Integer.parseInt(funcStr.split(" - ")[0]);
                int idTutor = Integer.parseInt(tutorStr.split(" - ")[0]);
                Funcionario func = SistemaService.findFuncionarioById(idFunc);
                Tutor tutor = SistemaService.findTutorById(idTutor);
                if (func == null || tutor == null) {
                    JOptionPane.showMessageDialog(this, "Funcionario ou tutor nao encontrado!");
                    return;
                }
                vendaAtual = SistemaService.cadastrarVenda(LocalDateTime.now(), func, tutor);
                produtosTableModel.setRowCount(0);
                lblTotal.setText("Total: R$ 0.00");
                JOptionPane.showMessageDialog(this, "Venda criada! Adicione produtos e finalize.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao criar venda!");
            }
        });

        btnAddProduto.addActionListener(e -> {
            if (vendaAtual == null) {
                JOptionPane.showMessageDialog(this, "Crie uma venda primeiro!");
                return;
            }
            try {
                String prodStr = (String) cmbProduto.getSelectedItem();
                if (prodStr == null || prodStr.equals("Nenhum")) {
                    JOptionPane.showMessageDialog(this, "Selecione um produto!");
                    return;
                }
                int idProd = Integer.parseInt(prodStr.split(" - ")[0]);
                Produto p = SistemaService.findProdutoById(idProd);
                if (p == null) { JOptionPane.showMessageDialog(this, "Produto nao encontrado!"); return; }
                int qtd = Integer.parseInt(txtQuantidade.getText());
                p.reduzirEstoque(qtd);
                vendaAtual.adicionarProduto(p);
                produtosTableModel.addRow(new Object[]{p.getNome(), "R$ " + String.format("%.2f", p.getPreco())});
                lblTotal.setText("Total: R$ " + String.format("%.2f", vendaAtual.getValorTotal()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantidade invalida!");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        btnFinalizar.addActionListener(e -> {
            if (vendaAtual == null) {
                JOptionPane.showMessageDialog(this, "Nenhuma venda em andamento!");
                return;
            }
            vendaAtual.calcularTotal();
            JOptionPane.showMessageDialog(this, "Venda finalizada! Total: R$ " + String.format("%.2f", vendaAtual.getValorTotal()));
            vendaAtual = null;
            produtosTableModel.setRowCount(0);
            lblTotal.setText("Total: R$ 0.00");
            atualizarTabela();
        });

        btnLimpar.addActionListener(e -> {
            cmbFuncionario.setSelectedIndex(0);
            cmbTutor.setSelectedIndex(0);
            cmbProduto.setSelectedIndex(0);
            txtQuantidade.setText("");
            vendaAtual = null;
            produtosTableModel.setRowCount(0);
            lblTotal.setText("Total: R$ 0.00");
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) tableModel.getValueAt(row, 0);
                Venda v = SistemaService.listarVendas().stream().filter(x -> x.getId() == id).findFirst().orElse(null);
                if (v != null) {
                    produtosTableModel.setRowCount(0);
                    v.getProdutos().forEach(p -> produtosTableModel.addRow(new Object[]{p.getNome(), "R$ " + String.format("%.2f", p.getPreco())}));
                }
            }
        });

        atualizarTabela();
    }

    public void atualizarCombos() {
        cmbFuncionario.removeAllItems();
        cmbFuncionario.addItem("Nenhum");
        SistemaService.listarFuncionarios().forEach(f -> cmbFuncionario.addItem(f.getId() + " - " + f.getNome()));

        cmbTutor.removeAllItems();
        cmbTutor.addItem("Nenhum");
        SistemaService.listarTutores().forEach(t -> cmbTutor.addItem(t.getId() + " - " + t.getNome()));

        cmbProduto.removeAllItems();
        cmbProduto.addItem("Nenhum");
        SistemaService.listarProdutos().forEach(p -> cmbProduto.addItem(p.getId() + " - " + p.getNome() + " (R$ " + String.format("%.2f", p.getPreco()) + ")"));
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        SistemaService.listarVendas().forEach(v ->
                tableModel.addRow(new Object[]{v.getId(), v.getDataHora().format(fmt),
                        v.getFuncionario().getNome(), v.getTutor().getNome(),
                        "R$ " + String.format("%.2f", v.getValorTotal())}));
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) atualizarCombos();
    }
}
