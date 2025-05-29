package com.ong.doacoes.Controller;

import com.ong.doacoes.DAO.ItemDAO;
import com.ong.doacoes.Model.Item;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/item")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class ItemController {
    private final ItemDAO itemDAO = new ItemDAO();

    @GetMapping
    public List<Item> listarItens() {
        return itemDAO.buscarTodos();
    }
}
