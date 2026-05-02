package DashboardAndApplicationManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame implements ActionListener {

    private final String fullName;
    private final String role;
    private final String email;
    private final String college;
    private final String course;
    private final String idNumber;
    private JButton logoutButton;

    public MainFrame(String fullName, String role, String email,
                     String college, String course, String idNumber) {
        this.fullName = fullName;
        this.role = role;
        this.email = email;
        this.college = college;
        this.course = course;
        this.idNumber = idNumber;

        setTitle("Cats on a Quest - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 520);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(248, 248, 248));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(128, 0, 0));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Welcome, " + fullName + "!");
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(255, 215, 0));
        logoutButton.setForeground(new Color(128, 0, 0));
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(this);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        contentPanel.add(createInfoLabel("Role: " + role));
        contentPanel.add(createInfoLabel("Email: " + email));
        contentPanel.add(createInfoLabel("College: " + college));
        contentPanel.add(createInfoLabel("Course: " + course));
        contentPanel.add(createInfoLabel("ID Number: " + idNumber));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 24)));

        JLabel hintLabel = new JLabel("This is the dashboard placeholder. Add your job listing and application UI here.");
        hintLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        hintLabel.setForeground(Color.DARK_GRAY);
        contentPanel.add(hintLabel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        label.setForeground(new Color(60, 60, 60));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        return label;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == logoutButton) {
            dispose();
            new LoginAndRegistration.LoginFrame().setVisible(true);
        }
    }
}
