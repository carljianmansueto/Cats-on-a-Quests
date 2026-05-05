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
 *  MyApplicationPanel.java
 *  MEMBER 4 — Package: DashboardAndApplicationManagement
 *
 *  Shows all job applications that the logged-in student submitted.
 *  Each row shows which listing they applied to and its current status:
 *    PENDING  → waiting for the poster to decide
 *    ACCEPTED → poster accepted them (shown in green)
 *    REJECTED → poster rejected them (shown in red)
 *
 *  The student can:
 *    - See a summary count (Pending / Accepted / Rejected)
 *    - Double-click a row to read their full message + listing details
 *    - Click "Browse More Jobs" to go back to BrowsePanel
 *    - Refresh to see latest status updates
 *
 *  KEY DIFFERENCES matched to your zip:
 *    - Uses Application.getApplicantEmail() (not getApplicantUsername)
 *    - Uses DataStore.getApplicationsByUser(email)
 *    - No UITheme — colors defined directly like rest of your group's code
 *    - Package is DashboardAndApplicationManagement
 * ═══════════════════════════════════════════════════════════════════
 */
public class MyApplicationPanel extends JPanel {

    // ─── COLOR PALETTE ────────────────────────────────────────────────────────
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
    private final String userEmail;       // logged-in user's email (unique ID)

    private JTable            table;
    private DefaultTableModel tableModel;
    private JLabel            countLabel;
    private JLabel            summaryLabel; // shows "Pending: 2 Accepted: 1 Rejected: 0"

    // Column headers — order must match addRow() in refresh()
    private static final String[] COLUMNS = {
            "App ID", "Job Title", "Category", "Pay", "Date Applied", "Status"
    };

    // ─── CONSTRUCTOR ──────────────────────────────────────────────────────────
    /**
     * Called by MainFrame:
     *   contentArea.add(new MyApplicationPanel(email), PANEL_MYAPPS);
     *
     * @param userEmail  The logged-in user's email address
     */
    public MyApplicationPanel(String userEmail) {
        this.userEmail = userEmail;
        setLayout(new BorderLayout());
        setBackground(LIGHT_BG);
        buildUI();
        refresh();
    }

    // ─── BUILD UI ─────────────────────────────────────────────────────────────
    private void buildUI() {

        // ── NORTH: Maroon top bar ─────────────────────────────────────────────
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(MAROON);
        topBar.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel titleLabel = new JLabel("My Applications");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(WHITE);
        topBar.add(titleLabel, BorderLayout.WEST);

        countLabel = new JLabel("");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        countLabel.setForeground(new Color(255, 220, 220));
        topBar.add(countLabel, BorderLayout.EAST);

        // Summary bar just below the top bar (Pending / Accepted / Rejected counts)
        summaryLabel = new JLabel("  ");
        summaryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        summaryLabel.setForeground(Color.DARK_GRAY);
        summaryLabel.setBorder(BorderFactory.createEmptyBorder(6, 20, 6, 20));

        JPanel summaryBar = new JPanel(new BorderLayout());
        summaryBar.setBackground(new Color(245, 238, 238));
        summaryBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        summaryBar.add(summaryLabel, BorderLayout.WEST);

        JPanel northStack = new JPanel(new BorderLayout());
        northStack.add(topBar,     BorderLayout.NORTH);
        northStack.add(summaryBar, BorderLayout.SOUTH);
        add(northStack, BorderLayout.NORTH);

        // ── CENTER: Table ─────────────────────────────────────────────────────
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // read-only table
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(180, 60, 60));
        table.setSelectionForeground(WHITE);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(MAROON);
        header.setForeground(WHITE);
        header.setReorderingAllowed(false);

