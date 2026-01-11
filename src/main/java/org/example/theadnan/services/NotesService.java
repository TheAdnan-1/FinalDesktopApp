package org.example.theadnan.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NotesService {

    public static ResultSet getNotes(String email) throws Exception {
        Connection conn = Database.connect();
        PreparedStatement ps = conn.prepareStatement(
                "SELECT title FROM notes WHERE user_email = ?"
        );
        ps.setString(1, email);
        return ps.executeQuery();
    }

    public static ResultSet getNote(String email, String title) throws Exception {
        Connection conn = Database.connect();
        PreparedStatement ps = conn.prepareStatement(
                "SELECT content FROM notes WHERE user_email = ? AND title = ?"
        );
        ps.setString(1, email);
        ps.setString(2, title);
        return ps.executeQuery();
    }

    public static void saveNote(String email, String title, String content) throws Exception {
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT OR REPLACE INTO notes (user_email, title, content) VALUES (?, ?, ?)"
             )) {
            ps.setString(1, email);
            ps.setString(2, title);
            ps.setString(3, content);
            ps.executeUpdate();
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
