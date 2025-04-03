package com.example.models;

public class AutomacaoProducao {

    private int id;
    private String nome_produto;
    private int preco;
    private int lote;
    private int codigo;

    // Construtor
    public AutomacaoProducao(int id, String nome_produto, int preco, int lote, int codigo) {
        this.id = id;
        this.nome_produto = nome_produto;
        this.preco = preco;
        this.lote = lote;
        this.codigo = codigo;
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome_produto() {
        return nome_produto;
    }

    public void setNome_produto(String nome_produto) {
        this.nome_produto = nome_produto;
    }

    public int getPreco() {
        return preco;
    }

    public void setPreco(int preco) {
        this.preco = preco;
    }

    public int getLote() {
        return lote;
    }

    public void setLote(int lote) {
        this.lote = lote;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
}

