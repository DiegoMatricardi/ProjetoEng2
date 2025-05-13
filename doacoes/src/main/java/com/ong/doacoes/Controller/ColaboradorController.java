package com.ong.doacoes.Controller;

import com.ong.doacoes.DAO.ColaboradorDAO;
import com.ong.doacoes.Model.Colaborador;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/colaborador")
@CrossOrigin(origins = "http://127.0.0.1:3000") // permite chamadas do frontend local
public class ColaboradorController {

    private final ColaboradorDAO dao = new ColaboradorDAO();

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrar(@RequestBody Colaborador colaborador) {
        if (colaborador.getEmailSecundario() == null || colaborador.getEmailSecundario().isEmpty()) {
            return new ResponseEntity<>("E-mail secundário é obrigatório!", HttpStatus.BAD_REQUEST);
        }

        Long idUsuario = dao.buscarIdUsuarioPorEmail(colaborador.getEmailSecundario());

        if (idUsuario == null) {
            return new ResponseEntity<>("Usuário não encontrado!", HttpStatus.BAD_REQUEST);
        }

        colaborador.setIdusuario(idUsuario);

        if (dao.colaboradorExistePorIdUsuario(colaborador.getIdusuario())) {
            return new ResponseEntity<>("Colaborador já cadastrado!", HttpStatus.BAD_REQUEST);
        }


        boolean sucesso = dao.salvar(colaborador);
        if (sucesso) {
            return new ResponseEntity<>("Colaborador cadastrado com sucesso!", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Erro ao salvar colaborador!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
