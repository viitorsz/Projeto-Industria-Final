package com.example.models;

public class AutomacaoProducao {

    private int id;
    private String nome_produto;
    private String preco;
    private int lote;
    private int codigo;

    public AutomacaoProducao(int id, String nome_produto, String preco, int lote, int codigo) {
        this.id = id;
        this.nome_produto = nome_produto;
        this.preco = preco;
        this.lote = lote;
        this.codigo = codigo;
    }

    public AutomacaoProducao() {}

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

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
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
