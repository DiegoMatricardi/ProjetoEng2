package com.ong.doacoes.Model;

public class DoacaoEntrada {
    private Long iddoacaoentrada;
    private Long iddoador;
    private Long idcolaborador;
    private String dataentrada;
    private String tipoentrega;
    private String endereco;
    private String bairro;
    private String cidade;
    private String estado;
    private String status;

    public DoacaoEntrada() {
    }

    public DoacaoEntrada(Long iddoacaoentrada, Long iddoador, Long idcolaborador, String dataentrada, String tipoentrega, String endereco, String bairro, String cidade, String estado, String status) {
        this.iddoacaoentrada = iddoacaoentrada;
        this.iddoador = iddoador;
        this.idcolaborador = idcolaborador;
        this.dataentrada = dataentrada;
        this.tipoentrega = tipoentrega;
        this.endereco = endereco;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.status = status;
    }

    public Long getIddoacaoentrada() {
        return iddoacaoentrada;
    }

    public void setIddoacaoentrada(Long iddoacaoentrada) {
        this.iddoacaoentrada = iddoacaoentrada;
    }

    public Long getIddoador() {
        return iddoador;
    }

    public void setIddoador(Long iddoador) {
        this.iddoador = iddoador;
    }

    public Long getIdcolaborador() {
        return idcolaborador;
    }

    public void setIdcolaborador(Long idcolaborador) {
        this.idcolaborador = idcolaborador;
    }

    public String getDataentrada() {
        return dataentrada;
    }

    public void setDataentrada(String dataentrada) {
        this.dataentrada = dataentrada;
    }

    public String getTipoentrega() {
        return tipoentrega;
    }

    public void setTipoentrega(String tipoentrega) {
        this.tipoentrega = tipoentrega;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
