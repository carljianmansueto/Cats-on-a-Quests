package LoginAndRegistration;

import DataAndModels.User;
import DataAndModels.DataStore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame implements ActionListener {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel messageLabel;

    // Colors
    private static final Color MAROON = new Color(128, 0, 0);
    private static final Color LIGHT_BG = new Color(248, 248, 248);
    private static final Color WHITE = new Color(255,255,255);
    private static final Color BORDER_GRAY = new Color(220, 220, 220);
    private static final Color GOLD = new Color(255, 215, 0);

    public LoginFrame() {
        setTitle("Cats on a Quest - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        initUI();
    }

    private void initUI() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);

        // Center panel with BoxLayout
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Title
        JLabel titleLabel = new JLabel("Cats on a Quest");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));  // ← CHANGED
        titleLabel.setForeground(MAROON);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        // Subtitle
        JLabel subtitleLabel = new JLabel("MSU-IIT Job & Service Finder");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));  // ← CHANGED
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(subtitleLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Email label
        JLabel emailLabel = new JLabel("Email address: ");
        emailLabel.setFont(new Font("SansSerif", Font.BOLD, 12));  // ← CHANGED
        emailLabel.setForeground(MAROON);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(emailLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Email field
        emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(280, 36));
        emailField.setPreferredSize(new Dimension(280, 36));
        emailField.setFont(new Font("SansSerif", Font.PLAIN, 13));  // ← CHANGED
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GRAY, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(emailField);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Password label
        JLabel passwordLabel = new JLabel("Password: ");
        passwordLabel.setFont(new Font("SansSerif", Font.BOLD, 12));  // ← CHANGED
        passwordLabel.setForeground(MAROON);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(passwordLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Password field
        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(280, 36));
        passwordField.setPreferredSize(new Dimension(280, 36));
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 13));  // ← CHANGED
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GRAY, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(passwordField);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        // Message label
        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));  // ← CHANGED
        messageLabel.setForeground(Color.RED);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(messageLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Log In button
        loginButton = new JButton("Log In");
        loginButton.setBackground(MAROON);
        loginButton.setForeground(GOLD);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));  // ← CHANGED
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setMaximumSize(new Dimension(280, 42));
        loginButton.setPreferredSize(new Dimension(280, 42));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(this);
        centerPanel.add(loginButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // OR label
        JLabel orLabel = new JLabel("— or —");
        orLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));  // ← CHANGED
        orLabel.setForeground(Color.GRAY);
        orLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(orLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Create account button
        registerButton = new JButton("Create an account");
        registerButton.setBackground(MAROON);
        registerButton.setForeground(GOLD);
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 14));  // ← CHANGED
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setMaximumSize(new Dimension(280, 40));
        registerButton.setPreferredSize(new Dimension(280, 40));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(this);
        centerPanel.add(registerButton);

        // Add to frame
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(LIGHT_BG);
        JLabel footerLabel = new JLabel("CCC102 - Cats on a Quest");
        footerLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));  // ← CHANGED
        footerLabel.setForeground(Color.GRAY);
        footerPanel.add(footerLabel);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            handleLogin();
        } else if (e.getSource() == registerButton) {
            dispose();
            new RegisterFrame().setVisible(true);
        }
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty()) {
            messageLabel.setText("Please enter your email");
            messageLabel.setForeground(Color.RED);
            return;
        }

        if (password.isEmpty()) {
            messageLabel.setText("Please enter your password");
            messageLabel.setForeground(Color.RED);
            return;
        }

        User user = DataStore.login(email, password);

        if (user != null) {
            messageLabel.setText("Welcome, " + user.getFullName() + "!");
            messageLabel.setForeground(new Color(0, 128, 0));
            JOptionPane.showMessageDialog(this,
                    "Login successful!\nWelcome " + user.getFullName(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            passwordField.setText("");
        } else {
            messageLabel.setText("Invalid email or password");
            messageLabel.setForeground(Color.RED);
        }

        Timer timer = new Timer(3000, e -> messageLabel.setText(" "));
        timer.setRepeats(false);
        timer.start();
    }
}