package com.ong.doacoes.Controller;

import com.ong.doacoes.ConnectionFactory;
import com.ong.doacoes.DAO.DoacaoEntradaDAO;
import com.ong.doacoes.DAO.DoacaoEntradaItemDAO;
import com.ong.doacoes.DAO.DoadorDAO;
import com.ong.doacoes.DAO.ItemDAO;
import com.ong.doacoes.Model.DoacaoEntrada;
import com.ong.doacoes.Model.DoacaoEntradaItem;
import com.ong.doacoes.Model.Doador;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/doacaoEntrada")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class DoacaoEntradaController {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final List<String> STATUS_PERMITIDOS = Arrays.asList("P", "A", "C");

    @PostMapping("/inserir")
    public ResponseEntity<?> inserirDoacao(@RequestBody Map<String, Object> requestBody) {
        try {
            Long idUsuario = Long.parseLong(requestBody.get("idusuario").toString());
            if (idUsuario <= 0) return ResponseEntity.badRequest().body("ID do usuário inválido.");

            DoadorDAO doadorDAO = new DoadorDAO();
            Doador doador = doadorDAO.buscarPorIdUsuario(idUsuario);
            if (doador == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doador não encontrado.");

            DoacaoEntrada doacao = new DoacaoEntrada();
            doacao.setIddoador(doador.getIdDoador());
            doacao.setIdcolaborador(1L);
            doacao.setStatus("P");


            try {
                String dataAberturaStr = (String) requestBody.get("dataentrada");
                if (dataAberturaStr == null) return ResponseEntity.badRequest().body("Data de abertura obrigatória.");
                doacao.setDataAbertura(new Timestamp(DATE_FORMAT.parse(dataAberturaStr).getTime()));

                String dataBuscaStr = (String) requestBody.get("tipoentrega");
                if (dataBuscaStr != null && !dataBuscaStr.isEmpty()) {
                    doacao.setDataBusca(new Timestamp(DATE_FORMAT.parse(dataBuscaStr).getTime()));
                }

                String dataFimStr = (String) requestBody.get("datafim");
                if (dataFimStr != null && !dataFimStr.isEmpty()) {
                    doacao.setDataFim(new Timestamp(DATE_FORMAT.parse(dataFimStr).getTime()));
                }

                String dataNotificacaoStr = (String) requestBody.get("datanotificacao");
                if (dataNotificacaoStr != null && !dataNotificacaoStr.isEmpty()) {
                    doacao.setDataNotificacao(new Timestamp(DATE_FORMAT.parse(dataNotificacaoStr).getTime()));
                }
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Formato de data inválido.");
            }

            String endereco = (String) requestBody.get("endereco");
            String bairro = (String) requestBody.get("bairro");
            String cidade = (String) requestBody.get("cidade");
            String estado = (String) requestBody.get("estado");
            doacao.setEnderecoBusca(String.format("%s, %s, %s - %s",
                    endereco != null ? endereco : "",
                    bairro != null ? bairro : "",
                    cidade != null ? cidade : "",
                    estado != null ? estado : "").trim());
            doacao.setObservacao("");

            List<DoacaoEntradaItem> itens = new ArrayList<>();
            List<Map<String, Object>> itensJson = (List<Map<String, Object>>) requestBody.get("itens");
            if (itensJson == null || itensJson.isEmpty()) return ResponseEntity.badRequest().body("Nenhum item fornecido.");

            ItemDAO itemDAO = new ItemDAO();
            for (Map<String, Object> itemJson : itensJson) {
                DoacaoEntradaItem item = new DoacaoEntradaItem();
                Long idItemTipo = Long.parseLong(itemJson.get("idItem").toString());
                Long idItem = itemDAO.buscarIdItemPorIdItemTipo(idItemTipo);
                if (idItem == null) return ResponseEntity.badRequest().body("Item não encontrado para o tipo: " + idItemTipo);
                item.setIdItem(idItem);
                item.setDescricao((String) itemJson.get("descricao"));
                item.setValorQtdeDoacaoEntradaItem(Double.parseDouble(itemJson.get("valorQtdeDoacaoEntradaItem").toString()));
                itens.add(item);
            }
            doacao.setItens(itens);

            DoacaoEntradaDAO dao = new DoacaoEntradaDAO();
            boolean sucesso = dao.inserirComItens(doacao);
            if (sucesso) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Doação registrada com sucesso!");
                response.put("iddoacaoentrada", doacao.getIddoacaoentrada());
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar doação.");
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no servidor: " + e.getMessage());
        }
    }

    @GetMapping("/listarPorIdUsuario/{idUsuario}")
    public ResponseEntity<?> listarPorIdUsuario(@PathVariable Long idUsuario) {
        try {
            DoacaoEntradaDAO dao = new DoacaoEntradaDAO();
            List<DoacaoEntrada> doacoes = dao.listarPorIdUsuario(idUsuario);
            // Mapear para incluir nomeItemTipo no JSON
            List<Map<String, Object>> response = doacoes.stream().map(doacao -> {
                Map<String, Object> doacaoMap = new HashMap<>();
                doacaoMap.put("iddoacaoEntrada", doacao.getIddoacaoentrada());
                doacaoMap.put("dataAbertura", doacao.getDataAbertura());
                doacaoMap.put("status", doacao.getStatus());
                doacaoMap.put("dataFim", doacao.getDataFim());
                doacaoMap.put("dataNotificacao", doacao.getDataNotificacao());
                doacaoMap.put("enderecoBusca", doacao.getEnderecoBusca());
                List<Map<String, Object>> itens = doacao.getItens().stream().map(item -> {
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("iddoacaoEntradaItem", item.getIddoacaoEntradaItem());
                    itemMap.put("idItem", item.getIdItem());
                    itemMap.put("descricao", item.getDescricao());
                    itemMap.put("valorQtdeDoacaoEntradaItem", item.getValorQtdeDoacaoEntradaItem());
                    // Buscar nomeItemTipo
                    try {
                        String sql = "SELECT it.nome FROM item i JOIN item_tipo it ON i.iditem_tipo = it.iditem_tipo WHERE i.iditem = ?";
                        try (Connection conn = ConnectionFactory.getConnection();
                             PreparedStatement stmt = conn.prepareStatement(sql)) {
                            stmt.setLong(1, item.getIdItem());
                            try (ResultSet rs = stmt.executeQuery()) {
                                if (rs.next()) {
                                    itemMap.put("nomeItemTipo", rs.getString("nome"));
                                }
                            }
                        }
                    } catch (SQLException e) {
                        System.err.println("Erro ao buscar nomeItemTipo: " + e.getMessage());
                    }
                    return itemMap;
                }).collect(Collectors.toList());
                doacaoMap.put("itens", itens);
                return doacaoMap;
            }).collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Erro ao listar doações: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar doações: " + e.getMessage());
        }
    }

    @GetMapping("/listarDoacao")
    public ResponseEntity<?> listar(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "tipoItem", required = false) String tipoItem)  {
        try {
            if (status != null && !status.isEmpty() && !STATUS_PERMITIDOS.contains(status) && !status.equals("Todos")) {
                return ResponseEntity.badRequest().body("Status inválido. Use: " + STATUS_PERMITIDOS + " ou 'Todos'.");
            }
            DoacaoEntradaDAO dao = new DoacaoEntradaDAO();
            List<DoacaoEntrada> doacoes = dao.listarComFiltros(status, tipoItem);
            List<Map<String, Object>> response = doacoes.stream().map(doacao -> {
                Map<String, Object> doacaoMap = new HashMap<>();
                doacaoMap.put("iddoacao_entrada", doacao.getIddoacaoentrada());
                doacaoMap.put("data_abertura", doacao.getDataAbertura());
                doacaoMap.put("status", doacao.getStatus());
                doacaoMap.put("data_fim", doacao.getDataFim());
                doacaoMap.put("data_notificacao", doacao.getDataNotificacao());
                doacaoMap.put("endereco_busca", doacao.getEnderecoBusca());
                doacaoMap.put("observacao", doacao.getObservacao());
                List<Map<String, Object>> itens = doacao.getItens().stream().map(item -> {
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("iddoacao_entrada_item", item.getIddoacaoEntradaItem());
                    itemMap.put("iditem", item.getIdItem());
                    itemMap.put("descricao", item.getDescricao());
                    itemMap.put("valor_qtde_doacao_entrada_item", item.getValorQtdeDoacaoEntradaItem());
                    try {
                        String sql = "SELECT it.nome FROM item i JOIN item_tipo it ON i.iditem_tipo = it.iditem_tipo WHERE i.iditem = ?";
                        try (Connection conn = ConnectionFactory.getConnection();
                             PreparedStatement stmt = conn.prepareStatement(sql)) {
                            stmt.setLong(1, item.getIdItem());
                            try (ResultSet rs = stmt.executeQuery()) {
                                if (rs.next()) {
                                    itemMap.put("nomeItemTipo", rs.getString("nome"));
                                }
                            }
                        }
                    } catch (SQLException e) {
                        System.err.println("Erro ao buscar nomeItemTipo: " + e.getMessage());
                    }
                    return itemMap;
                }).collect(Collectors.toList());
                doacaoMap.put("itens", itens);
                return doacaoMap;
            }).collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Erro ao listar doações: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar doações: " + e.getMessage());
        }
    }

    @GetMapping("/tiposItem")
    public ResponseEntity<?> listarTiposItem() {
        try {
            DoacaoEntradaDAO dao = new DoacaoEntradaDAO();
            List<String> tipos = dao.listarTiposItem();
            return ResponseEntity.ok(tipos);
        } catch (Exception e) {
            System.err.println("Erro ao listar tipos de item: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar tipos de item: " + e.getMessage());
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluir(@PathVariable long id, @RequestHeader("idusuario") String idUsuario) {
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false); // Iniciar transação

            // Validar colaborador
            String sql = "SELECT idusuario_tipo FROM usuario WHERE idusuario = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, Long.parseLong(idUsuario));
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String usuarioTipo = rs.getString("idusuario_tipo");
                        if (!"2".equals(usuarioTipo)) {
                            conn.rollback();
                            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso restrito a colaboradores.");
                        }
                    } else {
                        conn.rollback();
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não encontrado.");
                    }
                }
            }

            // Excluir itens associados
            DoacaoEntradaItemDAO itemDAO = new DoacaoEntradaItemDAO();
            itemDAO.excluirPorDoacaoEntrada(conn, id);

            // Excluir doação
            DoacaoEntradaDAO dao = new DoacaoEntradaDAO();
            boolean sucesso = dao.excluir(conn, id);
            if (sucesso) {
                conn.commit();
                Map<String, String> response = new HashMap<>();
                response.put("message", "Doação excluída com sucesso!");
                return ResponseEntity.ok(response);
            } else {
                conn.rollback();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doação não encontrada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao excluir doação: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Erro ao fazer rollback: " + ex.getMessage());
                }
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao excluir doação: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro ao excluir doação: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Erro ao fazer rollback: " + ex.getMessage());
                }
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao excluir doação: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
    }

    @GetMapping("/buscarPorCpf/{cpf}")
    public ResponseEntity<?> buscarPorCpf(@PathVariable String cpf) {
        try {
            DoadorDAO doadorDAO = new DoadorDAO();
            Doador doador = doadorDAO.buscarPorCpf(cpf);

            if (doador == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doador não encontrado.");
            }

            DoacaoEntradaDAO doacaoDAO = new DoacaoEntradaDAO();
            List<DoacaoEntrada> doacoes = doacaoDAO.listarPorIdUsuario(doador.getIdUsuario());

            List<Map<String, Object>> response = doacoes.stream().map(doacao -> {
                Map<String, Object> doacaoMap = new HashMap<>();
                doacaoMap.put("iddoacao_entrada", doacao.getIddoacaoentrada());
                doacaoMap.put("data_abertura", doacao.getDataAbertura());
                doacaoMap.put("status", doacao.getStatus());
                doacaoMap.put("data_fim", doacao.getDataFim() != null ? doacao.getDataFim() : "Não informado");
                doacaoMap.put("endereco_busca", doacao.getEnderecoBusca() != null && !doacao.getEnderecoBusca().isEmpty() ? doacao.getEnderecoBusca() : "Não informado");
                doacaoMap.put("observacao", doacao.getObservacao() != null && !doacao.getObservacao().isEmpty() ? doacao.getObservacao() : "Nenhuma");

                List<Map<String, Object>> itens = doacao.getItens().stream().map(item -> {
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("iddoacaoEntradaItem", item.getIddoacaoEntradaItem());
                    itemMap.put("idItem", item.getIdItem());
                    itemMap.put("descricao", item.getDescricao());
                    itemMap.put("valor_qtde_doacao_entrada_item", item.getValorQtdeDoacaoEntradaItem());

                    try (Connection conn = ConnectionFactory.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(
                                 "SELECT it.nome FROM item i JOIN item_tipo it ON i.iditem_tipo = it.iditem_tipo WHERE i.iditem = ?")) {
                        stmt.setLong(1, item.getIdItem());
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                itemMap.put("nomeItemTipo", rs.getString("nome"));
                            }
                        }
                    } catch (SQLException e) {
                        System.err.println("Erro ao buscar nomeItemTipo: " + e.getMessage());
                    }

                    return itemMap;
                }).collect(Collectors.toList());

                doacaoMap.put("itens", itens);
                return doacaoMap;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar doações por CPF: " + e.getMessage());
        }
    }
}