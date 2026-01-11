package org.example.theadnan.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NotesService {

    public static List<String> getNotes(String email) throws Exception {
        List<String> titles = new ArrayList<>();
        String sql = "SELECT title FROM notes WHERE user_email = ?";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    titles.add(rs.getString("title"));
                }
            }
        }
        return titles;
    }

    // renamed to getNoteContent to return a plain value and close DB resources
    public static Optional<String> getNoteContent(String email, String title) throws Exception {
        String sql = "SELECT content FROM notes WHERE user_email = ? AND title = ?";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, title);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.ofNullable(rs.getString("content"));
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    // Updated: try UPDATE first, then INSERT if update didn't affect any row.
    public static void saveNote(String email, String title, String content) throws Exception {
        String updateSql = "UPDATE notes SET content = ? WHERE user_email = ? AND title = ?";
        String insertSql = "INSERT INTO notes (user_email, title, content) VALUES (?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement updatePs = conn.prepareStatement(updateSql)) {

            updatePs.setString(1, content);
            updatePs.setString(2, email);
            updatePs.setString(3, title);

            int updated = updatePs.executeUpdate();

            if (updated == 0) {
                try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                    insertPs.setString(1, email);
                    insertPs.setString(2, title);
                    insertPs.setString(3, content);
                    insertPs.executeUpdate();
                }
            }
        }
    }

    public static void deleteNote(String email, String title) throws Exception {
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM notes WHERE user_email = ? AND title = ?"
             )) {
            ps.setString(1, email);
            ps.setString(2, title);
            ps.executeUpdate();
        }
    }
}