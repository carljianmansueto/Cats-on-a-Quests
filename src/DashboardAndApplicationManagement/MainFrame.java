package DashboardAndApplicationManagement;

import BrowseAndPostFeatures.BrowseJobsPanel;
import BrowseAndPostFeatures.PostAJobPanel;
import DataAndModels.DataStore;
import DataAndModels.User;
import LoginAndRegistration.LoginScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainFrame extends JFrame {

    // Colors
    private static final Color MAROON = new Color(128, 0, 0);
    private static final Color LIGHT_BG = new Color(248, 245, 245);
    private static final Color GOLD = new Color(255, 215, 0);

    // User info passed from LoginFrame
    private String fullName;
    private String role;
    private String email;
    private String college;
    private String course;
    private String idNumber;

    // Panels
    private BrowseJobsPanel browseJobsPanel;
    private PostAJobPanel postAJobPanel;
    private JobPostedPanel jobPostedPanel;
    private MyApplicationPanel myApplicationPanel;

    // Tabbed pane
    private JTabbedPane tabbedPane;

    public MainFrame(String fullName, String role, String email,
                     String college, String course, String idNumber) {
        this.fullName = fullName;
        this.role = role;
        this.email = email;
        this.college = college;
        this.course = course;
        this.idNumber = idNumber;

        setTitle("Cats on a Quest - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setIconImage(createDummyIcon());

        initUI();
    }

    private void initUI() {
        // Get the current user from DataStore
        User currentUser = DataStore.findUserByEmail(email);
        if (currentUser == null) {
            currentUser = new User(fullName, "", email, role, college, course, idNumber);
        }

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Top header bar
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(MAROON);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel titleLabel = new JLabel("Welcome, " + fullName);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(GOLD);

        JLabel userInfoLabel = new JLabel(role + " | " + email);
        userInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        userInfoLabel.setForeground(new Color(220, 200, 200));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(Color.WHITE);
        logoutBtn.setForeground(MAROON);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> handleLogout());

        JPanel leftHeader = new JPanel();
        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.Y_AXIS));
        leftHeader.setOpaque(false);
        leftHeader.add(titleLabel);
        leftHeader.add(userInfoLabel);

        headerPanel.add(leftHeader, BorderLayout.WEST);
        headerPanel.add(logoutBtn, BorderLayout.EAST);

        // Tabbed pane for different sections
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Initialize all panels
        browseJobsPanel = new BrowseJobsPanel(currentUser);
        postAJobPanel = new PostAJobPanel(currentUser);
        jobPostedPanel = new JobPostedPanel(currentUser);
        myApplicationPanel = new MyApplicationPanel(currentUser);

        // Add tabs
        tabbedPane.addTab("Browse Jobs", browseJobsPanel);
        tabbedPane.addTab("Post a Job", postAJobPanel);
        tabbedPane.addTab("Job Posted", jobPostedPanel);
        tabbedPane.addTab("My Applications", myApplicationPanel);

        // Set tab colors and styling
        styleTabPane();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void styleTabPane() {
        // Style tabs
        UIManager.put("TabbedPane.selected", GOLD);
        UIManager.put("TabbedPane.foreground", MAROON);
        tabbedPane.setBackground(LIGHT_BG);
        tabbedPane.setForeground(MAROON);

        // Add visual separator
        tabbedPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 180, 180)));
    }

    private void handleLogout() {
        int response = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            DataStore.setCurrentUser(null);
            dispose();
            new LoginScreen().setVisible(true);
        }
    }

    private Image createDummyIcon() {
        BufferedImage icon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = icon.createGraphics();
        g2d.setColor(MAROON);
        g2d.fillRect(0, 0, 32, 32);
        g2d.setColor(GOLD);
        g2d.drawString("Co", 5, 20);
        g2d.dispose();
        return icon;
    }

    /**
     * Called by MyListingPanel's "Create New Listing" button.
     * Switches to the "Post a Job or Service" tab (index 1).
     * Tab order: 0=Browse, 1=Post a Job, 2=My Listings, 3=My Applications
     */
    public void goToPostTab() {
        tabbedPane.setSelectedIndex(1);
    }

    /**
     * Called by MyApplicationPanel's "Browse More Jobs" button.
     * Switches to the "Browse Jobs & Services" tab (index 0).
     */
    public void goToBrowseTab() {
        tabbedPane.setSelectedIndex(0);
    }

}