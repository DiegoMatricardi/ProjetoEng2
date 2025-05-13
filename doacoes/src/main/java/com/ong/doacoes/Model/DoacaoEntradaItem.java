package com.ong.doacoes.Model;

public class DoacaoEntradaItem {

    private Long iddoacaoEntradaItem;
    private Double valorQtdeDoacaoEntradaItem;
    private Long iddoacaoEntrada;
    private String descricao;
    private Long idItem;

    // Construtores
    public DoacaoEntradaItem() {
    }

    public DoacaoEntradaItem(Long iddoacaoEntradaItem, Double valorQtdeDoacaoEntradaItem, Long iddoacaoEntrada, String descricao, Long idItem) {
        this.iddoacaoEntradaItem = iddoacaoEntradaItem;
        this.valorQtdeDoacaoEntradaItem = valorQtdeDoacaoEntradaItem;
        this.iddoacaoEntrada = iddoacaoEntrada;
        this.descricao = descricao;
        this.idItem = idItem;
    }

    // Getters e Setters
    public Long getIddoacaoEntradaItem() {
        return iddoacaoEntradaItem;
    }

    public void setIddoacaoEntradaItem(Long iddoacaoEntradaItem) {
        this.iddoacaoEntradaItem = iddoacaoEntradaItem;
    }

    public Double getValorQtdeDoacaoEntradaItem() {
        return valorQtdeDoacaoEntradaItem;
    }

    public void setValorQtdeDoacaoEntradaItem(Double valorQtdeDoacaoEntradaItem) {
        this.valorQtdeDoacaoEntradaItem = valorQtdeDoacaoEntradaItem;
    }

    public Long getIddoacaoEntrada() {
        return iddoacaoEntrada;
    }

    public void setIddoacaoEntrada(Long iddoacaoEntrada) {
        this.iddoacaoEntrada = iddoacaoEntrada;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getIdItem() {
        return idItem;
    }

    public void setIdItem(Long idItem) {
        this.idItem = idItem;
    }

    @Override
    public String toString() {
        return "DoacaoEntradaItem{" +
                "iddoacaoEntradaItem=" + iddoacaoEntradaItem +
                ", valorQtdeDoacaoEntradaItem=" + valorQtdeDoacaoEntradaItem +
                ", iddoacaoEntrada=" + iddoacaoEntrada +
                ", descricao='" + descricao + '\'' +
                ", idItem=" + idItem +
                '}';
    }
}
