package com.ong.doacoes.DAO;

import com.ong.doacoes.ConnectionFactory;
import com.ong.doacoes.Model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    public double verificarEstoque(Connection conn, Long idItem) {
        String sql = "SELECT estoque FROM item WHERE iditem = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idItem);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("estoque");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar estoque do item ID " + idItem + ": " + e.getMessage());
        }
        return 0.0; // Retorna 0 se o item não for encontrado ou houver erro
    }

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

    public List<Item> buscarTodos() {
        List<Item> itens = new ArrayList<>();
        String sql = "SELECT iditem, descricao, iditem_tipo, estoque FROM item";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Item item = new Item();
                item.setIdItem(rs.getLong("iditem"));
                item.setDescricao(rs.getString("descricao"));
                item.setIdItemTipo(rs.getLong("iditem_tipo"));
                item.setEstoque(rs.getInt("estoque"));
                itens.add(item);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar itens: " + e.getMessage());
        }

        return itens;
    }
}