package com.ong.doacoes.DAO;

import com.ong.doacoes.ConnectionFactory;
import com.ong.doacoes.Model.Doador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public Doador buscarPorIdUsuario(Long idUsuario) {
        String sql = "SELECT * FROM doador WHERE idusuario = ?";
        Doador doador = null;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                doador = new Doador();
                doador.setIdUsuario(rs.getLong("idusuario"));
                doador.setNome(rs.getString("nome"));
                doador.setCpf(rs.getString("cpf"));
                doador.setEmail(rs.getString("email"));
                doador.setTelefone(rs.getString("telefone"));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar doador por idusuario: " + e.getMessage());
        }

        return doador;
    }

    public boolean atualizar(Doador doador) {
        String sql = "UPDATE doador SET nome = ?, cpf = ?, email = ?, telefone = ? WHERE idusuario = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, doador.getNome());
            stmt.setString(2, doador.getCpf());
            stmt.setString(3, doador.getEmail());
            stmt.setString(4, doador.getTelefone());
            stmt.setLong(5, doador.getIdUsuario());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Doador> listarTodos() {
        List<Doador> doadores = new ArrayList<>();
        String sql = "SELECT * FROM doador";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Doador doador = new Doador();
                doador.setIdDoador(rs.getLong("iddoador"));
                doador.setIdUsuario(rs.getLong("idusuario"));
                doador.setCpf(rs.getString("cpf"));
                doador.setEmail(rs.getString("email"));
                doador.setNome(rs.getString("nome"));
                doador.setTelefone(rs.getString("telefone"));
                doadores.add(doador);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doadores;
    }

    public boolean excluir(Long id) {
        DoacaoEntradaDAO doacaoDAO = new DoacaoEntradaDAO();

        boolean doacoesExcluidas = doacaoDAO.excluirPorIdDoador(id);
        if (!doacoesExcluidas) {
            return false;
        }

        String sql = "DELETE FROM doador WHERE iddoador = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
