package DashboardAndApplicationManagement;

// ── ALL REQUIRED IMPORTS ──────────────────────────────────────────────────────
// These were MISSING from the original MainFrame.java — that's why
// the class couldn't be found as DashboardAndApplicationManagement.MainFrame
import DataAndModels.DataStore;
import DataAndModels.User;
import BrowseAndPostFeatures.BrowsePanel;
import BrowseAndPostFeatures.PostPanel;
import LoginAndRegistration.LoginFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {

    private static final Color MAROON       = new Color(128, 0, 0);
    private static final Color MAROON_DARK  = new Color(90, 0, 0);
    private static final Color LIGHT_BG     = new Color(248, 245, 245);
    private static final Color WHITE        = Color.WHITE;
    private static final Color GOLD         = new Color(255, 215, 0);
    private static final Color BORDER_COLOR = new Color(200, 180, 180);

    // User info — passed in from LoginFrame as 6 individual Strings
    private final String fullName;
    private final String role;
    private final String email;
    private final String college;
    private final String course;
    private final String idNumber;

    private JPanel     contentArea;
    private CardLayout cardLayout;

    // CardLayout panel name constants
    public static final String PANEL_BROWSE  = "BROWSE";
    public static final String PANEL_POST    = "POST";
    public static final String PANEL_MYLIST  = "MY_LISTINGS";
    public static final String PANEL_MYAPPS  = "MY_APPLICATIONS";

    // Sidebar nav buttons
    private JButton btnBrowse, btnPost, btnMyListings, btnMyApps;

    /**
     * Called by LoginFrame after successful login:
     *   new MainFrame(user.getFullName(), user.getRole(), user.getEmail(),
     *                 user.getCollege(), user.getCourse(), user.getIdNumber())
     *                 .setVisible(true);
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
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 550));

        buildUI();
        showPanel(PANEL_BROWSE); // default tab is Browse
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        add(buildHeader(),  BorderLayout.NORTH);
        add(buildSidebar(), BorderLayout.WEST);

        cardLayout  = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(LIGHT_BG);

        // Get the User object from DataStore using the email
        // so BrowsePanel and PostPanel receive a proper User object
        User userObj = DataStore.findUserByEmail(email);

        contentArea.add(new BrowsePanel(userObj),          PANEL_BROWSE);
        contentArea.add(new PostPanel(userObj),            PANEL_POST);
        contentArea.add(new MyListingPanel(email),         PANEL_MYLIST);
        contentArea.add(new MyApplicationPanel(email),     PANEL_MYAPPS);

        add(contentArea, BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MAROON);
        header.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

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

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);

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

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(MAROON_DARK);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));

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

        String collegeShort = college.length() > 28
                ? college.substring(0, 25) + "..." : college;
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

        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setForeground(new Color(160, 60, 60));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        sidebar.add(avatarPanel);
        sidebar.add(sep);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        btnBrowse     = navButton("🔍  Browse Jobs",     PANEL_BROWSE);
        btnPost       = navButton("➕  Post a Job",       PANEL_POST);
        btnMyListings = navButton("📋  My Listings",      PANEL_MYLIST);
        btnMyApps     = navButton("📩  My Applications",  PANEL_MYAPPS);

        sidebar.add(btnBrowse);
        sidebar.add(Box.createRigidArea(new Dimension(0, 4)));
        sidebar.add(btnPost);
        sidebar.add(Box.createRigidArea(new Dimension(0, 4)));
        sidebar.add(btnMyListings);
        sidebar.add(Box.createRigidArea(new Dimension(0, 4)));
        sidebar.add(btnMyApps);

        sidebar.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("CCC102 - Cats on a Quest", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        footer.setForeground(new Color(160, 80, 80));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(footer);
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));

        return sidebar;
    }

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

    public void showPanel(String panelName) {
        cardLayout.show(contentArea, panelName);

        // Reset all buttons to default style
        for (JButton b : new JButton[]{btnBrowse, btnPost, btnMyListings, btnMyApps}) {
            b.setBackground(MAROON_DARK);
            b.setForeground(new Color(240, 210, 210));
            b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        }

        // Highlight the active button in gold
        JButton active;
        switch (panelName) {
            case PANEL_POST:   active = btnPost;       break;
            case PANEL_MYLIST: active = btnMyListings; break;
            case PANEL_MYAPPS: active = btnMyApps;     break;
            default:           active = btnBrowse;     break;
        }
        active.setBackground(GOLD);
        active.setForeground(MAROON_DARK);
        active.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Refresh the visible panel so it shows latest data
        for (Component c : contentArea.getComponents()) {
            if (c.isVisible()) {
                if (c instanceof BrowsePanel)
                    ((BrowsePanel) c).refresh();
                else if (c instanceof MyListingPanel)
                    ((MyListingPanel) c).refresh();
                else if (c instanceof MyApplicationPanel)
                    ((MyApplicationPanel) c).refresh();
            }
        }
    }

    public String getEmail()    { return email; }
    public String getFullName() { return fullName; }
    public String getRole()     { return role; }

    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION
        );
        if (choice == JOptionPane.YES_OPTION) {
            DataStore.logout();
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        }
    }
