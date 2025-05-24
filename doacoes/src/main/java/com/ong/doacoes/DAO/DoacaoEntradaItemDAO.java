package com.ong.doacoes.DAO;

import com.ong.doacoes.ConnectionFactory;
import com.ong.doacoes.Model.DoacaoEntradaItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoacaoEntradaItemDAO {

    public boolean inserirItem(Connection conn, long idDoacao, DoacaoEntradaItem item) {
        String sqlVerificaItem = "SELECT COUNT(*) FROM item WHERE iditem = ?";
        String sqlVerifica = "SELECT COUNT(*) FROM doacao_entrada_item WHERE iddoacao_entrada = ? AND iditem = ?";
        String sqlItem = "INSERT INTO doacao_entrada_item (iddoacao_entrada, iditem, descricao, valor_qtde_doacao_entrada_item) VALUES (?, ?, ?, ?)";

        try {
            // Validar iditem
            try (PreparedStatement stmtVerificaItem = conn.prepareStatement(sqlVerificaItem)) {
                stmtVerificaItem.setLong(1, item.getIdItem());
                ResultSet rs = stmtVerificaItem.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    System.err.println("Item com id " + item.getIdItem() + " não existe.");
                    return false;
                }
            }

            // Verificar se o item já existe para essa doação
            try (PreparedStatement stmtVerifica = conn.prepareStatement(sqlVerifica)) {
                stmtVerifica.setLong(1, idDoacao);
                stmtVerifica.setLong(2, item.getIdItem());
                ResultSet rs = stmtVerifica.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    // Item existe, apenas atualizar estoque
                    ItemDAO itemDAO = new ItemDAO();
                    return itemDAO.atualizarEstoque(conn, item.getIdItem(), item.getValorQtdeDoacaoEntradaItem());
                }
            }

            // Item não existe, inserir na tabela doacao_entrada_item
            try (PreparedStatement stmtItem = conn.prepareStatement(sqlItem)) {
                stmtItem.setLong(1, idDoacao);
                stmtItem.setLong(2, item.getIdItem());
                stmtItem.setString(3, item.getDescricao());
                stmtItem.setDouble(4, item.getValorQtdeDoacaoEntradaItem());
                stmtItem.executeUpdate();
            }

            // Atualizar estoque usando ItemDAO
            ItemDAO itemDAO = new ItemDAO();
            if (!itemDAO.atualizarEstoque(conn, item.getIdItem(), item.getValorQtdeDoacaoEntradaItem())) {
                return false;
            }

            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir item: " + e.getMessage());
            return false;
        }
    }

    public boolean excluirPorDoacaoEntrada(Connection conn, long iddoacao_entrada) {
        String sql = "DELETE FROM doacao_entrada_item WHERE iddoacao_entrada = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, iddoacao_entrada);
            stmt.executeUpdate();
            return true; // Retorna true mesmo se nenhum item for excluído (caso válido)
        } catch (SQLException e) {
            System.err.println("Erro ao excluir itens da doação: " + e.getMessage());
            throw new RuntimeException("Erro ao excluir itens da doação: " + e.getMessage(), e);
        }
    }
}