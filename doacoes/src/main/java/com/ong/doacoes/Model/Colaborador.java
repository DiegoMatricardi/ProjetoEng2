package com.ong.doacoes.Model;

public class Colaborador {

    private Long idcolaborador;
    private Long idusuario;
    private String cpf;
    private String endereco;
    private String emailSecundario;

    public Colaborador() {
    }

    public Colaborador(Long idcolaborador, Long idusuario, String cpf, String endereco, String emailSecundario) {
        this.idcolaborador = idcolaborador;
        this.idusuario = idusuario;
        this.cpf = cpf;
        this.endereco = endereco;
        this.emailSecundario = emailSecundario;
    }

    public Long getIdcolaborador() {
        return idcolaborador;
    }

    public void setIdcolaborador(Long idcolaborador) {
        this.idcolaborador = idcolaborador;
    }

    public Long getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Long idusuario) {
        this.idusuario = idusuario;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEmailSecundario() {
        return emailSecundario;
    }

    public void setEmailSecundario(String emailSecundario) {
        this.emailSecundario = emailSecundario;
    }

    @Override
    public String toString() {
        return "Colaborador{" +
                "idcolaborador=" + idcolaborador +
                ", idusuario=" + idusuario +
                ", cpf='" + cpf + '\'' +
                ", endereco='" + endereco + '\'' +
                ", emailSecundario='" + emailSecundario + '\'' +
                '}';
    }
}
