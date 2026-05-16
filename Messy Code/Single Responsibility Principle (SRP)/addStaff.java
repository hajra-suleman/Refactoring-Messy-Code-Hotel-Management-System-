package Staff;

import Database.database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class addStaff {

    public addStaff() {
        // Create the JFrame
        JFrame frame = new JFrame("Add staff");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(1000, 500);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        // Define colors
        Color deepPurple = new Color(48, 17, 63);
        Color lightPurple = new Color(190, 183, 223);
        Color whiteText = Color.WHITE;
        Color hoverColor = new Color(230, 230, 250);

        // Create the main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(7, 2, 10, 10)); // 7 rows, 2 columns
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        mainPanel.setBackground(deepPurple);

        // Staff ID
        JLabel idLabel = new JLabel("Staff ID:");
        idLabel.setForeground(whiteText);
        JTextField idField = new JTextField();
        styleTextField(idField);
        mainPanel.add(idLabel);
        mainPanel.add(idField);

        // Staff Name
        JLabel staffNameLabel = new JLabel("Staff Name:");
        staffNameLabel.setForeground(whiteText);
        JTextField staffNameField = new JTextField();
        styleTextField(staffNameField);
        mainPanel.add(staffNameLabel);
        mainPanel.add(staffNameField);

        // Staff Email
        JLabel staffEmailLabel = new JLabel("Staff Email:");
        staffEmailLabel.setForeground(whiteText);
        JTextField emailField = new JTextField();
        styleTextField(emailField);
        mainPanel.add(staffEmailLabel);
        mainPanel.add(emailField);

        // Gender (Radio Buttons)
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setForeground(whiteText);

        JPanel genderCombo = createRadioButtonGroup(new String[]{"male", "female"}, deepPurple, whiteText);
        mainPanel.add(genderCombo);

        mainPanel.add(genderLabel);
        mainPanel.add(genderCombo);

        // Staff Salary
        JLabel salaryLabel = new JLabel("Salary:");
        salaryLabel.setForeground(whiteText);
        JTextField salaryField = new JTextField();
        styleTextField(salaryField);
        mainPanel.add(salaryLabel);
        mainPanel.add(salaryField);

        // isPaid (Radio Buttons)
        JLabel isPaid = new JLabel("Is Paid:");
        isPaid.setForeground(whiteText);

        JPanel paidCombo = createRadioButtonGroup(new String[]{"yes", "no"}, deepPurple, whiteText);
        mainPanel.add(paidCombo);

        mainPanel.add(isPaid);
        mainPanel.add(paidCombo);

        // Submit Button
        JButton submitButton = new JButton("Submit");
        styleButton(submitButton, lightPurple, deepPurple, hoverColor);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String staffIdString = idField.getText().trim();
                String staffNameString = staffNameField.getText().trim();
                String staffEmailString = emailField.getText().trim();
                String staffGender = getSelectedRadioButton(genderCombo);
                String staffSalaryString = salaryField.getText().trim();
                String staffPaid = getSelectedRadioButton(paidCombo);

                if (staffIdString.isEmpty() || staffNameString.isEmpty() || staffEmailString.isEmpty() || staffGender.equals("Nothing") || staffSalaryString.isEmpty() || staffPaid.equals("Nothing")) {
                    JOptionPane.showMessageDialog(frame, "Fill All Details.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Converting String into int(staffID)
                int staffId = Integer.parseInt(staffIdString);

                double staffSalary = 0.0;
                try {
                    staffSalary = Double.parseDouble(staffSalaryString);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Salary must be in Numbers.", "Database Error", JOptionPane.ERROR_MESSAGE);
                    return;

                }

                sendDataToDB(frame, staffId, staffNameString, staffEmailString, staffGender, staffSalary, staffPaid);

            }
        });

        // Add components to the frame
        frame.add(mainPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(deepPurple);
        buttonPanel.add(submitButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // Database Functions
    private void sendDataToDB(JFrame frame, int staffId, String staffName, String staffEmail, String staffGender, double staffSalary, String staffPaid) {
        try {
            String query = "INSERT INTO staff (staffId, staffName, staffEmail, staffGender, staffSalary, staffPaid) " + "VALUES (" + staffId + ", '" + staffName + "', '" + staffEmail + "', '" + staffGender + "', " + staffSalary + ", '" + staffPaid + "');";

            database.executeWriteQuery(query);

            JOptionPane.showMessageDialog(frame, "Staff Successfully Created.", "Success", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    // Style text fields
    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    // Style buttons
    private void styleButton(JButton button, Color backgroundColor, Color foregroundColor, Color hoverColor) {
        button.setBackground(backgroundColor);
        button.setForeground(foregroundColor);
        button.setFont(new Font("", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            private final Color originalColor = button.getBackground();

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor); // Lighter hover effect
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalColor); // Reset to original
            }
        });
    }

    // Create a panel with grouped radio buttons
    private JPanel createRadioButtonGroup(String[] options, Color background, Color textColor) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(background);

        ButtonGroup group = new ButtonGroup();

        for (String option : options) {
            JRadioButton radioButton = new JRadioButton(option);
            radioButton.setBackground(background);
            radioButton.setForeground(textColor);
            radioButton.setFont(new Font("", Font.PLAIN, 16));
            radioButton.setFocusPainted(false);
            group.add(radioButton);
            panel.add(radioButton);
        }

        return panel;
    }

    // Get the selected radio button's text
    private String getSelectedRadioButton(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JRadioButton) {
                JRadioButton radioButton = (JRadioButton) comp;
                if (radioButton.isSelected()) {
                    return radioButton.getText();
                }
            }
        }
        return "Nothing"; // Default if none selected
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new addStaff());
    }
}