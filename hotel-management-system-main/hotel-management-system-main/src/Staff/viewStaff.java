package Staff;

import Database.database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class viewStaff {

    private JFrame frame;
    private DefaultTableModel model;
    private JTable table;

    public viewStaff() {
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

        // Show All Staff by Default
        fetchAndDisplayData("SELECT * FROM staff");

        // Add Table to Scroll Pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(52, 17, 63))); // border
        scrollPane.getViewport().setBackground(new Color(52, 17, 63));

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // Frame Design
    private JFrame getJFrame() {
        JFrame frame = new JFrame("View Staff");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(1000, 500);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        return frame;
    }

    // Default Table Model
    private DefaultTableModel getDefaultTableModel() {
        return new DefaultTableModel(new String[]{"Staff ID", "Name", "Email", "Gender", "Salary", "isPaid"}, 0) {
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
        try (ResultSet rs = database.executeReadQuery(query)) {
            while (rs.next()) {
                int staffId = rs.getInt("staffId");
                String staffName = rs.getString("staffName") != null ? rs.getString("staffName") : "N/A";
                String staffEmail = rs.getString("staffEmail");
                String staffGender = rs.getString("staffGender");
                double staffSalary = rs.getDouble("staffSalary");
                String staffPaid = rs.getString("staffPaid");
                model.addRow(new Object[]{staffId, staffName, staffEmail, staffGender, "Rs. " + staffSalary, staffPaid});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error fetching data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error fetching data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new viewStaff();
    }
}
