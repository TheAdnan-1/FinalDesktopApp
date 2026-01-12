package org.example.theadnan.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

import org.example.theadnan.User;

public class AuthService {

    // ---------- REGISTER ----------
    public static boolean register(String email, String name, int age,
                                   String profession, String hobby, String password) {

        String sql = """
            INSERT INTO users(email, name, age, profession, hobby, password)
            VALUES(?,?,?,?,?,?)
            """;

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, name);
            ps.setInt(3, age);
            ps.setString(4, profession);
            ps.setString(5, hobby);
            ps.setString(6, password);

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            return false; // duplicate email or error
        }
    }

    // ---------- LOGIN STATUS ----------
    // returns "OK", "INVALID", or "BLOCKED"
    public static String loginStatus(String email, String password) {
        String sql = "SELECT password, blocked FROM users WHERE email = ?";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return "INVALID";
                int blocked = 0;
                try { blocked = rs.getInt("blocked"); } catch (Exception ignored) {}
                if (blocked == 1) return "BLOCKED";
                String actual = rs.getString("password");
                if (actual != null && actual.equals(password)) return "OK";
                return "INVALID";
            }

        } catch (Exception e) {
            return "INVALID";
        }
    }

    // keep old login for compatibility (simple wrapper)
    public static boolean login(String email, String password) {
        return "OK".equals(loginStatus(email, password));
    }

    // ---------- GET USER ----------
    public static Optional<User> getUser(String email) throws Exception {

        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double balance = 0.0;
                    boolean isAdmin = false;
                    boolean blocked = false;
                    try {
                        balance = rs.getDouble("balance");
                        if (rs.wasNull()) balance = 0.0;
                    } catch (Exception ignored) {}
                    try { isAdmin = rs.getInt("is_admin") == 1; } catch (Exception ignored) {}
                    try { blocked = rs.getInt("blocked") == 1; } catch (Exception ignored) {}

                    User user = new User(
                            rs.getString("email"),
                            rs.getString("name"),
                            rs.getInt("age"),
                            rs.getString("profession"),
                            rs.getString("hobby"),
                            balance,
                            isAdmin,
                            blocked
                    );
                    return Optional.of(user);
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    /**
     * Try to update user's balance after verifying email/password.
     * delta may be positive (add) or negative (deduct).
     * Returns null on success, or an error message on failure.
     */
    public static String updateBalanceWithAuth(String email, String password, double delta) {
        String selectSql = "SELECT balance, password FROM users WHERE email = ?";
        String updateSql = "UPDATE users SET balance = ? WHERE email = ?";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(selectSql)) {

            conn.setAutoCommit(false);

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    conn.rollback();
                    return "User not found";
                }
                String actualPassword = rs.getString("password");
                if (actualPassword == null || !actualPassword.equals(password)) {
                    conn.rollback();
                    return "Invalid password";
                }
                double current = 0.0;
                try {
                    current = rs.getDouble("balance");
                    if (rs.wasNull()) current = 0.0;
                } catch (Exception ignored) {}

                double updated = current + delta;
                if (updated < 0) {
                    conn.rollback();
                    return "Insufficient funds (balance cannot be negative)";
                }

                try (PreparedStatement ups = conn.prepareStatement(updateSql)) {
                    ups.setDouble(1, updated);
                    ups.setString(2, email);
                    ups.executeUpdate();
                }

                conn.commit();
                return null; // success
            } catch (Exception e) {
                try { conn.rollback(); } catch (Exception ignored) {}
                return "Error updating balance";
            }
        } catch (Exception e) {
            return "Database error";
        }
    }
}