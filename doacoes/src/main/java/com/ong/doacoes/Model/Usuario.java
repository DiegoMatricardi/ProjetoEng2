package com.ong.doacoes.Model;

public class Usuario {

    private Long idUsuario;
    private String email;
    private String senha;
    private int ativo;
    private Long idUsuarioTipo;

    public Usuario() {
    }

    public Usuario(Long idUsuario, String email, String senha, int ativo, Long idUsuarioTipo) {
        this.idUsuario = idUsuario;
        this.email = email;
        this.senha = senha;
        this.ativo = ativo;
        this.idUsuarioTipo = idUsuarioTipo;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getAtivo() {
        return ativo;
    }

    public void setAtivo(int ativo) {
        this.ativo = ativo;
    }

    public Long getIdUsuarioTipo() {
        return idUsuarioTipo;
    }

    public void setIdUsuarioTipo(Long idUsuarioTipo) {
        this.idUsuarioTipo = idUsuarioTipo;
    }
}
