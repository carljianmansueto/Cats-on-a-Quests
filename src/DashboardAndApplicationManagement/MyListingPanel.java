package DashboardAndApplicationManagement;

import DataAndModels.Application;
import DataAndModels.DataStore;
import DataAndModels.JobListing;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * ═══════════════════════════════════════════════════════════════════
 *  MyListingPanel.java
 *  MEMBER 4 — Package: DashboardAndApplicationManagement
 *
 *  Shows a table of ALL listings posted by the currently logged-in user.
 *  The user can:
 *    - See all their listings (title, pay, status, applicant count)
 *    - Double-click to view WHO applied and ACCEPT or REJECT them
 *    - Close a listing (removes it from Browse)
 *    - Delete a listing permanently
 *    - Jump to PostPanel via "+ Post New"
 *
 *  KEY DIFFERENCES from what was originally written (matched to your zip):
 *    - Package is DashboardAndApplicationManagement
 *    - Uses EMAIL (not username) to identify the user — matches DataStore
 *    - No UITheme class — colors defined directly like LoginFrame/BrowsePanel
 *    - applicantEmail used (not applicantUsername) — matches Application.java
 *    - refresh() is a public method (MainFrame calls it when tab is shown)
 * ═══════════════════════════════════════════════════════════════════
 */
public class MyListingPanel extends JPanel {

    // ─── COLOR PALETTE ────────────────────────────────────────────────────────
    // Same colors used across your group's LoginFrame, BrowsePanel, PostPanel
    private static final Color MAROON       = new Color(128, 0, 0);
    private static final Color MAROON_DARK  = new Color(90, 0, 0);
    private static final Color LIGHT_BG     = new Color(248, 245, 245);
    private static final Color WHITE        = Color.WHITE;
    private static final Color GOLD         = new Color(255, 215, 0);
    private static final Color BORDER_COLOR = new Color(200, 180, 180);
    private static final Color GREEN        = new Color(0, 130, 0);
    private static final Color RED          = new Color(180, 0, 0);
    private static final Color AMBER        = new Color(180, 110, 0);

    // ─── FIELDS ───────────────────────────────────────────────────────────────
    // userEmail — the logged-in user's email, used as the unique ID in DataStore
    // (Your group removed username — email is the identifier now)
    private final String userEmail;

    private JTable            table;        // the visual data grid
    private DefaultTableModel tableModel;   // the data behind the grid
    private JLabel            countLabel;   // "3 listing(s)" top-right

    // Column headers — order must match the addRow() calls in refresh()
    private static final String[] COLUMNS = {
            "Listing ID", "Title", "Category", "Pay", "Slots", "Deadline", "Status", "Applicants"
    };

    // ─── CONSTRUCTOR ──────────────────────────────────────────────────────────
    /**
     * Called by MainFrame when building the content area:
     *   contentArea.add(new MyListingPanel(email), PANEL_MYLIST);
     *
     * @param userEmail  The logged-in user's email (used in all DataStore calls)
     */
    public MyListingPanel(String userEmail) {
        this.userEmail = userEmail;
        setLayout(new BorderLayout());
        setBackground(LIGHT_BG);
        buildUI();
        refresh(); // load listings on first open
    }

    // ─── BUILD UI ─────────────────────────────────────────────────────────────
    private void buildUI() {

        // ── NORTH: Maroon top bar ─────────────────────────────────────────────
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(MAROON);
        topBar.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel titleLabel = new JLabel("My Listings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(WHITE);
        topBar.add(titleLabel, BorderLayout.WEST);

        countLabel = new JLabel("");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        countLabel.setForeground(new Color(255, 220, 220));
        topBar.add(countLabel, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // ── CENTER: Table ─────────────────────────────────────────────────────
        // DefaultTableModel stores the row data.
        // isCellEditable returns false so users cannot type directly into cells.
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // read-only — no direct editing in the table
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(28);
        table.setShowGrid(false);              // cleaner look without grid lines
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(180, 60, 60));
        table.setSelectionForeground(WHITE);
        table.setFillsViewportHeight(true);

        // Style the header row
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(MAROON);
        header.setForeground(WHITE);
        header.setReorderingAllowed(false);

        // Set column widths (pixels)
        int[] widths = {75, 185, 90, 100, 50, 90, 80, 80};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // Custom cell renderer: alternating row colors + color-coded Status column
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean selected,
                    boolean focused, int row, int col) {

                super.getTableCellRendererComponent(t, val, selected, focused, row, col);

                if (!selected) {
                    // Alternate white / light pink rows
                    setBackground(row % 2 == 0 ? WHITE : new Color(252, 245, 245));

                    // Color-code the Status column (index 6)
                    String status = (String) tableModel.getValueAt(row, 6);
                    if (col == 6) {
                        switch (status) {
                            case "OPEN"   -> { setForeground(GREEN); setFont(new Font("Segoe UI", Font.BOLD, 12)); }
                            case "CLOSED" -> { setForeground(RED);   setFont(new Font("Segoe UI", Font.BOLD, 12)); }
                            default       -> { setForeground(AMBER); setFont(new Font("Segoe UI", Font.BOLD, 12)); }
                        }
                    } else {
                        setForeground(Color.DARK_GRAY);
                        setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    }
                }
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        });

