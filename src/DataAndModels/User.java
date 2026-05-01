package DataAndModels;

public class User {

    private String fullName;
    private String password;
    private String email;
    private String role;
    private String college;
    private String course;
    private String idNumber;

    // Constructor
    public User(String fullName, String password, String email,
                String role, String college, String course, String idNumber) {
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.role = role;
        this.college = college;
        this.course = course;
        this.idNumber = idNumber;
    }

    // Getters
    public String getFullName() { return fullName; }
    public String getPassword() { return password; }
    public String getEmail()    { return email; }
    public String getRole()     { return role; }
    public String getCollege()  { return college; }
    public String getCourse()   { return course; }
    public String getIdNumber() { return idNumber; }

    // Setters
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email)       { this.email = email; }
    public void setRole(String role)         { this.role = role; }
    public void setCollege(String college)   { this.college = college; }
    public void setCourse(String course)     { this.course = course; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }

    /**
     * Converts User to file string format
     * Format: fullName|password|email|role|college|course|idNumber
     */
    public String toFileString() {
        return fullName + "|" + password + "|" + email + "|" + role + "|"
                + college + "|" + course + "|" + idNumber;
    }

    /**
     * Rebuilds User from file line
     * FIXED: Split by | not \n
     */
    public static User fromFileString(String line) {
        String[] parts = line.split("\\|");  // ← FIXED: pipe separator, not newline
        if (parts.length < 7) return null;
        return new User(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
    }

    @Override
    public String toString() {
        return fullName + " (" + role + ") - " + email;
    }
}