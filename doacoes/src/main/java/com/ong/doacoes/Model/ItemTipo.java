package com.ong.doacoes.Model;

public class ItemTipo {
    private Long iditemTipo;
    private String nome;
    private String descricao;

    public ItemTipo(Long iditemTipo, String nome, String descricao) {
        this.iditemTipo = iditemTipo;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Long getIditemTipo() {
        return iditemTipo;
    }

    public void setIditemTipo(Long iditemTipo) {
        this.iditemTipo = iditemTipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "ItemTipo{" +
                "iditemTipo=" + iditemTipo +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
