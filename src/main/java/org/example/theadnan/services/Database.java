package org.example.theadnan.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:sqlite:users.db";

    static {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    email TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    age INTEGER,
                    profession TEXT,
                    hobby TEXT,
                    password TEXT NOT NULL
                );
                """;

            stmt.execute(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection connect() throws Exception {
        return DriverManager.getConnection(URL);
    }
}
