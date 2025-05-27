package com.ong.doacoes.DAO;

import com.ong.doacoes.ConnectionFactory;
import com.ong.doacoes.Model.Beneficiario;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BeneficiarioDAO {
    public boolean inserir(Beneficiario beneficiario) {
        String sql = "INSERT INTO beneficiario (horario_dia_visita, bairro, cep, cidade, complemento, " +
                "email, endereco, nome, telefone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setObject(1, beneficiario.getHorarioDiaVisita());
            stmt.setString(2, beneficiario.getBairro());
            stmt.setString(3, beneficiario.getCep());
            stmt.setString(4, beneficiario.getCidade());
            stmt.setString(5, beneficiario.getComplemento());
            stmt.setString(6, beneficiario.getEmail());
            stmt.setString(7, beneficiario.getEndereco());
            stmt.setString(8, beneficiario.getNome());
            stmt.setString(9, beneficiario.getTelefone());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    beneficiario.setIdbeneficiario(rs.getLong(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir beneficiário: " + e.getMessage());
            return false;
        }
    }

    public Beneficiario buscarPorId(Connection conn, Long id) {
        String sql = "SELECT * FROM beneficiario WHERE idbeneficiario = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Beneficiario beneficiario = new Beneficiario();
                    beneficiario.setIdbeneficiario(rs.getLong("idbeneficiario"));
                    beneficiario.setHorarioDiaVisita(rs.getObject("horario_dia_visita", LocalDateTime.class));
                    beneficiario.setBairro(rs.getString("bairro"));
                    beneficiario.setCep(rs.getString("cep"));
                    beneficiario.setCidade(rs.getString("cidade"));
                    beneficiario.setComplemento(rs.getString("complemento"));
                    beneficiario.setEmail(rs.getString("email"));
                    beneficiario.setEndereco(rs.getString("endereco"));
                    beneficiario.setNome(rs.getString("nome"));
                    beneficiario.setTelefone(rs.getString("telefone"));
                    return beneficiario;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar beneficiário por ID: " + e.getMessage());
        }
        return null;
    }


    public List<Beneficiario> listarTodos() {
        List<Beneficiario> beneficiarios = new ArrayList<>();
        String sql = "SELECT * FROM beneficiario ORDER BY nome";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Beneficiario beneficiario = new Beneficiario();
                beneficiario.setIdbeneficiario(rs.getLong("idbeneficiario"));
                beneficiario.setHorarioDiaVisita(rs.getObject("horario_dia_visita", LocalDateTime.class));
                beneficiario.setBairro(rs.getString("bairro"));
                beneficiario.setCep(rs.getString("cep"));
                beneficiario.setCidade(rs.getString("cidade"));
                beneficiario.setComplemento(rs.getString("complemento"));
                beneficiario.setEmail(rs.getString("email"));
                beneficiario.setEndereco(rs.getString("endereco"));
                beneficiario.setNome(rs.getString("nome"));
                beneficiario.setTelefone(rs.getString("telefone"));
                beneficiarios.add(beneficiario);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar beneficiários: " + e.getMessage());
        }
        return beneficiarios;
    }
}
