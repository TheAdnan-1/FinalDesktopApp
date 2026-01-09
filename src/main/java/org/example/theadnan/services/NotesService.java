package org.example.theadnan.services;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotesService {

    // ---------- CREATE ----------
    public static void addNote(String email, String title, String content) throws Exception {
        String sql = """
            INSERT INTO notes(user_email, title, content, created_at)
            VALUES(?,?,?,?)
            """;

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, title);
            ps.setString(3, content);
            ps.setString(4, LocalDateTime.now().toString());
            ps.executeUpdate();
        }
    }

    // ---------- READ ----------
    public static List<String> getNotesTitles(String email) throws Exception {
        List<String> notes = new ArrayList<>();

        String sql = "SELECT id, title FROM notes WHERE user_email = ?";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                notes.add(rs.getInt("id") + " - " + rs.getString("title"));
            }
        }
        return notes;
    }

    // ---------- READ ONE ----------
    public static ResultSet getNoteById(int id) throws Exception {
        Connection conn = Database.connect();
        PreparedStatement ps =
                conn.prepareStatement("SELECT * FROM notes WHERE id = ?");
        ps.setInt(1, id);
        return ps.executeQuery();
    }

    // ---------- UPDATE ----------
    public static void updateNote(int id, String title, String content) throws Exception {
        String sql = "UPDATE notes SET title = ?, content = ? WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, content);
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }

    // ---------- DELETE ----------
    public static void deleteNote(int id) throws Exception {
        String sql = "DELETE FROM notes WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
