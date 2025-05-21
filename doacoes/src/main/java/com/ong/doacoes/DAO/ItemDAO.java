package com.ong.doacoes.DAO;

import com.ong.doacoes.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemDAO {

    public boolean atualizarEstoque(Connection conn, long idItem, double quantidade) {
        String sqlEstoque = "UPDATE item SET estoque = estoque + ? WHERE iditem = ?";

        try (PreparedStatement stmtEstoque = conn.prepareStatement(sqlEstoque)) {
            stmtEstoque.setDouble(1, quantidade);
            stmtEstoque.setLong(2, idItem);
            stmtEstoque.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar estoque do item: " + e.getMessage());
            return false;
        }
    }

    public Long buscarIdItemPorIdItemTipo(Long idItemTipo) throws SQLException {
        String sql = "SELECT iditem FROM item WHERE iditem_tipo = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idItemTipo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("iditem");
                }
                return null;
            }
        }
    }
}