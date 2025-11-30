package com.example.pooestoque.dao;

import com.example.pooestoque.model.Produto;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ProdutoDAO {
    private static final String ARQUIVO_CSV = "produtos.csv";

    public ProdutoDAO() {
        try {
            if (!Files.exists(Paths.get(ARQUIVO_CSV))) {
                Files.createFile(Paths.get(ARQUIVO_CSV));
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void salvarProduto(Produto produto) throws IOException {
        int novoId = carregarProximoId();
        produto.setId(novoId);
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(ARQUIVO_CSV), StandardOpenOption.APPEND)) {
            writer.write(produto.toCSV());
            writer.newLine();
        }
    }

    public List<Produto> listarProdutos() throws IOException {
        List<Produto> produtos = new ArrayList<>();
        List<String> linhas = Files.readAllLines(Paths.get(ARQUIVO_CSV));
        for (String linha : linhas) {
            if (!linha.trim().isEmpty()) produtos.add(Produto.fromCSV(linha));
        }
        return produtos;
    }

    public void atualizarProduto(Produto editado) throws IOException {
        List<Produto> lista = listarProdutos();
        boolean achou = false;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == editado.getId()) {
                lista.set(i, editado);
                achou = true;
                break;
            }
        }
        if (achou) reescreverArquivo(lista);
    }

    public void excluirProduto(int id) throws IOException {
        List<Produto> lista = listarProdutos();
        lista.removeIf(p -> p.getId() == id);
        reescreverArquivo(lista);
    }

    public int carregarProximoId() throws IOException {
        List<Produto> lista = listarProdutos();
        return lista.isEmpty() ? 1 : lista.get(lista.size() - 1).getId() + 1;
    }

    private void reescreverArquivo(List<Produto> lista) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(ARQUIVO_CSV), StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Produto p : lista) {
                writer.write(p.toCSV());
                writer.newLine();
            }
        }
    }
}