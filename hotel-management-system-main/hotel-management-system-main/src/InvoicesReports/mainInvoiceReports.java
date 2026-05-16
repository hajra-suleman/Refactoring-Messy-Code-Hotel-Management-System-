package InvoicesReports;

import GraphicalUserInterface.setupGUIafter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class mainInvoiceReports {
    public mainInvoiceReports() {
        setupGUIafter app = new setupGUIafter("INVOICES & REPORTS");

        // Main Panel with BorderLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(33, 26, 30));

        JLabel mainLabel = new JLabel("<html><div style='padding-top: 20px'>INVOICE OPTIONS</div></html>");
        mainLabel.setFont(new Font("Noto Sans Mono", Font.BOLD, 38));
        mainLabel.setHorizontalAlignment(JLabel.CENTER);
        mainLabel.setForeground(Color.white);

        JPanel buttonMainPanel = new JPanel();
        buttonMainPanel.setOpaque(false);
        buttonMainPanel.setLayout(new BoxLayout(buttonMainPanel, BoxLayout.Y_AXIS));

        buttonMainPanel.setBorder(BorderFactory.createEmptyBorder(350, 150, 0, 0));

        String[] values = new String[]{"VIEW INVOICES", "VIEW REPORTS"};
        for (String value : values) {
            JButton button = getJButton(value);
            buttonMainPanel.add(button);
            buttonMainPanel.add(Box.createVerticalStrut(10));
        }

        JPanel imagePanel = new JPanel();
        imagePanel.setOpaque(false);
        imagePanel.setLayout(new BorderLayout());
        JLabel imageLabel = new JLabel();
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/invoice.png"));
        imagePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 150));
        imageLabel.setIcon(imageIcon);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        mainPanel.add(mainLabel, BorderLayout.NORTH);
        mainPanel.add(buttonMainPanel, BorderLayout.WEST);
        mainPanel.add(imagePanel, BorderLayout.EAST);

        SwingUtilities.invokeLater(() -> app.setCenterPanel(mainPanel));
    }

    private static JButton getJButton(String value) {
        JButton button = new JButton(value);
        button.setMaximumSize(new Dimension(400, 80));
        button.setFocusable(false);
        button.setBackground(new Color(190, 183, 223));
        button.setForeground(new Color(33, 26, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFont(new Font("Noto Sans Mono", Font.BOLD, 22));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 220, 220));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(190, 183, 223));
            }
        });

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("VIEW INVOICES")) {
                    new viewInvoices();
                }else {
                    new viewReports();
                }
            }
        });

        button.setContentAreaFilled(false);
        button.setOpaque(true);
        return button;
    }

    public static void main(String[] args) {
        new mainInvoiceReports();
    }

}
