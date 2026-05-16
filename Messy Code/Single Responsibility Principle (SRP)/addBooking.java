package Booking;

import Database.database;
import InvoicesReports.invoiceGeneration;
import Mail.emailSender;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static Mail.emailSender.sendEmail;

public class addBooking {
    JComboBox<Integer> customerComboBox;
    static JComboBox<Integer> roomComboBox;
    static JLabel pointsValue;
    static JLabel priceValue;
    double roomRentPerNight, totalRent;
    static Date checkInDate;
    static Date checkOutDate;
    int loyaltyPoints;

    public addBooking() {
        JFrame frame = new JFrame("Add Booking");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(51, 0, 51));

        // Customer ComboBox
        addLabel(frame, "Customer:", 40, new Color(255, 255, 255));
        customerComboBox = addComboBox(frame, fillCustomerComboBoxByDB(frame), 450, 40);
        customerComboBox.setFont(new Font("", Font.PLAIN, 16));

        // Fetch Loyalty Points
        fetchLoyaltyPoints(customerComboBox, frame);

        // Room ComboBox
        addLabel(frame, "Room:", 90, new Color(255, 255, 255));
        roomComboBox = addComboBox(frame, fillRoomComboBoxByDB(frame), 450, 90);
        roomComboBox.setFont(new Font("", Font.PLAIN, 16));

        // Fetch Room Rent Per Night
        fetchRoomRent(roomComboBox, frame);

        // Check-in Date Picker
        addLabel(frame, "Check-in Date:", 140, new Color(255, 255, 255)); // White label
        JSpinner checkInDateSpinner = addDateSpinner(frame, 140);

        // Check-out Date Picker
        addLabel(frame, "Check-out Date:", 190, new Color(255, 255, 255)); // White label
        JSpinner checkOutDateSpinner = addDateSpinner(frame, 190);

        // Price Label
        addLabel(frame, "Price Per Night:", 240, new Color(255, 255, 255)); // White label
        priceValue = addDynamicLabel(frame, "Rs. 0", 240, new Color(0, 255, 0)); // Green price label

        // Loyalty Points Label
        addLabel(frame, "Loyalty Points:", 290, new Color(255, 255, 255));
        pointsValue = addDynamicLabel(frame, "0", 290, new Color(0, 255, 0));

        // Calculate Button
        JButton calculateButton = new JButton("Confirm Booking");
        calculateButton.setBounds(300, 350, 200, 40);
        calculateButton.setFont(new Font("", Font.BOLD, 18));
        calculateButton.setBackground(new Color(204, 204, 255)); // Light Purple button
        calculateButton.setForeground(new Color(51, 0, 51)); // Dark Purple text
        calculateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        frame.add(calculateButton);

