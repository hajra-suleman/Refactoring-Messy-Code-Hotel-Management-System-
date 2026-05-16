package GraphicalUserInterface;

import Booking.mainBooking;
import Customer.mainCustomer;
import InvoicesReports.mainInvoiceReports;
import Room.mainRoom;
import Staff.mainStaff;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class setupGUIafter {
    private JFrame frame;
    private JPanel panel1;
    private JPanel panel2;
    private JPanel panel3;
    private JPanel panel1a;
    private JPanel panel1b;
    private JLabel dateLabel;

    public setupGUIafter(String openedWindow) {
        setupFrame();
        setupPanels();
        setupNorthPanel();
        setupWestPanel(openedWindow, new String[]{"BOOKINGS", "ROOMS MANAGEMENT", "CUSTOMERS MANAGEMENT", "STAFF MANAGEMENT", "INVOICES & REPORTS", "LOG OUT", "EXIT"});
        setupNorthWestPanel();
        setupNorthEastPanel();
        startTimer();
        frame.setVisible(true);
    }

    private void setupFrame() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true); // Remove title bar and borders
        frame.setResizable(false);  // Optional: Make the frame non-resizable
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout(1, 1));
        frame.setVisible(true);
    }

    private void setupPanels() {
        panel1 = new JPanel();
        panel2 = new JPanel();
        panel3 = new JPanel();

        panel1.setBackground(new Color(52, 17, 63));
        panel2.setBackground(new Color(190, 183, 223));
        panel3.setBackground(new Color(52, 17, 63));

        panel1.setPreferredSize(new Dimension(0, 100));
        panel3.setPreferredSize(new Dimension(350, 0));

        frame.add(panel1, BorderLayout.NORTH);
        frame.add(panel2, BorderLayout.CENTER);
        frame.add(panel3, BorderLayout.WEST);
    }

    private void setupNorthPanel() {
        panel1.setLayout(new BorderLayout(5, 5));
        panel1a = new JPanel();
        panel1b = new JPanel();

        panel1a.setPreferredSize(new Dimension(400, 0));
        panel1b.setPreferredSize(new Dimension(300, 0));

        panel1a.setBackground(new Color(52, 17, 63));
        panel1b.setBackground(new Color(52, 17, 63));

        panel1.add(panel1a, BorderLayout.WEST);
        panel1.add(panel1b, BorderLayout.EAST);
    }

    private void setupWestPanel(String openedWindow, String[] values) {
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));

        JLabel navLabel = new JLabel("NAVIGATION MENU");
        navLabel.setForeground(Color.white);
        navLabel.setFont(new Font("Noto Sans Mono", Font.BOLD, 22));
        navLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        navLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        panel3.add(navLabel);


        for (String value : values) {

            JButton button = new JButton(value);
            button.setMaximumSize(new Dimension(300, 60));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setFocusable(false);
            button.setBackground(new Color(190, 183, 223));
            button.setForeground(new Color(52, 17, 63));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            button.setFont(new Font("Noto Sans Mono", Font.BOLD, 18));

            button.setHorizontalAlignment(SwingConstants.LEFT);

            if (openedWindow.equals(value)) {
                button.setBackground(new Color(220, 220, 220));

            } else {
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
            }


            // ROOM EVENT LISTENER
            button.addActionListener(e -> {

                if (openedWindow.equals(value)) {
                    return;
                }

                if (e.getActionCommand().equals("ROOMS MANAGEMENT")) {
                    frame.dispose();
                    new mainRoom();
                } else if (e.getActionCommand().equals("CUSTOMERS MANAGEMENT")) {
                    frame.dispose();
                    new mainCustomer();
                } else if (e.getActionCommand().equals("BOOKINGS")) {
                    frame.dispose();
                    new mainBooking();
                } else if (e.getActionCommand().equals("INVOICES & REPORTS")) {
                    frame.dispose();
                    new mainInvoiceReports();
                } else if (e.getActionCommand().equals("STAFF MANAGEMENT")) {
                    frame.dispose();
                    new mainStaff();
                } else if (e.getActionCommand().equals("EXIT")) {
                    int userResponse = JOptionPane.showConfirmDialog(null, "Do you want to EXIT?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                    if (userResponse == 0) {
                        System.exit(0);
                    }
                } else if (e.getActionCommand().equals(("LOG OUT"))) {
                    int userResponse = JOptionPane.showConfirmDialog(null, "Do you want to LOGOUT?", "LOGOUT Confirmation", JOptionPane.YES_NO_OPTION);
                    if (userResponse == 0) {
                        frame.dispose();
                        new HotelManagementSystem();
                    }
                }
            });

            button.setContentAreaFilled(false);
            button.setOpaque(true);
            panel3.add(Box.createVerticalStrut(15));
            panel3.add(button);
        }

    }

    private void setupNorthWestPanel() {
        panel1a.setLayout(new BorderLayout());
        ImageIcon logo = new ImageIcon(getClass().getResource("/logo.png"));
        Image scaledImage = logo.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        logo = new ImageIcon(scaledImage);

        JLabel logoLabel = new JLabel();
        logoLabel.setText("<html><div style='padding-top:10px;'>HMS BY HELLO WORLD</div></html>");
        logoLabel.setFont(new Font("Noto Sans Mono", Font.BOLD, 22));
        logoLabel.setForeground(Color.white);
        logoLabel.setIcon(logo);

        panel1a.add(logoLabel);
    }

    private void setupNorthEastPanel() {
        panel1b.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 32));

        Date date = new Date();
        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm:ss a, dd MMM yyyy");
        dateLabel = new JLabel(sdf2.format(date));

        dateLabel.setForeground(Color.white);
        dateLabel.setFont(new Font("Noto Sans Mono", Font.BOLD, 18));
        panel1b.add(dateLabel);
    }

    private void startTimer() {
        Timer timer = new Timer(950, e -> {
            Date currentDate = new Date();
            dateLabel.setText(new SimpleDateFormat("hh:mm:ss a, dd MMM yyyy").format(currentDate));
        });
        timer.start();
    }

    public void setCenterPanel(JPanel newCenterPanel) {
        frame.remove(panel2);
        panel2 = newCenterPanel;
        frame.add(panel2, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

}