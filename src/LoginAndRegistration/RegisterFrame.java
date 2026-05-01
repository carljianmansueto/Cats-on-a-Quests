package LoginAndRegistration;

import DataAndModels.User;
import DataAndModels.DataStore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterFrame extends JFrame implements ActionListener {

    private JTextField fullNameField;
    private JTextField emailField;
    private JComboBox<String> roleCombo;
    private JComboBox<String> collegeCombo;
    private JTextField courseField;
    private JTextField idNumberField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;
    private JButton registerButton;
    private JButton backButton;
    private JLabel messageLabel;

    private static final Color MAROON = new Color(128, 0, 0);
    private static final Color LIGHT_BG = new Color(248, 248, 248);
    private static final Color WHITE = Color.WHITE;
    private static final Color BORDER_GRAY = new Color(200, 200, 200);
    private static final Color GOLD = new Color(255, 215, 0);

    private static final String[] ROLES = {"STUDENT", "FACULTY", "ORGANIZATION"};
    private static final String[] COLLEGES = {
            "College of Engineering and Technology",
            "College of Science and Mathematics",
            "College of Computer Studies",
            "College of Education",
            "College of Arts and Science",
            "College of Business Administration & Accountancy",
            "College of Nursing"
    };

    public RegisterFrame() {
        setTitle("Cats on a Quest - Register");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 600);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);

        JPanel scrollContainer = new JPanel();
        scrollContainer.setLayout(new BoxLayout(scrollContainer, BoxLayout.Y_AXIS));
        scrollContainer.setBackground(LIGHT_BG);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        formPanel.setMaximumSize(new Dimension(380, 750));
        formPanel.setPreferredSize(new Dimension(380, 750));

        // Title
        JLabel titleLabel = new JLabel("Create an Account");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));  // ← CHANGED
        titleLabel.setForeground(MAROON);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(titleLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Subtitle
        JLabel subtitleLabel = new JLabel("Join the Cats on a Quest community");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));  // ← CHANGED
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(subtitleLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Full Name
        JLabel nameLabel = new JLabel("Full Name");
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));  // ← CHANGED
        nameLabel.setForeground(MAROON);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(nameLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        fullNameField = new JTextField();
        fullNameField.setFont(new Font("SansSerif", Font.PLAIN, 13));  // ← CHANGED
        fullNameField.setMaximumSize(new Dimension(340, 36));
        fullNameField.setPreferredSize(new Dimension(340, 36));
        fullNameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GRAY, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        fullNameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(fullNameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Email
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));  // ← CHANGED
        emailLabel.setForeground(MAROON);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(emailLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        emailField = new JTextField();
        emailField.setFont(new Font("SansSerif", Font.PLAIN, 13));  // ← CHANGED
        emailField.setMaximumSize(new Dimension(340, 36));
        emailField.setPreferredSize(new Dimension(340, 36));
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GRAY, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(emailField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Role
        JLabel roleLabel = new JLabel("Role");
        roleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));  // ← CHANGED
        roleLabel.setForeground(MAROON);
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(roleLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        roleCombo = new JComboBox<>(ROLES);
        roleCombo.setFont(new Font("SansSerif", Font.PLAIN, 13));  // ← CHANGED
        roleCombo.setMaximumSize(new Dimension(340, 36));
        roleCombo.setPreferredSize(new Dimension(340, 36));
        roleCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(roleCombo);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // College
        JLabel collegeLabel = new JLabel("College");
        collegeLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));  // ← CHANGED
        collegeLabel.setForeground(MAROON);
        collegeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(collegeLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        collegeCombo = new JComboBox<>(COLLEGES);
        collegeCombo.setFont(new Font("SansSerif", Font.PLAIN, 13));  // ← CHANGED
        collegeCombo.setMaximumSize(new Dimension(340, 36));
        collegeCombo.setPreferredSize(new Dimension(340, 36));
        collegeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(collegeCombo);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Course
        JLabel courseLabel = new JLabel("Course");
        courseLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));  // ← CHANGED
        courseLabel.setForeground(MAROON);
        courseLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(courseLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        courseField = new JTextField();
        courseField.setFont(new Font("SansSerif", Font.PLAIN, 13));  // ← CHANGED
        courseField.setMaximumSize(new Dimension(340, 36));
        courseField.setPreferredSize(new Dimension(340, 36));
        courseField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GRAY, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        courseField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(courseField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // ID Number
        JLabel idLabel = new JLabel("ID Number");
        idLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));  // ← CHANGED
        idLabel.setForeground(MAROON);
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(idLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        idNumberField = new JTextField();
        idNumberField.setFont(new Font("SansSerif", Font.PLAIN, 13));  // ← CHANGED
        idNumberField.setMaximumSize(new Dimension(340, 36));
        idNumberField.setPreferredSize(new Dimension(340, 36));
        idNumberField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GRAY, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        idNumberField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(idNumberField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Password
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));  // ← CHANGED
        passwordLabel.setForeground(MAROON);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(passwordLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 13));  // ← CHANGED
        passwordField.setMaximumSize(new Dimension(340, 36));
        passwordField.setPreferredSize(new Dimension(340, 36));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GRAY, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(passwordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Confirm Password
        JLabel confirmLabel = new JLabel("Confirm Password");
        confirmLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));  // ← CHANGED
        confirmLabel.setForeground(MAROON);
        confirmLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(confirmLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        confirmField = new JPasswordField();
        confirmField.setFont(new Font("SansSerif", Font.PLAIN, 13));  // ← CHANGED
        confirmField.setMaximumSize(new Dimension(340, 36));
        confirmField.setPreferredSize(new Dimension(340, 36));
        confirmField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GRAY, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        confirmField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(confirmField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Message
        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));  // ← CHANGED
        messageLabel.setForeground(Color.RED);
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(messageLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Register button
        registerButton = new JButton("Create Account");
        registerButton.setBackground(MAROON);
        registerButton.setForeground(GOLD);
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 14));  // ← CHANGED
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setMaximumSize(new Dimension(340, 42));
        registerButton.setPreferredSize(new Dimension(340, 42));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(this);
        formPanel.add(registerButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Back button
        backButton = new JButton("← Back to Login");
        backButton.setBackground(MAROON);
        backButton.setForeground(GOLD);
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 12));  // ← CHANGED
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setMaximumSize(new Dimension(340, 35));
        backButton.setPreferredSize(new Dimension(340, 35));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(this);
        formPanel.add(backButton);

        scrollContainer.add(Box.createVerticalGlue());
        scrollContainer.add(formPanel);
        scrollContainer.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(scrollContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(LIGHT_BG);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

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
        if (e.getSource() == registerButton) {
            handleRegister();
        } else if (e.getSource() == backButton) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }

    private void handleRegister() {
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String role = (String) roleCombo.getSelectedItem();
        String college = (String) collegeCombo.getSelectedItem();
        String course = courseField.getText().trim();
        String idNumber = idNumberField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());

        if (fullName.isEmpty()) {
            messageLabel.setText("Please enter your full name");
            return;
        }

        if (email.isEmpty()) {
            messageLabel.setText("Please enter your email");
            return;
        }

        if (!email.endsWith("@g.msuiit.edu.ph")) {
            messageLabel.setText("Email must be @g.msuiit.edu.ph");
            return;
        }

        if (course.isEmpty()) {
            messageLabel.setText("Please enter your course");
            return;
        }

        if (idNumber.isEmpty()) {
            messageLabel.setText("Please enter your ID number");
            return;
        }

        if (password.isEmpty()) {
            messageLabel.setText("Please enter a password");
            return;
        }

        if (password.length() < 6) {
            messageLabel.setText("Password must be at least 6 characters");
            return;
        }

        if (!password.equals(confirm)) {
            messageLabel.setText("Passwords do not match");
            return;
        }

        User newUser = new User(fullName, password, email, role, college, course, idNumber);
        String error = DataStore.register(newUser);

        if (error == null) {
            JOptionPane.showMessageDialog(this,
                    "✓ Registration successful!\nYou can now login with: " + email,
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginFrame().setVisible(true);
        } else {
            messageLabel.setText(error);
            messageLabel.setForeground(Color.RED);
        }
    }
}