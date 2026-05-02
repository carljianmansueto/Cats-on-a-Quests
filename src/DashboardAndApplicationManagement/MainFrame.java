import LoginAndRegistration.LoginFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * ═══════════════════════════════════════════════════════════════════
 *  MainFrame.java
 *  MEMBER 4 — Dashboard & Application Management
 *  Package: DashboardAndApplicationManagement
 *
 *  This is the MAIN WINDOW that opens after a successful login.
 *  LoginFrame creates this by passing user details as Strings:
 *
 *      new MainFrame(fullName, role, email, college, course, idNumber)
 *
 *  NOTE: Your group's User class does NOT have a username field.
 *  Identity is tracked by EMAIL. All DataStore calls use email.
 *
 *  LAYOUT:
 *  ┌──────────────────────────────────────────────────────────────┐
 *  │  HEADER  (maroon top bar — app name + user info + logout)    │
 *  ├──────────────┬───────────────────────────────────────────────┤
 *  │              │                                               │
 *  │   SIDEBAR    │       CONTENT AREA  (CardLayout)              │
 *  │  (200px wide)│  BrowsePanel / PostPanel /                    │
 *  │              │  MyListingPanel / MyApplicationPanel          │
 *  │              │                                               │
 *  └──────────────┴───────────────────────────────────────────────┘
 *
 *  KEY CONCEPT — CardLayout:
 *    All 4 panels are loaded once and stacked. Only ONE shows at a time.
 *    Clicking a sidebar button calls showPanel() to flip the visible one.
 * ═══════════════════════════════════════════════════════════════════
 */
public class MainFrame extends JFrame {

    // ─── COLOR PALETTE ────────────────────────────────────────────────────────
    // Matched exactly to your group's LoginFrame / RegisterFrame / BrowsePanel
    private static final Color MAROON       = new Color(128, 0, 0);
    private static final Color MAROON_DARK  = new Color(90, 0, 0);
    private static final Color LIGHT_BG     = new Color(248, 245, 245);
    private static final Color WHITE        = Color.WHITE;
    private static final Color GOLD         = new Color(255, 215, 0);
    private static final Color BORDER_COLOR = new Color(200, 180, 180);

    // ─── USER INFO FIELDS ─────────────────────────────────────────────────────
    // Your group's LoginFrame passes these individually (not a User object).
    // We store them here so all panels can access them.
    private final String fullName;   // e.g. "Juan Dela Cruz"
    private final String role;       // e.g. "STUDENT"
    private final String email;      // e.g. "juan.delacruz@g.msuiit.edu.ph"
    private final String college;    // e.g. "College of Engineering"
    private final String course;     // e.g. "BS Computer Engineering"
    private final String idNumber;   // e.g. "2021-00001"

    // ─── LAYOUT FIELDS ────────────────────────────────────────────────────────
    private JPanel     contentArea;  // right side — holds all 4 panels
    private CardLayout cardLayout;   // controls which panel is visible

    // Panel name constants — used by CardLayout and sidebar buttons
    public static final String PANEL_BROWSE  = "BROWSE";
    public static final String PANEL_POST    = "POST";
    public static final String PANEL_MYLIST  = "MY_LISTINGS";
    public static final String PANEL_MYAPPS  = "MY_APPLICATIONS";

    // Sidebar nav buttons — stored as fields so showPanel() can highlight them
    private JButton btnBrowse, btnPost, btnMyListings, btnMyApps;

