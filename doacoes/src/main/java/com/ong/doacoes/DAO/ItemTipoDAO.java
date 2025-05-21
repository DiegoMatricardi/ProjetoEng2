package com.ong.doacoes.DAO;

import com.ong.doacoes.ConnectionFactory;
import com.ong.doacoes.Model.ItemTipo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ItemTipoDAO {

    public List<ItemTipo> listar() throws Exception {
        List<ItemTipo> tipos = new ArrayList<>();
        String sql = "SELECT iditem_tipo, nome, descricao FROM item_tipo";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ItemTipo tipo = new ItemTipo(
                        rs.getLong("iditem_tipo"),
                        rs.getString("nome"),
                        rs.getString("descricao")
                );
                tipos.add(tipo);
            }
        }
        return tipos;
    }
}