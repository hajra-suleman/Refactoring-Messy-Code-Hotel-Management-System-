package Customer;

import Database.database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class viewCustomers {

    private JFrame frame;
    private DefaultTableModel model;
    private JTable table;

    public viewCustomers() {
        // GET JFrame
        frame = getJFrame();

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

        // Show All Rooms by Default
        fetchAndDisplayData("SELECT * FROM customers");

        // Add Table to Scroll Pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(52, 17, 63))); // border
        scrollPane.getViewport().setBackground(new Color(52, 17, 63));

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // Frame Design
    private JFrame getJFrame() {
        JFrame frame = new JFrame("View Customers");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(1000, 500);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        return frame;
    }

    // Default Table Model
    private DefaultTableModel getDefaultTableModel() {
        return new DefaultTableModel(new String[]{"Customer ID", "Name", "Email", "Gender", "Points"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Makes all cells non-editable
            }
        };
    }

    private void fetchAndDisplayData(String query) {
        // Clear existing data
        model.setRowCount(0);

        // Fetch data from the database
        try (ResultSet rs = database.executeReadQuery(query)) { // Assuming roomsDB.executeQuery runs a custom query
            while (rs.next()) {
                int customerId = rs.getInt("customerId");
                String customerName = rs.getString("customerName") != null ? rs.getString("customerName") : "N/A";
                String customerEmail = rs.getString("customerEmail");
                String customerGender = rs.getString("customerGender");
                int customerPoints = rs.getInt("customerPoints");
                model.addRow(new Object[]{customerId, customerName, customerEmail, customerGender, customerPoints});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error fetching data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error fetching data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new viewCustomers();
    }
}
