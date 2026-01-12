package org.example.theadnan.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:sqlite:users.db";

    static {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            // users table (email is primary key). Add balance/is_admin/blocked columns where possible.
            String usersSql = """
CREATE TABLE IF NOT EXISTS users (
    email TEXT PRIMARY KEY,
    name TEXT,
    age INTEGER,
    profession TEXT,
    hobby TEXT,
    password TEXT,
    balance REAL DEFAULT 0,
    is_admin INTEGER DEFAULT 0,
    blocked INTEGER DEFAULT 0
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

            // money_requests table (existing or created earlier)
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

            // reports table (new)
            String reportsSql = """
CREATE TABLE IF NOT EXISTS reports (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    reporter_email TEXT NOT NULL,
    target_email TEXT NOT NULL,
    message TEXT,
    status TEXT NOT NULL, -- PENDING / REVIEWED / ACTIONED
    created_at TEXT,
    FOREIGN KEY(reporter_email) REFERENCES users(email),
    FOREIGN KEY(target_email) REFERENCES users(email)
);
""";

            stmt.execute(usersSql);
            stmt.execute(notesSql);
            stmt.execute(requestsSql);
            stmt.execute(reportsSql);

            // For older DBs, try to add individual columns (ignored if they exist)
            try { stmt.execute("ALTER TABLE users ADD COLUMN balance REAL DEFAULT 0"); } catch (Exception ignored) {}
            try { stmt.execute("ALTER TABLE users ADD COLUMN is_admin INTEGER DEFAULT 0"); } catch (Exception ignored) {}
            try { stmt.execute("ALTER TABLE users ADD COLUMN blocked INTEGER DEFAULT 0"); } catch (Exception ignored) {}

            // Ensure admin account exists with the requested credentials and is_admin flag set.
            // If admin doesn't exist, INSERT OR IGNORE adds it; then UPDATE ensures password/is_admin are set.
            try {
                String adminEmail = "admin@example.app.com";
                String adminPassword = "AdminIsTheKing";
                // Insert with defaults only if not exists
                stmt.execute(String.format(
                        "INSERT OR IGNORE INTO users(email, name, age, profession, hobby, password, balance, is_admin, blocked) VALUES ('%s', 'Admin', 0, 'admin', 'admin', '%s', 0, 1, 0)",
                        adminEmail, adminPassword
                ));
                // Ensure admin flag and password are correct (sets password and admin flag even if row existed)
                stmt.execute(String.format(
                        "UPDATE users SET is_admin = 1, password = '%s' WHERE email = '%s'",
                        adminPassword, adminEmail
                ));
            } catch (Exception ignored) {
                // ignore any errors here (best-effort)
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection connect() throws Exception {
        return DriverManager.getConnection(URL);
    }
}