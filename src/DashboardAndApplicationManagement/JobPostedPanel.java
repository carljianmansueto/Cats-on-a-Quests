package DashboardAndApplicationManagement;

import DataAndModels.Application;
import DataAndModels.DataStore;
import DataAndModels.JobListing;
import DataAndModels.User;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * MyListingPanel.java — Member 4
 *
 * BUGS FIXED IN THIS VERSION:
 * ─────────────────────────────────────────────────────────────────
 * BUG 1 — Edit button did nothing (just printed to console)
 *   Fix: editBtn now opens a real dialog where the poster can
 *        update the title, description, pay, slots, and deadline.
 *
 * BUG 2 — Delete button did nothing (just printed to console)
 *   Fix: deleteBtn now shows a YES/NO confirmation dialog, then
 *        calls DataStore.removeListing() and refreshes the board.
 *
 * BUG 3 — No way to see who applied or accept/reject them
 *   Fix: Added "View Applicants" button on every card.
 *        Opens a popup table showing all applicants with their
 *        email, date applied, message, and status.
 *        Poster can click Accept or Reject for each applicant.
 *
 * BUG 4 — "Create New Listing" button did nothing
 *   Fix: Now navigates to the Post a Job tab via MainFrame.
 *
 * BUG 5 — Edit/Delete disabled for all non-OPEN listings
 *   Fix: Delete always works (you can delete a closed listing too).
 *        Edit is only for OPEN listings (makes logical sense).
 *        View Applicants always works regardless of status.
 * ─────────────────────────────────────────────────────────────────
 */
public class JobPostedPanel extends JPanel {

    private static final Color MAROON      = new Color(128, 0, 0);
    private static final Color LIGHT_BG    = new Color(248, 245, 245);
    private static final Color CARD_WHITE  = Color.WHITE;
    private static final Color GOLD        = new Color(255, 215, 0);
    private static final Color GREEN       = new Color(40, 167, 69);
    private static final Color RED_BTN     = new Color(200, 50, 50);
    private static final Color BORDER_CLR  = new Color(200, 180, 180);

    private BufferedImage backgroundImage;
    private final User    currentUser;

    private JPanel            boardPanel;
    private JLabel            noListingsLabel;
    private JComboBox<String> statusFilter;

