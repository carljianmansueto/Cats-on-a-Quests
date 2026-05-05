package DashboardAndApplicationManagement;

import DataAndModels.DataStore;
import DataAndModels.JobListing;
import DataAndModels.Application;
import DataAndModels.User;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class MyListingPanel extends JPanel {

    // Colors — match your theme
    private static final Color MAROON = new Color(128, 0, 0);
    private static final Color LIGHT_BG = new Color(248, 245, 245);
    private static final Color CARD_WHITE = Color.WHITE;
    private static final Color GOLD = new Color(255, 215, 0);

    // Background image
    private BufferedImage backgroundImage;

    // Current user
    private final User currentUser;

    // UI components
    private JPanel boardPanel;
    private JLabel noListingsLabel;
    private JComboBox<String> statusFilter;

    public MyListingPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(LIGHT_BG);

        // Load the background image
        loadBackgroundImage();

        // Build the UI
        buildUI();

        // Load and display user's listings
        loadUserListings();
    }

    private void loadBackgroundImage() {
        try {
            // Path to the ChatGPT image
            File imgFile = new File("ChatGPT Image May 4, 2026, 12_26_37 PM.png");
            if (imgFile.exists()) {
                backgroundImage = ImageIO.read(imgFile);
            }
        } catch (Exception e) {
            System.err.println("Failed to load background image: " + e.getMessage());
        }
    }

    private void buildUI() {
        // Top bar with title, filter, and action buttons
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(MAROON);
        topBar.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        // Left side: title
        JLabel titleLabel = new JLabel("My Listings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        // Center: status filter
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        centerPanel.setOpaque(false);

        JLabel filterLabel = new JLabel("Filter by Status:");
        filterLabel.setForeground(Color.WHITE);
        filterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        String[] statusOptions = {"All Listings", "OPEN", "IN_PROGRESS", "CLOSED"};
        statusFilter = new JComboBox<>(statusOptions);
        statusFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusFilter.setSelectedIndex(0); // Default to "All Listings"

        centerPanel.add(filterLabel);
        centerPanel.add(statusFilter);

        // Right side: create new button
        JButton createNewBtn = new JButton("Create New Listing");
        createNewBtn.setBackground(GOLD);
        createNewBtn.setForeground(MAROON);
        createNewBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        createNewBtn.setFocusPainted(false);
        createNewBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        topBar.add(titleLabel, BorderLayout.WEST);
        topBar.add(centerPanel, BorderLayout.CENTER);
        topBar.add(createNewBtn, BorderLayout.EAST);

        // Main board panel with background image
        boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image if loaded
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        boardPanel.setLayout(new GridLayout(2, 2, 15, 15)); // 2x2 grid, 15px gaps
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // No listings message
        noListingsLabel = new JLabel("No listings found for the selected filter.");
        noListingsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        noListingsLabel.setHorizontalAlignment(JLabel.CENTER);

        JScrollPane scrollPane = new JScrollPane(boardPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(topBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Button listeners
        createNewBtn.addActionListener(e -> {
            // TODO: Open dialog to create new listing
            System.out.println("Create new listing clicked");
        });

        statusFilter.addActionListener(e -> loadUserListings());
    }

    private void loadUserListings() {
        boardPanel.removeAll();

        // Get all listings and filter by current user
        ArrayList<JobListing> allListings = DataStore.getListings();
        ArrayList<JobListing> userListings = new ArrayList<>();

        String selectedStatus = (String) statusFilter.getSelectedItem();

        for (JobListing listing : allListings) {
            if (listing.getPostedBy().equals(currentUser.getEmail())) {
                // Filter by status if not "All Listings"
                if (selectedStatus.equals("All Listings") ||
                        listing.getStatus().equals(selectedStatus)) {
                    userListings.add(listing);
                }
            }
        }

        if (userListings.isEmpty()) {
            boardPanel.add(noListingsLabel);
        } else {
            // Create a card for each listing
            for (JobListing listing : userListings) {
                JPanel card = createListingCard(listing);
                boardPanel.add(card);
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    private JPanel createListingCard(JobListing listing) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(CARD_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 180, 180)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        card.setPreferredSize(new Dimension(250, 200));

        // Title
        JLabel titleLabel = new JLabel(listing.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(MAROON);

        // Details panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);

        // Status with color coding
        JLabel statusLabel = new JLabel("Status: " + listing.getStatus());
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        // Color code status
        if (listing.getStatus().equals("OPEN")) {
            statusLabel.setForeground(new Color(40, 167, 69)); // Green
        } else if (listing.getStatus().equals("IN_PROGRESS")) {
            statusLabel.setForeground(new Color(255, 193, 7)); // Yellow/Orange
        } else if (listing.getStatus().equals("CLOSED")) {
            statusLabel.setForeground(new Color(220, 53, 69)); // Red
        }

        JLabel dateLabel = new JLabel("Posted: " + listing.getDatePosted());
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JLabel categoryLabel = new JLabel("Category: " + listing.getCategory());
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        // Count applications for this listing
        int appCount = getApplicationCount(listing.getListingId());
        JLabel appLabel = new JLabel("Applications: " + appCount);
        appLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        detailsPanel.add(statusLabel);
        detailsPanel.add(dateLabel);
        detailsPanel.add(categoryLabel);
        detailsPanel.add(appLabel);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setOpaque(false);

        JButton editBtn = new JButton("Edit");
        editBtn.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        editBtn.setBackground(GOLD);
        editBtn.setFocusPainted(false);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        deleteBtn.setBackground(new Color(255, 200, 200));
        deleteBtn.setFocusPainted(false);

        // Only show edit/delete for OPEN listings
        if (!listing.getStatus().equals("OPEN")) {
            editBtn.setEnabled(false);
            editBtn.setText("View Only");
            deleteBtn.setEnabled(false);
            deleteBtn.setText("Cannot Delete");
        }

        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(detailsPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        editBtn.addActionListener(e -> {
            if (listing.getStatus().equals("OPEN")) {
                // TODO: Open edit dialog
                System.out.println("Edit listing: " + listing.getListingId());
            } else {
                // TODO: Open view-only dialog
                System.out.println("View listing: " + listing.getListingId());
            }
        });

        deleteBtn.addActionListener(e -> {
            if (listing.getStatus().equals("OPEN")) {
                // TODO: Delete listing with confirmation
                System.out.println("Delete listing: " + listing.getListingId());
            }
        });

        return card;
    }

    // Helper method to count applications for a listing
    private int getApplicationCount(String listingId) {
        ArrayList<Application> allApplications = DataStore.getApplications();
        int count = 0;
        for (Application app : allApplications) {
            if (app.getListingId().equals(listingId)) {
                count++;
            }
        }
        return count;
    }

    // Call this to refresh the board when listings change
    public void refreshListings() {
        loadUserListings();
    }
}