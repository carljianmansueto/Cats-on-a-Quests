package LoginAndRegistration;

import DataAndModels.User;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    // SIMPLE IN-MEMORY STORAGE (no files, no database)
    private static ArrayList<User> users = new ArrayList<>();

    // MSU-IIT Theme Colors
    private static final Color MAROON = new Color(128, 0, 0);
    private static final Color DARK_MAROON = new Color(102, 0, 0);
    private static final Color LIGHT_MAROON_BG = new Color(255, 240, 240);
    private static final Color GOLD = new Color(255, 215, 0);

    public LoginFrame() {
        setTitle("🐱 Cats on a Quest - Login | MSU-IIT");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 420);
        setLocationRelativeTo(null);
        setResizable(false);

        // Add demo users for testing
        seedDemoUsers();

        initUI();
    }

    private void seedDemoUsers() {
        // Demo accounts so you can test login immediately
        users.add(new User("juan.delacruz", "password123", "Juan Dela Cruz",
                "juan.delacruz@g.msuiit.edu.ph", "STUDENT", "College of Engineering", "2021-00001"));
        users.add(new User("maria.santos", "password123", "Maria Santos",
                "maria.santos@g.msuiit.edu.ph", "STUDENT", "College of Science", "2021-00002"));
        users.add(new User("prof.reyes", "faculty123", "Dr. Ana Reyes",
                "ana.reyes@msuiit.edu.ph", "FACULTY", "College of Engineering", "FAC-0001"));
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(LIGHT_MAROON_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("🐱 Cats on a Quest 🐱", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(MAROON);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel("MSU-IIT Student Job & Service Finder", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(DARK_MAROON);
        gbc.gridy = 1;
        mainPanel.add(subtitleLabel, gbc);

        // Spacer
        gbc.gridy = 2;
        mainPanel.add(Box.createVerticalStrut(10), gbc);

        // Username
        gbc.gridwidth = 1;
        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        usernameLabel.setForeground(MAROON);
        mainPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        mainPanel.add(usernameField, gbc);

        // Password
        gbc.gridy = 4;
        gbc.gridx = 0;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passwordLabel.setForeground(MAROON);
        mainPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        mainPanel.add(passwordField, gbc);

        // Demo note
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JLabel noteLabel = new JLabel("Demo: juan.delacruz / password123  |  maria.santos / password123", SwingConstants.CENTER);
        noteLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        noteLabel.setForeground(Color.GRAY);
        mainPanel.add(noteLabel, gbc);

        // Message
        gbc.gridy = 6;
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        messageLabel.setForeground(Color.RED);
        mainPanel.add(messageLabel, gbc);

        // Buttons
        gbc.gridy = 7;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        loginButton = new JButton("🐱 Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(MAROON);
        loginButton.setForeground(GOLD);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(120, 40));
        loginButton.setBorder(BorderFactory.createLineBorder(GOLD, 2));

        JButton registerButton = new JButton("📝 Register");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setBackground(DARK_MAROON);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setPreferredSize(new Dimension(120, 40));

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        mainPanel.add(buttonPanel, gbc);

        // Footer
        gbc.gridy = 8;
        JLabel footerLabel = new JLabel("🏛️ MSU-Iligan Institute of Technology | Cats on a Quest © 2024", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        footerLabel.setForeground(MAROON);
        mainPanel.add(footerLabel, gbc);

        // Button Actions
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> {
            dispose();
            new RegisterFrame().setVisible(true);
        });

        add(mainPanel);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("❌ Please enter username and password", Color.RED);
            return;
        }

        // SIMPLE LOGIN - just loop through the list
        User loggedInUser = null;
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                loggedInUser = u;
                break;
            }
        }

        if (loggedInUser != null) {
            showMessage("✓ Welcome, " + loggedInUser.getFullName() + "!", new Color(0, 128, 0));
            JOptionPane.showMessageDialog(this, "Login successful!\nWelcome " + loggedInUser.getFullName());
            // TODO: Open MainFrame when Member 4 is ready
            // dispose();
            // new MainFrame(loggedInUser).setVisible(true);
        } else {
            showMessage("❌ Invalid username or password", Color.RED);
        }
    }

    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
        Timer timer = new Timer(3000, e -> messageLabel.setText(" "));
        timer.setRepeats(false);
        timer.start();
    }

    // Public method so RegisterFrame can add new users
    public static void addUser(User user) {
        users.add(user);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}