    public JobPostedPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(LIGHT_BG);
        loadBackgroundImage();
        buildUI();
        loadUserListings();
    }

    // ── Load background image (kept from original) ────────────────────────────
    private void loadBackgroundImage() {
        try {
            File imgFile = new File("ChatGPT Image May 4, 2026, 12_26_37 PM.png");
            if (imgFile.exists()) backgroundImage = ImageIO.read(imgFile);
        } catch (Exception e) {
            System.err.println("Failed to load background image: " + e.getMessage());
        }
    }

    // ── Build the top bar + board panel ──────────────────────────────────────
    private void buildUI() {
        // ── Top bar ──────────────────────────────────────────────────────────
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(MAROON);
        topBar.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel titleLabel = new JLabel("Job Posted");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        centerPanel.setOpaque(false);
        JLabel filterLabel = new JLabel("Filter by Status:");
        filterLabel.setForeground(Color.WHITE);
        filterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusFilter = new JComboBox<>(new String[]{"All Listings", "OPEN", "IN_PROGRESS", "CLOSED"});
        statusFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        centerPanel.add(filterLabel);
        centerPanel.add(statusFilter);

        // FIX 4: "Create New Listing" now actually navigates to Post a Job tab
        JButton createNewBtn = makeBtn("Create New Listing", GOLD, MAROON);
        createNewBtn.addActionListener(e -> {
            // MainFrame uses JTabbedPane — navigate by switching to tab index 1
            // Tab order: 0=Browse, 1=Post a Job, 2=My Listings, 3=My Applications
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w instanceof MainFrame) {
                ((MainFrame) w).goToPostTab();
            }
        });

        topBar.add(titleLabel,   BorderLayout.WEST);
        topBar.add(centerPanel,  BorderLayout.CENTER);
        topBar.add(createNewBtn, BorderLayout.EAST);

        // ── Board panel with background image ─────────────────────────────────
        boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null)
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        boardPanel.setLayout(new BoxLayout(boardPanel, BoxLayout.Y_AXIS));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        noListingsLabel = new JLabel("No listings found for the selected filter.");
        noListingsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        noListingsLabel.setHorizontalAlignment(JLabel.CENTER);
        noListingsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JScrollPane scrollPane = new JScrollPane(boardPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(topBar,    BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        statusFilter.addActionListener(e -> loadUserListings());
    }

    // ── Load and display this user's listings ─────────────────────────────────
    private void loadUserListings() {
        boardPanel.removeAll();

        String selectedStatus = (String) statusFilter.getSelectedItem();
        ArrayList<JobListing> userListings = new ArrayList<>();

        for (JobListing jl : DataStore.getListings()) {
            if (jl.getPostedBy().equalsIgnoreCase(currentUser.getEmail())) {
                if (selectedStatus.equals("All Listings") || jl.getStatus().equals(selectedStatus))
                    userListings.add(jl);
            }
        }

        if (userListings.isEmpty()) {
            boardPanel.add(Box.createVerticalStrut(40));
            boardPanel.add(noListingsLabel);
        } else {
            for (JobListing jl : userListings) {
                boardPanel.add(createListingCard(jl));
                boardPanel.add(Box.createVerticalStrut(12)); // gap between cards
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    // ── Build one card per listing ────────────────────────────────────────────
    private JPanel createListingCard(JobListing listing) {
        JPanel card = new JPanel(new BorderLayout(10, 8));
        card.setBackground(CARD_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_CLR, 1),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        // ── Title ──────────────────────────────────────────────────────────
        JLabel titleLabel = new JLabel(listing.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLabel.setForeground(MAROON);
        card.add(titleLabel, BorderLayout.NORTH);

        // ── Details ────────────────────────────────────────────────────────
        JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.setOpaque(false);

        JLabel statusLabel = new JLabel("Status: " + listing.getStatus());
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        statusLabel.setForeground(
                "OPEN".equals(listing.getStatus()) ? GREEN :
                        "CLOSED".equals(listing.getStatus()) ? RED_BTN : Color.ORANGE.darker()
        );

        int appCount = DataStore.getApplicationsForListing(listing.getListingId()).size();
        String pay = listing.getPayType().equals("PER_HOUR")
                ? "₱" + listing.getPayRate() + "/hr"
                : "₱" + listing.getPayRate() + " fixed";

        details.add(statusLabel);
        details.add(smallLabel("Posted: "    + listing.getDatePosted()));
        details.add(smallLabel("Category: "  + listing.getCategory()));
        details.add(smallLabel("Pay: "        + pay));
        details.add(smallLabel("Listing ID: " + listing.getListingId()));
        details.add(smallLabel("Applications: " + appCount));
        card.add(details, BorderLayout.CENTER);

        // ── Buttons ────────────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);

        // FIX 3: "View Applicants" button — fully working, always shown
        JButton viewAppsBtn = makeBtn("View Applicants", MAROON, Color.WHITE);
        viewAppsBtn.addActionListener(e -> openApplicantsDialog(listing));

        // FIX 1: Edit button — opens real edit dialog (only for OPEN listings)
        JButton editBtn = makeBtn("Edit", GOLD, MAROON);
        editBtn.setEnabled("OPEN".equals(listing.getStatus()));
        if (!editBtn.isEnabled()) editBtn.setToolTipText("Can only edit OPEN listings");
        editBtn.addActionListener(e -> openEditDialog(listing));

        // FIX 2: Delete button — now actually deletes with confirmation
        JButton deleteBtn = makeBtn("Delete", new Color(255, 200, 200), RED_BTN);
        deleteBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    SwingUtilities.getWindowAncestor(this),
                    "Delete \"" + listing.getTitle() + "\"?\nThis cannot be undone.",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                DataStore.removeListing(listing.getListingId());
                JOptionPane.showMessageDialog(
                        SwingUtilities.getWindowAncestor(this),
                        "Listing deleted successfully.",
                        "Deleted", JOptionPane.INFORMATION_MESSAGE
                );
                loadUserListings(); // refresh the board
            }
        });

        btnPanel.add(viewAppsBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        card.add(btnPanel, BorderLayout.SOUTH);

        return card;
    }

    // ── FIX 3: View Applicants dialog ────────────────────────────────────────
    /**
     * Opens a popup showing all applicants for the selected listing.
     * The poster can ACCEPT or REJECT each applicant from here.
     *
     * This is the main feature that was MISSING — posters had no way
     * to see who applied or to accept/reject them.
     */
    private void openApplicantsDialog(JobListing listing) {
        Window parent = SwingUtilities.getWindowAncestor(this);

        ArrayList<Application> apps = DataStore.getApplicationsForListing(listing.getListingId());

        JDialog dialog = new JDialog(parent, "Applicants — " + listing.getTitle(), Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(700, 440);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());

        // Dialog header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        header.setBackground(MAROON);
        JLabel hTitle = new JLabel("Applicants for: " + listing.getTitle());
        hTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        hTitle.setForeground(Color.WHITE);
        JLabel hCount = new JLabel("(" + apps.size() + " total)");
        hCount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        hCount.setForeground(GOLD);
        header.add(hTitle);
        header.add(hCount);
        dialog.add(header, BorderLayout.NORTH);

        if (apps.isEmpty()) {
            JLabel none = new JLabel("No one has applied to this listing yet.", SwingConstants.CENTER);
            none.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            none.setForeground(Color.GRAY);
            dialog.add(none, BorderLayout.CENTER);
        } else {
            // Build the applicants table
            String[] cols = {"App ID", "Applicant Email", "Date Applied", "Status", "Message"};
            DefaultTableModel model = new DefaultTableModel(cols, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };

            for (Application a : apps) {
                String preview = a.getMessage().length() > 50
                        ? a.getMessage().substring(0, 50) + "..."
                        : a.getMessage();
                model.addRow(new Object[]{
                        a.getApplicationId(),
                        a.getApplicantEmail(),
                        a.getDateApplied(),
                        a.getStatus(),
                        preview
                });
            }

            JTable table = new JTable(model);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            table.setRowHeight(26);
            table.setShowGrid(false);
            table.setSelectionBackground(new Color(180, 60, 60));
            table.setSelectionForeground(Color.WHITE);

            // Style the header
            JTableHeader th = table.getTableHeader();
            th.setFont(new Font("Segoe UI", Font.BOLD, 12));
            th.setBackground(MAROON);
            th.setForeground(Color.WHITE);
            th.setReorderingAllowed(false);

            // Color-code the Status column (index 3)
            table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(
                        JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                    super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                    if (!sel) {
                        setBackground(row % 2 == 0 ? Color.WHITE : new Color(252, 245, 245));
                        String st = (String) model.getValueAt(row, 3);
                        if (col == 3) {
                            setForeground("ACCEPTED".equals(st) ? GREEN :
                                    "REJECTED".equals(st) ? RED_BTN : new Color(180, 110, 0));
                            setFont(new Font("Segoe UI", Font.BOLD, 12));
                        } else {
                            setForeground(Color.DARK_GRAY);
                            setFont(new Font("Segoe UI", Font.PLAIN, 12));
                        }
                    }
                    setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                    return this;
                }
            });

            dialog.add(new JScrollPane(table), BorderLayout.CENTER);

            // Accept / Reject / Close buttons
            JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
            btnRow.setBackground(LIGHT_BG);
            btnRow.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_CLR));

            JLabel hint = new JLabel("Select a row then click Accept or Reject");
            hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            hint.setForeground(Color.GRAY);
            btnRow.add(hint);
            btnRow.add(Box.createHorizontalStrut(20));

            JButton acceptBtn = makeBtn("✓  Accept", GREEN, Color.WHITE);
            JButton rejectBtn = makeBtn("✗  Reject", RED_BTN, Color.WHITE);
            JButton closeBtn  = makeBtn("Close", new Color(120,120,120), Color.WHITE);

            acceptBtn.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(dialog, "Please select an applicant row first.");
                    return;
                }
                String appId = (String) model.getValueAt(row, 0);
                String current = (String) model.getValueAt(row, 3);
                if ("ACCEPTED".equals(current)) {
                    JOptionPane.showMessageDialog(dialog, "This applicant is already accepted.");
                    return;
                }
                DataStore.updateApplicationStatus(appId, "ACCEPTED");
                model.setValueAt("ACCEPTED", row, 3); // update table live
                table.repaint();
                JOptionPane.showMessageDialog(dialog,
                        "Applicant accepted! They will see this update in their My Applications tab.",
                        "Accepted", JOptionPane.INFORMATION_MESSAGE);
                loadUserListings(); // refresh card's application count
            });

            rejectBtn.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(dialog, "Please select an applicant row first.");
                    return;
                }
                String appId = (String) model.getValueAt(row, 0);
                String current = (String) model.getValueAt(row, 3);
                if ("REJECTED".equals(current)) {
                    JOptionPane.showMessageDialog(dialog, "This applicant is already rejected.");
                    return;
                }
                DataStore.updateApplicationStatus(appId, "REJECTED");
                model.setValueAt("REJECTED", row, 3);
                table.repaint();
            });

            closeBtn.addActionListener(e -> dialog.dispose());

            btnRow.add(rejectBtn);
            btnRow.add(acceptBtn);
            btnRow.add(closeBtn);
            dialog.add(btnRow, BorderLayout.SOUTH);
        }

        dialog.setVisible(true);
    }

    // ── FIX 1: Edit dialog ────────────────────────────────────────────────────
    /**
     * Opens a dialog to edit an OPEN listing.
     * The poster can change: title, description, pay, slots, deadline.
     * Changes are saved to DataStore (and listings.txt) immediately.
     */
    private void openEditDialog(JobListing listing) {
        Window parent = SwingUtilities.getWindowAncestor(this);

        JDialog dialog = new JDialog(parent, "Edit Listing — " + listing.getTitle(), Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(480, 440);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        header.setBackground(MAROON);
        JLabel hTitle = new JLabel("Edit: " + listing.getTitle());
        hTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        hTitle.setForeground(Color.WHITE);
        header.add(hTitle);
        dialog.add(header, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        form.setBackground(LIGHT_BG);

        JTextField titleField = formField(form, "Title:", listing.getTitle());
        JTextArea descArea    = formArea(form,  "Description:", listing.getDescription());
        JTextField payField   = formField(form, "Pay Amount (₱):", String.valueOf(listing.getPayRate()));
        JTextField slotsField = formField(form, "Slots Available:", String.valueOf(listing.getSlotsAvailable()));
        JTextField deadlineField = formField(form, "Deadline (YYYY-MM-DD):", listing.getDeadline());

        JLabel errLabel = new JLabel(" ");
        errLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        errLabel.setForeground(Color.RED);
        errLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(errLabel);

        dialog.add(new JScrollPane(form), BorderLayout.CENTER);

        // Save / Cancel buttons
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btns.setBackground(LIGHT_BG);
        btns.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_CLR));

        JButton saveBtn   = makeBtn("Save Changes", MAROON, Color.WHITE);
        JButton cancelBtn = makeBtn("Cancel", new Color(120,120,120), Color.WHITE);

        saveBtn.addActionListener(e -> {
            // Validate inputs
            String newTitle   = titleField.getText().trim();
            String newDesc    = descArea.getText().trim();
            String payStr     = payField.getText().trim();
            String slotsStr   = slotsField.getText().trim();
            String newDeadline = deadlineField.getText().trim();

            if (newTitle.isEmpty() || newDesc.isEmpty() || payStr.isEmpty()
                    || slotsStr.isEmpty() || newDeadline.isEmpty()) {
                errLabel.setText("All fields are required."); return;
            }

            double newPay;
            try { newPay = Double.parseDouble(payStr); }
            catch (NumberFormatException ex) { errLabel.setText("Pay must be a number (e.g. 75 or 500.00)."); return; }
            if (newPay <= 0) { errLabel.setText("Pay must be greater than 0."); return; }

            int newSlots;
            try { newSlots = Integer.parseInt(slotsStr); }
            catch (NumberFormatException ex) { errLabel.setText("Slots must be a whole number."); return; }
            if (newSlots < 1) { errLabel.setText("Slots must be at least 1."); return; }

            // Apply changes to the listing object (Encapsulation: using setters)
            listing.setTitle(newTitle);
            listing.setDescription(newDesc);
            listing.setPayRate(newPay);
            listing.setSlotsAvailable(newSlots);
            listing.setDeadline(newDeadline);

            // Save to file
            DataStore.saveAll();

            JOptionPane.showMessageDialog(dialog, "Listing updated successfully!", "Saved", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            loadUserListings(); // refresh the board
        });

        cancelBtn.addActionListener(e -> dialog.dispose());
        btns.add(cancelBtn);
        btns.add(saveBtn);
        dialog.add(btns, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private JLabel smallLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        l.setForeground(Color.DARK_GRAY);
        return l;
    }

    private JButton makeBtn(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    private JTextField formField(JPanel form, String label, String value) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(MAROON);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField field = new JTextField(value);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_CLR),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        form.add(lbl);
        form.add(Box.createRigidArea(new Dimension(0, 3)));
        form.add(field);
        form.add(Box.createRigidArea(new Dimension(0, 10)));
        return field;
    }

    private JTextArea formArea(JPanel form, String label, String value) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(MAROON);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea area = new JTextArea(value, 3, 20);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_CLR),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        JScrollPane sp = new JScrollPane(area);
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        form.add(lbl);
        form.add(Box.createRigidArea(new Dimension(0, 3)));
        form.add(sp);
        form.add(Box.createRigidArea(new Dimension(0, 10)));
        return area;
    }

    public void refreshListings() { loadUserListings(); }
    public void refresh()         { loadUserListings(); }
}