package com.ong.doacoes.DAO;

import com.ong.doacoes.ConnectionFactory;
import com.ong.doacoes.Model.Colaborador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ColaboradorDAO {

    public Long buscarIdUsuarioPorEmail(String email) throws SQLException {
        String sql = "SELECT idusuario FROM usuario WHERE email = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("idusuario");
                }
            }
            return null;
        }
    }

    public boolean colaboradorExistePorIdUsuario(Long idusuario) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM colaborador WHERE idusuario = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idusuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total") > 0;
                }
            }
            return false;
        }
    }

    public boolean salvar(Colaborador colaborador) throws SQLException {
        String sql = "INSERT INTO colaborador (idusuario, cpf, endereco, email_secundario) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, colaborador.getIdusuario());
            stmt.setString(2, colaborador.getCpf());
            stmt.setString(3, colaborador.getEndereco());
            stmt.setString(4, colaborador.getEmailSecundario());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        colaborador.setIdcolaborador(rs.getLong(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    public List<Colaborador> listarTodos() throws SQLException {
        List<Colaborador> colaboradores = new ArrayList<>();
        String sql = "SELECT * FROM colaborador";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Colaborador colaborador = new Colaborador();
                colaborador.setIdcolaborador(rs.getLong("idcolaborador"));
                colaborador.setIdusuario(rs.getLong("idusuario"));
                colaborador.setCpf(rs.getString("cpf"));
                colaborador.setEndereco(rs.getString("endereco"));
                colaborador.setEmailSecundario(rs.getString("email_secundario"));
                colaboradores.add(colaborador);
            }
        }
        return colaboradores;
    }
}