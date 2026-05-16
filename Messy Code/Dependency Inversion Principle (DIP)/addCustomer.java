package Customer;

import Database.database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class addCustomer {

    public addCustomer() {
        // Create the JFrame
        JFrame frame = new JFrame("Add Customer");
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

        // Customer ID
        JLabel idLabel = new JLabel("Customer ID:");
        idLabel.setForeground(whiteText);
        JTextField idField = new JTextField();
        styleTextField(idField);
        mainPanel.add(idLabel);
        mainPanel.add(idField);

        // Customer Name
        JLabel customerNameLabel = new JLabel("Customer Name:");
        customerNameLabel.setForeground(whiteText);
        JTextField customerNameField = new JTextField();
        styleTextField(customerNameField);
        mainPanel.add(customerNameLabel);
        mainPanel.add(customerNameField);

        // Customer Email
        JLabel customerEmailLabel = new JLabel("Customer Email:");
        customerEmailLabel.setForeground(whiteText);
        JTextField emailField = new JTextField();
        styleTextField(emailField);
        mainPanel.add(customerEmailLabel);
        mainPanel.add(emailField);

        // Gender (Radio Buttons)
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setForeground(whiteText);

        JPanel genderCombo = createRadioButtonGroup(new String[]{"male", "female"}, deepPurple, whiteText);
        mainPanel.add(genderCombo);

        mainPanel.add(genderLabel);
        mainPanel.add(genderCombo);

        // Submit Button
        JButton submitButton = new JButton("Submit");
        styleButton(submitButton, lightPurple, deepPurple, hoverColor);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String customerIdString = idField.getText().trim();
                String customerNameString = customerNameField.getText().trim();
                String customerEmailString = emailField.getText().trim();
                String customerGender = getSelectedRadioButton(genderCombo);

                if (customerIdString.isEmpty() || customerNameString.equals("") || customerEmailString.equals("") || customerGender.equals("Nothing")) {
                    JOptionPane.showMessageDialog(frame, "Fill All Details.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Converting String into int(customerID)
                int customerId = Integer.parseInt(customerIdString);

                sendDataToDB(frame, customerId, customerNameString, customerEmailString, customerGender);

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
    private void sendDataToDB(JFrame frame, int customerId, String customerName, String customerEmail, String customerGender) {
        try {
            String query = "INSERT INTO customers (customerId, customerName, customerEmail, customerGender) VALUES (" + customerId + ", '" + customerName + "', '" + customerEmail + "', '" + customerGender + "');";


            database.executeWriteQuery(query);

            JOptionPane.showMessageDialog(frame, "Customer Successfully Created.", "Success", JOptionPane.INFORMATION_MESSAGE);
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

    // Style combo boxes
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Arial", Font.PLAIN, 16));
    }

    // Style spinners
    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(new Font("Arial", Font.PLAIN, 16));
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
        SwingUtilities.invokeLater(() -> new addCustomer());
    }
}