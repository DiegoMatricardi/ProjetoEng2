package com.ong.doacoes.Model;

public class Doador {
    private Long idDoador;
    private Long idUsuario;
    private String cpf;
    private String email;
    private String nome;
    private String telefone;

    public Doador() {
    }

    public Doador(Long idDoador, Long idUsuario, String cpf, String email, String nome, String telefone) {
        this.idDoador = idDoador;
        this.idUsuario = idUsuario;
        this.cpf = cpf;
        this.email = email;
        this.nome = nome;
        this.telefone = telefone;
    }

    public Long getIdDoador() {
        return idDoador;
    }

    public void setIdDoador(Long idDoador) {
        this.idDoador = idDoador;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return "Doador{" +
                "idDoador=" + idDoador +
                ", idUsuario=" + idUsuario +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                ", nome='" + nome + '\'' +
                ", telefone='" + telefone + '\'' +
                '}';
    }
}
