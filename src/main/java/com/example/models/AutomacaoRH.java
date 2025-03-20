package com.example.models;

public class AutomacaoRH {
    private int id;
    private String nomeAutomacao;
    private String responsavel;
    private String categoria;
    private String descricao;
    private String operacao;
    private String setor;
    private String localizacao;
    private String situacao;
    private String prioridade;


    public AutomacaoRH(int id, String nomeAutomacao, String responsavel, String categoria, String descricao, String operacao, String setor, String localizacao, String situacao, String prioridade){
        this.id = id;
        this.nomeAutomacao = nomeAutomacao;
        this.responsavel = responsavel;
        this.categoria = categoria;
        this.descricao = descricao;
        this.operacao =  operacao;
        this.setor = setor;
        this.localizacao = localizacao;
        this.situacao = situacao;
        this.prioridade = prioridade;
    }

    public int getId(){
        return id;
    }
    public void setID(int id){
        this.id = id;

    }

    public String getNomeAutomacao() {
        return nomeAutomacao;  
    }

    public void setNomeAutomacao(String nomeAutomacao) {
        this.nomeAutomacao = nomeAutomacao;
    }

    public String getResponsavel(){
        return responsavel;

    }

    public void setResponsavel(String responsavel){
        this.responsavel = responsavel;

    }

    public String getCategoria(){
        return categoria;

    }

    public void setCategoria(String categoria){
        this.categoria = categoria;
    }

    public String getDescricao(){
        return descricao;
    }

    public void setDescricao(String descricao){
        this.descricao = descricao;
    }

    public String getOperacao(){
        return operacao;
    }

    public void setOperacao(String operacao){
        this.operacao = operacao;
    }

    public String getSetor(){
        return setor;
    }

    public void  setSetor(String setor){
        this.setor = setor;
    }

    public void setLocalizacao(String localizacao){
        this.localizacao = localizacao;
    }

    public String getLocalizacao(){
        return localizacao;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getPrioridade(){
        return prioridade;
    }

    public void setPrioridade(String prioridade){
        this.prioridade = prioridade;
    }

}