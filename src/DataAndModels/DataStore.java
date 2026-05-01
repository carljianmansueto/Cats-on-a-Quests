package DataAndModels;

import java.util.ArrayList;
import java.io.*;
import java.time.LocalDate;

/**
 * DataStore.java
 * Member 1 - Data & Models
 *
 * DataStore is the central data manager for the entire application.
 * It stores all users, listings, and applications in memory using ArrayLists,
 * and also saves/loads them from text files so data persists between runs.
 *
 * UPDATED: Removed username, using email as unique identifier.
 * UPDATED: Added 'course' field to User.
 */
public class DataStore {

    // -----------------------------------------------------------------------
    // File paths — all data is saved in a "data" folder next to your .class files
    // -----------------------------------------------------------------------
    private static final String DATA_DIR        = "data/";
    private static final String USERS_FILE      = DATA_DIR + "users.txt";
    private static final String LISTINGS_FILE   = DATA_DIR + "listings.txt";
    private static final String APPS_FILE       = DATA_DIR + "applications.txt";

    // -----------------------------------------------------------------------
    // In-memory storage using ArrayList
    // -----------------------------------------------------------------------
    private static ArrayList<User>        users        = new ArrayList<>();
    private static ArrayList<JobListing>  listings     = new ArrayList<>();
    private static ArrayList<Application> applications = new ArrayList<>();

    // Keeps track of who is currently logged in
    private static User currentUser = null;

    // Auto-incrementing ID counters
    private static int listingCounter = 1;
    private static int appCounter     = 1;

    // -----------------------------------------------------------------------
    // INITIALIZATION — call this once at app startup
    // -----------------------------------------------------------------------

    /**
     * Loads all saved data from files into memory.
     * If files don't exist yet, creates the data folder and default accounts.
     */
    public static void initialize() {
        createDataDirectory();
        loadUsers();
        loadListings();
        loadApplications();

        // Seed default data if no users exist
        if (users.isEmpty()) {
            seedDefaultData();
        }
    }

    private static void createDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // -----------------------------------------------------------------------
    // USER OPERATIONS
    // -----------------------------------------------------------------------

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    /**
     * Finds a user by their EMAIL (now the unique identifier).
     * Returns null if not found.
     */
    public static User findUserByEmail(String email) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Checks login credentials using EMAIL and password.
     * Returns the User if correct, null otherwise.
     */
    public static User login(String email, String password) {
        User u = findUserByEmail(email);
        if (u != null && u.getPassword().equals(password)) {
            currentUser = u;
            return u;
        }
        return null;
    }

    /**
     * Registers a new user after validation.
     * Returns an error message if invalid, or null if registration succeeded.
     */
    public static String register(User newUser) {
        // Validate: email must be MSU-IIT domain
        if (!newUser.getEmail().endsWith("@g.msuiit.edu.ph")
                && !newUser.getEmail().endsWith("@msuiit.edu.ph")) {
            return "Email must be a valid MSU-IIT email (@g.msuiit.edu.ph)";
        }
        // Validate: email must be unique (now using email instead of username)
        if (findUserByEmail(newUser.getEmail()) != null) {
            return "Email already registered. Please use another email.";
        }
        // Validate: password length
        if (newUser.getPassword().length() < 6) {
            return "Password must be at least 6 characters.";
        }
        users.add(newUser);
        saveUsers();
        return null; // null means success
    }

    public static void logout() {
        currentUser = null;
    }

    // -----------------------------------------------------------------------
    // LISTING OPERATIONS
    // -----------------------------------------------------------------------

    public static ArrayList<JobListing> getListings() {
        return listings;
    }

    /**
     * Returns only OPEN listings — what students see on the browse page.
     */
    public static ArrayList<JobListing> getOpenListings() {
        ArrayList<JobListing> open = new ArrayList<>();
        for (JobListing jl : listings) {
            if (jl.getStatus().equals("OPEN")) {
                open.add(jl);
            }
        }
        return open;
    }

    /**
     * Returns listings posted by a specific user (by email).
     */
    public static ArrayList<JobListing> getListingsByUser(String email) {
        ArrayList<JobListing> result = new ArrayList<>();
        for (JobListing jl : listings) {
            if (jl.getPostedBy().equalsIgnoreCase(email)) {
                result.add(jl);
            }
        }
        return result;
    }

