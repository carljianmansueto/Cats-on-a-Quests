package DataAndModels;

/**
 * User.java
 * Member 1 - Data & Models
 * This class represents a registered user of the Cats on a Quest system.
 * It demonstrates ENCAPSULATION — all fields are private, and we use
 * getters/setters to safely access or change them.
 */
public class User {

    // --- Private fields (encapsulation: hidden from outside) ---
    private String username;     // e.g., "juan.delacruz"
    private String password;     // stored as plain text (simple version)
    private String fullName;     // e.g., "Juan Dela Cruz"
    private String email;        // must end with @g.msuiit.edu.ph
    private String role;         // "STUDENT", "FACULTY", or "ORGANIZATION"
    private String college;      // e.g., "College of Engineering"
    private String idNumber;     // MSU-IIT ID number

    // --- Constructor ---
    // Called when you do: new User("juan", "pass123", ...)
    public User(String username, String password, String fullName,
                String email, String role, String college, String idNumber) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.college = college;
        this.idNumber = idNumber;
    }

    // --- Getters (read the private fields) ---
    public String getUsername()  { return username; }
    public String getPassword()  { return password; }
    public String getFullName()  { return fullName; }
    public String getEmail()     { return email; }
    public String getRole()      { return role; }
    public String getCollege()   { return college; }
    public String getIdNumber()  { return idNumber; }

    // --- Setters (change the private fields safely) ---
    public void setPassword(String password) { this.password = password; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setCollege(String college)   { this.college = college; }

    /**
     * Converts this User object into a single line of text for saving to a file.
     * Format: username|password|fullName|email|role|college|idNumber
     */
    public String toFileString() {
        return username + "|" + password + "|" + fullName + "|"
                + email + "|" + role + "|" + college + "|" + idNumber;
    }

    /**
     * Rebuilds a User object from a saved file line.
     * This is a STATIC method — you call it as User.fromFileString(line)
     * without needing an existing User object.
     */
    public static User fromFileString(String line) {
        String[] parts = line.split("\\|");
        // Make sure the line has all 7 fields before parsing
        if (parts.length < 7) return null;
        return new User(parts[0], parts[1], parts[2],
                parts[3], parts[4], parts[5], parts[6]);
    }

    /**
     * Returns a readable summary of this user (useful for debugging).
     */
    @Override
    public String toString() {
        return fullName + " (" + role + ") — " + email;
    }
}