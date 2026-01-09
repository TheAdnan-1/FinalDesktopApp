package org.example.theadnan.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:sqlite:users.db";

    static {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            String notesSql = """
    CREATE TABLE IF NOT EXISTS notes (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        user_email TEXT NOT NULL,
        title TEXT NOT NULL,
        content TEXT NOT NULL,
        created_at TEXT,
        FOREIGN KEY(user_email) REFERENCES users(email)
    );
    """;

            stmt.execute(notesSql);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection connect() throws Exception {
        return DriverManager.getConnection(URL);
    }
}
