package DashboardAndApplicationManagement;

import DataAndModels.DataStore;
import DataAndModels.Application;
import DataAndModels.JobListing;
import DataAndModels.User;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MyApplicationPanel extends JPanel {

    // Colors — match your theme
    private static final Color MAROON = new Color(128, 0, 0);
    private static final Color LIGHT_BG = new Color(248, 245, 245);
    private static final Color CARD_WHITE = Color.WHITE;
    private static final Color GOLD = new Color(255, 215, 0);

    // Current user
    private final User currentUser;

    // UI components
    private JPanel applicationsPanel;
    private JLabel noApplicationsLabel;

    public MyApplicationPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(LIGHT_BG);
        buildUI();
        loadUserApplications();
    }

    private void buildUI() {
        // Top bar with title
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(MAROON);
        topBar.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel titleLabel = new JLabel("My Applications");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBackground(Color.WHITE);
        refreshBtn.setForeground(MAROON);
        refreshBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> loadUserApplications());

        topBar.add(titleLabel, BorderLayout.WEST);
        topBar.add(refreshBtn, BorderLayout.EAST);

        // Main applications area
        applicationsPanel = new JPanel();
        applicationsPanel.setLayout(new BoxLayout(applicationsPanel, BoxLayout.Y_AXIS));
        applicationsPanel.setBackground(LIGHT_BG);
        applicationsPanel.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JScrollPane scrollPane = new JScrollPane(applicationsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(LIGHT_BG);

        // No applications message
        noApplicationsLabel = new JLabel("You haven't applied to any listings yet.");
        noApplicationsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        noApplicationsLabel.setHorizontalAlignment(JLabel.CENTER);

        add(topBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadUserApplications() {
        applicationsPanel.removeAll();

        // Get all applications and filter by current user
        ArrayList<Application> allApplications = DataStore.getApplications();
        ArrayList<Application> userApplications = new ArrayList<>();

        for (Application app : allApplications) {
            if (app.getApplicantEmail().equals(currentUser.getEmail())) {
                userApplications.add(app);
            }
        }

        if (userApplications.isEmpty()) {
            applicationsPanel.add(noApplicationsLabel);
        } else {
            // Create a card for each application
            for (Application app : userApplications) {
                JPanel card = createApplicationCard(app);
                applicationsPanel.add(card);
                applicationsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        applicationsPanel.revalidate();
        applicationsPanel.repaint();
    }

    private JPanel createApplicationCard(Application app) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(CARD_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 180, 180)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Find the listing title using DataStore.findListing()
        String listingTitle = "Unknown Listing";
        JobListing listing = DataStore.findListing(app.getListingId());
        if (listing != null) {
            listingTitle = listing.getTitle();
        }

        // Title and status
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        // FIXED: Use DataStore.findListing() to get the title
        JLabel titleLabel = new JLabel(listingTitle);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(MAROON);

        // Status badge
        JLabel statusLabel = new JLabel("  " + app.getStatus() + "  ");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        statusLabel.setOpaque(true);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));

        // Color code status
        if (app.getStatus().equals("PENDING")) {
            statusLabel.setBackground(new Color(255, 193, 7)); // Yellow
            statusLabel.setForeground(Color.BLACK);
        } else if (app.getStatus().equals("ACCEPTED")) {
            statusLabel.setBackground(new Color(40, 167, 69)); // Green
            statusLabel.setForeground(Color.WHITE);
        } else if (app.getStatus().equals("REJECTED")) {
            statusLabel.setBackground(new Color(220, 53, 69)); // Red
            statusLabel.setForeground(Color.WHITE);
        }

        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(statusLabel, BorderLayout.EAST);

        // Details panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);

        // FIXED: Use correct method name getDateApplied()
        JLabel dateLabel = new JLabel("Applied on: " + app.getDateApplied());
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        // Show message preview
        String messagePreview = app.getMessage();
        if (messagePreview.length() > 80) {
            messagePreview = messagePreview.substring(0, 80) + "...";
        }
        JLabel messageLabel = new JLabel("Message: " + messagePreview);
        messageLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        messageLabel.setForeground(Color.GRAY);

        detailsPanel.add(dateLabel);
        detailsPanel.add(messageLabel);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setOpaque(false);

        JButton viewBtn = new JButton("View Details");
        viewBtn.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        viewBtn.setBackground(GOLD);
        viewBtn.setFocusPainted(false);

        JButton withdrawBtn = new JButton("Withdraw");
        withdrawBtn.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        withdrawBtn.setBackground(new Color(255, 200, 200));
        withdrawBtn.setFocusPainted(false);

        // Only show withdraw button if status is PENDING
        if (!app.getStatus().equals("PENDING")) {
            withdrawBtn.setEnabled(false);
            withdrawBtn.setText("Cannot Withdraw");
        }

        buttonPanel.add(viewBtn);
        buttonPanel.add(withdrawBtn);

        card.add(titlePanel, BorderLayout.NORTH);
        card.add(detailsPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        viewBtn.addActionListener(e -> showApplicationDetails(app, listing));
        withdrawBtn.addActionListener(e -> withdrawApplication(app));

        return card;
    }

    private void showApplicationDetails(Application app, JobListing listing) {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Application Details", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel((listing != null ? listing.getTitle() : "Unknown Listing"));
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(MAROON);

        // Details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        detailsPanel.add(new JLabel("Status: " + app.getStatus()));
        detailsPanel.add(new JLabel("Applied on: " + app.getDateApplied()));
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        detailsPanel.add(new JLabel("Your Message:"));
        JTextArea messageArea = new JTextArea(app.getMessage());
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setPreferredSize(new Dimension(450, 150));

        detailsPanel.add(scrollPane);

        if (listing != null) {
            detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            detailsPanel.add(new JLabel("Listing Details:"));
            detailsPanel.add(new JLabel("Category: " + listing.getCategory()));
            detailsPanel.add(new JLabel("Location: " + listing.getLocation()));
            detailsPanel.add(new JLabel("Pay: P" + listing.getPayRate() + " " + listing.getPayType().toLowerCase()));
        }

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(detailsPanel, BorderLayout.CENTER);
        panel.add(closeBtn, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void withdrawApplication(Application app) {
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to withdraw this application?\nThis action cannot be undone.",
                "Withdraw Application",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            // Remove the application from DataStore
            DataStore.getApplications().remove(app);

            // Save to file
            DataStore.saveApplications();

            JOptionPane.showMessageDialog(this,
                    "Application withdrawn successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Refresh the panel
            loadUserApplications();
        }
    }

    // Call this to refresh the applications when they change
    public void refreshApplications() {
        loadUserApplications();
    }
}