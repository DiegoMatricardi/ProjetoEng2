package com.ong.doacoes;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    private static Connection connection;

    private ConnectionFactory() {
        // Construtor privado
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Properties props = new Properties();
                InputStream input = ConnectionFactory.class.getClassLoader().getResourceAsStream("application.properties");
                props.load(input);

                String url = props.getProperty("db.url");
                String username = props.getProperty("db.username");
                String password = props.getProperty("db.password");

                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Conexão estabelecida com sucesso!");
            } catch (Exception e) {
                System.err.println("Erro na conexão: " + e.getMessage());
                throw new SQLException(e);
            }
        }
        return connection;
    }
}
