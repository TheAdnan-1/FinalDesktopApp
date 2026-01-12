package org.example.theadnan.services;

import org.example.theadnan.MoneyRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MoneyRequestService {

    public static void createRequest(String fromEmail, String toEmail, double amount) throws Exception {
        String sql = "INSERT INTO money_requests (from_email, to_email, amount, status, created_at) VALUES (?, ?, ?, ?, datetime('now'))";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fromEmail);
            ps.setString(2, toEmail);
            ps.setDouble(3, amount);
            ps.setString(4, "PENDING");
            ps.executeUpdate();
        }
    }

    public static List<MoneyRequest> getRequestsForUser(String email) throws Exception {
        String sql = "SELECT id, from_email, to_email, amount, status, created_at FROM money_requests WHERE from_email = ? OR to_email = ? ORDER BY created_at DESC";
        List<MoneyRequest> list = new ArrayList<>();
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, email);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new MoneyRequest(
                            rs.getInt("id"),
                            rs.getString("from_email"),
                            rs.getString("to_email"),
                            rs.getDouble("amount"),
                            rs.getString("status"),
                            rs.getString("created_at")
                    ));
                }
            }
        }
        return list;
    }

    /**
     * Recipient accepts a pending request. Perform transfer in a transaction.
     * Returns null on success, or error message on failure.
     */
    public static String acceptRequest(int requestId, String recipientEmail) {
        String selectReq = "SELECT from_email, to_email, amount, status FROM money_requests WHERE id = ?";
        String updateStatus = "UPDATE money_requests SET status = ? WHERE id = ?";
        String debitSql = "UPDATE users SET balance = balance - ? WHERE email = ? AND balance >= ?";
        String creditSql = "UPDATE users SET balance = balance + ? WHERE email = ?";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(selectReq)) {

            conn.setAutoCommit(false);

            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    conn.rollback();
                    return "Request not found";
                }
                String from = rs.getString("from_email");
                String to = rs.getString("to_email");
                double amount = rs.getDouble("amount");
                String status = rs.getString("status");
                if (!"PENDING".equals(status)) {
                    conn.rollback();
                    return "Request is not pending";
                }
                if (!recipientEmail.equals(to)) {
                    conn.rollback();
                    return "Only recipient can accept this request";
                }

                // Debit recipient (to)
                try (PreparedStatement debit = conn.prepareStatement(debitSql)) {
                    debit.setDouble(1, amount);
                    debit.setString(2, to);
                    debit.setDouble(3, amount);
                    int updated = debit.executeUpdate();
                    if (updated == 0) {
                        conn.rollback();
                        return "Insufficient funds";
                    }
                }

                // Credit requester (from)
                try (PreparedStatement credit = conn.prepareStatement(creditSql)) {
                    credit.setDouble(1, amount);
                    credit.setString(2, from);
                    credit.executeUpdate();
                }

                // Mark accepted
                try (PreparedStatement ups = conn.prepareStatement(updateStatus)) {
                    ups.setString(1, "ACCEPTED");
                    ups.setInt(2, requestId);
                    ups.executeUpdate();
                }

                conn.commit();
                return null; // success
            } catch (Exception e) {
                try { conn.rollback(); } catch (Exception ignored) {}
                return "Error processing request";
            }
        } catch (Exception e) {
            return "Database error";
        }
    }

    /**
     * Cancel request by either from or to (if pending). Returns null on success else error message.
     */
    public static String cancelRequest(int requestId, String actorEmail) {
        String selectReq = "SELECT from_email, to_email, status FROM money_requests WHERE id = ?";
        String updateStatus = "UPDATE money_requests SET status = ? WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(selectReq)) {

            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return "Request not found";
                String from = rs.getString("from_email");
                String to = rs.getString("to_email");
                String status = rs.getString("status");
                if (!"PENDING".equals(status)) return "Request is not pending";
                if (!actorEmail.equals(from) && !actorEmail.equals(to)) return "Not authorized to cancel";

                try (PreparedStatement ups = conn.prepareStatement(updateStatus)) {
                    ups.setString(1, "CANCELLED");
                    ups.setInt(2, requestId);
                    ups.executeUpdate();
                }
                return null;
            }
        } catch (Exception e) {
            return "Database error";
        }
    }
}