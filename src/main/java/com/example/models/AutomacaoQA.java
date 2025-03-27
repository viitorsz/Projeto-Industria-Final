package com.example.models;

    public class AutomacaoQA {
        
        private int id;
        private int produto;
        private String seloQualidade;
        private String descricaoQA;
        private String caso;
        private String chegada;
        private String saida;
        private String porcentagemQualidade;
    
        // Construtor
        public AutomacaoQA(int id, int produto, String seloQualidade, String descricaoQA, String caso, 
                           String chegada, String saida, String porcentagemQualidade) {
            this.id = id;
            this.produto = produto;
            this.seloQualidade = seloQualidade;
            this.descricaoQA = descricaoQA;
            this.caso = caso;
            this.chegada = chegada;
            this.saida = saida;
            this.porcentagemQualidade = porcentagemQualidade;
        }
    
        // Getters e Setters
        public int getId() {
            return id;
        }
    
        public void setId(int id) {
            this.id = id;
        }
    
        public int getProduto() {
            return produto;
        }
    
        public void setProduto(int produto) {
            this.produto = produto;
        }
    
        public String getSeloQualidade() {
            return seloQualidade;
        }
    
        public void setSeloQualidade(String seloQualidade) {
            this.seloQualidade = seloQualidade;
        }
    
        public String getDescricao() {
            return descricaoQA;
        }
    
        public void setDescricao(String descricao) {
            this.descricaoQA = descricao;
        }
    
        public String getCaso() {
            return caso;
        }
    
        public void setCaso(String caso) {
            this.caso = caso;
        }
    
        public String getChegada() {
            return chegada;
        }
    
        public void setChegada(String chegada) {
            this.chegada = chegada;
        }
    
        public String getSaida() {
            return saida;
        }
    
        public void setSaida(String saida) {
            this.saida = saida;
        }
    
        public String getPorcentagemQualidade() {
            return porcentagemQualidade;
        }
    
        public void setPorcentagemQualidade(String porcentagemQualidade) {
            this.porcentagemQualidade = porcentagemQualidade;
        }
    }
    
