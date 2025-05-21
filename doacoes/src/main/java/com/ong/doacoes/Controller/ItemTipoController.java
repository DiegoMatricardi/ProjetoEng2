package com.ong.doacoes.Controller;

import com.ong.doacoes.DAO.ItemTipoDAO;
import com.ong.doacoes.Model.ItemTipo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itemTipo")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class ItemTipoController {

    @GetMapping("/listar")
    public ResponseEntity<?> listarTiposItens() {
        try {
            ItemTipoDAO itemTipoDAO = new ItemTipoDAO();
            List<ItemTipo> tipos = itemTipoDAO.listar();
            return ResponseEntity.ok(tipos);
        } catch (Exception e) {
            System.err.println("Erro ao listar tipos de itens: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar tipos de itens: " + e.getMessage());
        }
    }
}