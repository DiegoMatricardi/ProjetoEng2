package com.ong.doacoes.Controller;

import com.ong.doacoes.DAO.BeneficiarioDAO;
import com.ong.doacoes.Model.Beneficiario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/beneficiarios")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class BeneficiarioController {

    private final BeneficiarioDAO dao = new BeneficiarioDAO();

    @PostMapping("/inserir")
    public ResponseEntity<?> inserir(@RequestBody Beneficiario beneficiario) {
        try {
            if (beneficiario.getNome() == null || beneficiario.getNome().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Nome do beneficiário é obrigatório.");
            }
            boolean sucesso = dao.inserir(beneficiario);
            if (sucesso) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Beneficiário inserido com sucesso!");
                response.put("idbeneficiario", beneficiario.getIdbeneficiario());
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao inserir beneficiário.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao inserir beneficiário: " + e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listar() {
        try {
            List<Beneficiario> beneficiarios = dao.listarTodos();
            if (beneficiarios.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nenhum beneficiário encontrado.");
            }
            return ResponseEntity.ok(beneficiarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar beneficiários: " + e.getMessage());
        }
    }
}