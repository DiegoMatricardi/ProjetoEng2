package com.ong.doacoes.DAO;

import com.ong.doacoes.ConnectionFactory;
import com.ong.doacoes.Model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public boolean salvar(Usuario usuario) {
        String sql = "INSERT INTO usuario (email, senha, ativo) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getEmail());
            stmt.setString(2, usuario.getSenha());
            stmt.setInt(3, usuario.getAtivo());

            stmt.executeUpdate();
            System.out.println("Usuário salvo com sucesso.");
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar usuário: " + e.getMessage());
        }
        return false;
    }

    public Usuario buscarUsuarioPorEmailESenha(String email, String senha) {
        String sql = "SELECT * FROM usuario WHERE email = ? AND senha = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getLong("idusuario"));
                usuario.setIdUsuarioTipo(rs.getLong("idusuario_tipo"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setAtivo(rs.getInt("ativo"));
                return usuario;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário: " + e.getMessage());
        }

        return null;
    }

    public boolean usuarioExistePorEmail(String email) {
        String sql = "SELECT COUNT(*) AS total FROM usuario WHERE email = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total") > 0;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao verificar existência de usuário: " + e.getMessage());
        }

        return false;
    }

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
            System.err.println("Erro ao buscar ID do usuário: " + e.getMessage());
        }

        return null;
    }

    public boolean promoverParaTipo2(Connection conn, Long idUsuario) {
        String sql = "UPDATE usuario SET idusuario_tipo = 2 WHERE idusuario = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idUsuario);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao promover usuário: " + e.getMessage());
            return false;
        }
    }
}
