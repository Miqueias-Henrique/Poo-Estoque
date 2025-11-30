package com.example.pooestoque.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Produto {
    private int id;
    private String nome;
    private double preco;
    private int quantidade;

    public String toCSV() {
        return id + "," + nome + "," + preco + "," + quantidade;
    }

    public static Produto fromCSV(String linha) {
        String[] partes = linha.split(",");
        return new Produto(
                Integer.parseInt(partes[0]),
                partes[1],
                Double.parseDouble(partes[2]),
                Integer.parseInt(partes[3])
        );
    }
}