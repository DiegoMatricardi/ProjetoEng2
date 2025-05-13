package com.ong.doacoes.DAO;

import com.ong.doacoes.ConnectionFactory;
import com.ong.doacoes.Model.Doador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoadorDAO {

    public Long buscarIdUsuarioPorEmail(String email) {
        String sql = "SELECT idusuario FROM usuario WHERE email = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("idusuario");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar idusuario: " + e.getMessage());
        }
        return null;
    }

    public boolean doadorExistePorIdUsuario(Long idUsuario) {
        String sql = "SELECT 1 FROM doador WHERE idusuario = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idUsuario);

            ResultSet rs = stmt.executeQuery();
            return rs.next();  // Se encontrar, retorna true
        } catch (SQLException e) {
            System.err.println("Erro ao verificar doador: " + e.getMessage());
            return false;
        }
    }

    public void salvar(Doador doador) {
        String sql = "INSERT INTO doador (idusuario, nome, cpf, email, telefone) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, doador.getIdUsuario());
            stmt.setString(2, doador.getNome());
            stmt.setString(3, doador.getCpf());
            stmt.setString(4, doador.getEmail());
            stmt.setString(5, doador.getTelefone());
            stmt.executeUpdate();

            System.out.println("Doador salvo com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao salvar doador: " + e.getMessage());
        }
    }
}
