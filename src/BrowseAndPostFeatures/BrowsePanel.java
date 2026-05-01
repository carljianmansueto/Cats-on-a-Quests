package BrowseAndPostFeatures;

import DataAndModels.DataStore;
import DataAndModels.JobListing;
import DataAndModels.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class BrowsePanel extends JPanel {

    // Colors — matched to LoginFrame and RegisterFrame maroon theme
    private static final Color MAROON       = new Color(128, 0, 0);
    private static final Color LIGHT_BG     = new Color(248, 245, 245);
    private static final Color CARD_WHITE   = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(200, 180, 180);
    private static final Color GOLD         = new Color(255, 215, 0);

    // The currently logged-in user — passed in from MainFrame
    private User currentUser;

    // UI components we need to access across methods
    private JPanel            listingsPanel;
    private JTextField        searchField;
    private JComboBox<String> categoryFilter;
    private JLabel            resultLabel;

    // Constructor
  
    public BrowsePanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout(0, 0));
        setBackground(LIGHT_BG);
        buildUI();
        loadListings("", "All Categories");
    }

    // Build the full panel layout
  
    private void buildUI() {

        // Top bar: title + search + filter
        JPanel topBar = new JPanel(new BorderLayout(10, 0));
        topBar.setBackground(MAROON);
        topBar.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel titleLabel = new JLabel("Browse Jobs & Services");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchRow.setOpaque(false);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        searchField = new JTextField(16);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setPreferredSize(new Dimension(180, 30));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        String[] categories = {
                "All Categories", "Tutoring", "Design", "Research",
                "Errand", "Technical", "Creative", "Labor", "Other"
        };
        categoryFilter = new JComboBox<>(categories);
        categoryFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        categoryFilter.setPreferredSize(new Dimension(150, 30));

        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(GOLD);
        searchBtn.setForeground(MAROON);
        searchBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchBtn.setFocusPainted(false);
        searchBtn.setBorderPainted(false);
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchBtn.setPreferredSize(new Dimension(80, 30));

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBackground(Color.WHITE);
        refreshBtn.setForeground(MAROON);
        refreshBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.setPreferredSize(new Dimension(80, 30));

        searchRow.add(searchLabel);
        searchRow.add(searchField);
        searchRow.add(categoryFilter);
        searchRow.add(searchBtn);
        searchRow.add(refreshBtn);

        topBar.add(titleLabel, BorderLayout.WEST);
        topBar.add(searchRow,  BorderLayout.EAST);

        // Sub bar: result count
        JPanel subBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 6));
        subBar.setBackground(new Color(245, 240, 240));
        subBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));

        resultLabel = new JLabel("Loading listings...");
        resultLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        resultLabel.setForeground(Color.GRAY);
        subBar.add(resultLabel);

        // Scrollable listings area
        listingsPanel = new JPanel();
        listingsPanel.setLayout(new BoxLayout(listingsPanel, BoxLayout.Y_AXIS));
        listingsPanel.setBackground(LIGHT_BG);
        listingsPanel.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JScrollPane scrollPane = new JScrollPane(listingsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(LIGHT_BG);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(topBar, BorderLayout.NORTH);
        northPanel.add(subBar, BorderLayout.SOUTH);

        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Button listeners
        searchBtn.addActionListener(e ->
                loadListings(searchField.getText().trim(),
                        (String) categoryFilter.getSelectedItem()));

        refreshBtn.addActionListener(e -> {
            searchField.setText("");
            categoryFilter.setSelectedIndex(0);
            loadListings("", "All Categories");
        });

        searchField.addActionListener(e ->
                loadListings(searchField.getText().trim(),
                        (String) categoryFilter.getSelectedItem()));
    }

    // Load listings from DataStore, filter, and display as cards
  
    private void loadListings(String keyword, String category) {
        listingsPanel.removeAll();

        ArrayList<JobListing> jobs;
        if (keyword.isEmpty()) {
            jobs = DataStore.getOpenListings();
        } else {
            jobs = DataStore.searchListings(keyword);
        }

        ArrayList<JobListing> filtered = new ArrayList<>();
        for (JobListing job : jobs) {
            boolean matchesCategory = category.equals("All Categories")
                    || job.getCategory().equalsIgnoreCase(category);
            boolean isOpen = job.getStatus().equals("OPEN");
            if (matchesCategory && isOpen) {
                filtered.add(job);
            }
        }

        resultLabel.setText(filtered.size() + " listing(s) found");

        if (filtered.isEmpty()) {
            JLabel none = new JLabel("No listings found. Try a different search.");
            none.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            none.setForeground(Color.GRAY);
            none.setAlignmentX(Component.CENTER_ALIGNMENT);
            none.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
            listingsPanel.add(none);
        } else {
            for (JobListing job : filtered) {
                listingsPanel.add(buildJobCard(job));
                listingsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        listingsPanel.revalidate();
        listingsPanel.repaint();
    }

    // Build a card panel for a single JobListing object
    // Encapsulation: we use getters to read private fields
    // Polymorphism: job.getSummary() runs JobListing's @Override version
  
    private JPanel buildJobCard(JobListing job) {
        JPanel card = new JPanel(new BorderLayout(10, 6));
        card.setBackground(CARD_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 165));

        // Top row: title + category badge
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        JLabel titleLabel = new JLabel(job.getTitle()); // Encapsulation
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(MAROON);

        JLabel categoryBadge = new JLabel("  " + job.getCategory() + "  ");
        categoryBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        categoryBadge.setForeground(Color.WHITE);
        categoryBadge.setBackground(MAROON);
        categoryBadge.setOpaque(true);
        categoryBadge.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));

        topRow.add(titleLabel,    BorderLayout.WEST);
        topRow.add(categoryBadge, BorderLayout.EAST);

        // Description
        String desc = job.getDescription(); // Encapsulation
        if (desc.length() > 110) {
            desc = desc.substring(0, 110) + "...";
        }
        JLabel descLabel = new JLabel("<html>" + desc + "</html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(Color.DARK_GRAY);

        // Bottom row: meta info + apply button
        JPanel bottomRow = new JPanel(new BorderLayout(8, 0));
        bottomRow.setOpaque(false);

        String payText = job.getPayType().equals("PER_HOUR")
                ? "P" + job.getPayRate() + "/hr"
                : "P" + job.getPayRate() + " fixed";

        JLabel metaLabel = new JLabel(
                "  " + payText
                        + "   " + job.getLocation()
                        + "   " + job.getSlotsAvailable() + " slot(s)"
                        + "   Deadline: " + job.getDeadline()
        );
        metaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        metaLabel.setForeground(new Color(100, 100, 100));

        JLabel posterLabel = new JLabel("Posted by: " + job.getPostedBy());
        posterLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        posterLabel.setForeground(Color.GRAY);

        JPanel metaPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        metaPanel.setOpaque(false);
        metaPanel.add(metaLabel);
        metaPanel.add(posterLabel);

        JButton applyBtn = new JButton("Apply");
        applyBtn.setBackground(MAROON);
        applyBtn.setForeground(GOLD);
        applyBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        applyBtn.setFocusPainted(false);
        applyBtn.setBorderPainted(false);
        applyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        applyBtn.setPreferredSize(new Dimension(90, 36));

        boolean alreadyApplied = DataStore.hasApplied(
                currentUser.getEmail(), job.getListingId()
        );
        boolean isOwnListing = job.getPostedBy()
                .equalsIgnoreCase(currentUser.getEmail());

        if (alreadyApplied) {
            applyBtn.setText("Applied");
            applyBtn.setBackground(new Color(180, 180, 180));
            applyBtn.setEnabled(false);
        } else if (isOwnListing) {
            applyBtn.setText("Your Post");
            applyBtn.setBackground(new Color(180, 180, 180));
            applyBtn.setEnabled(false);
        }

        applyBtn.addActionListener(e -> handleApply(job, applyBtn));

        bottomRow.add(metaPanel, BorderLayout.CENTER);
        bottomRow.add(applyBtn,  BorderLayout.EAST);

        card.add(topRow,    BorderLayout.NORTH);
        card.add(descLabel, BorderLayout.CENTER);
        card.add(bottomRow, BorderLayout.SOUTH);

        return card;
    }

    // Handle the Apply button click
  
    private void handleApply(JobListing job, JButton applyBtn) {
        JTextArea messageArea = new JTextArea(4, 30);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JPanel dialogPanel = new JPanel(new BorderLayout(0, 8));
        dialogPanel.add(
                new JLabel("Write a short message to the poster:"),
                BorderLayout.NORTH
        );
        dialogPanel.add(new JScrollPane(messageArea), BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(
                this, dialogPanel,
                "Apply to: " + job.getTitle(),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        String message = messageArea.getText().trim();
        if (message.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please write a message before applying.",
                    "Message Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String error = DataStore.applyToListing(job.getListingId(), message);

        if (error == null) {
            JOptionPane.showMessageDialog(this,
                    "Successfully applied to \"" + job.getTitle() + "\"!\n"
                            + "The poster will review your application.",
                    "Application Sent!", JOptionPane.INFORMATION_MESSAGE);
            applyBtn.setText("Applied");
            applyBtn.setBackground(new Color(180, 180, 180));
            applyBtn.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this,
                    error, "Could Not Apply", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Called by MainFrame when switching back to this tab
  
    public void refresh() {
        loadListings(
                searchField.getText().trim(),
                (String) categoryFilter.getSelectedItem()
        );
    }
}
