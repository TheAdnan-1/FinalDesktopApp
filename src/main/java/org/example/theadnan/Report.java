package org.example.theadnan;

public class Report {
    private final int id;
    private final String reporterEmail;
    private final String targetEmail;
    private final String message;
    private final String status;
    private final String createdAt;

    public Report(int id, String reporterEmail, String targetEmail, String message, String status, String createdAt) {
        this.id = id;
        this.reporterEmail = reporterEmail;
        this.targetEmail = targetEmail;
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public String getReporterEmail() { return reporterEmail; }
    public String getTargetEmail() { return targetEmail; }
    public String getMessage() { return message; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return String.format("Report #%d: %s → %s : %s (%s)", id, reporterEmail, targetEmail, status, createdAt);
    }
}