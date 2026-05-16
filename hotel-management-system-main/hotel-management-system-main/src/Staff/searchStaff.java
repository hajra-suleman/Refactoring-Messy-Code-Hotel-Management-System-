package Staff;

import Database.database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class searchStaff {
    JLabel staffIdLabel;
    JLabel staffNameLabel;
    JLabel staffMailLabel;
    JLabel staffGenderLabel;
    JLabel staffSalaryLabel;
    JLabel staffPaidLabel;

    public searchStaff() {
        // Create the JFrame
        JFrame frame = new JFrame("Search Staff");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
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
        mainPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for centering
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(deepPurple);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Room ID Input Section
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(deepPurple);

        JLabel idLabel = new JLabel("Enter Staff ID to Search:");
        idLabel.setFont(new Font("", Font.BOLD, 16));
        idLabel.setForeground(whiteText);

        JTextField idField = new JTextField();
        idField.setPreferredSize(new Dimension(200, 30)); // Limit the input field size
        styleTextField(idField);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(idField, gbc);

        mainPanel.add(inputPanel, gbc);

        // Staff Details Section
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(deepPurple);

        staffIdLabel = createDetailLabel("Staff ID:");
        staffNameLabel = createDetailLabel("Staff Name:");
        staffMailLabel = createDetailLabel("Staff Email:");
        staffGenderLabel = createDetailLabel("Staff Gender:");
        staffSalaryLabel = createDetailLabel("Staff Salary:");
        staffPaidLabel = createDetailLabel("Staff Paid:");

        // Add labels to details panel
        addDetailToPanel(detailsPanel, staffIdLabel, 0);
        addDetailToPanel(detailsPanel, staffNameLabel, 1);
        addDetailToPanel(detailsPanel, staffMailLabel, 2);
        addDetailToPanel(detailsPanel, staffGenderLabel, 3);
        addDetailToPanel(detailsPanel, staffSalaryLabel, 4);
        addDetailToPanel(detailsPanel, staffPaidLabel, 5);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        mainPanel.add(detailsPanel, gbc);

        // Submit Button
        JButton submitButton = new JButton("Submit");
        styleButton(submitButton, lightPurple, deepPurple, hoverColor);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String staffIdString = idField.getText().trim();

                if (staffIdString.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Fill Staff ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Converting String into int(roomID)
                int staffId = 0;
                try {
                    staffId = Integer.parseInt(staffIdString);

                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(frame, "Staff ID must be number.", "Error.", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                sendDataToDB(frame, staffId);
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
    private void sendDataToDB(JFrame frame, int staffId) {
        try {
            String query = "SELECT * FROM staff WHERE staffId = " + staffId + ";";

            ResultSet rs = database.executeReadQuery(query);

            if (rs.next()) {
                staffIdLabel.setText("<html>Staff ID: <b>" + rs.getInt("staffId") + "</b></html>");
                staffNameLabel.setText("<html>Staff Name: <b>" + rs.getString("staffName") + "</b></html>");
                staffMailLabel.setText("<html>Staff Email: <b>" + rs.getString("staffEmail") + "</b></html>");
                staffGenderLabel.setText("<html>Staff Gender: <b>" + rs.getString("staffGender") + "</b></html>");
                staffSalaryLabel.setText("<html>Staff Salary: <b>" + "Rs. " + rs.getDouble("staffSalary") + "</b></html>");
                staffPaidLabel.setText("<html>Staff Paid: <b>" + rs.getString("staffPaid") + "</b></html>");
            } else {
                JOptionPane.showMessageDialog(frame, "No Staff Found against this ID.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (RuntimeException | SQLException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper method to add details to the panel
    private void addDetailToPanel(JPanel panel, JLabel label, int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);
        panel.add(label, gbc);
    }

    // Helper method to create detail labels
    private JLabel createDetailLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("", Font.PLAIN, 16));
        return label;
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new searchStaff());
    }
}
