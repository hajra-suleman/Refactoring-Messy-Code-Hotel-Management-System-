package Room;

import Database.database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class viewRooms {

    private JFrame frame;
    private DefaultTableModel model;
    private JTable table;

    public viewRooms() {
        // GET JFrame
        frame = getJFrame();

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton viewAllButton = new JButton("View All Rooms");
        JButton viewAvailableButton = new JButton("View Available Rooms");
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(52, 17, 63)); // Purplish background

        // Initial button colors
        viewAllButton.setBackground(new Color(190, 183, 223)); // Light purple
        viewAvailableButton.setBackground(new Color(190, 183, 223));
        viewAllButton.setForeground(new Color(52, 17, 63)); // Purplish text
        viewAvailableButton.setForeground(new Color(52, 17, 63));
        viewAllButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewAvailableButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect to buttons
        addHoverEffect(viewAllButton);
        addHoverEffect(viewAvailableButton);

        // Button Panel Adding
        buttonPanel.add(viewAllButton);
        buttonPanel.add(viewAvailableButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Table Setup
        model = getDefaultTableModel();
        table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                if (isRowSelected(row)) {
                    component.setBackground(new Color(190, 183, 223)); // Soft purple background
                } else {
                    component.setBackground(Color.WHITE); // Pure white background for cells
                }
                return component;
            }
        };
        table.getTableHeader().setReorderingAllowed(false); // Prevent column reordering

        // Table Header Styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(52, 17, 63)); // Purplish background
        header.setForeground(Color.WHITE); // White text
        header.setFont(new Font("Arial", Font.BOLD, 16)); // Bold and larger font for header

        // Table Styling
        table.setRowHeight(35); // Increase row height for better readability
        table.setFont(new Font("Arial", Font.PLAIN, 14)); // Set a readable font for table cells

        // Button Styling: Increased size and bold font
        styleButton(viewAllButton);
        styleButton(viewAvailableButton);

        // Button Actions
        viewAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchAndDisplayData("SELECT * FROM rooms");
            }
        });

        viewAvailableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchAndDisplayData("SELECT * FROM rooms WHERE isAvailable = true");
            }
        });

        // Show All Rooms by Default
        fetchAndDisplayData("SELECT * FROM rooms");

        // Add Table to Scroll Pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(52, 17, 63))); // border
        scrollPane.getViewport().setBackground(new Color(52, 17, 63));

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // Frame Design
    private JFrame getJFrame() {
        JFrame frame = new JFrame("View Rooms");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(1000, 500);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        return frame;
    }

    // Default Table Model
    private DefaultTableModel getDefaultTableModel() {
        return new DefaultTableModel(new String[]{"Room ID", "Room Type", "No. of Beds", "Available", "Clean", "Smoking"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Makes all cells non-editable
            }
        };
    }

    // Button Styling Method
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Bold and large font
        button.setFocusPainted(false); // Remove focus highlight
        button.setBorder(BorderFactory.createLineBorder(new Color(52, 17, 63), 1)); // Subtle border
        button.setPreferredSize(new Dimension(180, 50)); // Larger button size
    }

    // Add hover effect to button
    private void addHoverEffect(JButton button) {
        Color defaultBackground = button.getBackground();
        Color hoverBackground = Color.WHITE;

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverBackground); // Change to white on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(defaultBackground); // Revert to default color
            }
        });
    }

    private void fetchAndDisplayData(String query) {
        // Clear existing data
        model.setRowCount(0);

        // Fetch data from the database
        try (ResultSet rs = database.executeReadQuery(query)) { // Assuming roomsDB.executeQuery runs a custom query
            while (rs.next()) {
                int roomID = rs.getInt("roomId");
                String roomType = rs.getString("roomType") != null ? rs.getString("roomType") : "N/A";
                int numberOfBeds = rs.getInt("bedNumbers");
                boolean isAvailable = rs.getBoolean("isAvailable");
                boolean isClean = rs.getBoolean("isClean");
                boolean isSmoking = rs.getBoolean("isSmoking");
                model.addRow(new Object[]{roomID, roomType, numberOfBeds, isAvailable, isClean, isSmoking});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error fetching data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error fetching data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new viewRooms();
    }
}
