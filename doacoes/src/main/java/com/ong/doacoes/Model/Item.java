package com.ong.doacoes.Model;

public class Item {

    private Long idItem;
    private String descricao;
    private Long idItemTipo;
    private Integer estoque;

    // Construtores
    public Item() {
    }

    public Item(Long idItem, String descricao, Long idItemTipo, Integer estoque) {
        this.idItem = idItem;
        this.descricao = descricao;
        this.idItemTipo = idItemTipo;
        this.estoque = estoque;
    }

    // Getters e Setters
    public Long getIdItem() {
        return idItem;
    }

    public void setIdItem(Long idItem) {
        this.idItem = idItem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getIdItemTipo() {
        return idItemTipo;
    }

    public void setIdItemTipo(Long idItemTipo) {
        this.idItemTipo = idItemTipo;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    @Override
    public String toString() {
        return "Item{" +
                "idItem=" + idItem +
                ", descricao='" + descricao + '\'' +
                ", idItemTipo=" + idItemTipo +
                ", estoque=" + estoque +
                '}';
    }
}