    /**
     * Searches listings by keyword (checks title, description, category).
     */
    public static ArrayList<JobListing> searchListings(String keyword) {
        ArrayList<JobListing> result = new ArrayList<>();
        String lower = keyword.toLowerCase();
        for (JobListing jl : listings) {
            if (jl.getTitle().toLowerCase().contains(lower)
                    || jl.getDescription().toLowerCase().contains(lower)
                    || jl.getCategory().toLowerCase().contains(lower)) {
                result.add(jl);
            }
        }
        return result;
    }

    /**
     * Adds a new listing and generates a unique ID for it.
     */
    public static void addListing(JobListing jl) {
        listings.add(jl);
        listingCounter++;
        saveListings();
    }

    /**
     * Removes a listing by its ID.
     */
    public static boolean removeListing(String listingId) {
        for (int i = 0; i < listings.size(); i++) {
            if (listings.get(i).getListingId().equals(listingId)) {
                listings.remove(i);
                saveListings();
                return true;
            }
        }
        return false;
    }

    /**
     * Finds a listing by ID.
     */
    public static JobListing findListing(String listingId) {
        for (JobListing jl : listings) {
            if (jl.getListingId().equals(listingId)) {
                return jl;
            }
        }
        return null;
    }

    /**
     * Generates the next unique listing ID like "LST-007".
     */
    public static String generateListingId() {
        return String.format("LST-%03d", listingCounter);
    }

    // -----------------------------------------------------------------------
    // APPLICATION OPERATIONS
    // -----------------------------------------------------------------------

    public static ArrayList<Application> getApplications() {
        return applications;
    }

    /**
     * Returns all applications made by a specific applicant (by email).
     */
    public static ArrayList<Application> getApplicationsByUser(String email) {
        ArrayList<Application> result = new ArrayList<>();
        for (Application a : applications) {
            if (a.getApplicantEmail().equalsIgnoreCase(email)) {
                result.add(a);
            }
        }
        return result;
    }

    /**
     * Returns all applications for a specific listing.
     */
    public static ArrayList<Application> getApplicationsForListing(String listingId) {
        ArrayList<Application> result = new ArrayList<>();
        for (Application a : applications) {
            if (a.getListingId().equals(listingId)) {
                result.add(a);
            }
        }
        return result;
    }

