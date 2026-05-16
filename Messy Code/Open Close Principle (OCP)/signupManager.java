package Manager;

import Database.database;
import GraphicalUserInterface.HotelManagementSystem;

import javax.mail.internet.InternetAddress;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class signupManager extends JFrame implements ActionListener {

    private final JPanel panel;
    private final JLabel pageTitle;
    private final JLabel managerName;
    private final JTextField managerNameField;
    private final JLabel managerPassword;
    private final JPasswordField managerPasswordField;
    private final JLabel managerGender;
    private final JRadioButton male;
    private final JRadioButton female;
    private final ButtonGroup genderGroup;
    private final JLabel managerEmail;
    private final JTextField managerEmailField;
    private final JButton submitButton;
    private final JButton backButton;

    public signupManager() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);

        panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(52, 17, 63));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        // Title
        pageTitle = new JLabel("Manager Registration Form");
        pageTitle.setFont(new Font("Noto Sans Mono", Font.BOLD, 28));
        pageTitle.setForeground(Color.white);
        pageTitle.setBorder(BorderFactory.createEmptyBorder(0, 80, 0, 0));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(pageTitle, gbc);

        // Name label and field
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 1;
        managerName = new JLabel("Name:");
        managerName.setForeground(Color.white);
        managerName.setFont(new Font("Noto Sans Mono", Font.BOLD, 18));
        panel.add(managerName, gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        managerNameField = new JTextField();
        managerNameField.setPreferredSize(new Dimension(500, 40));
        managerNameField.setFont(new Font("Noto Sans Mono", Font.BOLD, 18));
        panel.add(managerNameField, gbc);

        // Password label and field
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 2;
        managerPassword = new JLabel("Password:");
        managerPassword.setForeground(Color.white);
        managerPassword.setFont(new Font("Noto Sans Mono", Font.BOLD, 18));
        panel.add(managerPassword, gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        managerPasswordField = new JPasswordField();
        managerPasswordField.setPreferredSize(new Dimension(500, 40));
        managerPasswordField.setFont(new Font("Noto Sans Mono", Font.BOLD, 18));
        panel.add(managerPasswordField, gbc);

        // Email label and field
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 3;
        managerEmail = new JLabel("Email:");
        managerEmail.setForeground(Color.white);
        managerEmail.setFont(new Font("Noto Sans Mono", Font.BOLD, 18));
        panel.add(managerEmail, gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        managerEmailField = new JTextField();
        managerEmailField.setPreferredSize(new Dimension(500, 40));
        managerEmailField.setFont(new Font("Noto Sans Mono", Font.BOLD, 18));
        panel.add(managerEmailField, gbc);

        // Gender label and radio buttons
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 4;
        managerGender = new JLabel("Gender:");
        managerGender.setForeground(Color.white);
        managerGender.setFont(new Font("Noto Sans Mono", Font.BOLD, 18));
        panel.add(managerGender, gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;

        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        male = new JRadioButton("Male");
        male.setFont(new Font("Noto Sans Mono", Font.BOLD, 18));
        male.setBackground(new Color(52, 17, 63));
        male.setForeground(Color.WHITE);
        male.setBorder(null);
        male.setSelected(true);

        female = new JRadioButton("Female");
        female.setFont(new Font("Noto Sans Mono", Font.BOLD, 18));
        female.setBackground(new Color(52, 17, 63));
        female.setForeground(Color.WHITE);
        female.setBorder(null);

        genderGroup = new ButtonGroup();
        genderGroup.add(male);
        genderGroup.add(female);

        genderPanel.add(male);
        genderPanel.add(female);
        genderPanel.setBackground(new Color(52, 17, 63));
        panel.add(genderPanel, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        buttonPanel.setBackground(new Color(52, 17, 63));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 115, 0, 0)); // Add margin from left

        // Back button
        backButton = new JButton("BACK");
        backButton.setPreferredSize(new Dimension(500, 60));
        backButton.setFocusable(false);
        backButton.setBackground(new Color(190, 183, 223));
        backButton.setForeground(new Color(52, 17, 63));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton.setFont(new Font("Noto Sans Mono", Font.BOLD, 18));

        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(220, 220, 220));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(190, 183, 223));
            }
        });

        backButton.addActionListener(this);

        // Submit button
        submitButton = new JButton("SUBMIT");
        submitButton.setPreferredSize(new Dimension(500, 60));
        submitButton.setFocusable(false);
        submitButton.setBackground(new Color(190, 183, 223));
        submitButton.setForeground(new Color(52, 17, 63));
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        submitButton.setFont(new Font("Noto Sans Mono", Font.BOLD, 18));

        submitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                submitButton.setBackground(new Color(220, 220, 220));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                submitButton.setBackground(new Color(190, 183, 223));
            }
        });

        submitButton.addActionListener(this);

        buttonPanel.add(backButton);
        buttonPanel.add(submitButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        add(panel);
        setVisible(true);
    }

    // GET DATA FROM HERE
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {

            String managerName = managerNameField.getText();
            String managerPassword = managerPasswordField.getText();
            String managerEmail = managerEmailField.getText();
            String managerGender = male.isSelected() ? "male" : "female";

            // Return if anything is empty
            if (managerName.equals("") || managerPassword.equals("") || managerEmail.equals("")) {
                JOptionPane.showMessageDialog(this, "No fields can be empty.", "Error.", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Checking if Email is Valid or not
            if (!isValidEmail(managerEmail)) {
                JOptionPane.showMessageDialog(this, "Invalid Email", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Saving in Database

            String query = "INSERT INTO manager (managerName, managerPassword, managerEmail, managerGender) VALUES ('"
                    + managerName + "', '"
                    + managerPassword + "', '"
                    + managerEmail + "', '"
                    + managerGender + "')";

            try {
                database.executeWriteQuery(query);
                JOptionPane.showMessageDialog(this, "Manager Successfully Created.", "Success", JOptionPane.INFORMATION_MESSAGE);
                managerNameField.setText("");
                managerPasswordField.setText("");
                managerEmailField.setText("");
            } catch (RuntimeException error) {
                JOptionPane.showMessageDialog(this, error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }



        } else if (e.getSource() == backButton) {
            dispose();
            SwingUtilities.invokeLater(() -> new HotelManagementSystem());
        }
    }

    public static boolean isValidEmail(String email) {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}