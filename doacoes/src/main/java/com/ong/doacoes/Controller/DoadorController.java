package com.ong.doacoes.Controller;

import com.ong.doacoes.DAO.DoadorDAO;
import com.ong.doacoes.Model.Doador;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.badRequest().build(); // Retorna 400 para IDs inválidos
        }

        Doador doador = dao.buscarPorIdUsuario(id);
        if (doador != null) {
            return ResponseEntity.ok(doador);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Retorna 404 se não encontrado
        }
    }



}
