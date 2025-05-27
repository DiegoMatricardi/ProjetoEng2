package com.ong.doacoes.Model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DoacaoSaida {

    private Long iddoacaoSaida;
    private Long idbeneficiario;
    private Long idcolaborador;
    private Timestamp dataEntrega;
    private List<DoacaoSaidaItem> itens = new ArrayList<>();

    public DoacaoSaida() {
    }

    public DoacaoSaida(Long iddoacaoSaida, Long idbeneficiario, Long idcolaborador, Timestamp dataEntrega, List<DoacaoSaidaItem> itens) {
        this.iddoacaoSaida = iddoacaoSaida;
        this.idbeneficiario = idbeneficiario;
        this.idcolaborador = idcolaborador;
        this.dataEntrega = dataEntrega;
        this.itens = itens;
    }

    public Long getIddoacaoSaida() {
        return iddoacaoSaida;
    }

    public void setIddoacaoSaida(Long iddoacaoSaida) {
        this.iddoacaoSaida = iddoacaoSaida;
    }

    public Long getIdbeneficiario() {
        return idbeneficiario;
    }

    public void setIdbeneficiario(Long idbeneficiario) {
        this.idbeneficiario = idbeneficiario;
    }

    public Long getIdcolaborador() {
        return idcolaborador;
    }

    public void setIdcolaborador(Long idcolaborador) {
        this.idcolaborador = idcolaborador;
    }

    public Timestamp getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(Timestamp dataEntrega) {
        this.dataEntrega = dataEntrega;
    }


    public List<DoacaoSaidaItem> getItens() {
        return itens;
    }

    public void setItens(List<DoacaoSaidaItem> itens) {
        this.itens = itens;
    }

    public void addItem(DoacaoSaidaItem item) {
        this.itens.add(item);
    }
}
