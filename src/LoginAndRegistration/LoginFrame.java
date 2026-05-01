package LoginAndRegistration;

import DataAndModels.User;
import DataAndModels.DataStore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame implements ActionListener {

    private JTextField emailField;  // Text field where the user types their email
    private JPasswordField passwordField;       // Password field for security so input is hidden
    private JButton loginButton;        // Button that triggers the login process
    private JButton registerButton;     // Button that opens the registration screen
    private JLabel messageLabel;        // Label used to display messages like errors or success feedback

    // Color palette for the UI, grouped them here so it's easier to adjust the theme later
    private static final Color MAROON      = new Color(128, 0, 0);
    private static final Color LIGHT_BG    = new Color(248, 248, 248);
    private static final Color WHITE       = new Color(255, 255, 255);
    private static final Color BORDER_GRAY = new Color(220, 220, 220);
    private static final Color GOLD        = new Color(255, 215, 0);

    public LoginFrame() {
        setTitle("Cats on a Quest - Login");    // Set the window title shown on top of the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // Makes sure the application fully closes when this window is closed
        setSize(400, 500);          // Fixed window size so layout stays consistent
        setLocationRelativeTo(null);            // Centers the window on the screen when opened
        initUI();         // Calls the method that builds and arranges all UI components
    }

    private void initUI() {
        // Main container panel that holds everything
        // Using BorderLayout to separate header, center, and footer easily
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);

        // This is the top part with the app name and subtitle
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(MAROON);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(24, 40, 24, 40));

        JLabel titleLabel = new JLabel("Cats on a Quest");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(GOLD);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        JLabel subtitleLabel = new JLabel("MSU-IIT Job & Service Finder");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(255, 220, 220));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(subtitleLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // This is where inputs and buttons are placed
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 40, 40));

        // Email label + field
        centerPanel.add(leftLabel("Email address:"));
        centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(280, 36));
        emailField.setPreferredSize(new Dimension(280, 36));
        emailField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GRAY, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(emailField);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Password label + field
        centerPanel.add(leftLabel("Password:"));
        centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(280, 36));
        passwordField.setPreferredSize(new Dimension(280, 36));
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GRAY, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(passwordField);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        // This is where validation messages appear
        // Kept it blank at first so layout does not jump later
        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        messageLabel.setForeground(Color.RED);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(messageLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Log In button
        loginButton = new JButton("Log In");
        loginButton.setBackground(MAROON);
        loginButton.setForeground(GOLD);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setMaximumSize(new Dimension(280, 42));
        loginButton.setPreferredSize(new Dimension(280, 42));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(this);
        centerPanel.add(loginButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // OR label, separates "Login" and "Create Account"
        JLabel orLabel = new JLabel("— or —");
        orLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        orLabel.setForeground(Color.GRAY);
        orLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(orLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Create account button
        registerButton = new JButton("Create an account");
        registerButton.setBackground(MAROON);
        registerButton.setForeground(GOLD);
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setMaximumSize(new Dimension(280, 40));
        registerButton.setPreferredSize(new Dimension(280, 40));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(this);
        centerPanel.add(registerButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(LIGHT_BG);
        JLabel footerLabel = new JLabel("CCC102 - Cats on a Quest");
        footerLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        footerLabel.setForeground(Color.GRAY);
        footerPanel.add(footerLabel);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // Helper method to keep labels aligned with input fields
    // Without this, labels would not line up cleanly
    private JPanel leftLabel(String text) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.setBackground(WHITE);
        wrapper.setMaximumSize(new Dimension(280, 20));
        wrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(MAROON);
        wrapper.add(label);
        return wrapper;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Identify which button triggered the event
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

        // Validator part
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

        // Authentication
        // Calls the DataStore to check if credentials match
        User user = DataStore.login(email, password);

        if (user != null) {
            messageLabel.setText("Welcome, " + user.getFullName() + "!");
            messageLabel.setForeground(new Color(0, 128, 0));
            JOptionPane.showMessageDialog(this,
                    "Login successful!\nWelcome " + user.getFullName(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            // Instead of just showing a popup, go to the main screen
            //dispose(); // close the login window
            // new MainFrame(user.getFullName()).setVisible(true); // open the dashboard
        } else {
            messageLabel.setText("Invalid email or password");
            messageLabel.setForeground(Color.RED);
        }

        // Clear the message after 3 seconds so it does not stay on screen
        Timer timer = new Timer(3000, e -> messageLabel.setText(" "));
        timer.setRepeats(false);
        timer.start();
    }
}