        // Column widths
        int[] widths = {70, 220, 90, 100, 100, 90};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // Custom renderer: alternating rows + color-coded status column
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean selected,
                    boolean focused, int row, int col) {

                super.getTableCellRendererComponent(t, val, selected, focused, row, col);

                if (!selected) {
                    String status = (String) tableModel.getValueAt(row, 5);

                    // Color entire row background based on status
                    if ("ACCEPTED".equals(status)) {
                        setBackground(col == 5 ? new Color(220, 245, 220)
                                : (row % 2 == 0 ? WHITE : new Color(240, 252, 240)));
                        setForeground(col == 5 ? GREEN : Color.DARK_GRAY);
                    } else if ("REJECTED".equals(status)) {
                        setBackground(col == 5 ? new Color(252, 228, 228)
                                : (row % 2 == 0 ? WHITE : new Color(252, 240, 240)));
                        setForeground(col == 5 ? RED : Color.DARK_GRAY);
                    } else {
                        // PENDING
                        setBackground(row % 2 == 0 ? WHITE : new Color(252, 245, 245));
                        setForeground(col == 5 ? AMBER : Color.DARK_GRAY);
                    }

                    if (col == 5) setFont(new Font("Segoe UI", Font.BOLD, 12));
                    else          setFont(new Font("Segoe UI", Font.PLAIN, 12));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        });

        // Double-click → open full details popup
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewDetails();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        scroll.getViewport().setBackground(WHITE);
        add(scroll, BorderLayout.CENTER);

        // ── SOUTH: Buttons ────────────────────────────────────────────────────
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomBar.setBackground(LIGHT_BG);
        bottomBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));

        JLabel hint = new JLabel("Double-click a row to view full application details");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(Color.GRAY);
        bottomBar.add(hint);
        bottomBar.add(Box.createHorizontalStrut(20));

        JButton detailBtn  = makeButton("View Details",     MAROON, GOLD);
        JButton browseBtn  = makeButton("Browse More Jobs", GOLD,   MAROON);
        JButton refreshBtn = makeButton("↻  Refresh",       new Color(120, 120, 120), WHITE);

        detailBtn.addActionListener(e  -> viewDetails());
        refreshBtn.addActionListener(e -> refresh());
        browseBtn.addActionListener(e  -> {
            // Navigate to BrowsePanel via MainFrame
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w instanceof MainFrame) {
                ((MainFrame) w).showPanel(MainFrame.PANEL_BROWSE);
            }
        });

        bottomBar.add(detailBtn);
        bottomBar.add(browseBtn);
        bottomBar.add(refreshBtn);
        add(bottomBar, BorderLayout.SOUTH);
    }

    // ─── REFRESH ──────────────────────────────────────────────────────────────
    /**
     * Reloads the table with the latest applications from DataStore.
     * Called by MainFrame.showPanel() every time this panel becomes visible.
     *
     * DATA FLOW:
     *   DataStore.getApplicationsByUser(userEmail)
     *     → returns ArrayList<Application> for this user's email
     *     → for each Application, we look up the matching JobListing
     *       using DataStore.findListing(listingId)
     *     → count Pending / Accepted / Rejected for the summary bar
     */
    public void refresh() {
        tableModel.setRowCount(0); // clear existing rows

        ArrayList<Application> myApps = DataStore.getApplicationsByUser(userEmail);

        int pending = 0, accepted = 0, rejected = 0;

        for (Application a : myApps) {
            // Look up the listing this application is for
            // DataStore.findListing() searches listings ArrayList by ID
            JobListing jl = DataStore.findListing(a.getListingId());

            String title    = jl != null ? jl.getTitle()    : "(Listing deleted)";
            String category = jl != null ? jl.getCategory() : "—";
            String pay      = "—";
            if (jl != null) {
                pay = jl.getPayType().equals("PER_HOUR")
                        ? "P" + jl.getPayRate() + "/hr"
                        : "P" + jl.getPayRate() + " fixed";
            }

            // addRow() — order must match COLUMNS array exactly
            tableModel.addRow(new Object[]{
                    a.getApplicationId(),   // col 0
                    title,                  // col 1
                    category,               // col 2
                    pay,                    // col 3
                    a.getDateApplied(),     // col 4
                    a.getStatus()           // col 5 (color-coded by renderer)
            });

            // Count for summary bar
            switch (a.getStatus()) {
                case "ACCEPTED" -> accepted++;
                case "REJECTED" -> rejected++;
                default         -> pending++;
            }
        }

        countLabel.setText(myApps.size() + " application(s)  ");
        summaryLabel.setText(
                "  ⏳ Pending: " + pending +
                        "     ✅ Accepted: " + accepted +
                        "     ❌ Rejected: " + rejected
        );
    }

    // ─── VIEW DETAILS ─────────────────────────────────────────────────────────
    /**
     * Opens a popup showing the full details of the selected application:
     *   - Application ID, date applied, status
     *   - Job title, pay, location, posted by
     *   - The full message the student wrote when applying
     *
     * The header color changes based on status:
     *   ACCEPTED → green header   REJECTED → red header   PENDING → maroon
     */
    private void viewDetails() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select an application first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String appId  = (String) tableModel.getValueAt(row, 0);
        String status = (String) tableModel.getValueAt(row, 5);

        // Find the full Application object from DataStore
        Application found = null;
        for (Application a : DataStore.getApplicationsByUser(userEmail)) {
            if (a.getApplicationId().equals(appId)) { found = a; break; }
        }
        if (found == null) return;

        JobListing jl = DataStore.findListing(found.getListingId());

        // Header color depends on status
        Color headerColor = switch (status) {
            case "ACCEPTED" -> GREEN;
            case "REJECTED" -> RED;
            default         -> MAROON;
        };

        // ── Build the details popup ───────────────────────────────────────────
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                "Application Details", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(480, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Dialog header
        JPanel dHeader = new JPanel(new BorderLayout());
        dHeader.setBackground(headerColor);
        dHeader.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        String jobTitle = jl != null ? jl.getTitle() : "(Listing deleted)";
        JLabel dTitle = new JLabel(jobTitle);
        dTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dTitle.setForeground(WHITE);
        JLabel dStatus = new JLabel(status);
        dStatus.setFont(new Font("Segoe UI", Font.BOLD, 13));
        dStatus.setForeground(GOLD);
        dHeader.add(dTitle,  BorderLayout.WEST);
        dHeader.add(dStatus, BorderLayout.EAST);
        dialog.add(dHeader, BorderLayout.NORTH);

        // Details body
        JPanel body = new JPanel();
        body.setBackground(new Color(252, 248, 248));
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        body.add(detailRow("Application ID", found.getApplicationId()));
        body.add(Box.createRigidArea(new Dimension(0, 6)));
        body.add(detailRow("Date Applied",   found.getDateApplied()));
        body.add(Box.createRigidArea(new Dimension(0, 6)));
        body.add(detailRow("Status",         found.getStatus()));
        body.add(Box.createRigidArea(new Dimension(0, 6)));

        if (jl != null) {
            String pay = jl.getPayType().equals("PER_HOUR")
                    ? "P" + jl.getPayRate() + " per hour"
                    : "P" + jl.getPayRate() + " (fixed)";
            body.add(detailRow("Pay",        pay));
            body.add(Box.createRigidArea(new Dimension(0, 6)));
            body.add(detailRow("Location",   jl.getLocation()));
            body.add(Box.createRigidArea(new Dimension(0, 6)));
            body.add(detailRow("Posted By",  jl.getPostedBy()));
            body.add(Box.createRigidArea(new Dimension(0, 6)));
        }

        // Full message the student wrote
        body.add(Box.createRigidArea(new Dimension(0, 8)));
        JLabel msgTitle = new JLabel("Your message:");
        msgTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        msgTitle.setForeground(MAROON);
        msgTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(msgTitle);
        body.add(Box.createRigidArea(new Dimension(0, 4)));

        JTextArea msgArea = new JTextArea(found.getMessage());
        msgArea.setEditable(false);
        msgArea.setLineWrap(true);
        msgArea.setWrapStyleWord(true);
        msgArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        msgArea.setBackground(new Color(245, 240, 240));
        msgArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        JScrollPane msgScroll = new JScrollPane(msgArea);
        msgScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        msgScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(msgScroll);

        // Status-specific message
        body.add(Box.createRigidArea(new Dimension(0, 10)));
        if ("ACCEPTED".equals(status)) {
            JLabel congrats = new JLabel("🎉 Congratulations! You were accepted. Contact the poster to get started.");
            congrats.setFont(new Font("Segoe UI", Font.BOLD, 12));
            congrats.setForeground(GREEN);
            congrats.setAlignmentX(Component.LEFT_ALIGNMENT);
            body.add(congrats);
        } else if ("REJECTED".equals(status)) {
            JLabel sorry = new JLabel("This application was not accepted. Don't give up — keep applying!");
            sorry.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            sorry.setForeground(RED);
            sorry.setAlignmentX(Component.LEFT_ALIGNMENT);
            body.add(sorry);
        } else {
            JLabel pending = new JLabel("⏳ Your application is pending review by the poster.");
            pending.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            pending.setForeground(AMBER);
            pending.setAlignmentX(Component.LEFT_ALIGNMENT);
            body.add(pending);
        }

        dialog.add(new JScrollPane(body), BorderLayout.CENTER);

        // Close button
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(new Color(252, 248, 248));
        btnPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));
        JButton closeBtn = makeButton("Close", MAROON, GOLD);
        closeBtn.addActionListener(e -> dialog.dispose());
        btnPanel.add(closeBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // ─── HELPERS ──────────────────────────────────────────────────────────────

    /** Creates one label: value row for the details popup */
    private JPanel detailRow(String label, String value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));

        JLabel lbl = new JLabel(label + ":");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(MAROON);
        lbl.setPreferredSize(new Dimension(100, 20));

        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        val.setForeground(Color.DARK_GRAY);

        row.add(lbl);
        row.add(val);
        return row;
    }

    /** Creates a consistently styled button */
    private JButton makeButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(150, 34));
        return btn;
    }
}
