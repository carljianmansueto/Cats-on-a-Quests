/**
 * JobListing.java
 * Member 1 - Data & Models
 *
 * JobListing EXTENDS Listing — meaning it INHERITS all fields and methods
 * from Listing, and adds its own extra fields (pay, location, slots).
 *
 * Key OOP concepts shown here:
 *  - Inheritance: extends Listing
 *  - super() — calls the parent constructor
 *  - @Override — replaces the parent's getSummary() with a better one
 */
public class JobListing extends Listing {

    // Extra fields specific to job listings
    private double payRate;         // Pay per hour or flat rate (in PHP)
    private String payType;         // "PER_HOUR" or "FIXED"
    private String location;        // Where the job takes place on campus
    private int slotsAvailable;     // How many students can apply
    private String deadline;        // Application deadline

    // --- Constructor ---
    // We call super(...) to pass the common fields to the parent Listing class
    public JobListing(String listingId, String title, String description,
                      String postedBy, String datePosted, String category,
                      double payRate, String payType, String location,
                      int slotsAvailable, String deadline) {
        // This calls Listing's constructor with the first 6 arguments
        super(listingId, title, description, postedBy, datePosted, category);

        // Then we set our own extra fields
        this.payRate        = payRate;
        this.payType        = payType;
        this.location       = location;
        this.slotsAvailable = slotsAvailable;
        this.deadline       = deadline;
    }

    // --- Getters ---
    public double getPayRate()       { return payRate; }
    public String getPayType()       { return payType; }
    public String getLocation()      { return location; }
    public int    getSlotsAvailable(){ return slotsAvailable; }
    public String getDeadline()      { return deadline; }

    // --- Setters ---
    public void setSlotsAvailable(int slots) { this.slotsAvailable = slots; }
    public void setPayRate(double rate)      { this.payRate = rate; }

    /**
     * @Override — replaces the parent class's getSummary() with a better version.
     * This is POLYMORPHISM: same method name, different behavior for JobListing.
     */
    @Override
    public String getSummary() {
        String pay = (payType.equals("PER_HOUR"))
                ? "₱" + payRate + "/hr"
                : "₱" + payRate + " (fixed)";
        return "[" + getCategory() + "] " + getTitle()
                + " | " + pay
                + " | " + slotsAvailable + " slot(s)"
                + " | Deadline: " + deadline
                + " | " + getStatus();
    }

    /**
     * Saves this JobListing to a file.
     * We call super.toFileString() to get the base fields, then append our extra ones.
     * Type tag "JOB" helps us identify this as a JobListing when reading the file back.
     */
    @Override
    public String toFileString() {
        return "JOB|" + super.toFileString()
                + "|" + payRate + "|" + payType + "|" + location
                + "|" + slotsAvailable + "|" + deadline;
    }

    /**
     * Rebuilds a JobListing from a saved file line.
     * Static method — call as: JobListing.fromFileString(line)
     */
    public static JobListing fromFileString(String line) {
        // Remove the "JOB|" prefix first
        String data = line.startsWith("JOB|") ? line.substring(4) : line;
        String[] p = data.split("\\|");
        if (p.length < 12) return null;

        JobListing jl = new JobListing(
            p[0], p[1], p[2], p[3], p[4], p[6],     // base Listing fields
            Double.parseDouble(p[7]), p[8], p[9],   // payRate, payType, location
            Integer.parseInt(p[10]), p[11]          // slots, deadline
        );
        jl.setStatus(p[5]); // restore status (OPEN / CLOSED / IN_PROGRESS)
        return jl;
    }
}
