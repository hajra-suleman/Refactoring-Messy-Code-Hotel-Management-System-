package Room;

import Database.database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class deleteRoom {
    public deleteRoom() {
        // Create the JFrame
        JFrame frame = new JFrame("Delete Room");
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

        JLabel idLabel = new JLabel("Enter Room ID to Delete:");
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

        // Submit Button
        JButton submitButton = new JButton("Submit");
        styleButton(submitButton, lightPurple, deepPurple, hoverColor);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String roomIdString = idField.getText().trim();

                if (roomIdString.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Fill Room ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Converting String into int(roomID)
                int roomId = 0;
                try {
                    roomId = Integer.parseInt(roomIdString);

                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(frame, "ROOM ID must be number.", "Error.", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                sendDataToDB(frame, roomId);
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
    private void sendDataToDB(JFrame frame, int roomId) {
        try {

            String query = "SELECT * FROM rooms WHERE roomID = " + roomId + ";";

            ResultSet rs = database.executeReadQuery(query);

            if (rs.next()) {
                String queryToDelete = "DELETE FROM rooms WHERE roomId = " + roomId + ";";
                database.executeWriteQuery(queryToDelete);
                JOptionPane.showMessageDialog(frame, "Room Deleted with ID:" + roomId, "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "No Room Found against this ID.", "Database Error", JOptionPane.ERROR_MESSAGE);
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
        SwingUtilities.invokeLater(() -> new deleteRoom());
    }
}
