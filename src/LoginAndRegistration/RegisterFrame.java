package LoginAndRegistration;

import DataAndModels.User;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JComboBox<String> roleCombo;
    private JTextField collegeField;
    private JTextField idNumberField;
    private JButton registerButton;
    private JButton cancelButton;
    private JLabel messageLabel;

    private static final Color MAROON = new Color(128, 0, 0);
    private static final Color DARK_MAROON = new Color(102, 0, 0);
    private static final Color LIGHT_MAROON_BG = new Color(255, 240, 240);
    private static final Color GOLD = new Color(255, 215, 0);

    public RegisterFrame() {
        setTitle("🐱 Cats on a Quest - Register | MSU-IIT");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(LIGHT_MAROON_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // Title
        JLabel titleLabel = new JLabel("📝 Create Your Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(MAROON);
        gbc.gridx = 0;
        gbc.gridy = y++;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        // Username
        gbc.gridy = y++;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        mainPanel.add(usernameField, gbc);

        // Full Name
        gbc.gridy = y++;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        fullNameField = new JTextField(20);
        mainPanel.add(fullNameField, gbc);

        // Email
        gbc.gridy = y++;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        emailField.setToolTipText("Must end with @g.msuiit.edu.ph");
        mainPanel.add(emailField, gbc);

        // Role
        gbc.gridy = y++;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        roleCombo = new JComboBox<>(new String[]{"STUDENT", "FACULTY", "ORGANIZATION"});
        mainPanel.add(roleCombo, gbc);

        // College
        gbc.gridy = y++;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("College:"), gbc);
        gbc.gridx = 1;
        collegeField = new JTextField(20);
        mainPanel.add(collegeField, gbc);

        // ID Number
        gbc.gridy = y++;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("ID Number:"), gbc);
        gbc.gridx = 1;
        idNumberField = new JTextField(20);
        mainPanel.add(idNumberField, gbc);

        // Password
        gbc.gridy = y++;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        mainPanel.add(passwordField, gbc);

        // Confirm Password
        gbc.gridy = y++;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        confirmField = new JPasswordField(20);
        mainPanel.add(confirmField, gbc);

        // Message
        gbc.gridy = y++;
        gbc.gridwidth = 2;
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);
        mainPanel.add(messageLabel, gbc);

        // Buttons
        gbc.gridy = y++;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        registerButton = new JButton("✅ Register");
        registerButton.setBackground(MAROON);
        registerButton.setForeground(GOLD);
        registerButton.setBorder(BorderFactory.createLineBorder(GOLD, 2));
        registerButton.setFocusPainted(false);
        registerButton.setPreferredSize(new Dimension(120, 40));

        cancelButton = new JButton("◀ Back to Login");
        cancelButton.setBackground(DARK_MAROON);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setPreferredSize(new Dimension(140, 40));

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, gbc);

        // Actions
        registerButton.addActionListener(e -> handleRegister());
        cancelButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        add(mainPanel);
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String role = (String) roleCombo.getSelectedItem();
        String college = collegeField.getText().trim();
        String idNumber = idNumberField.getText().trim();

        // Validations
        if (username.isEmpty() || fullName.isEmpty() || email.isEmpty() ||
                college.isEmpty() || idNumber.isEmpty()) {
            showMessage("❌ Please fill all fields", Color.RED);
            return;
        }

        if (!email.endsWith("@g.msuiit.edu.ph") && !email.endsWith("@msuiit.edu.ph")) {
            showMessage("❌ Email must be a valid MSU-IIT email", Color.RED);
            return;
        }

        if (password.isEmpty()) {
            showMessage("❌ Please enter a password", Color.RED);
            return;
        }

        if (password.length() < 6) {
            showMessage("❌ Password must be at least 6 characters", Color.RED);
            return;
        }

        if (!password.equals(confirm)) {
            showMessage("❌ Passwords do not match", Color.RED);
            return;
        }

        // Check if username already exists
        // Note: This requires LoginFrame to have a way to check users
        // For simplicity, we'll just assume it's unique for now

        // Create new user
        User newUser = new User(username, password, fullName, email, role, college, idNumber);
        LoginFrame.addUser (newUser);

        JOptionPane.showMessageDialog(this,
                "✓ Registration successful!\n\nYou can now login with your username: " + username,
                "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        new LoginFrame().setVisible(true);
    }

    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
        Timer timer = new Timer(3000, e -> messageLabel.setText(" "));
        timer.setRepeats(false);
        timer.start();
    }
}