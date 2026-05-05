package BrowseAndPostFeatures;

import DataAndModels.DataStore;
import DataAndModels.JobListing;
import DataAndModels.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PostPanel extends JPanel {

    // Colors — matched to LoginFrame and RegisterFrame maroon theme
    private static final Color MAROON       = new Color(128, 0, 0);
    private static final Color LIGHT_BG     = new Color(248, 245, 245);
    private static final Color CARD_WHITE   = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(200, 180, 180);
    private static final Color GOLD         = new Color(255, 215, 0);

    // The currently logged-in user — passed in from MainFrame
    private User currentUser;

    // Form fields
    private JTextField        titleField;
    private JTextArea         descArea;
    private JComboBox<String> categoryCombo;
    private JComboBox<String> payTypeCombo;
    private JTextField        payRateField;
    private JTextField        locationField;
    private JTextField        slotsField;
    private JTextField        deadlineField;
    private JLabel            messageLabel;


    // Constructor

    public PostPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(LIGHT_BG);
        buildUI();
    }

    // Build the full panel layout

    private void buildUI() {

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(MAROON);
        topBar.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel titleLabel = new JLabel("Post a Job or Service");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        // Encapsulation: reading private fields through getters
        JLabel subLabel = new JLabel(
                "Logged in as: " + currentUser.getFullName()
                        + "  |  " + currentUser.getEmail()
        );
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subLabel.setForeground(new Color(220, 200, 200));

        topBar.add(titleLabel, BorderLayout.WEST);
        topBar.add(subLabel,   BorderLayout.EAST);

        // Form card
        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(CARD_WHITE);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(24, 32, 24, 32)
        ));

        JLabel formTitle = new JLabel("New Listing Details");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        formTitle.setForeground(MAROON);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel formSub = new JLabel(
                "Fill in the details below. Fields marked with * are required."
        );
        formSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        formSub.setForeground(Color.GRAY);
        formSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        formCard.add(formTitle);
        formCard.add(Box.createRigidArea(new Dimension(0, 4)));
        formCard.add(formSub);
        formCard.add(Box.createRigidArea(new Dimension(0, 20)));

        // Form fields
        titleField = makeTextField();
        addFormRow(formCard, "Job / Service Title *", titleField);

        descArea = new JTextArea(4, 30);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        descScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        addFormRowComponent(formCard, "Description *", descScroll);

        String[] categories = {
                "Tutoring", "Design", "Research",
                "Errand", "Technical", "Creative", "Labor", "Other"
        };
        categoryCombo = new JComboBox<>(categories);
        styleCombo(categoryCombo);
        addFormRow(formCard, "Category *", categoryCombo);

        payTypeCombo = new JComboBox<>(new String[]{"PER_HOUR", "FIXED"});
        styleCombo(payTypeCombo);
        addFormRow(formCard, "Pay Type *", payTypeCombo);

        payRateField = makeTextField();
        addFormRow(formCard, "Pay Rate (PHP) *", payRateField);

        locationField = makeTextField();
        addFormRow(formCard, "Location", locationField);

        slotsField = makeTextField();
        slotsField.setText("1");
        addFormRow(formCard, "Slots Available *", slotsField);

        deadlineField = makeTextField();
        deadlineField.setText(
                LocalDate.now().plusDays(14)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
        addFormRow(formCard, "Deadline (yyyy-MM-dd) *", deadlineField);

        // Message label
        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        messageLabel.setForeground(Color.RED);
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(Box.createRigidArea(new Dimension(0, 8)));
        formCard.add(messageLabel);
        formCard.add(Box.createRigidArea(new Dimension(0, 12)));

        // Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnRow.setOpaque(false);
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton postBtn = new JButton("Post Listing");
        postBtn.setBackground(MAROON);
        postBtn.setForeground(GOLD);
        postBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        postBtn.setFocusPainted(false);
        postBtn.setBorderPainted(false);
        postBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        postBtn.setPreferredSize(new Dimension(130, 38));

        JButton clearBtn = new JButton("Clear Form");
        clearBtn.setBackground(new Color(220, 220, 220));
        clearBtn.setForeground(Color.DARK_GRAY);
        clearBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        clearBtn.setFocusPainted(false);
        clearBtn.setBorderPainted(false);
        clearBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearBtn.setPreferredSize(new Dimension(110, 38));

        btnRow.add(postBtn);
        btnRow.add(clearBtn);
        formCard.add(btnRow);

        // Wrap in scrollable centered panel
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(LIGHT_BG);
        wrapper.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        wrapper.add(formCard, gbc);

        JScrollPane scrollPane = new JScrollPane(wrapper);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(LIGHT_BG);

        add(topBar,     BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        postBtn.addActionListener(e -> handlePost());
        clearBtn.addActionListener(e -> clearForm());
    }

    // Validate form input and create a new JobListing object

    private void handlePost() {
        String title    = titleField.getText().trim();
        String desc     = descArea.getText().trim();
        String category = (String) categoryCombo.getSelectedItem();
        String payType  = (String) payTypeCombo.getSelectedItem();
        String payStr   = payRateField.getText().trim();
        String location = locationField.getText().trim();
        String slotsStr = slotsField.getText().trim();
        String deadline = deadlineField.getText().trim();

        // Validation
        if (title.isEmpty()) {
            showError("Title is required."); return;
        }
        if (desc.isEmpty()) {
            showError("Description is required."); return;
        }
        if (payStr.isEmpty()) {
            showError("Pay rate is required."); return;
        }
        if (slotsStr.isEmpty()) {
            showError("Slots available is required."); return;
        }
        if (deadline.isEmpty()) {
            showError("Deadline is required."); return;
        }
        if (location.isEmpty()) {
            location = "MSU-IIT Campus";
        }

        double payRate;
        try {
            payRate = Double.parseDouble(payStr);
            if (payRate <= 0) {
                showError("Pay rate must be a positive number."); return;
            }
        } catch (NumberFormatException ex) {
            showError("Pay rate must be a valid number (e.g. 75 or 500).");
            return;
        }

        int slots;
        try {
            slots = Integer.parseInt(slotsStr);
            if (slots <= 0) {
                showError("Slots must be at least 1."); return;
            }
        } catch (NumberFormatException ex) {
            showError("Slots must be a whole number (e.g. 1 or 3).");
            return;
        }

        // Generate unique listing ID — uses DataStore so IDs never collide
        String listingId = DataStore.generateListingId();
        String today = LocalDate.now().toString();

        // Create a new JobListing object — Object Creation + Constructor
        // Encapsulation: currentUser.getEmail() reads a private field via getter
        // Inheritance: JobListing constructor calls super() internally
        JobListing newJob = new JobListing(
                listingId,
                title,
                desc,
                currentUser.getEmail(),     // Encapsulation: getter
                today,
                category,
                payRate,
                payType,
                location,
                slots,
                deadline
        );

        DataStore.addListing(newJob);

        messageLabel.setForeground(new Color(0, 128, 0));
        messageLabel.setText("Listing posted! ID: " + listingId);

        JOptionPane.showMessageDialog(this,
                "Your listing has been posted!\n"
                        + "Job ID: " + listingId + "\n"
                        + "Title: " + title,
                "Posted!", JOptionPane.INFORMATION_MESSAGE);

        clearForm();
    }

    // Reset all fields

    private void clearForm() {
        titleField.setText("");
        descArea.setText("");
        categoryCombo.setSelectedIndex(0);
        payTypeCombo.setSelectedIndex(0);
        payRateField.setText("");
        locationField.setText("");
        slotsField.setText("1");
        deadlineField.setText(
                LocalDate.now().plusDays(14)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
        messageLabel.setText(" ");
        titleField.requestFocus();
    }

    private void showError(String message) {
        messageLabel.setForeground(Color.RED);
        messageLabel.setText(message);
    }

    // Helper methods for building the form

    private void addFormRow(JPanel panel, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(MAROON);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
    }

    private void addFormRowComponent(JPanel panel, String labelText, JComponent component) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(MAROON);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        component.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(component);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
    }

    private JTextField makeTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return tf;
    }

    private void styleCombo(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(Color.WHITE);
    }
}