    /**
     * Checks if a user has already applied to a specific listing.
     */
    public static boolean hasApplied(String email, String listingId) {
        for (Application a : applications) {
            if (a.getApplicantEmail().equalsIgnoreCase(email)
                    && a.getListingId().equals(listingId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Submits a new application.
     * Returns an error string if invalid, or null on success.
     */
    public static String applyToListing(String listingId, String message) {
        if (currentUser == null) return "You must be logged in to apply.";

        JobListing jl = findListing(listingId);
        if (jl == null) return "Listing not found.";
        if (!jl.getStatus().equals("OPEN")) return "This listing is no longer open.";
        if (hasApplied(currentUser.getEmail(), listingId))
            return "You have already applied to this listing.";
        if (jl.getPostedBy().equalsIgnoreCase(currentUser.getEmail()))
            return "You cannot apply to your own listing.";

        String appId = String.format("APP-%03d", appCounter++);
        String today = LocalDate.now().toString();
        Application app = new Application(appId, listingId,
                currentUser.getEmail(), today, message);
        applications.add(app);
        saveApplications();
        return null; // success
    }

    /**
     * Updates the status of an application (ACCEPTED / REJECTED).
     */
    public static void updateApplicationStatus(String appId, String status) {
        for (Application a : applications) {
            if (a.getApplicationId().equals(appId)) {
                a.setStatus(status);
                saveApplications();
                return;
            }
        }
    }

    // -----------------------------------------------------------------------
    // FILE I/O — Save & Load
    // -----------------------------------------------------------------------

    public static void saveAll() {
        saveUsers();
        saveListings();
        saveApplications();
    }

    // --- Users ---
    private static void saveUsers() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User u : users) {
                pw.println(u.toFileString());
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    private static void loadUsers() {
        users.clear();
        File f = new File(USERS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    User u = User.fromFileString(line);
                    if (u != null) users.add(u);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    // --- Listings ---
    private static void saveListings() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(LISTINGS_FILE))) {
            for (JobListing jl : listings) {
                pw.println(jl.toFileString());
            }
        } catch (IOException e) {
            System.err.println("Error saving listings: " + e.getMessage());
        }
    }

    private static void loadListings() {
        listings.clear();
        listingCounter = 1;
        File f = new File(LISTINGS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    JobListing jl = JobListing.fromFileString(line);
                    if (jl != null) {
                        listings.add(jl);
                        listingCounter++;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading listings: " + e.getMessage());
        }
    }

    // --- Applications ---
    private static void saveApplications() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(APPS_FILE))) {
            for (Application a : applications) {
                pw.println(a.toFileString());
            }
        } catch (IOException e) {
            System.err.println("Error saving applications: " + e.getMessage());
        }
    }

    private static void loadApplications() {
        applications.clear();
        appCounter = 1;
        File f = new File(APPS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Application a = Application.fromFileString(line);
                    if (a != null) {
                        applications.add(a);
                        appCounter++;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading applications: " + e.getMessage());
        }
    }

    // -----------------------------------------------------------------------
    // SEED DATA — pre-loads demo accounts and sample listings
    // -----------------------------------------------------------------------
    private static void seedDefaultData() {
        // Demo accounts (UPDATED: no username, added course)
        users.add(new User("Juan Dela Cruz", "password123",
                "juan.delacruz@g.msuiit.edu.ph", "STUDENT",
                "College of Engineering", "BS Computer Engineering", "2021-00001"));

        users.add(new User("Maria Santos", "password123",
                "maria.santos@g.msuiit.edu.ph", "STUDENT",
                "College of Science and Mathematics", "BS Biology", "2021-00002"));

        users.add(new User("Dr. Ana Reyes", "faculty123",
                "ana.reyes@msuiit.edu.ph", "FACULTY",
                "College of Engineering", "PhD in Engineering", "FAC-0001"));

        users.add(new User("CS Student Organization", "org12345",
                "csso@g.msuiit.edu.ph", "ORGANIZATION",
                "College of Computer Studies", "Computer Science", "ORG-001"));

        saveUsers();

        // Sample listings
        String today = LocalDate.now().toString();
        String deadline = LocalDate.now().plusDays(14).toString();

        listings.add(new JobListing(
                generateListingId(), "Math Tutor Needed",
                "Looking for a 2nd year or higher student who can tutor Calculus 1. " +
                        "Sessions are twice a week, 1 hour each. Must be patient and good at explaining concepts.",
                "juan.delacruz@g.msuiit.edu.ph", today, "Tutoring",
                75.0, "PER_HOUR", "Library Study Rooms", 2, deadline
        ));
        listingCounter++;

        listings.add(new JobListing(
                generateListingId(), "Graphic Designer for Org Tarpaulin",
                "We need someone to design a tarpaulin for our upcoming seminar. " +
                        "Must know Canva or Photoshop. Deliver in 3 days from acceptance.",
                "csso@g.msuiit.edu.ph", today, "Design",
                500.0, "FIXED", "Online / Remote", 1, deadline
        ));
        listingCounter++;

        listings.add(new JobListing(
                generateListingId(), "Research Assistant (Data Encoding)",
                "Faculty research project needs help encoding survey responses into Excel. " +
                        "Approximately 200 entries. Work from home, submit by deadline.",
                "ana.reyes@msuiit.edu.ph", today, "Research",
                800.0, "FIXED", "Remote", 1, deadline
        ));
        listingCounter++;

        listings.add(new JobListing(
                generateListingId(), "Campus Errand Runner",
                "Need someone to photocopy and submit documents to the Registrar and OVCAA. " +
                        "Quick task, can be done within 2 hours.",
                "maria.santos@g.msuiit.edu.ph", today, "Errand",
                150.0, "FIXED", "Admin Building Area", 1, deadline
        ));
        listingCounter++;

        saveListings();
    }
}