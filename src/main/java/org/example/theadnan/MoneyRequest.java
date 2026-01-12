package org.example.theadnan;

public class MoneyRequest {
    private final int id;
    private final String fromEmail;
    private final String toEmail;
    private final double amount;
    private final String status;
    private final String createdAt;

    public MoneyRequest(int id, String fromEmail, String toEmail, double amount, String status, String createdAt) {
        this.id = id;
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public String getFromEmail() { return fromEmail; }
    public String getToEmail() { return toEmail; }
    public double getAmount() { return amount; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return String.format("Request #%d from %s → %s : %.2f (%s)", id, fromEmail, toEmail, amount, status);
    }
}