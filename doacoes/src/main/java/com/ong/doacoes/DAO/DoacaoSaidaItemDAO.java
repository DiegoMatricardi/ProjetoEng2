package com.ong.doacoes.DAO;

import com.ong.doacoes.Model.DoacaoSaidaItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoacaoSaidaItemDAO {
    public boolean inserir(Connection conn, DoacaoSaidaItem item) {
        String sql = "INSERT INTO doacao_saida_item (iddoacao_saida, iditem, valor_qtde_doacao_saida_item) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, item.getIddoacaoSaida());
            stmt.setLong(2, item.getIditem());
            stmt.setDouble(3, item.getValorQtdeDoacaoSaidaItem());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        item.setIddoacaoSaidaItem(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir item da doação de saída: " + e.getMessage());
            return false;
        }
    }
}