        // MouseListener for Hover Effect
        calculateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                calculateButton.setBackground(Color.WHITE); // Change background on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                calculateButton.setBackground(new Color(204, 204, 255)); // Change back when hover ends
            }
        });

        // Action Listener for Button
        calculateButton.addActionListener(e -> {
            try {
                checkInDate = (Date) checkInDateSpinner.getValue();
                checkOutDate = (Date) checkOutDateSpinner.getValue();

                // Validate date range
                if (checkOutDate.before(checkInDate)) {
                    JOptionPane.showMessageDialog(frame, "Check-out date must be after check-in date.");
                    return;
                }

                // Normalize dates to midnight
                checkInDate = normalizeToMidnight(checkInDate);
                checkOutDate = normalizeToMidnight(checkOutDate);
                long duration = 0;

                if (checkOutDate.equals(checkInDate)) {
                    duration = 1;
                    totalRent = roomRentPerNight - loyaltyPoints;
                } else {
                    duration = (checkOutDate.getTime() - checkInDate.getTime()) / (1000 * 60 * 60 * 24);
                    totalRent = (duration * roomRentPerNight) - loyaltyPoints;
                }

                String customerName = null;
                String customerEmail = null;

                try {
                    String query = "SELECT * FROM customers WHERE customerID = " + customerComboBox.getSelectedItem() + ";";
                    ResultSet rs = database.executeReadQuery(query);

                    if (rs.next()) {
                        customerName = rs.getString("customerName");
                        customerEmail = rs.getString("customerEmail");
                    }

                } catch (RuntimeException | SQLException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }

                sendDataToDb(frame);
                makeRoomUnavailable(frame);
                increasingCustomerPoints(frame, totalRent);

                emailBookingNotification(customerName, customerEmail, customerComboBox.getSelectedItem().toString(), roomComboBox.getSelectedItem().toString(), totalRent, loyaltyPoints, checkInDate, checkOutDate, duration, roomRentPerNight);

                invoiceGeneration.invoiceGenerate(customerName, customerEmail, customerComboBox.getSelectedItem().toString(), roomComboBox.getSelectedItem().toString(), totalRent, loyaltyPoints, checkInDate, checkOutDate, duration, roomRentPerNight);

                JOptionPane.showMessageDialog(frame, "Room Availability Set to False, and Customers Points are Added.", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        // Show Frame
        frame.setVisible(true);
    }

    // Database Call to Get Customers-IDs
    private ArrayList<Integer> fillCustomerComboBoxByDB(JFrame frame) {
        ArrayList<Integer> listToSend = new ArrayList<>();
        try (ResultSet rs = database.executeReadQuery("SELECT * FROM customers")) {
            while (rs.next()) {
                listToSend.add(rs.getInt("customerId"));
            }
        } catch (RuntimeException | SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error fetching data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error fetching data: " + e.getMessage());
        }
        return listToSend;
    }

    // Database Call to Get Room-IDs
    private ArrayList<Integer> fillRoomComboBoxByDB(JFrame frame) {
        ArrayList<Integer> listToSend = new ArrayList<>();
        try (ResultSet rs = database.executeReadQuery("SELECT * FROM rooms WHERE isAvailable")) {
            while (rs.next()) {
                listToSend.add(rs.getInt("roomId"));
            }
        } catch (RuntimeException | SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error fetching data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error fetching data: " + e.getMessage());
        }
        return listToSend;
    }

    // Database Call to Fetch Loyalty Points
    private void fetchLoyaltyPoints(JComboBox<Integer> customerComboBox, JFrame frame) {
        customerComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer customerIdFromComboBox = (Integer) customerComboBox.getSelectedItem();
                try {
                    String query = "SELECT * FROM customers WHERE customerID = " + customerIdFromComboBox + ";";
                    ResultSet rs = database.executeReadQuery(query);

                    if (rs.next()) {
                        loyaltyPoints = rs.getInt("customerPoints");
                        pointsValue.setText(String.valueOf(loyaltyPoints));
                    }

                } catch (RuntimeException | SQLException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Database Call to Fetch Room Type and Calculate Price
    private void fetchRoomRent(JComboBox<Integer> roomComboBox, JFrame frame) {
        roomComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer roomIdFromComboBox = (Integer) roomComboBox.getSelectedItem();
                try {
                    String query = "SELECT * FROM rooms WHERE roomID = " + roomIdFromComboBox + ";";

                    ResultSet rs = database.executeReadQuery(query);

                    if (rs.next()) {
                        String roomType = rs.getString("roomType");
                        if (roomType.equals("Single")) {
                            roomRentPerNight = 1000;
                            priceValue.setText("Rs. 1000");
                        } else if (roomType.equals("Double")) {
                            roomRentPerNight = 2000;
                            priceValue.setText("Rs. 2000");
                        } else {
                            roomRentPerNight = 3500;
                            priceValue.setText("Rs. 3500");
                        }
                    }

                } catch (RuntimeException | SQLException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Making Room Unavailable
    private void makeRoomUnavailable(JFrame frame) {
        try {
            int roomId = Integer.parseInt(roomComboBox.getSelectedItem().toString());

            String query = "UPDATE rooms SET isAvailable = false WHERE roomId = " + roomId;
            database.executeWriteQuery(query);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(frame, "Error fetching data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error Setting Room Unavailable: " + e.getMessage());
        }

    }

    // Increasing Customer Loyalty Points
    private void increasingCustomerPoints(JFrame frame, double totalRent) {
        // Calculate points to add (rounding to avoid decimal issues)
        int pointsToAdd = (int) Math.round((totalRent * 2) / 100);
        loyaltyPoints += pointsToAdd; // Add new points to existing loyalty points

        try {
            int customerId = Integer.parseInt(customerComboBox.getSelectedItem().toString());

            String query = "UPDATE customers SET customerPoints = " + loyaltyPoints + " WHERE customerId = " + customerId;

            database.executeWriteQuery(query);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(frame, "Error updating loyalty points: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error updating loyalty points: " + e.getMessage());
        }
    }

    // Final Database Call to Add Booking
    private void sendDataToDb(JFrame frame) {

        // Normalising Dates
        Date checkInDateNormalized = checkInDate;
        Date checkOutDateNormalized = checkOutDate;

        // Data to Send
        int roomId = Integer.parseInt(roomComboBox.getSelectedItem().toString()); // Example for roomId
        int customerId = Integer.parseInt(customerComboBox.getSelectedItem().toString()); // Example for customerId

        java.sql.Date sqlCheckInDate = new java.sql.Date(checkInDateNormalized.getTime());
        java.sql.Date sqlCheckOutDate = new java.sql.Date(checkOutDateNormalized.getTime());

        try {
            String query = "INSERT INTO booking (roomId, customerId, totalRent, checkIn, checkOut) VALUES (" + roomId + ", " + customerId + ", " + totalRent + ", '" + sqlCheckInDate + "', '" + sqlCheckOutDate + "');";

            database.executeWriteQuery(query);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    // Sending Email
    public static void emailBookingNotification(
            String customerName, String customerEmail, String customerId, String roomId,
            double totalRent, int loyaltyPoints, Date checkInDate, Date checkOutDate,
            long duration, double roomRentPerNight) {

        // Date Formatting
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM dd, yyyy");
        Date currentDate = new Date();
        String bookingDateTime = new SimpleDateFormat("hh:mm:ss a, dd MMM yyyy").format(currentDate);

        // Calculate Gross Rent
        double grossRent = duration * roomRentPerNight;
        String roomType = roomRentPerNight == 1000 ? "Single" : roomRentPerNight == 2000 ? "Double" : "Suite";

        // Compose Email Content
        String emailContent = "Dear " + customerName + ",\n\n"
                + "Thank you for booking with us. Here are your booking details:\n\n"
                + "Guest Information:\n"
                + "  - Name: " + customerName + "\n"
                + "  - Email: " + customerEmail + "\n"
                + "  - Guest ID: " + customerId + "\n\n"
                + "Room Information:\n"
                + "  - Room ID: " + roomId + "\n"
                + "  - Room Type: " + roomType + "\n"
                + "  - Room Rent Per Night: Rs. " + String.format("%.2f", roomRentPerNight) + "\n\n"
                + "Booking Details:\n"
                + "  - Check-In Date: " + dateFormatter.format(checkInDate) + "\n"
                + "  - Check-Out Date: " + dateFormatter.format(checkOutDate) + "\n"
                + "  - Duration: " + duration + " days\n"
                + "  - Gross Rent: Rs. " + String.format("%.2f", grossRent) + "\n"
                + "  - Discounted Total Rent (after Loyalty Points): Rs. " + String.format("%.2f", totalRent) + "\n"
                + "  - Loyalty Points Available: " + loyaltyPoints + "\n\n"
                + "Booking Summary:\n"
                + "  - Booking Date and Time: " + bookingDateTime + "\n\n"
                + "We look forward to hosting you!\n\n"
                + "Best regards,\n"
                + "Hello World Hotel Management System";

        // Send Email
        sendEmail(customerEmail, "Booking Confirmation - Hello World HMS", emailContent);
        System.out.println("Booking email sent successfully to: " + customerEmail);
    }

    // To Add Combo Box (Value are passed as a parameter)
    private JComboBox<Integer> addComboBox(JFrame frame, ArrayList<Integer> customerIDs, int x, int y) {
        // Convert ArrayList<Integer> to Integer[] for JComboBox
        Integer[] options = customerIDs.toArray(new Integer[0]);

        // Create the JComboBox with customer IDs
        JComboBox<Integer> comboBox = new JComboBox<>(options);

        // Set the position and size of the combo box
        comboBox.setBounds(x, y, 300, 35);

        // Set background and foreground colors for the combo box
        comboBox.setBackground(new Color(204, 204, 255)); // Light Purple background
        comboBox.setForeground(new Color(51, 0, 51)); // Dark Purple text

        // Add the combo box to the frame
        frame.add(comboBox);

        // Return the combo box with the customer IDs
        return comboBox;
    }

    // Utility Methods
    private void addLabel(JFrame frame, String text, int y, Color textColor) {
        JLabel label = new JLabel(text);
        label.setBounds(80, y, 200, 35);
        label.setForeground(textColor); // Set label color
        label.setFont(new Font("", Font.BOLD, 18));
        frame.add(label);
    }

    private JLabel addDynamicLabel(JFrame frame, String text, int y, Color textColor) {
        JLabel label = new JLabel(text);
        label.setBounds(450, y, 200, 35);
        label.setForeground(textColor); // Set label color
        label.setFont(new Font("", Font.BOLD, 18));
        frame.add(label);
        return label;
    }

    private JSpinner addDateSpinner(JFrame frame, int y) {
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setBounds(450, y, 300, 35);
        frame.add(dateSpinner);
        return dateSpinner;
    }

    // Solving Problem of Duration of Dates
    private Date normalizeToMidnight(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static void main(String[] args) {
        new addBooking();
    }
}