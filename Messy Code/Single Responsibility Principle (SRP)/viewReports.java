package InvoicesReports;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static Database.database.connectDB;

public class viewReports {

    public viewReports() {
        JFrame frame = new JFrame("View Reports");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        DefaultTableModel model = getDefaultTableModel();

        // Fetch data from the database
        int totalGains = 0;
        int totalExpenditures = 0;
        try (Connection conn = connectDB(); Statement stmt = conn.createStatement()) {
            // Gains
            ResultSet rs = stmt.executeQuery("SELECT SUM(totalRent) AS total_gains FROM booking");
            if (rs.next()) {
                totalGains = rs.getInt("total_gains");
            }

            // Expenditures
            rs = stmt.executeQuery("SELECT SUM(staffSalary) AS total_expenditures FROM staff WHERE staffPaid = 'yes'");
            if (rs.next()) {
                totalExpenditures = rs.getInt("total_expenditures");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error fetching data from the database: " + e.getMessage());
        }

        // Add the data to the table model
        Object[] row = {"Total Gains", "Rs. " + totalGains};
        model.addRow(row);
        row = new Object[]{"Total Expenditures", "Rs. " + totalExpenditures};
        model.addRow(row);

        // Calculate Net Result (Profit or Loss)
        int netResult = totalGains - totalExpenditures;

        JTable table = new JTable(model) {
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

        // Prevent column reordering
        table.getTableHeader().setReorderingAllowed(false);

        // Table Header Styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(52, 17, 63));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("", Font.BOLD, 16));

        // Table Styling
        table.setRowHeight(35);
        table.setFont(new Font("", Font.PLAIN, 14));

        // Add Table to Scroll Pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(52, 17, 63))); // Border
        scrollPane.getViewport().setBackground(new Color(52, 17, 63));

        // Create a panel for the Net Result at the bottom
        JPanel netResultPanel = getResultPanel(netResult);

        // Adding components to the frame
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(netResultPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static JPanel getResultPanel(int netResult) {
        JPanel netResultPanel = new JPanel();
        netResultPanel.setLayout(new BorderLayout());
        netResultPanel.setBackground(new Color(52, 17, 63));

        String netResultText = "<html><span style='color:white;'>Net Result: Rs. </span>" + "<span style='color:" + (netResult < 0 ? "red" : "green") + "; font-size:22px;'>" + netResult + "</span></html>";

        JLabel netResultLabel = new JLabel(netResultText, JLabel.CENTER);
        netResultLabel.setFont(new Font("", Font.BOLD, 25));

        netResultPanel.add(netResultLabel, BorderLayout.CENTER);

        return netResultPanel;
    }


    // Default Table Model
    private static DefaultTableModel getDefaultTableModel() {
        return new DefaultTableModel(new String[]{"Description", "Amount"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Makes all cells non-editable
            }
        };
    }

    public static void main(String[] args) {
        new viewReports();
    }
}
