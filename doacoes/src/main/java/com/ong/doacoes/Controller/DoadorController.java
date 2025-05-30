package com.ong.doacoes.Controller;

import com.ong.doacoes.DAO.DoadorDAO;
import com.ong.doacoes.Model.Doador;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doador")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class DoadorController {

    private final DoadorDAO dao = new DoadorDAO();

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarDoador(@RequestBody Doador doador) {
        if (dao.doadorExistePorIdUsuario(doador.getIdUsuario())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Doador já cadastrado.");
        }

        dao.salvar(doador);
        return ResponseEntity.status(HttpStatus.CREATED).body("Doador cadastrado com sucesso.");
    }


    @GetMapping("/verificar")
    public ResponseEntity<Boolean> verificarDoador(@RequestParam String email) {
        Long idUsuario = dao.buscarIdUsuarioPorEmail(email);
        if (idUsuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }

        boolean existe = dao.doadorExistePorIdUsuario(idUsuario);
        return ResponseEntity.ok(existe);
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<Doador> buscarPorIdUsuario(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        Doador doador = dao.buscarPorIdUsuario(id);
        if (doador != null) {
            return ResponseEntity.ok(doador);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/atualizar")
    public ResponseEntity<String> atualizarDoador(@RequestBody Doador doador) {
        if (doador.getIdUsuario() == null || doador.getIdUsuario() <= 0) {
            return ResponseEntity.badRequest().body("Id de usuario invalido!");
        }
        boolean atualizado = dao.atualizar(doador);

        if (atualizado) {
            return ResponseEntity.ok("Atualizado com sucesso.");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doador não encontrado.");
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Doador>> listarTodos() {
        List<Doador> doadores = dao.listarTodos();
        if (doadores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(doadores);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirDoador(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("ID inválido.");
        }

        boolean excluido = dao.excluir(id);

        if (excluido) {
            return ResponseEntity.ok("Doador excluído com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doador não encontrado.");
        }
    }

    @GetMapping("/buscarIdPorIdUsuario/{idUsuario}")
    public ResponseEntity<Long> buscarIdDoadorPorIdUsuario(@PathVariable Long idUsuario) {
        if (idUsuario == null || idUsuario <= 0) {
            return ResponseEntity.badRequest().body(null);
        }

        Doador doador = dao.buscarPorIdUsuario(idUsuario);
        if (doador != null) {
            return ResponseEntity.ok(doador.getIdDoador());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/buscarPorCpf")
    public ResponseEntity<Doador> buscarPorCpf(@RequestParam String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Doador doador = dao.buscarPorCpf(cpf.trim());
        if (doador != null) {
            return ResponseEntity.ok(doador);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
