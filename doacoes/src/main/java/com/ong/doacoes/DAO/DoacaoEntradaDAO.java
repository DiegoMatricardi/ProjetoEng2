package com.ong.doacoes.DAO;

import com.ong.doacoes.ConnectionFactory;
import com.ong.doacoes.Model.DoacaoEntrada;
import com.ong.doacoes.Model.DoacaoEntradaItem;
import com.ong.doacoes.Model.DoacaoSaidaItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoacaoEntradaDAO {

    public boolean excluirPorIdDoador(Long idDoador) {
        String sql = "DELETE FROM doacao_entrada WHERE iddoador = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idDoador);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir doações: " + e.getMessage());
            return false;
        }
    }

    public boolean inserirComItens(DoacaoEntrada doacao) {
        Connection conn = null;
        PreparedStatement stmtDoacao = null;
        PreparedStatement stmtItem = null;
        ResultSet rs = null;
        ItemDAO itemDAO = new ItemDAO();

        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            String sqlDoacao = "INSERT INTO doacao_entrada (iddoador, idcolaborador, data_abertura, data_busca, data_fim, data_notificacao, endereco_busca, observacao, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stmtDoacao = conn.prepareStatement(sqlDoacao, PreparedStatement.RETURN_GENERATED_KEYS);
            stmtDoacao.setLong(1, doacao.getIddoador());
            stmtDoacao.setLong(2, doacao.getIdcolaborador());
            stmtDoacao.setTimestamp(3, doacao.getDataAbertura());
            stmtDoacao.setTimestamp(4, doacao.getDataBusca());
            stmtDoacao.setTimestamp(5, doacao.getDataFim());
            stmtDoacao.setTimestamp(6, doacao.getDataNotificacao());
            stmtDoacao.setString(7, doacao.getEnderecoBusca());
            stmtDoacao.setString(8, doacao.getObservacao());
            stmtDoacao.setString(9, doacao.getStatus());
            stmtDoacao.executeUpdate();

            rs = stmtDoacao.getGeneratedKeys();
            if (rs.next()) {
                doacao.setIddoacaoentrada(rs.getLong(1));
            }

            String sqlItem = "INSERT INTO doacao_entrada_item (iddoacao_entrada, iditem, descricao, valor_qtde_doacao_entrada_item) " +
                    "VALUES (?, ?, ?, ?)";
            stmtItem = conn.prepareStatement(sqlItem);
            for (DoacaoEntradaItem item : doacao.getItens()) {
                stmtItem.setLong(1, doacao.getIddoacaoentrada());
                stmtItem.setLong(2, item.getIdItem());
                stmtItem.setString(3, item.getDescricao());
                stmtItem.setDouble(4, item.getValorQtdeDoacaoEntradaItem());
                stmtItem.executeUpdate();

                // Atualizar estoque
                if (!itemDAO.atualizarEstoque(conn, item.getIdItem(), item.getValorQtdeDoacaoEntradaItem())) {
                    throw new SQLException("Falha ao atualizar estoque para iditem: " + item.getIdItem());
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir doação: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Erro ao fazer rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmtDoacao != null) stmtDoacao.close();
                if (stmtItem != null) stmtItem.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }

    public List<DoacaoEntrada> listarPorIdUsuario(Long idUsuario) throws SQLException {
        List<DoacaoEntrada> doacoes = new ArrayList<>();
        String sqlDoacao = "SELECT de.iddoacao_entrada, de.iddoador, de.idcolaborador, de.data_abertura, de.data_busca, de.data_fim, de.data_notificacao, de.endereco_busca, de.observacao, de.status " +
                "FROM doacao_entrada de " +
                "JOIN doador d ON de.iddoador = d.iddoador " +
                "WHERE d.idusuario = ?";
        String sqlItens = "SELECT dei.iddoacao_entrada_item, dei.iditem, dei.descricao, dei.valor_qtde_doacao_entrada_item, it.nome " +
                "FROM doacao_entrada_item dei " +
                "JOIN item i ON dei.iditem = i.iditem " +
                "JOIN item_tipo it ON i.iditem_tipo = it.iditem_tipo " +
                "WHERE dei.iddoacao_entrada = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmtDoacao = conn.prepareStatement(sqlDoacao)) {
            stmtDoacao.setLong(1, idUsuario);
            try (ResultSet rsDoacao = stmtDoacao.executeQuery()) {
                while (rsDoacao.next()) {
                    DoacaoEntrada doacao = new DoacaoEntrada();
                    doacao.setIddoacaoentrada(rsDoacao.getLong("iddoacao_entrada"));
                    doacao.setIddoador(rsDoacao.getLong("iddoador"));
                    doacao.setIdcolaborador(rsDoacao.getLong("idcolaborador"));
                    doacao.setDataAbertura(rsDoacao.getTimestamp("data_abertura"));
                    doacao.setDataBusca(rsDoacao.getTimestamp("data_busca"));
                    doacao.setDataFim(rsDoacao.getTimestamp("data_fim"));
                    doacao.setDataNotificacao(rsDoacao.getTimestamp("data_notificacao"));
                    doacao.setEnderecoBusca(rsDoacao.getString("endereco_busca"));
                    doacao.setObservacao(rsDoacao.getString("observacao"));
                    doacao.setStatus(rsDoacao.getString("status"));

                    List<DoacaoEntradaItem> itens = new ArrayList<>();
                    try (PreparedStatement stmtItens = conn.prepareStatement(sqlItens)) {
                        stmtItens.setLong(1, doacao.getIddoacaoentrada());
                        try (ResultSet rsItens = stmtItens.executeQuery()) {
                            while (rsItens.next()) {
                                DoacaoEntradaItem item = new DoacaoEntradaItem();
                                item.setIddoacaoEntradaItem(rsItens.getLong("iddoacao_entrada_item"));
                                item.setIdItem(rsItens.getLong("iditem"));
                                item.setDescricao(rsItens.getString("descricao"));
                                item.setValorQtdeDoacaoEntradaItem(rsItens.getDouble("valor_qtde_doacao_entrada_item"));
                                // Adicionar nomeItemTipo como propriedade dinâmica no JSON
                                itens.add(item);
                            }
                        }
                    }
                    doacao.setItens(itens);
                    doacoes.add(doacao);
                }
            }
        }
        return doacoes;
    }

    public List<DoacaoEntrada> listarComFiltros(String status, String tipoItem) throws SQLException {
        List<DoacaoEntrada> doacoes = new ArrayList<>();
        Map<Long, DoacaoEntrada> doacaoMap = new HashMap<>();

        StringBuilder sql = new StringBuilder(
                "SELECT de.iddoacao_entrada, de.iddoador, de.idcolaborador, de.status, de.data_abertura, de.data_busca, " +
                        "de.data_fim, de.data_notificacao, de.endereco_busca, de.observacao, " +
                        "dei.iddoacao_entrada_item, dei.valor_qtde_doacao_entrada_item, dei.descricao, dei.iditem, it.nome AS nomeItemTipo " +
                        "FROM doacao_entrada de " +
                        "LEFT JOIN doacao_entrada_item dei ON de.iddoacao_entrada = dei.iddoacao_entrada " +
                        "LEFT JOIN item i ON dei.iditem = i.iditem " +
                        "LEFT JOIN item_tipo it ON i.iditem_tipo = it.iditem_tipo " +
                        "WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();
        if (status != null && !status.isEmpty() && !status.equals("Todos")) {
            sql.append(" AND de.status = ?");
            params.add(status);
        }
        if (tipoItem != null && !tipoItem.isEmpty() && !tipoItem.equals("Todos")) {
            sql.append(" AND it.nome = ?");
            params.add(tipoItem);
        }

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Long idDoacao = rs.getLong("iddoacao_entrada");
                    DoacaoEntrada doacao = doacaoMap.get(idDoacao);

                    if (doacao == null) {
                        doacao = new DoacaoEntrada();
                        doacao.setIddoacaoentrada(idDoacao);
                        doacao.setIddoador(rs.getLong("iddoador"));
                        doacao.setIdcolaborador(rs.getLong("idcolaborador"));
                        doacao.setStatus(rs.getString("status"));
                        doacao.setDataAbertura(rs.getTimestamp("data_abertura"));
                        doacao.setDataBusca(rs.getTimestamp("data_busca"));
                        doacao.setDataFim(rs.getTimestamp("data_fim"));
                        doacao.setDataNotificacao(rs.getTimestamp("data_notificacao"));
                        doacao.setEnderecoBusca(rs.getString("endereco_busca"));
                        doacao.setObservacao(rs.getString("observacao"));
                        doacao.setItens(new ArrayList<>());
                        doacaoMap.put(idDoacao, doacao);
                        doacoes.add(doacao);
                    }

                    if (rs.getLong("iddoacao_entrada_item") != 0) {
                        DoacaoEntradaItem item = new DoacaoEntradaItem();
                        item.setIddoacaoEntradaItem(rs.getLong("iddoacao_entrada_item"));
                        item.setValorQtdeDoacaoEntradaItem(rs.getDouble("valor_qtde_doacao_entrada_item"));
                        item.setIddoacaoEntrada(idDoacao);
                        item.setDescricao(rs.getString("descricao"));
                        item.setIdItem(rs.getLong("iditem"));
                        doacao.getItens().add(item);
                    }
                }
            }
        }
        return doacoes;
    }

    public List<String> listarTiposItem() throws SQLException {
        List<String> tipos = new ArrayList<>();
        String sql = "SELECT DISTINCT nome FROM item_tipo WHERE nome IS NOT NULL";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                tipos.add(rs.getString("nome"));
            }
        }
        return tipos;
    }

    public boolean excluir(Connection conn, long iddoacao_entrada) {
        String sql = "DELETE FROM doacao_entrada WHERE iddoacao_entrada = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, iddoacao_entrada);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir doação: " + e.getMessage());
            throw new RuntimeException("Erro ao excluir doação: " + e.getMessage(), e);
        }
    }

    public boolean isDoacaoPendente(Connection conn, Long idDoacaoEntrada) {
        String sql = "SELECT status FROM doacao_entrada WHERE iddoacao_entrada = ? AND status = 'P'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idDoacaoEntrada);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao validar doação de entrada ID " + idDoacaoEntrada + ": " + e.getMessage());
            return false;
        }
    }

    public List<DoacaoSaidaItem> listarItens(Connection conn, Long idDoacaoEntrada) {
        List<DoacaoSaidaItem> itens = new ArrayList<>();
        String sql = "SELECT iditem, valor_qtde_doacao_entrada_item FROM doacao_entrada_item WHERE iddoacao_entrada = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idDoacaoEntrada);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DoacaoSaidaItem item = new DoacaoSaidaItem();
                    item.setIditem(rs.getLong("iditem"));
                    item.setValorQtdeDoacaoSaidaItem(rs.getDouble("valor_qtde_doacao_entrada_item"));
                    itens.add(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar itens da doação de entrada ID " + idDoacaoEntrada + ": " + e.getMessage());
        }
        return itens;
    }

    public boolean atualizarStatus(Connection conn, Long idDoacaoEntrada, char status) {
        String sql = "UPDATE doacao_entrada SET status = ? WHERE iddoacao_entrada = ? AND status = 'P'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(status));
            stmt.setLong(2, idDoacaoEntrada);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status da doação de entrada ID " + idDoacaoEntrada + ": " + e.getMessage());
            return false;
        }
    }
}

