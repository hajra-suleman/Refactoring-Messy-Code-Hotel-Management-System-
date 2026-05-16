package Room;

import Database.database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

public class updateRoom {
    JComboBox<Integer> idComboBox;
    JComboBox<String> typeCombo;
    JSpinner bedsSpinner;
    ButtonGroup availableGroup;
    ButtonGroup cleanGroup;
    ButtonGroup smokingGroup;

    public updateRoom() {
        // Create the JFrame
        JFrame frame = new JFrame("Update Room");
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

        // Room ID
        JLabel idLabel = new JLabel("Room ID:");
        idLabel.setForeground(whiteText);

        idComboBox = new JComboBox<>();
        mainPanel.add(idLabel);
        mainPanel.add(idComboBox);

        // DB CALL TO GET ID in COMBO BOX
        fillComboBox(frame);

        // Combo Box Action Listener
        fetchPreviousDetails(frame);

        // Room Type
        JLabel typeLabel = new JLabel("Room Type:");
        typeLabel.setForeground(whiteText);
        typeCombo = new JComboBox<>(new String[]{"Single", "Double", "Suite"});
        styleComboBox(typeCombo);
        mainPanel.add(typeLabel);
        mainPanel.add(typeCombo);

        // Bed Numbers
        JLabel bedsLabel = new JLabel("Number of Beds:");
        bedsLabel.setForeground(whiteText);
        bedsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        styleSpinner(bedsSpinner);
        mainPanel.add(bedsLabel);
        mainPanel.add(bedsSpinner);

        // Availability (Radio Buttons)
        JLabel availableLabel = new JLabel("Is Available:");
        availableLabel.setForeground(whiteText);

        availableGroup = new ButtonGroup();
        JPanel availablePanel = createRadioButtonGroup(new String[]{"true", "false"}, deepPurple, whiteText, availableGroup);
        mainPanel.add(availableLabel);
        mainPanel.add(availablePanel);

        // Is Clean (Radio Buttons)
        JLabel cleanLabel = new JLabel("Is Clean:");
        cleanLabel.setForeground(whiteText);

        cleanGroup = new ButtonGroup();
        JPanel cleanPanel = createRadioButtonGroup(new String[]{"true", "false"}, deepPurple, whiteText, cleanGroup);
        mainPanel.add(cleanLabel);
        mainPanel.add(cleanPanel);

        // Smoking Allowed (Radio Buttons)
        JLabel smokingLabel = new JLabel("Is Smoking Allowed:");
        smokingLabel.setForeground(whiteText);

        smokingGroup = new ButtonGroup();
        JPanel smokingPanel = createRadioButtonGroup(new String[]{"true", "false"}, deepPurple, whiteText, smokingGroup);
        mainPanel.add(smokingLabel);
        mainPanel.add(smokingPanel);

        // Update Button
        JButton updateButton = new JButton("Update Room");
        styleButton(updateButton, lightPurple, deepPurple, hoverColor);

        updateButtonActionListener(updateButton, availablePanel, cleanPanel, smokingPanel, frame);

        // Add components to the frame
        frame.add(mainPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(deepPurple);
        buttonPanel.add(updateButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // Action Button Listener
    private void updateButtonActionListener(JButton updateButton, JPanel availablePanel, JPanel cleanPanel, JPanel smokingPanel, JFrame frame) {
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String roomIdString = idComboBox.getSelectedItem().toString();
                String roomType = (String) typeCombo.getSelectedItem();
                int bedNumbers = (int) bedsSpinner.getValue();

                // Extract values from radio buttons
                String isAvailableString = getSelectedRadioButton(availablePanel);
                String isCleanString = getSelectedRadioButton(cleanPanel);
                String isSmokingString = getSelectedRadioButton(smokingPanel);
                System.out.println(isAvailableString + " " + isCleanString + " " + isSmokingString);

                if (roomIdString.isEmpty() || isAvailableString.equals("Not Selected") || isCleanString.equals("Not Selected") || isSmokingString.equals("Not Selected")) {
                    JOptionPane.showMessageDialog(frame, "Fill All Details.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Converting String into int(roomID)
                int roomId = Integer.parseInt(roomIdString);

                // Converting Strings into Boolean(radioButtons)
                boolean isAvailable = Boolean.parseBoolean(isAvailableString);
                boolean isClean = Boolean.parseBoolean(isCleanString);
                boolean isSmoking = Boolean.parseBoolean(isSmokingString);
                sendDataToDB(frame, roomId, roomType, bedNumbers, isAvailable, isClean, isSmoking);

            }
        });
    }

    // Fill Room ID Combo Box by Database Call.
    private void fillComboBox(JFrame frame) {
        try (ResultSet rs = database.executeReadQuery("SELECT * FROM rooms")) {
            while (rs.next()) {
                idComboBox.addItem(rs.getInt("roomId"));
            }
        } catch (RuntimeException | SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error fetching data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error fetching data: " + e.getMessage());
        }
    }

    // Get all Details of Selected Room ID.
    private void fetchPreviousDetails(JFrame frame) {
        idComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer roomIdFromComboBox = (Integer) idComboBox.getSelectedItem();
                try {
                    String query = "SELECT * FROM rooms WHERE roomID = " + roomIdFromComboBox + ";";

                    ResultSet rs = database.executeReadQuery(query);

                    if (rs.next()) {
                        String roomType = rs.getString("roomType") != null ? rs.getString("roomType") : "N/A";
                        int numberOfBeds = rs.getInt("bedNumbers");
                        boolean isAvailable = rs.getBoolean("isAvailable");
                        boolean isClean = rs.getBoolean("isClean");
                        boolean isSmoking = rs.getBoolean("isSmoking");

                        typeCombo.setSelectedItem(roomType);
                        bedsSpinner.setValue(numberOfBeds);

                        setRadioButtonSelection(availableGroup, isAvailable ? "true" : "false");
                        setRadioButtonSelection(cleanGroup, isClean ? "true" : "false");
                        setRadioButtonSelection(smokingGroup, isSmoking ? "true" : "false");

                    }


                } catch (RuntimeException | SQLException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Update Data Database Call.
    private void sendDataToDB(JFrame frame, int roomId, String roomType, int bedNumbers, boolean isAvailable, boolean isClean, boolean isSmoking) {
        try {
            // Construct the SQL UPDATE query
            String query = "UPDATE rooms " + "SET roomType = '" + roomType + "', " + "bedNumbers = " + bedNumbers + ", " + "isAvailable = " + isAvailable + ", " + "isClean = " + isClean + ", " + "isSmoking = " + isSmoking + " " + "WHERE roomId = " + roomId + ";";

            // Execute the query
            database.executeWriteQuery(query);
            JOptionPane.showMessageDialog(frame, "Room Details Updated", "Success", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose(); // Close the frame
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
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("", Font.PLAIN, 16));
    }

    // Style spinners
    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(new Font("", Font.PLAIN, 16));
    }

    // Style buttons
    private void styleButton(JButton button, Color backgroundColor, Color foregroundColor, Color hoverColor) {
        button.setBackground(backgroundColor);
        button.setForeground(foregroundColor);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
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
        new updateRoom();
    }
}