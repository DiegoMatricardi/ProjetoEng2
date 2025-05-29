package com.ong.doacoes.Controller;

import com.ong.doacoes.ConnectionFactory;
import com.ong.doacoes.DAO.BeneficiarioDAO;
import com.ong.doacoes.DAO.DoacaoEntradaDAO;
import com.ong.doacoes.DAO.DoacaoSaidaDAO;
import com.ong.doacoes.DAO.DoacaoSaidaItemDAO;
import com.ong.doacoes.DAO.ItemDAO;
import com.ong.doacoes.Model.Beneficiario;
import com.ong.doacoes.Model.DoacaoSaida;
import com.ong.doacoes.Model.DoacaoSaidaItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/doacaoSaida")
@CrossOrigin(origins = {"http://127.0.0.1:3000", "http://127.0.0.1:5500"})
public class DoacaoSaidaController {

    private void checkConnection(Connection conn, String stage) throws SQLException {
        if (conn == null || conn.isClosed()) {
            System.err.println("Erro: Conexão fechada em " + stage);
            throw new SQLException("Conexão fechada em " + stage);
        }
        System.out.println("Conexão OK em " + stage + ": " + conn);
    }

    @PostMapping("/inserir")
    public ResponseEntity<?> inserirDoacaoSaida(@RequestBody Map<String, Object> requestBody, @RequestHeader("idusuario") String idUsuario) {
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            checkConnection(conn, "início da transação");
            conn.setAutoCommit(false);

            String sqlUsuario = "SELECT idusuario_tipo FROM usuario WHERE idusuario = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlUsuario)) {
                stmt.setLong(1, Long.parseLong(idUsuario));
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next() || !"2".equals(rs.getString("idusuario_tipo"))) {
                        conn.rollback();
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso restrito a colaboradores.");
                    }
                }
            }
            checkConnection(conn, "após validar colaborador");

            // Validar beneficiário
            Long idBeneficiario = Long.parseLong(requestBody.get("idbeneficiario").toString());
            BeneficiarioDAO beneficiarioDAO = new BeneficiarioDAO();
            Beneficiario beneficiario = beneficiarioDAO.buscarPorId(conn, idBeneficiario);
            if (beneficiario == null) {
                conn.rollback();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Beneficiário inválido.");
            }
            checkConnection(conn, "após validar beneficiário");

            // Validar doação de entrada
            Long idDoacaoEntrada = Long.parseLong(requestBody.get("iddoacaoEntrada").toString());
            DoacaoEntradaDAO doacaoEntradaDAO = new DoacaoEntradaDAO();
            if (!doacaoEntradaDAO.isDoacaoPendente(conn, idDoacaoEntrada)) {
                conn.rollback();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Doação de entrada não encontrada ou não está pendente.");
            }
            checkConnection(conn, "após validar doação de entrada");

            // Obter itens da doação de entrada
            List<DoacaoSaidaItem> itens = doacaoEntradaDAO.listarItens(conn, idDoacaoEntrada);
            if (itens.isEmpty()) {
                conn.rollback();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nenhum item encontrado para a doação de entrada.");
            }
            checkConnection(conn, "após listar itens");

            // Verificar estoque
            ItemDAO itemDAO = new ItemDAO();
            for (DoacaoSaidaItem item : itens) {
                double estoqueAtual = itemDAO.verificarEstoque(conn, item.getIditem());
                if (estoqueAtual < item.getValorQtdeDoacaoSaidaItem()) {
                    conn.rollback();
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Estoque insuficiente para item ID: " + item.getIditem() + ". Disponível: " + estoqueAtual);
                }
            }
            checkConnection(conn, "após verificar estoque");

            // Criar doação de saída
            DoacaoSaida doacaoSaida = new DoacaoSaida();
            doacaoSaida.setIdbeneficiario(idBeneficiario);
            doacaoSaida.setIdcolaborador(Long.parseLong(idUsuario));
            doacaoSaida.setItens(itens);

            DoacaoSaidaDAO doacaoSaidaDAO = new DoacaoSaidaDAO();
            if (!doacaoSaidaDAO.inserir(conn, doacaoSaida)) {
                conn.rollback();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao inserir doação de saída.");
            }
            checkConnection(conn, "após inserir doação de saída");

            // Inserir itens e reduzir estoque
            DoacaoSaidaItemDAO doacaoSaidaItemDAO = new DoacaoSaidaItemDAO();
            for (DoacaoSaidaItem item : itens) {
                item.setIddoacaoSaida(doacaoSaida.getIddoacaoSaida());
                if (!doacaoSaidaItemDAO.inserir(conn, item)) {
                    conn.rollback();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao inserir item da doação de saída.");
                }
                if (!itemDAO.atualizarEstoque(conn, item.getIditem(), -item.getValorQtdeDoacaoSaidaItem())) {
                    conn.rollback();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao atualizar estoque para item ID: " + item.getIditem());
                }
            }
            checkConnection(conn, "após inserir itens e atualizar estoque");

            // Atualizar status da doação de entrada
            if (!doacaoEntradaDAO.atualizarStatus(conn, idDoacaoEntrada, 'A')) {
                conn.rollback();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao atualizar status da doação de entrada.");
            }
            checkConnection(conn, "após atualizar status da doação de entrada");

            conn.commit();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Doação de saída registrada com sucesso!");
            response.put("iddoacaoSaida", doacaoSaida.getIddoacaoSaida());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (SQLException e) {
            System.err.println("Erro ao inserir doação de saída: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Erro ao fazer rollback: " + ex.getMessage());
                }
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao inserir doação de saída: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Erro ao fazer rollback: " + ex.getMessage());
                }
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    if (!conn.isClosed()) {
                        conn.setAutoCommit(true);
                        conn.close();
                        System.out.println("Conexão fechada com sucesso.");
                    } else {
                        System.err.println("Conexão já estava fechada no bloco finally.");
                    }
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
    }
}