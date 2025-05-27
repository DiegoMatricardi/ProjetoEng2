package com.ong.doacoes.DAO;

import com.ong.doacoes.ConnectionFactory;
import com.ong.doacoes.Model.DoacaoSaida;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoacaoSaidaDAO {

    public boolean inserir(Connection conn, DoacaoSaida doacao) {
        String sql = "INSERT INTO doacao_saida (idbeneficiario, idcolaborador, data_entrega) VALUES (?, ?, NOW())";
        try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, doacao.getIdbeneficiario());
            stmt.setLong(2, doacao.getIdcolaborador());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        doacao.setIddoacaoSaida(rs.getLong(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir doação de saída: " + e.getMessage());
            return false;
        }
    }

    public List<DoacaoSaida> listarPendentes() {
        List<DoacaoSaida> doacoes = new ArrayList<>();
        String sql = "SELECT ds.*, b.nome, b.endereco, b.bairro, b.cidade, b.cep, b.complemento " +
                "FROM doacao_saida ds JOIN beneficiario b ON ds.idbeneficiario = b.idbeneficiario";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                DoacaoSaida doacao = new DoacaoSaida();
                doacao.setIddoacaoSaida(rs.getLong("iddoacao_saida"));
                doacao.setIdbeneficiario(rs.getLong("idbeneficiario"));
                doacao.setIdcolaborador(rs.getLong("idcolaborador"));
                doacao.setDataEntrega(rs.getTimestamp("data_entrega"));
                doacoes.add(doacao);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar doações: " + e.getMessage());
        }
        return doacoes;
    }
}