        // Double-click any row → open the applicants popup
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewApplicants();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        scroll.getViewport().setBackground(WHITE);
        add(scroll, BorderLayout.CENTER);

        // ── SOUTH: Action buttons ─────────────────────────────────────────────
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomBar.setBackground(LIGHT_BG);
        bottomBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));

        JLabel hint = new JLabel("Double-click a listing to see applicants");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(Color.GRAY);
        bottomBar.add(hint);

        // Spacer to push buttons right
        bottomBar.add(Box.createHorizontalStrut(20));

        JButton viewBtn  = makeButton("View Applicants", MAROON,      GOLD);
        JButton closeBtn = makeButton("Close Listing",   new Color(120, 120, 120), WHITE);
        JButton deleteBtn= makeButton("Delete",          RED,          WHITE);
        JButton newBtn   = makeButton("+ Post New",      GOLD,         MAROON);

        viewBtn.addActionListener(e  -> viewApplicants());
        closeBtn.addActionListener(e -> closeListing());
        deleteBtn.addActionListener(e-> deleteListing());
        newBtn.addActionListener(e   -> {
            // Navigate to PostPanel via MainFrame
            // We walk up the component tree to find MainFrame
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w instanceof MainFrame) {
                ((MainFrame) w).showPanel(MainFrame.PANEL_POST);
            }
        });

        bottomBar.add(viewBtn);
        bottomBar.add(closeBtn);
        bottomBar.add(deleteBtn);
        bottomBar.add(newBtn);

        add(bottomBar, BorderLayout.SOUTH);
    }

    // ─── REFRESH ──────────────────────────────────────────────────────────────
    /**
     * Reloads the table with the latest listings from DataStore.
     * Called by MainFrame.showPanel() every time this panel becomes visible.
     *
     * DATA FLOW:
     *   DataStore.getListingsByUser(userEmail)
     *     → returns ArrayList<JobListing> for this user
     *     → we loop and add each one as a table row
     */
    public void refresh() {
        tableModel.setRowCount(0); // clear existing rows

        ArrayList<JobListing> mine = DataStore.getListingsByUser(userEmail);

        for (JobListing jl : mine) {
            // Count how many students applied to this listing
            int appCount = DataStore.getApplicationsForListing(jl.getListingId()).size();

            // Format pay: "₱75/hr" or "₱500 fixed"
            String pay = jl.getPayType().equals("PER_HOUR")
                    ? "P" + jl.getPayRate() + "/hr"
                    : "P" + jl.getPayRate() + " fixed";

            // addRow() — order must match COLUMNS array exactly
            tableModel.addRow(new Object[]{
                    jl.getListingId(),          // col 0
                    jl.getTitle(),              // col 1
                    jl.getCategory(),           // col 2
                    pay,                        // col 3
                    jl.getSlotsAvailable(),     // col 4
                    jl.getDeadline(),           // col 5
                    jl.getStatus(),             // col 6  (color-coded by renderer)
                    appCount + " applied"       // col 7
            });
        }

        countLabel.setText(mine.size() + " listing(s)  ");
    }

    // ─── VIEW APPLICANTS ──────────────────────────────────────────────────────
    /**
     * Opens a popup dialog showing everyone who applied to the selected listing.
     * Poster can ACCEPT or REJECT each applicant.
     * Uses Application.getApplicantEmail() — matches your Application.java.
     */
    private void viewApplicants() {
        int row = getSelectedRow();
        if (row < 0) return;

        String listingId = (String) tableModel.getValueAt(row, 0);
        String title     = (String) tableModel.getValueAt(row, 1);

        ArrayList<Application> apps = DataStore.getApplicationsForListing(listingId);

        // Build popup dialog
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                "Applicants — " + title, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(620, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Dialog header
        JPanel dHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dHeader.setBackground(MAROON);
        dHeader.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        JLabel dTitle = new JLabel("Applicants for: " + title);
        dTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dTitle.setForeground(WHITE);
        JLabel dCount = new JLabel("   (" + apps.size() + " total)");
        dCount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dCount.setForeground(GOLD);
        dHeader.add(dTitle);
        dHeader.add(dCount);
        dialog.add(dHeader, BorderLayout.NORTH);

        if (apps.isEmpty()) {
            JLabel none = new JLabel("No applications yet for this listing.", SwingConstants.CENTER);
            none.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            none.setForeground(Color.GRAY);
            dialog.add(none, BorderLayout.CENTER);
        } else {
            // Build applicant table
            String[] cols = {"App ID", "Applicant Email", "Date Applied", "Status", "Message"};
            DefaultTableModel appModel = new DefaultTableModel(cols, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };

            for (Application a : apps) {
                // Truncate message for preview
                String preview = a.getMessage().length() > 45
                        ? a.getMessage().substring(0, 45) + "..."
                        : a.getMessage();
                appModel.addRow(new Object[]{
                        a.getApplicationId(),
                        a.getApplicantEmail(),   // ← uses getApplicantEmail() not getApplicantUsername()
                        a.getDateApplied(),
                        a.getStatus(),
                        preview
                });
            }

            JTable appTable = new JTable(appModel);
            appTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            appTable.setRowHeight(26);
            appTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            appTable.getTableHeader().setBackground(MAROON);
            appTable.getTableHeader().setForeground(WHITE);

            // Color-code Status column in applicant table
            appTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(
                        JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                    super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                    if (!sel) {
                        setBackground(r % 2 == 0 ? WHITE : new Color(252, 245, 245));
                        if (c == 3) {
                            String st = (String) appModel.getValueAt(r, 3);
                            switch (st) {
                                case "ACCEPTED" -> { setForeground(GREEN); setFont(new Font("Segoe UI", Font.BOLD, 12)); }
                                case "REJECTED" -> { setForeground(RED);   setFont(new Font("Segoe UI", Font.BOLD, 12)); }
                                default         -> { setForeground(AMBER); setFont(new Font("Segoe UI", Font.BOLD, 12)); }
                            }
                        } else {
                            setForeground(Color.DARK_GRAY);
                            setFont(new Font("Segoe UI", Font.PLAIN, 12));
                        }
                    }
                    return this;
                }
            });

            dialog.add(new JScrollPane(appTable), BorderLayout.CENTER);

            // Accept / Reject buttons
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
            btnPanel.setBackground(LIGHT_BG);
            btnPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));

            JButton acceptBtn = makeButton("✓  Accept", GREEN, WHITE);
            JButton rejectBtn = makeButton("✗  Reject", RED,   WHITE);

            acceptBtn.addActionListener(e -> {
                int r = appTable.getSelectedRow();
                if (r < 0) { JOptionPane.showMessageDialog(dialog, "Select an applicant first."); return; }
                String appId = (String) appModel.getValueAt(r, 0);
                DataStore.updateApplicationStatus(appId, "ACCEPTED");
                appModel.setValueAt("ACCEPTED", r, 3);
                JOptionPane.showMessageDialog(dialog, "Applicant accepted!");
                refresh();
            });

            rejectBtn.addActionListener(e -> {
                int r = appTable.getSelectedRow();
                if (r < 0) { JOptionPane.showMessageDialog(dialog, "Select an applicant first."); return; }
                String appId = (String) appModel.getValueAt(r, 0);
                DataStore.updateApplicationStatus(appId, "REJECTED");
                appModel.setValueAt("REJECTED", r, 3);
                refresh();
            });

            JButton closeDialogBtn = makeButton("Close", new Color(120,120,120), WHITE);
            closeDialogBtn.addActionListener(e -> dialog.dispose());

            btnPanel.add(rejectBtn);
            btnPanel.add(acceptBtn);
            btnPanel.add(closeDialogBtn);
            dialog.add(btnPanel, BorderLayout.SOUTH);
        }

        dialog.setVisible(true);
    }

    // ─── CLOSE LISTING ────────────────────────────────────────────────────────
    /**
     * Changes selected listing's status from OPEN → CLOSED.
     * Closed listings no longer appear in BrowsePanel.
     */
    private void closeListing() {
        int row = getSelectedRow();
        if (row < 0) return;

        String listingId = (String) tableModel.getValueAt(row, 0);
        String status    = (String) tableModel.getValueAt(row, 6);

        if ("CLOSED".equals(status)) {
            JOptionPane.showMessageDialog(this, "This listing is already closed.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Close this listing? It will no longer appear to other students.",
                "Close Listing", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            JobListing jl = DataStore.findListing(listingId);
            if (jl != null) {
                jl.setStatus("CLOSED");
                DataStore.saveAll();
            }
            refresh();
        }
    }

    // ─── DELETE LISTING ───────────────────────────────────────────────────────
    /**
     * Permanently deletes the selected listing from DataStore and the txt file.
     */
    private void deleteListing() {
        int row = getSelectedRow();
        if (row < 0) return;

        String listingId = (String) tableModel.getValueAt(row, 0);
        String title     = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Permanently delete \"" + title + "\"?\nThis cannot be undone.",
                "Delete Listing", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            DataStore.removeListing(listingId);
            refresh();
            JOptionPane.showMessageDialog(this, "Listing deleted.");
        }
    }

    // ─── HELPER: Get selected row safely ──────────────────────────────────────
    private int getSelectedRow() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a listing from the table first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
        }
        return row;
    }

    // ─── HELPER: Button factory ────────────────────────────────────────────────
    private JButton makeButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 34));
        return btn;
    }
}
