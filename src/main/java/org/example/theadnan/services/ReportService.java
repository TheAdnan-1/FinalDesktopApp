package org.example.theadnan.services;

import org.example.theadnan.Report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReportService {

    public static void createReport(String reporterEmail, String targetEmail, String message) throws Exception {
        String sql = "INSERT INTO reports (reporter_email, target_email, message, status, created_at) VALUES (?, ?, ?, ?, datetime('now'))";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reporterEmail);
            ps.setString(2, targetEmail);
            ps.setString(3, message);
            ps.setString(4, "PENDING");
            ps.executeUpdate();
        }
    }

    public static List<Report> getAllReports() throws Exception {
        String sql = "SELECT id, reporter_email, target_email, message, status, created_at FROM reports ORDER BY created_at DESC";
        List<Report> list = new ArrayList<>();
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Report(
                        rs.getInt("id"),
                        rs.getString("reporter_email"),
                        rs.getString("target_email"),
                        rs.getString("message"),
                        rs.getString("status"),
                        rs.getString("created_at")
                ));
            }
        }
        return list;
    }

    public static List<Report> getReportsForUser(String email) throws Exception {
        String sql = "SELECT id, reporter_email, target_email, message, status, created_at FROM reports WHERE reporter_email = ? OR target_email = ? ORDER BY created_at DESC";
        List<Report> list = new ArrayList<>();
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, email);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Report(
                            rs.getInt("id"),
                            rs.getString("reporter_email"),
                            rs.getString("target_email"),
                            rs.getString("message"),
                            rs.getString("status"),
                            rs.getString("created_at")
                    ));
                }
            }
        }
        return list;
    }

    public static String reviewReport(int reportId, String adminEmail, boolean actionBlock) {
        String select = "SELECT target_email, status FROM reports WHERE id = ?";
        String updateReport = "UPDATE reports SET status = ? WHERE id = ?";
        String blockUserSql = "UPDATE users SET blocked = 1 WHERE email = ?";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(select)) {

            conn.setAutoCommit(false);

            ps.setInt(1, reportId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    conn.rollback();
                    return "Report not found";
                }
                String status = rs.getString("status");
                String target = rs.getString("target_email");
                if (!"PENDING".equals(status)) {
                    conn.rollback();
                    return "Report is already processed";
                }

                if (actionBlock) {
                    try (PreparedStatement block = conn.prepareStatement(blockUserSql)) {
                        block.setString(1, target);
                        block.executeUpdate();
                    }
                    try (PreparedStatement ups = conn.prepareStatement(updateReport)) {
                        ups.setString(1, "ACTIONED");
                        ups.setInt(2, reportId);
                        ups.executeUpdate();
                    }
                } else {
                    try (PreparedStatement ups = conn.prepareStatement(updateReport)) {
                        ups.setString(1, "REVIEWED");
                        ups.setInt(2, reportId);
                        ups.executeUpdate();
                    }
                }

                conn.commit();
                return null;
            } catch (Exception e) {
                try { conn.rollback(); } catch (Exception ignored) {}
                return "Error processing report";
            }
        } catch (Exception e) {
            return "Database error";
        }
    }
}