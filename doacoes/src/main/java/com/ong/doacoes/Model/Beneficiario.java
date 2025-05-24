package com.ong.doacoes.Model;

import java.time.LocalDateTime;

public class Beneficiario {
    private Long idbeneficiario;
    private LocalDateTime horarioDiaVisita;
    private String bairro;
    private String cep;
    private String cidade;
    private String complemento;
    private String email;
    private String endereco;
    private String nome;
    private String telefone;

    public Beneficiario() {
    }

    public Beneficiario(Long idbeneficiario, LocalDateTime horarioDiaVisita, String bairro, String cep, String cidade, String complemento, String email, String endereco, String nome, String telefone) {
        this.idbeneficiario = idbeneficiario;
        this.horarioDiaVisita = horarioDiaVisita;
        this.bairro = bairro;
        this.cep = cep;
        this.cidade = cidade;
        this.complemento = complemento;
        this.email = email;
        this.endereco = endereco;
        this.nome = nome;
        this.telefone = telefone;
    }

    public Long getIdbeneficiario() {
        return idbeneficiario;
    }

    public void setIdbeneficiario(Long idbeneficiario) {
        this.idbeneficiario = idbeneficiario;
    }

    public LocalDateTime getHorarioDiaVisita() {
        return horarioDiaVisita;
    }

    public void setHorarioDiaVisita(LocalDateTime horarioDiaVisita) {
        this.horarioDiaVisita = horarioDiaVisita;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
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
        return "Beneficiario{" +
                "idbeneficiario=" + idbeneficiario +
                ", horarioDiaVisita=" + horarioDiaVisita +
                ", bairro='" + bairro + '\'' +
                ", cep='" + cep + '\'' +
                ", cidade='" + cidade + '\'' +
                ", complemento='" + complemento + '\'' +
                ", email='" + email + '\'' +
                ", endereco='" + endereco + '\'' +
                ", nome='" + nome + '\'' +
                ", telefone='" + telefone + '\'' +
                '}';
    }
}
