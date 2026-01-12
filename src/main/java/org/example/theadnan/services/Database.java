package org.example.theadnan.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:sqlite:users.db";

    static {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            // users table (email is primary key). balance column may already exist; we try to add it below.
            String usersSql = """
CREATE TABLE IF NOT EXISTS users (
    email TEXT PRIMARY KEY,
    name TEXT,
    age INTEGER,
    profession TEXT,
    hobby TEXT,
    password TEXT,
    balance REAL DEFAULT 0
);
""";

            // notes table (existing)
            String notesSql = """
CREATE TABLE IF NOT EXISTS notes (
    user_email TEXT NOT NULL,
    title TEXT NOT NULL,
    content TEXT NOT NULL,
    created_at TEXT,
    PRIMARY KEY (user_email, title),
    FOREIGN KEY(user_email) REFERENCES users(email)
);
""";

            // money_requests table
            String requestsSql = """
CREATE TABLE IF NOT EXISTS money_requests (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    from_email TEXT NOT NULL,
    to_email TEXT NOT NULL,
    amount REAL NOT NULL,
    status TEXT NOT NULL, -- PENDING / ACCEPTED / CANCELLED
    created_at TEXT,
    FOREIGN KEY(from_email) REFERENCES users(email),
    FOREIGN KEY(to_email) REFERENCES users(email)
);
""";

            stmt.execute(usersSql);
            stmt.execute(notesSql);
            stmt.execute(requestsSql);

            // If users table existed without balance column, try to add it.
            try {
                stmt.execute("ALTER TABLE users ADD COLUMN balance REAL DEFAULT 0");
            } catch (Exception ignored) {
                // column already exists or cannot be added; ignore
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection connect() throws Exception {
        return DriverManager.getConnection(URL);
    }
}