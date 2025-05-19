package com.ong.doacoes.DAO;

import com.ong.doacoes.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DoacaoEntradaDAO {

    public boolean excluirPorIdDoador(Long idDoador) {
        String sql = "DELETE FROM doacao_entrada WHERE iddoador = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idDoador);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
