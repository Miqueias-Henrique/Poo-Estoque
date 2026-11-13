package com.example.pooestoque;

import com.example.pooestoque.dao.ProdutoDAO;
import com.example.pooestoque.model.Produto;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Tela extends Application {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final TableView<Produto> tabela = new TableView<>();
    private final TextField txtId = new TextField();
    private final TextField txtNome = new TextField();
    private final TextField txtPreco = new TextField();
    private final TextField txtQuantidade = new TextField();

    @Override
    public void start(Stage stage) {
        stage.setTitle("Sistema de Estoque");

        txtId.setEditable(false);
        txtId.setPromptText("ID (Auto)");
        txtNome.setPromptText("Nome do Produto");
        txtPreco.setPromptText("Preço (0.00)");
        txtQuantidade.setPromptText("Quantidade");

        Button btnAdd = new Button("Adicionar");
        Button btnUpd = new Button("Atualizar");
        Button btnDel = new Button("Excluir");
        Button btnLimpar = new Button("Limpar");

        btnAdd.setOnAction(e -> acaoAdicionar());
        btnUpd.setOnAction(e -> acaoAtualizar());
        btnDel.setOnAction(e -> acaoExcluir());
        btnLimpar.setOnAction(e -> limparCampos());

        configurarTabela();
        atualizarTabela();

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

        stage.setScene(new Scene(raiz, 800, 500));
        stage.show();
    }

    private void configurarTabela() {
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
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        tabela.getSelectionModel().selectedItemProperty().addListener((obs, old, novo) -> {
            if (novo != null) {
                txtId.setText(String.valueOf(novo.getId()));
                txtNome.setText(novo.getNome());
                txtPreco.setText(String.valueOf(novo.getPreco()));
                txtQuantidade.setText(String.valueOf(novo.getQuantidade()));
            }
        });
    }

    private Produto lerProdutoDoFormulario(int id) {
        String nome = txtNome.getText() == null ? "" : txtNome.getText().trim();
        if (nome.isEmpty()) {
            throw new IllegalArgumentException("Informe o nome do produto.");
        }
        double preco = Double.parseDouble(txtPreco.getText().trim().replace(",", "."));
        if (preco < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo.");
        }
        int quantidade = Integer.parseInt(txtQuantidade.getText().trim());
        if (quantidade < 0) {
            throw new IllegalArgumentException("A quantidade não pode ser negativa.");
        }
        return new Produto(id, nome, preco, quantidade);
    }

    private void acaoAdicionar() {
        try {
            produtoDAO.salvarProduto(lerProdutoDoFormulario(0));
            atualizarTabela();
            limparCampos();
        } catch (Exception ex) {
            alerta("Erro ao adicionar: " + ex.getMessage());
        }
    }

    private void acaoAtualizar() {
        try {
            if (txtId.getText().isEmpty()) {
                return;
            }
            int id = Integer.parseInt(txtId.getText());
            produtoDAO.atualizarProduto(lerProdutoDoFormulario(id));
            atualizarTabela();
            limparCampos();
        } catch (Exception ex) {
            alerta("Erro ao atualizar: " + ex.getMessage());
        }
    }

    private void acaoExcluir() {
        try {
            if (txtId.getText().isEmpty()) {
                return;
            }
            produtoDAO.excluirProduto(Integer.parseInt(txtId.getText()));
            atualizarTabela();
            limparCampos();
        } catch (Exception ex) {
            alerta("Erro ao excluir: " + ex.getMessage());
        }
    }

    private void atualizarTabela() {
        try {
            tabela.setItems(FXCollections.observableArrayList(produtoDAO.listarProdutos()));
        } catch (Exception ex) {
            alerta("Erro ao carregar produtos: " + ex.getMessage());
        }
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