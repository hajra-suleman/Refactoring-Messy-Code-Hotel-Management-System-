package Staff;

import Database.database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

public class updateStaff {
    JComboBox<Integer> idComboBox;
    ButtonGroup genderGroup, paidGroup;
    JPanel genderPanel, paidPanel;
    JTextField nameField, mailField, salaryField;

    public updateStaff() {
        // Create the JFrame
        JFrame frame = new JFrame("Update Staff");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 500);
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

        idComboBox = new JComboBox<>();
        styleComboBox(idComboBox);
        mainPanel.add(idLabel);
        mainPanel.add(idComboBox);

        // DB CALL TO GET ID in COMBO BOX
        fillComboBox(frame);

        // Combo Box Action Listener
        fetchPreviousDetails(frame);

        // Staff Name
        JLabel nameLabel = new JLabel("Staff Name:");
        nameLabel.setForeground(whiteText);

        nameField = new JTextField();

        mainPanel.add(nameLabel);
        mainPanel.add(nameField);

        // Staff Mail
        JLabel mailLabel = new JLabel("Staff Email:");
        mailLabel.setForeground(whiteText);

        mailField = new JTextField();

        mainPanel.add(mailLabel);
        mainPanel.add(mailField);

        // Gender (Radio Buttons)
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setForeground(whiteText);

        genderGroup = new ButtonGroup();
        genderPanel = createRadioButtonGroup(new String[]{"male", "female"}, deepPurple, whiteText, genderGroup);
        mainPanel.add(genderLabel);
        mainPanel.add(genderPanel);

        // Staff Salary
        JLabel salaryLabel = new JLabel("Salary:");
        salaryLabel.setForeground(whiteText);

        salaryField = new JTextField();

        mainPanel.add(salaryLabel);
        mainPanel.add(salaryField);

        // isPaid (Radio Buttons)
        JLabel paidLabel = new JLabel("is Paid:");
        paidLabel.setForeground(whiteText);

        paidGroup = new ButtonGroup();
        paidPanel = createRadioButtonGroup(new String[]{"yes", "no"}, deepPurple, whiteText, paidGroup);
        mainPanel.add(paidLabel);
        mainPanel.add(paidPanel);

        // Update Button
        JButton updateButton = new JButton("Update Staff");
        styleButton(updateButton, lightPurple, deepPurple, hoverColor);

        updateButtonActionListener(updateButton, frame);

        // Add components to the frame
        frame.add(mainPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(deepPurple);
        buttonPanel.add(updateButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // Action Button Listener
    private void updateButtonActionListener(JButton updateButton, JFrame frame) {
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String staffIdString = idComboBox.getSelectedItem().toString();
                String staffNameString = nameField.getText();
                String staffMailString = mailField.getText();
                String staffSalaryString = salaryField.getText();

                // Extract values from radio button
                String genderString = getSelectedRadioButton(genderPanel);
                String paidString = getSelectedRadioButton(paidPanel);

                if (staffIdString.isEmpty() || staffNameString.isEmpty() || staffMailString.isEmpty() || staffSalaryString.isEmpty() || genderString.equals("Not Selected") || paidString.equals("Not Selected")) {
                    JOptionPane.showMessageDialog(frame, "Fill All Details.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Converting String into int(staffID)
                int staffId = Integer.parseInt(staffIdString);

                // Converting String into double(staff Salary)
                double staffSalary = Double.parseDouble(staffSalaryString);

                sendDataToDB(frame, staffId, staffNameString, staffMailString, genderString, staffSalary, paidString);
            }
        });
    }

    // Fill Staff ID Combo Box by Database Call.
    private void fillComboBox(JFrame frame) {
        try (ResultSet rs = database.executeReadQuery("SELECT * FROM staff")) {
            while (rs.next()) {
                idComboBox.addItem(rs.getInt("staffId"));
            }
        } catch (RuntimeException | SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error fetching data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error fetching data: " + e.getMessage());
        }
    }

    // Get all Details of Selected Staff ID. (Completed)
    private void fetchPreviousDetails(JFrame frame) {
        idComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer staffIdFromComboBox = (Integer) idComboBox.getSelectedItem();
                try {
                    String query = "SELECT * FROM staff WHERE staffId = " + staffIdFromComboBox + ";";

                    ResultSet rs = database.executeReadQuery(query);

                    if (rs.next()) {
                        String staffName = rs.getString("staffName") != null ? rs.getString("staffName") : "N/A";
                        String staffEmail = rs.getString("staffEmail") != null ? rs.getString("staffEmail") : "N/A";
                        String staffGender = rs.getString("staffGender") != null ? rs.getString("staffGender") : "N/A";
                        double staffSalary = rs.getDouble("staffSalary");
                        String staffPaid = rs.getString("staffPaid") != null ? rs.getString("staffPaid") : "N/A";

                        // Inserting Data
                        setRadioButtonSelection(genderGroup, staffGender);
                        setRadioButtonSelection(paidGroup, staffPaid);
                        nameField.setText(staffName);
                        mailField.setText(staffEmail);
                        salaryField.setText(String.valueOf(staffSalary));

                    }


                } catch (RuntimeException | SQLException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Update Data Database Call.
    private void sendDataToDB(JFrame frame, int staffId, String staffName, String staffMail, String staffGender, double staffSalary, String staffPaid) {
        try {
            // Construct the SQL UPDATE query
            String query = "UPDATE staff SET " +
                    "staffName = '" + staffName + "', " +
                    "staffEmail = '" + staffMail + "', " +
                    "staffGender = '" + staffGender + "', " + "staffSalary = " + staffSalary + ", " +
                    "staffPaid = '" + staffPaid + "' " +
                    "WHERE staffId = " + staffId + ";";


            // Execute the query
            database.executeWriteQuery(query);
            JOptionPane.showMessageDialog(frame, "Staff Details Updated", "Success", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
        } catch (RuntimeException ex) {
            // Handle any exceptions
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Set Default Values to Radio Buttons
    private void setRadioButtonSelection(ButtonGroup group, String label) {
        for (AbstractButton button : Collections.list(group.getElements())) {
            if (button.getText().equals(label)) {
                button.setSelected(true);
                break;
            }
        }
    }

    // Style combo boxes
    private void styleComboBox(JComboBox<Integer> comboBox) {
        comboBox.setFont(new Font("", Font.PLAIN, 16));
    }

    // Style buttons
    private void styleButton(JButton button, Color backgroundColor, Color foregroundColor, Color hoverColor) {
        button.setBackground(backgroundColor);
        button.setForeground(foregroundColor);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            private final Color originalColor = button.getBackground();

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });
    }

    // Create a panel with grouped radio buttons
    private JPanel createRadioButtonGroup(String[] options, Color background, Color textColor, ButtonGroup group) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(background);

        for (String option : options) {
            JRadioButton radioButton = new JRadioButton(option);
            radioButton.setBackground(background);
            radioButton.setForeground(textColor);
            radioButton.setFont(new Font("Arial", Font.PLAIN, 16));
            radioButton.setFocusPainted(false);
            group.add(radioButton); // Add the button to the group
            panel.add(radioButton); // Add the button to the panel
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
        return "Not Selected"; // Default if none selected
    }

    public static void main(String[] args) {
        new updateStaff();
    }
}