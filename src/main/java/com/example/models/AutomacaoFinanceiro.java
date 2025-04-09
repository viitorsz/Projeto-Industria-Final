package com.example.models;

public class AutomacaoFinanceiro {
    

    private int id;
    private String nome_AutomacaoFin;
    private String descricaoFin;
    private String setorFin;
    private String categoriaFin;
    private String estadoFin;



    public AutomacaoFinanceiro(int id, String nome_AutomacaoFin, String descricaoFin, String setorFin, String categoriaFin, String estadoFin){
        this.id = id;
        this.nome_AutomacaoFin = nome_AutomacaoFin;
        this.descricaoFin = descricaoFin;
        this.setorFin = setorFin;
        this.categoriaFin = categoriaFin;
        this.estadoFin = estadoFin;
    }

    public AutomacaoFinanceiro(){}

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getNome_AutomacaoFin(){
        return nome_AutomacaoFin;
    }

    public void setNome_AutomacaoFin(String nome_AutomacaoFin){
        this.nome_AutomacaoFin = nome_AutomacaoFin;
    }

    public String getDescricaoFin() {
        return descricaoFin;
    }

    public void setDescricaoFin(String descricaoFin){
        this.descricaoFin = descricaoFin;
    }

    public String getSetorFin() {
        return setorFin;
    }

    public void setSetorFin(String setorFin){
        this.setorFin = setorFin;
    }

    public String getCategoriaFin(){
        return categoriaFin;
    }

    public void setCategoriaFin(String categoriaFin){
        this.categoriaFin = categoriaFin;
    }

    public String getEstadoFin(){
        return estadoFin;
    }

    public void setEstadoFin(String estadoFin){
        this.estadoFin = estadoFin;
    }
}
