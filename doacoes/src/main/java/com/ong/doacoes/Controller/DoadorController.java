package com.ong.doacoes.Controller;

import com.ong.doacoes.DAO.DoadorDAO;
import com.ong.doacoes.Model.Doador;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doador")
public class DoadorController {

    private final DoadorDAO dao = new DoadorDAO();

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrar(@RequestBody Doador doador) {
        Long idUsuario = dao.buscarIdUsuarioPorEmail(doador.getEmail());

        if (idUsuario == null) {
            return new ResponseEntity<>("Usuário não encontrado!", HttpStatus.BAD_REQUEST); // Se não encontrar o usuário
        }

        // Atribui o idUsuario encontrado ao doador
        doador.setIdUsuario(idUsuario);

        // Verifica se o doador já existe
        if (dao.doadorExistePorIdUsuario(doador.getIdUsuario())) {
            return new ResponseEntity<>("Doador já cadastrado!", HttpStatus.BAD_REQUEST);  // Se doador já estiver cadastrado
        }

        // Caso o doador não exista, salva o novo doador
        dao.salvar(doador);
        return new ResponseEntity<>("Doador cadastrado com sucesso!", HttpStatus.CREATED);  // Retorna 201 - Created
    }
}
