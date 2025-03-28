package com.example.models;

    public class AutomacaoQA {
        
        private int id;
        private int produto;
        private String selo;
        private String descricaoQA;
        private String caso;
        private String chegada;
        private String saida;
        private String porcentagem;
    
        // Construtor
        public AutomacaoQA(int id, int produto, String selo, String descricaoQA, String caso, 
                           String chegada, String saida, String porcentagem) {
            this.id = id;
            this.produto = produto;
            this.selo = selo;
            this.descricaoQA = descricaoQA;
            this.caso = caso;
            this.chegada = chegada;
            this.saida = saida;
            this.porcentagem = porcentagem;
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
    
        public String getSelo() {
            return selo;
        }
    
        public void setSelo(String selo) {
            this.selo = selo;
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
    
        public String getPorcentagem() {
            return porcentagem;
        }
    
        public void setPorcentagem(String porcentagem) {
            this.porcentagem = porcentagem;
        }
    }
    
