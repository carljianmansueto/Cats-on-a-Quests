package DataAndModels;

/**
 * Application.java
 * Member 1 - Data & Models
 *
 * Represents a student's application to a job listing.
 */
public class Application {
    private String applicationId;   // e.g., "APP-001"
    private String listingId;       // Which listing they applied to
    private String applicantEmail;  // Who applied (email instead of username)
    private String dateApplied;     // When they applied
    private String message;         // Cover letter / message
    private String status;          // "PENDING", "ACCEPTED", "REJECTED"

    public Application(String applicationId, String listingId, String applicantEmail,
                       String dateApplied, String message) {
        this.applicationId = applicationId;
        this.listingId = listingId;
        this.applicantEmail = applicantEmail;
        this.dateApplied = dateApplied;
        this.message = message;
        this.status = "PENDING";
    }

    // Getters
    public String getApplicationId() { return applicationId; }
    public String getListingId() { return listingId; }
    public String getApplicantEmail() { return applicantEmail; }
    public String getDateApplied() { return dateApplied; }
    public String getMessage() { return message; }
    public String getStatus() { return status; }

    // Setter
    public void setStatus(String status) { this.status = status; }

    public String toFileString() {
        return applicationId + "|" + listingId + "|" + applicantEmail + "|"
                + dateApplied + "|" + message + "|" + status;
    }

    public static Application fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 6) return null;
        Application app = new Application(parts[0], parts[1], parts[2], parts[3], parts[4]);
        app.setStatus(parts[5]);
        return app;
    }
}