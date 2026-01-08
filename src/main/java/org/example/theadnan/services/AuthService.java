package org.example.theadnan.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            return false;
        }
    }

    // ---------- GET USER ----------
    public static ResultSet getUser(String email) throws Exception {

        String sql = "SELECT * FROM users WHERE email = ?";

        Connection conn = Database.connect();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);

        return ps.executeQuery();
    }
}
