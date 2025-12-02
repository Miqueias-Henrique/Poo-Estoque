package com.example.pooestoque.dao;

import com.example.pooestoque.model.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    // --- CONFIGURAÇÃO DE CONEXÃO ---
    // Verifique se o nome do banco 'poo_estoque' está criado no DBeaver
    private static final String URL = "jdbc:postgresql://localhost:5432/poo_estoque";
    private static final String USER = "postgres";
    private static final String PASS = "postgres"; // Sua senha do PostgreSQL

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // CREATE
    public void salvarProduto(Produto produto) {
        String sql = "INSERT INTO produtos (nome, preco, quantidade) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, produto.getNome());
            // setDouble funciona perfeitamente com NUMERIC(10,2) no Postgres
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getQuantidade());

            stmt.executeUpdate();

            // Recupera o ID gerado (SERIAL)
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    produto.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            tratarErro(e);
        }
    }

    // READ
    public List<Produto> listarProdutos() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produtos ORDER BY id ASC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto p = new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDouble("preco"), // O driver converte NUMERIC para double
                        rs.getInt("quantidade")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            tratarErro(e);
        }
        return lista;
    }

    // UPDATE
    public void atualizarProduto(Produto p) {
        String sql = "UPDATE produtos SET nome = ?, preco = ?, quantidade = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNome());
            stmt.setDouble(2, p.getPreco());
            stmt.setInt(3, p.getQuantidade());
            stmt.setInt(4, p.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            tratarErro(e);
        }
    }

    // DELETE
    public void excluirProduto(int id) {
        String sql = "DELETE FROM produtos WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            tratarErro(e);
        }
    }

    private void tratarErro(SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Erro de Banco de Dados: " + e.getMessage());
    }
}