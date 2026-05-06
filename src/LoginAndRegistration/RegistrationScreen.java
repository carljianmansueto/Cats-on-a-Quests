package LoginAndRegistration;

import DataAndModels.User;
import DataAndModels.DataStore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegistrationScreen extends JFrame implements ActionListener {

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

    // Same color palette used
    private static final Color MAROON      = new Color(128, 0, 0);
    private static final Color LIGHT_BG    = new Color(248, 248, 248);
    private static final Color WHITE       = new Color(255, 255, 255);
    private static final Color BORDER_GRAY = new Color(220, 220, 220);
    private static final Color GOLD        = new Color(255, 215, 0);

    private static final String[] ROLES = {"Student", "Faculty", "Staff"};
    private static final String[] COLLEGES = { "College of Engineering and Technology", "College of Science and Mathematics", "College of Computer Studies", "College of Education", "College of Arts and Science", "College of Business Administration & Accountancy", "College of Nursing", "Other"};

    public RegistrationScreen() {
        setTitle("Cats on a Quest - Register");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 620);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);

        // ── Maroon header banner (just like LoginFrame) ──────────
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(MAROON);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(24, 40, 24, 40));

        JLabel titleLabel = new JLabel("Create an Account");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(GOLD);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        JLabel subtitleLabel = new JLabel("Join the Cats on a Quest community");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(255, 220, 220));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(subtitleLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center panel — same structure as LoginFrame
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Fields
        addLabel(centerPanel, "Full Name");
        fullNameField = addTextField(centerPanel);

        addLabel(centerPanel, "Email");
        emailField = addTextField(centerPanel);

        addLabel(centerPanel, "Role");
        roleCombo = addComboBox(centerPanel, ROLES);

        addLabel(centerPanel, "College");
        collegeCombo = addComboBox(centerPanel, COLLEGES);

        addLabel(centerPanel, "Course");
        courseField = addTextField(centerPanel);

        addLabel(centerPanel, "ID Number");
        idNumberField = addTextField(centerPanel);

        addLabel(centerPanel, "Password");
        passwordField = addPasswordField(centerPanel);

        addLabel(centerPanel, "Confirm Password");
        confirmField = addPasswordField(centerPanel);

        // Message label
        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        messageLabel.setForeground(Color.RED);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(messageLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Register button — same style as loginButton
        registerButton = new JButton("Create Account");
        registerButton.setBackground(MAROON);
        registerButton.setForeground(GOLD);
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setMaximumSize(new Dimension(280, 42));
        registerButton.setPreferredSize(new Dimension(280, 42));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(this);
        centerPanel.add(registerButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // OR label
        JLabel orLabel = new JLabel("— or —");
        orLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        orLabel.setForeground(Color.GRAY);
        orLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(orLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Back button — same style as registerButton in LoginFrame
        backButton = new JButton("Back to Login");
        backButton.setBackground(MAROON);
        backButton.setForeground(GOLD);
        backButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setMaximumSize(new Dimension(280, 40));
        backButton.setPreferredSize(new Dimension(280, 40));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(this);
        centerPanel.add(backButton);

        // Scroll pane in case window is short
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        scrollPane.getViewport().setBackground(WHITE);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer — same as LoginFrame
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(LIGHT_BG);
        JLabel footerLabel = new JLabel("CCC102 - Cats on a Quest");
        footerLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        footerLabel.setForeground(Color.GRAY);
        footerPanel.add(footerLabel);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // ── Helpers (same field sizing/style as LoginFrame) ──────────

    private void addLabel(JPanel panel, String text) {
        // Wrap in a 280px-wide panel so the label hugs the left edge of the fields
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.setBackground(WHITE);
        wrapper.setMaximumSize(new Dimension(280, 20));
        wrapper.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(text + ":");
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(MAROON);
        wrapper.add(label);

        panel.add(wrapper);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private JTextField addTextField(JPanel panel) {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(280, 36));
        field.setPreferredSize(new Dimension(280, 36));
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GRAY, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        return field;
    }

    private JPasswordField addPasswordField(JPanel panel) {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(280, 36));
        field.setPreferredSize(new Dimension(280, 36));
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GRAY, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        return field;
    }

    private JComboBox<String> addComboBox(JPanel panel, String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setMaximumSize(new Dimension(280, 36));
        combo.setPreferredSize(new Dimension(280, 36));
        combo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        combo.setBackground(WHITE);
        combo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(combo);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        return combo;
    }

    // ── Logic ────────────────────────────────────────────────────

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            handleRegister();
        } else if (e.getSource() == backButton) {
            dispose();
            new LoginScreen().setVisible(true);
        }
    }

    private void handleRegister() {
        String fullName = fullNameField.getText().trim();
        String email    = emailField.getText().trim();
        String role     = (String) roleCombo.getSelectedItem();
        String college  = (String) collegeCombo.getSelectedItem();
        String course   = courseField.getText().trim();
        String idNumber = idNumberField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm  = new String(confirmField.getPassword());

        //Validator part, checks whether user inputs a correct info
        if (fullName.isEmpty())    { showMessage("Please enter your full name"); return; }
        if (email.isEmpty())       { showMessage("Please enter your email"); return; }
        if (!email.endsWith("@g.msuiit.edu.ph")) { showMessage("Email must be @g.msuiit.edu.ph"); return;}
        if (course.isEmpty())      { showMessage("Please enter your course"); return; }
        if (idNumber.isEmpty())    { showMessage("Please enter your ID number"); return; }
        if (password.isEmpty())    { showMessage("Please enter a password"); return; }
        if (password.length() < 6) { showMessage("Password must be at least 6 characters"); return; }
        if (!password.equals(confirm)) { showMessage("Passwords do not match"); return; }

        //Registers the user after creating the account and put it in the data store
        User newUser = new User(fullName, password, email, role, college, course, idNumber);
        String error = DataStore.register(newUser);

        //
        if (error == null) {
            JOptionPane.showMessageDialog(this,
                    "Registration successful!\nYou can now login with: " + email,
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginScreen().setVisible(true);
        } else {
            showMessage(error);
        }
    }

    private void showMessage(String msg) {
        messageLabel.setText(msg);
        messageLabel.setForeground(Color.RED);
        Timer timer = new Timer(3000, e -> messageLabel.setText(" "));
        timer.setRepeats(false);
        timer.start();
    }
}