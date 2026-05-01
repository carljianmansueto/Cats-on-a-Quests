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

    // Color Scheme
    private static final Color MAROON = new Color(128, 0, 0);
    private static final Color DARK_MAROON = new Color(100, 0, 0);
    private static final Color LIGHT_MAROON_BG = new Color(255, 240, 240);
    private static final Color GOLD = new Color(255, 215, 0);
    private static final Color CARD_WHITE = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(200, 180, 180);

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
        setSize(480, 620);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_MAROON_BG);

        // Scroll container
        JPanel scrollContainer = new JPanel();
        scrollContainer.setLayout(new BoxLayout(scrollContainer, BoxLayout.Y_AXIS));
        scrollContainer.setBackground(LIGHT_MAROON_BG);

        // White card panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(CARD_WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(25, 35, 25, 35)
        ));
        cardPanel.setPreferredSize(new Dimension(380, 700));
        cardPanel.setMaximumSize(new Dimension(380, 700));

        // Top glue to push content down
        cardPanel.add(Box.createVerticalGlue());

        // Title
        JLabel titleLabel = new JLabel("Create an Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(MAROON);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(titleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Subtitle
        JLabel subtitleLabel = new JLabel("Join the Cats on a Quest community");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(subtitleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Form fields
        addTextField(cardPanel, "Full Name", fullNameField = new JTextField(), "Juan dela Cruz");
        addTextField(cardPanel, "Email", emailField = new JTextField(), "juan@g.msuiit.edu.ph");
        addComboBox(cardPanel, "Role", roleCombo = new JComboBox<>(ROLES));
        addComboBox(cardPanel, "College", collegeCombo = new JComboBox<>(COLLEGES));
        addTextField(cardPanel, "Course", courseField = new JTextField(), "BSCS");
        addTextField(cardPanel, "ID Number", idNumberField = new JTextField(), "2024-1234");
        addPasswordField(cardPanel, "Password", passwordField = new JPasswordField());
        addPasswordField(cardPanel, "Confirm Password", confirmField = new JPasswordField());

        // Message
        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        messageLabel.setForeground(Color.RED);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(messageLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Register Button
        registerButton = new JButton("Create Account");
        registerButton.setBackground(MAROON);
        registerButton.setForeground(GOLD);
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setMaximumSize(new Dimension(300, 42));
        registerButton.setPreferredSize(new Dimension(300, 42));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(this);
        cardPanel.add(registerButton);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Back button
        backButton = new JButton("← Back to Login");
        backButton.setBackground(MAROON);
        backButton.setForeground(GOLD);
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setMaximumSize(new Dimension(300, 38));
        backButton.setPreferredSize(new Dimension(300, 38));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(this);
        cardPanel.add(backButton);

        // Bottom glue to balance
        cardPanel.add(Box.createVerticalGlue());

        scrollContainer.add(Box.createVerticalGlue());
        scrollContainer.add(cardPanel);
        scrollContainer.add(Box.createVerticalGlue());

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(scrollContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(LIGHT_MAROON_BG);

        // Center scroll pane
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(LIGHT_MAROON_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        centerWrapper.add(scrollPane, gbc);

        mainPanel.add(centerWrapper, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(LIGHT_MAROON_BG);
        JLabel footerLabel = new JLabel("🏛️ MSU-IIT · Cats on a Quest © 2024");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        footerLabel.setForeground(DARK_MAROON);
        footerPanel.add(footerLabel);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void addTextField(JPanel panel, String labelText, JTextField field, String placeholder) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(MAROON);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));

        field.setMaximumSize(new Dimension(350, 36));
        field.setPreferredSize(new Dimension(350, 36));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (!placeholder.isEmpty()) {
            field.setText(placeholder);
            field.setForeground(Color.GRAY);

            field.addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    if (field.getText().equals(placeholder)) {
                        field.setText("");
                        field.setForeground(Color.BLACK);
                    }
                }
                public void focusLost(FocusEvent e) {
                    if (field.getText().isEmpty()) {
                        field.setText(placeholder);
                        field.setForeground(Color.GRAY);
                    }
                }
            });
        }

        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
    }

    private void addComboBox(JPanel panel, String labelText, JComboBox<String> combo) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(MAROON);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));

        combo.setMaximumSize(new Dimension(350, 36));
        combo.setPreferredSize(new Dimension(350, 36));
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(CARD_WHITE);
        combo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(combo);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
    }

    private void addPasswordField(JPanel panel, String labelText, JPasswordField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(MAROON);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));

        field.setMaximumSize(new Dimension(350, 36));
        field.setPreferredSize(new Dimension(350, 36));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            handleRegister();
        } else if (e.getSource() == backButton) {
            dispose();
            //new LoginFrame().setVisible(true);
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

        if (fullName.isEmpty() || fullName.equals("Juan dela Cruz")) {
            messageLabel.setText("Please enter your full name");
            return;
        }

        if (email.isEmpty() || email.equals("juan@g.msuiit.edu.ph")) {
            messageLabel.setText("Please enter your email");
            return;
        }

        if (!email.endsWith("@g.msuiit.edu.ph") && !email.endsWith("@msuiit.edu.ph")) {
            messageLabel.setText("Email must be @g.msuiit.edu.ph");
            return;
        }

        if (course.isEmpty() || course.equals("BSCS")) {
            messageLabel.setText("Please enter your course");
            return;
        }

        if (idNumber.isEmpty() || idNumber.equals("2024-1234")) {
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
            //new LoginFrame().setVisible(true);
        } else {
            messageLabel.setText(error);
            messageLabel.setForeground(Color.RED);
        }
    }
}