    // ─── CONSTRUCTOR ──────────────────────────────────────────────────────────
    /**
     * Called by LoginFrame after a successful login:
     *
     *   new MainFrame(
     *       user.getFullName(),
     *       user.getRole(),
     *       user.getEmail(),
     *       user.getCollege(),
     *       user.getCourse(),
     *       user.getIdNumber()
     *   ).setVisible(true);
     *
     * We receive individual Strings (not a User object) because that is
     * exactly how your LoginFrame constructs this window.
     */
    public MainFrame(String fullName, String role, String email,
                     String college, String course, String idNumber) {
        this.fullName = fullName;
        this.role     = role;
        this.email    = email;
        this.college  = college;
        this.course   = course;
        this.idNumber = idNumber;

        setTitle("Cats on a Quest - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 680);
        setLocationRelativeTo(null);          // center on screen
        setMinimumSize(new Dimension(900, 550));

        buildUI();
        showPanel(PANEL_BROWSE);              // open on Browse tab by default
    }

    // ─── BUILD UI ─────────────────────────────────────────────────────────────
    /**
     * Assembles the full window.
     * Uses BorderLayout: NORTH=header, WEST=sidebar, CENTER=content panels.
     */
    private void buildUI() {
        setLayout(new BorderLayout());
        add(buildHeader(),  BorderLayout.NORTH);
        add(buildSidebar(), BorderLayout.WEST);

        // ── Content area (CardLayout holds all 4 panels) ──────────────────────
        cardLayout  = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(LIGHT_BG);

        // Each panel gets the user's email so it can call DataStore correctly.
        // BrowsePanel and PostPanel use your group's User object — we rebuild
        // a DataStore.getCurrentUser() call to get it, or pass email directly.
        // We pass a DataAndModels.User rebuilt from what we have:
        DataAndModels.User userObj = DataStore.findUserByEmail(email);

        contentArea.add(new BrowsePanel(userObj),         PANEL_BROWSE);
        contentArea.add(new PostPanel(userObj),           PANEL_POST);
        contentArea.add(new MyListingPanel(email),        PANEL_MYLIST);
        contentArea.add(new MyApplicationPanel(email),    PANEL_MYAPPS);

        add(contentArea, BorderLayout.CENTER);
    }

    // ─── HEADER ───────────────────────────────────────────────────────────────
    /**
     * Builds the maroon top bar spanning the full window width.
     * Left: app name + subtitle
     * Right: logged-in user info + Logout button
     */
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MAROON);
        header.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        // ── Left: App identity ───────────────────────────────────────────────
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel appName = new JLabel("Cats on a Quest!");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 20));
        appName.setForeground(GOLD);

        JLabel appSub = new JLabel("MSU-IIT Job & Service Finder");
        appSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        appSub.setForeground(new Color(255, 220, 220));

        left.add(appName);
        left.add(appSub);
        header.add(left, BorderLayout.WEST);

        // ── Right: User info + Logout ────────────────────────────────────────
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);

        // Show user's name, role, and email
        JPanel userInfo = new JPanel();
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));
        userInfo.setOpaque(false);

        JLabel nameLabel = new JLabel(fullName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setForeground(WHITE);
        nameLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel roleLabel = new JLabel(role + "  |  " + email);
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        roleLabel.setForeground(new Color(255, 220, 220));
        roleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        userInfo.add(nameLabel);
        userInfo.add(roleLabel);

        // Logout button — gold border to stand out on maroon
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setBackground(MAROON_DARK);
        logoutBtn.setForeground(GOLD);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createLineBorder(GOLD, 1));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setPreferredSize(new Dimension(90, 34));
        logoutBtn.addActionListener(e -> handleLogout());

        right.add(userInfo);
        right.add(logoutBtn);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    // ─── SIDEBAR ──────────────────────────────────────────────────────────────
    /**
     * Builds the dark left sidebar with:
     *   - User avatar + name + role
     *   - 4 navigation buttons
     *   - MSU-IIT footer
     *
     * BoxLayout.Y_AXIS stacks everything top-to-bottom.
     * Box.createVerticalGlue() pushes the footer to the very bottom.
     */
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(MAROON_DARK);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(200, 0)); // fixed 200px width
        sidebar.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));

        // ── Avatar section ───────────────────────────────────────────────────
        JPanel avatarPanel = new JPanel();
        avatarPanel.setOpaque(false);
        avatarPanel.setLayout(new BoxLayout(avatarPanel, BoxLayout.Y_AXIS));
        avatarPanel.setBorder(BorderFactory.createEmptyBorder(0, 12, 16, 12));
        avatarPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        JLabel catIcon = new JLabel("🐱", SwingConstants.CENTER);
        catIcon.setFont(new Font("SansSerif", Font.PLAIN, 36));
        catIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(fullName, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        nameLabel.setForeground(WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel roleLabel = new JLabel(role, SwingConstants.CENTER);
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        roleLabel.setForeground(GOLD);
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Show college (truncated if too long for the sidebar width)
        String collegeShort = college.length() > 28
                ? college.substring(0, 25) + "..."
                : college;
        JLabel collegeLabel = new JLabel(collegeShort, SwingConstants.CENTER);
        collegeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        collegeLabel.setForeground(new Color(220, 180, 180));
        collegeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        avatarPanel.add(catIcon);
        avatarPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        avatarPanel.add(nameLabel);
        avatarPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        avatarPanel.add(roleLabel);
        avatarPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        avatarPanel.add(collegeLabel);

        // Thin divider between avatar and nav buttons
        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setForeground(new Color(160, 60, 60));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        sidebar.add(avatarPanel);
        sidebar.add(sep);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        // ── Navigation buttons ────────────────────────────────────────────────
        btnBrowse     = navButton("🔍  Browse Jobs",       PANEL_BROWSE);
        btnPost       = navButton("➕  Post a Job",         PANEL_POST);
        btnMyListings = navButton("📋  My Listings",        PANEL_MYLIST);
        btnMyApps     = navButton("📩  My Applications",    PANEL_MYAPPS);

        sidebar.add(btnBrowse);
        sidebar.add(Box.createRigidArea(new Dimension(0, 4)));
        sidebar.add(btnPost);
        sidebar.add(Box.createRigidArea(new Dimension(0, 4)));
        sidebar.add(btnMyListings);
        sidebar.add(Box.createRigidArea(new Dimension(0, 4)));
        sidebar.add(btnMyApps);

        // Spring — pushes footer to the bottom
        sidebar.add(Box.createVerticalGlue());

        // ── Footer ────────────────────────────────────────────────────────────
        JLabel footer = new JLabel("CCC102 - Cats on a Quest", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        footer.setForeground(new Color(160, 80, 80));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(footer);
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));

        return sidebar;
    }

    // ─── NAV BUTTON HELPER ────────────────────────────────────────────────────
    /**
     * Creates one styled sidebar navigation button.
     * Clicking it calls showPanel(panelName) to switch the visible content.
     * Hover effect: slightly lighter maroon background while hovering.
     */
    private JButton navButton(String text, String panelName) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setForeground(new Color(240, 210, 210));
        btn.setBackground(MAROON_DARK);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);

        btn.addActionListener(e -> showPanel(panelName));

        // Hover: lighter maroon, but don't override the gold "active" state
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!btn.getBackground().equals(GOLD))
                    btn.setBackground(MAROON);
            }
            public void mouseExited(MouseEvent e) {
                if (!btn.getBackground().equals(GOLD))
                    btn.setBackground(MAROON_DARK);
            }
        });

        return btn;
    }

    // ─── SHOW PANEL ───────────────────────────────────────────────────────────
    /**
     * Switches the visible content panel and updates sidebar button highlight.
     *
     * Steps:
     *   1. CardLayout flips to the named panel
     *   2. Reset all nav buttons to default dark style
     *   3. Highlight the active button in gold
     *   4. Call refresh() on the now-visible panel (if it supports it)
     *
     * Called by:
     *   - Sidebar buttons (navButton's ActionListener)
     *   - MyListingPanel's "+ Post New" button
     *   - Any panel that needs to redirect the user
     */
    public void showPanel(String panelName) {
        // 1. Flip the CardLayout to show the named panel
        cardLayout.show(contentArea, panelName);

        // 2. Reset all sidebar buttons to default style
        for (JButton b : new JButton[]{btnBrowse, btnPost, btnMyListings, btnMyApps}) {
            b.setBackground(MAROON_DARK);
            b.setForeground(new Color(240, 210, 210));
            b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        }

        // 3. Highlight the active button with gold background
        JButton active;
        switch (panelName) {
            case PANEL_POST:    active = btnPost;       break;
            case PANEL_MYLIST:  active = btnMyListings; break;
            case PANEL_MYAPPS:  active = btnMyApps;     break;
            default:            active = btnBrowse;     break;
        }
        active.setBackground(GOLD);
        active.setForeground(MAROON_DARK);
        active.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // 4. Call refresh() on the now-visible panel so it shows latest data
        //    We loop through contentArea's children, find the visible one,
        //    and call refresh() if the panel has that method.
        for (Component c : contentArea.getComponents()) {
            if (c.isVisible()) {
                if (c instanceof BrowsePanel)         ((BrowsePanel) c).refresh();
                else if (c instanceof MyListingPanel) ((MyListingPanel) c).refresh();
                else if (c instanceof MyApplicationPanel) ((MyApplicationPanel) c).refresh();
            }
        }
    }

    // ─── GETTERS (used by child panels) ───────────────────────────────────────
    public String getEmail()    { return email; }
    public String getFullName() { return fullName; }
    public String getRole()     { return role; }

    // ─── LOGOUT ───────────────────────────────────────────────────────────────
    /**
     * Handles the Logout button.
     * Asks for confirmation, then clears the DataStore session,
     * closes this window, and opens a fresh LoginFrame.
     */
    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            DataStore.logout();                    // clears currentUser in DataStore
            dispose();                             // close and destroy MainFrame
            new LoginFrame().setVisible(true);     // open a fresh Login window
        }
    }
}
