package com.ong.doacoes.Controller;

import com.ong.doacoes.ConnectionFactory;
import com.ong.doacoes.DAO.UsuarioDAO;
import com.ong.doacoes.Model.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class UsuarioController {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Usuario usuario) {
        Usuario usuarioAutenticado = usuarioDAO.buscarUsuarioPorEmailESenha(usuario.getEmail(), usuario.getSenha());

        Map<String, Object> response = new HashMap<>();

        if (usuarioAutenticado != null && usuarioAutenticado.getAtivo() == 1) {
            response.put("message", "Login bem-sucedido!");
            response.put("idusuario", String.valueOf(usuarioAutenticado.getIdUsuario()));
            response.put("idusuario_tipo", String.valueOf(usuarioAutenticado.getIdUsuarioTipo()));
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Email ou senha inválidos, ou usuário inativo.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/criar")
    public ResponseEntity<Map<String, String>> criarUsuario(@RequestBody Usuario usuario) {
        Map<String, String> response = new HashMap<>();

        if (usuarioDAO.usuarioExistePorEmail(usuario.getEmail())) {
            response.put("message", "Usuário já existe!");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        usuario.setAtivo(1);
        usuario.setIdUsuarioTipo(1L);

        boolean sucesso = usuarioDAO.salvar(usuario);
        if (sucesso) {
            response.put("message", "Usuário criado com sucesso!");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            response.put("message", "Erro ao criar usuário.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/promover/{id}")
    public String promoverUsuario(@PathVariable("id") Long idUsuario) {
        try (Connection conn = ConnectionFactory.getConnection()) {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            boolean sucesso = usuarioDAO.promoverParaTipo2(conn, idUsuario);

            if (sucesso) {
                return "Usuário promovido com sucesso.";
            } else {
                return "Erro ao promover usuário.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro interno no servidor.";
        }
    }
}


