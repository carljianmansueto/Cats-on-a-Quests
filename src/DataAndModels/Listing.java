package DataAndModels;

/**
 * Listing.java
 * Member 1 - Data & Models
 *
 * This is the BASE (parent) class for all types of listings in the system.
 * Both JobListing will EXTEND (inherit from) this class.
 *
 * Key OOP concepts shown here:
 *  - Encapsulation: private fields with getters/setters
 *  - Inheritance base: other classes will extend this
 *  - Polymorphism: getSummary() can be overridden by subclasses
 */
public class Listing {

    // --- Private fields ---
    private String listingId;       // Unique ID like "LST-001"
    private String title;           // Short title of the listing
    private String description;     // Full details
    private String postedBy;        // Username of who posted this
    private String datePosted;      // Date string e.g. "2025-01-15"
    private String status;          // "OPEN", "CLOSED", or "IN_PROGRESS"
    private String category;        // e.g., "Tutoring", "Delivery", "Design"

    // --- Constructor ---
    public Listing(String listingId, String title, String description,
                   String postedBy, String datePosted, String category) {
        this.listingId   = listingId;
        this.title       = title;
        this.description = description;
        this.postedBy    = postedBy;
        this.datePosted  = datePosted;
        this.category    = category;
        this.status      = "OPEN"; // All new listings start as OPEN
    }

    // --- Getters ---
    public String getListingId()   { return listingId; }
    public String getTitle()       { return title; }
    public String getDescription() { return description; }
    public String getPostedBy()    { return postedBy; }
    public String getDatePosted()  { return datePosted; }
    public String getStatus()      { return status; }
    public String getCategory()    { return category; }

    // --- Setters ---
    public void setStatus(String status)           { this.status = status; }
    public void setDescription(String description) { this.description = description; }
    public void setTitle(String title)             { this.title = title; }

    /**
     * getSummary() — can be overridden by subclasses using @Override.
     * This is the POLYMORPHISM concept: each listing type can describe itself differently.
     */
    public String getSummary() {
        return "[" + category + "] " + title + " — Posted by: " + postedBy + " (" + status + ")";
    }

    /**
     * Converts listing to a file-saveable string.
     * Subclasses should call super.toFileString() and add their own fields.
     */
    public String toFileString() {
        return listingId + "|" + title + "|" + description + "|"
                + postedBy + "|" + datePosted + "|" + status + "|" + category;
    }

    @Override
    public String toString() {
        return getSummary();
    }
}