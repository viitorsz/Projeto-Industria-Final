package com.example.models;

public class AutomacaoEst {

    private int id;
    private String material;
    private String descricao;
    private int quantidade;
    private String estado;

    // Construtor
    public AutomacaoEst(int id, String material, String descricao, int quantidade, String estado) {
        this.id = id;
        this.material = material;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.estado = estado;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
