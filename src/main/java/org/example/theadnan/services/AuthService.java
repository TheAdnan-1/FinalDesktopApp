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

    // ---------- LOGIN ----------
    public static boolean login(String email, String password) {

        String sql = """
            SELECT * FROM users WHERE email = ? AND password = ?
            """;

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            return false;
        }
    }

    // ---------- GET USER ----------
    // Read the user while the connection is open and return a plain object.
    public static Optional<User> getUser(String email) throws Exception {

        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getString("email"),
                            rs.getString("name"),
                            rs.getInt("age"),
                            rs.getString("profession"),
                            rs.getString("hobby")
                    );
                    return Optional.of(user);
                } else {
                    return Optional.empty();
                }
            }
        }
    }
}