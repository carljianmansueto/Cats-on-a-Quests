package BrowseAndPostFeatures;

import DataAndModels.DataStore;
import DataAndModels.JobListing;
import DataAndModels.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class BrowseJobsPanel extends JPanel {

    private static final Color MAROON        = new Color(128, 0, 0);
    private static final Color LIGHT_BG      = new Color(248, 245, 245);
    private static final Color CARD_WHITE    = Color.WHITE;
    private static final Color CARD_EXPANDED = new Color(255, 249, 249);
    private static final Color BORDER_COLOR  = new Color(200, 180, 180);
    private static final Color BORDER_ACTIVE = new Color(128, 0, 0);
    private static final Color GOLD          = new Color(255, 215, 0);

    private User              currentUser;
    private JPanel            listingsPanel;
    private JTextField        searchField;
    private JComboBox<String> categoryFilter;
    private JLabel            resultLabel;

    public BrowseJobsPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout(0, 0));
        setBackground(LIGHT_BG);
        buildUI();
        loadListings("", "All Categories");
    }

    private void buildUI() {
        // Top bar
        JPanel topBar = new JPanel(new BorderLayout(10, 0));
        topBar.setBackground(MAROON);
        topBar.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel titleLabel = new JLabel("Browse Jobs & Services");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchRow.setOpaque(false);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        searchField = new JTextField(16);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 12));
        searchField.setPreferredSize(new Dimension(180, 30));

        String[] categories = {
                "All Categories", "Tutoring", "Design", "Research",
                "Errand", "Technical", "Creative", "Labor", "Other"
        };
        categoryFilter = new JComboBox<>(categories);
        categoryFilter.setFont(new Font("SansSerif", Font.PLAIN, 12));
        categoryFilter.setPreferredSize(new Dimension(150, 30));

        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(GOLD);
        searchBtn.setForeground(MAROON);
        searchBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        searchBtn.setFocusPainted(false);
        searchBtn.setBorderPainted(false);
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchBtn.setPreferredSize(new Dimension(80, 30));

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBackground(Color.WHITE);
        refreshBtn.setForeground(MAROON);
        refreshBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
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

        // Sub bar
        JPanel subBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 6));
        subBar.setBackground(new Color(245, 240, 240));
        subBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));

        resultLabel = new JLabel("Loading listings...");
        resultLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        resultLabel.setForeground(Color.GRAY);

        JLabel hint = new JLabel("Click a card to expand details");
        hint.setFont(new Font("SansSerif", Font.PLAIN, 11));
        hint.setForeground(new Color(160, 120, 120));
        subBar.add(resultLabel);
        subBar.add(hint);

        // Listings scroll area
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

        add(northPanel,  BorderLayout.NORTH);
        add(scrollPane,  BorderLayout.CENTER);

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

    private void loadListings(String keyword, String category) {
        listingsPanel.removeAll();

        ArrayList<JobListing> jobs = keyword.isEmpty()
                ? DataStore.getOpenListings()
                : DataStore.searchListings(keyword);

        ArrayList<JobListing> filtered = new ArrayList<>();
        for (JobListing job : jobs) {
            boolean matchesCategory = category.equals("All Categories")
                    || job.getCategory().equalsIgnoreCase(category);
            if (matchesCategory && job.getStatus().equals("OPEN")) {
                filtered.add(job);
            }
        }

        resultLabel.setText(filtered.size() + " listing(s) found");

        if (filtered.isEmpty()) {
            JLabel none = new JLabel("No listings found. Try a different search.");
            none.setFont(new Font("SansSerif", Font.ITALIC, 13));
            none.setForeground(Color.GRAY);
            none.setAlignmentX(Component.CENTER_ALIGNMENT);
            none.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
            listingsPanel.add(none);
        } else {
            for (JobListing job : filtered) {
                listingsPanel.add(new ExpandableCard(job));
                listingsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        listingsPanel.revalidate();
        listingsPanel.repaint();
    }

    public void refresh() {
        loadListings(
                searchField.getText().trim(),
                (String) categoryFilter.getSelectedItem()
        );
    }

    // Expandable card component

    private class ExpandableCard extends JPanel {

        private boolean expanded = false;
        private JPanel  collapsedView;
        private JPanel  expandedView;
        private JobListing job;

        ExpandableCard(JobListing job) {
            this.job = job;
            setLayout(new BorderLayout());
            setBackground(CARD_WHITE);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 9999));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(0, 0, 0, 0)
            ));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            collapsedView = buildCollapsedView();
            expandedView  = buildExpandedView();
            expandedView.setVisible(false);

            add(collapsedView, BorderLayout.NORTH);
            add(expandedView,  BorderLayout.CENTER);

            // Click anywhere on card to toggle
            MouseAdapter toggle = new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    // Don't collapse if clicking a button
                    if (e.getSource() instanceof JButton) return;
                    toggleExpand();
                }
            };
            addMouseListener(toggle);
            addMouseListenerRecursively(this, toggle);
        }

        private void addMouseListenerRecursively(Container c, MouseAdapter ma) {
            for (Component child : c.getComponents()) {
                if (!(child instanceof JButton)) {
                    child.addMouseListener(ma);
                }
                if (child instanceof Container) {
                    addMouseListenerRecursively((Container) child, ma);
                }
            }
        }

        private void toggleExpand() {
            expanded = !expanded;
            expandedView.setVisible(expanded);

            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(expanded ? BORDER_ACTIVE : BORDER_COLOR, expanded ? 2 : 1),
                    BorderFactory.createEmptyBorder(0, 0, 0, 0)
            ));
            setBackground(expanded ? CARD_EXPANDED : CARD_WHITE);
            collapsedView.setBackground(expanded ? CARD_EXPANDED : CARD_WHITE);

            revalidate();
            repaint();

            // Scroll to show the expanded card
            SwingUtilities.invokeLater(() -> scrollRectToVisible(getBounds()));
        }

        // ollapsed view (always visible)

        private JPanel buildCollapsedView() {
            JPanel panel = new JPanel(new BorderLayout(10, 4));
            panel.setBackground(CARD_WHITE);
            panel.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

            // Title + category badge
            JPanel topRow = new JPanel(new BorderLayout());
            topRow.setOpaque(false);

            JLabel titleLabel = new JLabel(job.getTitle());
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            titleLabel.setForeground(MAROON);

            JPanel rightBadges = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
            rightBadges.setOpaque(false);

            JLabel categoryBadge = badge(job.getCategory(), MAROON, Color.WHITE);
            JLabel chevron = new JLabel("▾");
            chevron.setFont(new Font("SansSerif", Font.BOLD, 14));
            chevron.setForeground(new Color(160, 100, 100));

            rightBadges.add(categoryBadge);
            rightBadges.add(chevron);

            topRow.add(titleLabel,   BorderLayout.WEST);
            topRow.add(rightBadges,  BorderLayout.EAST);

            // Short description preview
            String desc = job.getDescription();
            if (desc.length() > 110) desc = desc.substring(0, 110) + "...";
            JLabel descPreview = new JLabel("<html>" + desc + "</html>");
            descPreview.setFont(new Font("SansSerif", Font.PLAIN, 12));
            descPreview.setForeground(Color.DARK_GRAY);

            // Meta row
            String payText = job.getPayType().equals("PER_HOUR")
                    ? "₱" + job.getPayRate() + "/hr"
                    : "₱" + job.getPayRate() + " fixed";

            JLabel metaLabel = new JLabel(
                    payText + "   |   " + job.getLocation()
                            + "   |   " + job.getSlotsAvailable() + " slot(s)"
                            + "   |   Deadline: " + job.getDeadline()
            );
            metaLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
            metaLabel.setForeground(new Color(120, 100, 100));

            panel.add(topRow,    BorderLayout.NORTH);
            panel.add(descPreview, BorderLayout.CENTER);
            panel.add(metaLabel, BorderLayout.SOUTH);

            return panel;
        }

        // Expanded view (shown on click)

        private JPanel buildExpandedView() {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(CARD_EXPANDED);
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 200, 200)),
                    BorderFactory.createEmptyBorder(14, 16, 16, 16)
            ));

            // Section: Full Description
            panel.add(sectionLabel("Description"));
            panel.add(Box.createRigidArea(new Dimension(0, 4)));

            JTextArea descArea = new JTextArea(job.getDescription());
            descArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
            descArea.setForeground(Color.DARK_GRAY);
            descArea.setBackground(CARD_EXPANDED);
            descArea.setEditable(false);
            descArea.setLineWrap(true);
            descArea.setWrapStyleWord(true);
            descArea.setFocusable(false);
            descArea.setAlignmentX(Component.LEFT_ALIGNMENT);
            descArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
            panel.add(descArea);
            panel.add(Box.createRigidArea(new Dimension(0, 14)));

            // Section: Details grid
            panel.add(sectionLabel("Details"));
            panel.add(Box.createRigidArea(new Dimension(0, 6)));

            JPanel detailsGrid = new JPanel(new GridLayout(0, 2, 10, 6));
            detailsGrid.setBackground(CARD_EXPANDED);
            detailsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
            detailsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

            String payText = job.getPayType().equals("PER_HOUR")
                    ? "₱" + job.getPayRate() + " / hour"
                    : "₱" + job.getPayRate() + " (fixed rate)";

            addDetailRow(detailsGrid, "💰 Pay",        payText);
            addDetailRow(detailsGrid, "📍 Location",   job.getLocation());
            addDetailRow(detailsGrid, "🏷 Category",   job.getCategory());
            addDetailRow(detailsGrid, "👥 Slots",      job.getSlotsAvailable() + " available");
            addDetailRow(detailsGrid, "📅 Deadline",   job.getDeadline());
            addDetailRow(detailsGrid, "📬 Posted by",  job.getPostedBy());
            addDetailRow(detailsGrid, "🗓 Date posted", job.getDatePosted());
            addDetailRow(detailsGrid, "🆔 Listing ID", job.getListingId());

            panel.add(detailsGrid);
            panel.add(Box.createRigidArea(new Dimension(0, 16)));

            // Apply button (only for non-owners)
            boolean isOwn = job.getPostedBy().equalsIgnoreCase(currentUser.getEmail());
            boolean alreadyApplied = !isOwn && DataStore.hasApplied(currentUser.getEmail(), job.getListingId());

            JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            btnRow.setOpaque(false);
            btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);

            JButton applyBtn = new JButton();
            applyBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
            applyBtn.setFocusPainted(false);
            applyBtn.setBorderPainted(false);
            applyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            applyBtn.setPreferredSize(new Dimension(140, 38));

            if (isOwn) {
                applyBtn.setText("Your Listing");
                applyBtn.setBackground(new Color(180, 180, 180));
                applyBtn.setForeground(Color.WHITE);
                applyBtn.setEnabled(false);
            } else if (alreadyApplied) {
                applyBtn.setText("✓ Applied");
                applyBtn.setBackground(new Color(180, 180, 180));
                applyBtn.setForeground(Color.WHITE);
                applyBtn.setEnabled(false);
            } else {
                applyBtn.setText("Apply Now");
                applyBtn.setBackground(MAROON);
                applyBtn.setForeground(GOLD);
                applyBtn.addActionListener(e -> handleApply(job, applyBtn));
            }

            btnRow.add(applyBtn);
            panel.add(btnRow);

            return panel;
        }

        private JLabel sectionLabel(String text) {
            JLabel lbl = new JLabel(text.toUpperCase());
            lbl.setFont(new Font("SansSerif", Font.BOLD, 10));
            lbl.setForeground(new Color(160, 100, 100));
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            return lbl;
        }

        private void addDetailRow(JPanel grid, String label, String value) {
            JLabel lbl = new JLabel(label);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
            lbl.setForeground(new Color(80, 50, 50));

            JLabel val = new JLabel(value);
            val.setFont(new Font("SansSerif", Font.PLAIN, 12));
            val.setForeground(Color.DARK_GRAY);

            grid.add(lbl);
            grid.add(val);
        }

        private JLabel badge(String text, Color bg, Color fg) {
            JLabel lbl = new JLabel("  " + text + "  ");
            lbl.setFont(new Font("SansSerif", Font.BOLD, 10));
            lbl.setForeground(fg);
            lbl.setBackground(bg);
            lbl.setOpaque(true);
            lbl.setBorder(BorderFactory.createEmptyBorder(3, 4, 3, 4));
            return lbl;
        }
    }

    // Apply handler

    private void handleApply(JobListing job, JButton applyBtn) {
        JTextArea messageArea = new JTextArea(4, 30);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JPanel dialogPanel = new JPanel(new BorderLayout(0, 8));
        dialogPanel.add(new JLabel("Write a short message to the poster:"), BorderLayout.NORTH);
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
            applyBtn.setText("✓ Applied");
            applyBtn.setBackground(new Color(180, 180, 180));
            applyBtn.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this,
                    error, "Could Not Apply", JOptionPane.WARNING_MESSAGE);
        }
    }
}