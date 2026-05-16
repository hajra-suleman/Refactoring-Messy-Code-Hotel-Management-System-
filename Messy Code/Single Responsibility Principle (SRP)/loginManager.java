package Manager;

import Database.database;
import GraphicalUserInterface.Dashboard;
import GraphicalUserInterface.HotelManagementSystem;
import Mail.emailSender;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class loginManager extends JFrame implements ActionListener {

    private final JPanel panel;
    private final JLabel pageTitle;
    private final JLabel managerPassword;
    private final JPasswordField managerPasswordField;
    private final JLabel managerEmail;
    private final JTextField managerEmailField;
    private final JButton submitButton;
    private final JButton backButton;

    public loginManager() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);

        panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(52, 17, 63));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.LINE_START;

        // Title
        pageTitle = new JLabel("Manager Log-in Form");
        pageTitle.setFont(new Font("Noto Sans Mono", Font.BOLD, 28));
        pageTitle.setForeground(Color.white);
        pageTitle.setBorder(BorderFactory.createEmptyBorder(0, 170, 0, 0));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(pageTitle, gbc);

        // Mobile label and field
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        managerEmail = new JLabel("Email:");
        managerEmail.setForeground(Color.white);
        managerEmail.setFont(new Font("Noto Sans Mono", Font.BOLD, 18));
        panel.add(managerEmail, gbc);

        gbc.gridx = 1;
        managerEmailField = new JTextField();
        managerEmailField.setPreferredSize(new Dimension(500, 40));
        managerEmailField.setFont(new Font("Noto Sans Mono", Font.BOLD, 18));
        panel.add(managerEmailField, gbc);

        // Email label and field
        gbc.gridx = 0;
        gbc.gridy = 2;


        managerPassword = new JLabel("Password:");
        managerPassword.setForeground(Color.white);
        managerPassword.setFont(new Font("Noto Sans Mono", Font.BOLD, 18));
        panel.add(managerPassword, gbc);

        gbc.gridx = 1;

        managerPasswordField = new JPasswordField();
        managerPasswordField.setPreferredSize(new Dimension(500, 40));
        managerPasswordField.setFont(new Font("Noto Sans Mono", Font.BOLD, 18));
        panel.add(managerPasswordField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        buttonPanel.setBackground(new Color(52, 17, 63));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 120, 0, 0)); // Add margin from left

        // Back button
        backButton = new JButton("BACK");
        backButton.setPreferredSize(new Dimension(500, 60));
        backButton.setFocusable(false);
        backButton.setBackground(new Color(190, 183, 223));
        backButton.setForeground(new Color(52, 17, 63));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton.setFont(new Font("Noto Sans Mono", Font.BOLD, 18));

        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(220, 220, 220));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(190, 183, 223));
            }
        });

        backButton.addActionListener(this);

        // Submit button
        submitButton = new JButton("SUBMIT");
        submitButton.setPreferredSize(new Dimension(500, 60));
        submitButton.setFocusable(false);
        submitButton.setBackground(new Color(190, 183, 223));
        submitButton.setForeground(new Color(52, 17, 63));
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        submitButton.setFont(new Font("Noto Sans Mono", Font.BOLD, 18));

        submitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                submitButton.setBackground(new Color(220, 220, 220));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                submitButton.setBackground(new Color(190, 183, 223));
            }
        });

        submitButton.addActionListener(this);

        buttonPanel.add(backButton);
        buttonPanel.add(submitButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        add(panel);
        setVisible(true);
    }

    // GET AND POST DATA FROM HERE (Mail Function is commented)
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {

            String managerPassword = managerPasswordField.getText();
            String managerEmail = managerEmailField.getText();

            String query = "SELECT * FROM manager WHERE managerEmail = '" + managerEmail + "' AND managerPassword = '" + managerPassword + "'";

            // Operations if Manager Exists.
            try {
                ResultSet rs = database.executeReadQuery(query);
                if (rs.next()) {
                    emailLoginNotification(rs, managerEmail);
                    dispose();
                    SwingUtilities.invokeLater(() -> new Dashboard());
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Credentials.", "Error", JOptionPane.WARNING_MESSAGE);
                }
            } catch (RuntimeException | SQLException error) {
                JOptionPane.showMessageDialog(this, error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource() == backButton) {
            dispose();
            SwingUtilities.invokeLater(() -> new HotelManagementSystem());
        }
    }

    // Email Configurations
    private static void emailLoginNotification(ResultSet rs, String managerEmail) throws SQLException {
        String managerName = rs.getString("managerName");
        Date currentDate = new Date();
        String date = new SimpleDateFormat("hh:mm:ss a, dd MMM yyyy").format(currentDate);
        String content = "Dear, " + managerName + ". You have been Logged into system at: " + date + ".";
        emailSender.sendEmail(managerEmail, "Hello World HMS", content);
    }


}
