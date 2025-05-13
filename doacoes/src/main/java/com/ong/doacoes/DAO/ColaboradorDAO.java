package com.ong.doacoes.DAO;

import com.ong.doacoes.ConnectionFactory;
import com.ong.doacoes.Model.Colaborador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ColaboradorDAO {

    public Long buscarIdUsuarioPorEmail(String email) {
        String sql = "SELECT idusuario FROM usuario WHERE email = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getLong("idusuario");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean colaboradorExistePorIdUsuario(Long idusuario) {
        String sql = "SELECT COUNT(*) AS total FROM colaborador WHERE idusuario = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idusuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total") > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean salvar(Colaborador colaborador) {
        String sql = "INSERT INTO colaborador (idusuario, cpf, endereco, email_secundario) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, colaborador.getIdusuario());
            stmt.setString(2, colaborador.getCpf());
            stmt.setString(3, colaborador.getEndereco());
            stmt.setString(4, colaborador.getEmailSecundario());

            stmt.executeUpdate();

            System.out.println("Colaborador salvo com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao salvar colaborador: " + e.getMessage());
        }
        return false;
    }
}
