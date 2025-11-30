package com.example.pooestoque;

import com.example.pooestoque.dao.ProdutoDAO;
import com.example.pooestoque.model.Produto;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private TableView<Produto> tabela = new TableView<>();
    private TextField txtId = new TextField();
    private TextField txtNome = new TextField();
    private TextField txtPreco = new TextField();
    private TextField txtQuantidade = new TextField();

    @Override
    public void start(Stage stage) {
        stage.setTitle("Sistema de Estoque - P2");

        // Configuração dos Inputs
        txtId.setEditable(false);
        txtId.setPromptText("ID (Auto)");
        txtNome.setPromptText("Nome do Produto");
        txtPreco.setPromptText("Preço (0.00)");
        txtQuantidade.setPromptText("Quantidade");

        // Botões
        Button btnAdd = new Button("Adicionar");
        Button btnUpd = new Button("Atualizar");
        Button btnDel = new Button("Excluir");
        Button btnLimpar = new Button("Limpar");

        // Ações dos Botões
        btnAdd.setOnAction(e -> acaoAdicionar());
        btnUpd.setOnAction(e -> acaoAtualizar());
        btnDel.setOnAction(e -> acaoExcluir());
        btnLimpar.setOnAction(e -> limparCampos());

        // Configuração da Tabela
        TableColumn<Produto, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Produto, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colNome.setMinWidth(150);

        TableColumn<Produto, Double> colPreco = new TableColumn<>("Preço");
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));

        TableColumn<Produto, Integer> colQtd = new TableColumn<>("Qtd");
        colQtd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        tabela.getColumns().addAll(colId, colNome, colPreco, colQtd);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Preencher campos ao clicar na tabela
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, old, novo) -> {
            if (novo != null) {
                txtId.setText(String.valueOf(novo.getId()));
                txtNome.setText(novo.getNome());
                txtPreco.setText(String.valueOf(novo.getPreco()));
                txtQuantidade.setText(String.valueOf(novo.getQuantidade()));
            }
        });

        // Carregar dados iniciais do CSV
        atualizarTabela();

        // Layout (Organização Visual)
        VBox painelEsquerdo = new VBox(10);
        painelEsquerdo.setPadding(new Insets(10));
        painelEsquerdo.setPrefWidth(250);
        painelEsquerdo.getChildren().addAll(
                new Label("Dados do Produto:"),
                new Label("ID:"), txtId,
                new Label("Nome:"), txtNome,
                new Label("Preço:"), txtPreco,
                new Label("Quantidade:"), txtQuantidade,
                new Separator(),
                new HBox(10, btnAdd, btnUpd),
                new HBox(10, btnDel, btnLimpar)
        );

        HBox raiz = new HBox(10, painelEsquerdo, tabela);
        HBox.setHgrow(tabela, Priority.ALWAYS);
        raiz.setPadding(new Insets(15));

        Scene scene = new Scene(raiz, 800, 500);
        stage.setScene(scene);
        stage.show();
    }

    // Métodos CRUD
    private void acaoAdicionar() {
        try {
            String nome = txtNome.getText();
            double preco = Double.parseDouble(txtPreco.getText().replace(",", "."));
            int qtd = Integer.parseInt(txtQuantidade.getText());

            Produto p = new Produto(0, nome, preco, qtd);
            produtoDAO.salvarProduto(p);
            atualizarTabela();
            limparCampos();
        } catch (Exception ex) { alerta("Erro ao adicionar: " + ex.getMessage()); }
    }

    private void acaoAtualizar() {
        try {
            if (txtId.getText().isEmpty()) return;
            int id = Integer.parseInt(txtId.getText());
            String nome = txtNome.getText();
            double preco = Double.parseDouble(txtPreco.getText().replace(",", "."));
            int qtd = Integer.parseInt(txtQuantidade.getText());

            Produto p = new Produto(id, nome, preco, qtd);
            produtoDAO.atualizarProduto(p);
            atualizarTabela();
            limparCampos();
        } catch (Exception ex) { alerta("Erro ao atualizar: " + ex.getMessage()); }
    }

    private void acaoExcluir() {
        try {
            if (txtId.getText().isEmpty()) return;
            produtoDAO.excluirProduto(Integer.parseInt(txtId.getText()));
            atualizarTabela();
            limparCampos();
        } catch (Exception ex) { alerta("Erro ao excluir: " + ex.getMessage()); }
    }

    private void atualizarTabela() {
        try {
            tabela.setItems(FXCollections.observableArrayList(produtoDAO.listarProdutos()));
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void limparCampos() {
        txtId.clear();
        txtNome.clear();
        txtPreco.clear();
        txtQuantidade.clear();
        tabela.getSelectionModel().clearSelection();
    }

    private void alerta(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).show();
    }

    public static void main(String[] args) {
        launch();
    }
}