package com.ong.doacoes.Model;

public class DoacaoSaidaItem {
    private int iddoacaoSaidaItem;
    private Long iddoacaoSaida;
    private Long iditem;
    private double valorQtdeDoacaoSaidaItem;

    // Construtores
    public DoacaoSaidaItem() {
    }

    public DoacaoSaidaItem(int iddoacaoSaidaItem, Long iddoacaoSaida, Long iditem, double valorQtdeDoacaoSaidaItem) {
        this.iddoacaoSaidaItem = iddoacaoSaidaItem;
        this.iddoacaoSaida = iddoacaoSaida;
        this.iditem = iditem;
        this.valorQtdeDoacaoSaidaItem = valorQtdeDoacaoSaidaItem;
    }

    // Getters e Setters
    public int getIddoacaoSaidaItem() {
        return iddoacaoSaidaItem;
    }

    public void setIddoacaoSaidaItem(int iddoacaoSaidaItem) {
        this.iddoacaoSaidaItem = iddoacaoSaidaItem;
    }

    public Long getIddoacaoSaida() {
        return iddoacaoSaida;
    }

    public void setIddoacaoSaida(Long iddoacaoSaida) {
        this.iddoacaoSaida = iddoacaoSaida;
    }

    public Long getIditem() {
        return iditem;
    }

    public void setIditem(Long iditem) {
        this.iditem = iditem;
    }

    public double getValorQtdeDoacaoSaidaItem() {
        return valorQtdeDoacaoSaidaItem;
    }

    public void setValorQtdeDoacaoSaidaItem(double valorQtdeDoacaoSaidaItem) {
        this.valorQtdeDoacaoSaidaItem = valorQtdeDoacaoSaidaItem;
    }
}