package com.ong.doacoes.Model;

import java.sql.Timestamp;
import java.util.List;

public class DoacaoEntrada {

    private Long iddoacaoentrada;
    private String status;
    private Timestamp dataAbertura;
    private Timestamp dataBusca;
    private Timestamp dataFim;
    private Timestamp dataNotificacao;
    private Long iddoador;
    private String enderecoBusca;
    private String observacao;
    private Long idcolaborador;

    private List<DoacaoEntradaItem> itens;

    public DoacaoEntrada() {
    }

    public DoacaoEntrada(Long iddoacaoentrada, String status, Timestamp dataAbertura, Timestamp dataBusca,
                         Timestamp dataFim, Timestamp dataNotificacao, Long iddoador, String enderecoBusca,
                         String observacao, Long idcolaborador) {
        this.iddoacaoentrada = iddoacaoentrada;
        this.status = status;
        this.dataAbertura = dataAbertura;
        this.dataBusca = dataBusca;
        this.dataFim = dataFim;
        this.dataNotificacao = dataNotificacao;
        this.iddoador = iddoador;
        this.enderecoBusca = enderecoBusca;
        this.observacao = observacao;
        this.idcolaborador = idcolaborador;
    }

    // Getters e Setters

    public Long getIddoacaoentrada() {
        return iddoacaoentrada;
    }

    public void setIddoacaoentrada(Long iddoacaoentrada) {
        this.iddoacaoentrada = iddoacaoentrada;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(Timestamp dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public Timestamp getDataBusca() {
        return dataBusca;
    }

    public void setDataBusca(Timestamp dataBusca) {
        this.dataBusca = dataBusca;
    }

    public Timestamp getDataFim() {
        return dataFim;
    }

    public void setDataFim(Timestamp dataFim) {
        this.dataFim = dataFim;
    }

    public Timestamp getDataNotificacao() {
        return dataNotificacao;
    }

    public void setDataNotificacao(Timestamp dataNotificacao) {
        this.dataNotificacao = dataNotificacao;
    }

    public Long getIddoador() {
        return iddoador;
    }

    public void setIddoador(Long iddoador) {
        this.iddoador = iddoador;
    }

    public String getEnderecoBusca() {
        return enderecoBusca;
    }

    public void setEnderecoBusca(String enderecoBusca) {
        this.enderecoBusca = enderecoBusca;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Long getIdcolaborador() {
        return idcolaborador;
    }

    public void setIdcolaborador(Long idcolaborador) {
        this.idcolaborador = idcolaborador;
    }

    public List<DoacaoEntradaItem> getItens() {
        return itens;
    }

    public void setItens(List<DoacaoEntradaItem> itens) {
        this.itens = itens;
    }
}
