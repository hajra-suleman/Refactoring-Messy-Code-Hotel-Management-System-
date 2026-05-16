package InvoicesReports;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;

public class viewInvoices {

    public viewInvoices() {
        JFrame frame = new JFrame("View Invoices");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Directory to display files
        String invoicePath = "PDFs";
        File directory = new File(invoicePath);
        File[] files = directory.listFiles();

        DefaultTableModel model = getDefaultTableModel();


        if (files != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            int index = 1;
            for (int i = files.length - 1; i >= 0; i--) {
                File file = files[i];
                Object[] row = {index++, file.getName(), sdf.format(file.lastModified())};
                model.addRow(row);
            }

        }

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
        table.getTableHeader().setReorderingAllowed(false); // Prevent column reordering

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
        scrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(52, 17, 63))); // border
        scrollPane.getViewport().setBackground(new Color(52, 17, 63));


        // Add action listener to open file when clicked
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                if (row >= 0) {
                    String fileName = table.getValueAt(row, 1).toString();
                    File selectedFile = new File(directory, fileName);
                    try {
                        Desktop.getDesktop().open(selectedFile);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(frame, "Error opening file: " + e.getMessage());
                    }
                }
            }
        });

        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // Default Table Model
    private static DefaultTableModel getDefaultTableModel() {
        return new DefaultTableModel(new String[]{"#", "Name", "Date & Time"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Makes all cells non-editable
            }
        };
    }

    public static void main(String[] args) {
        new viewInvoices();
    }
}