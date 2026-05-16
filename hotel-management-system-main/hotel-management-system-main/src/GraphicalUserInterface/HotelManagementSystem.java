package GraphicalUserInterface;

import Manager.loginManager;
import Manager.signupManager;

import javax.swing.*;
import java.awt.*;

public class HotelManagementSystem {
    private JFrame frame;
    private JLabel label;

    public HotelManagementSystem() {
        setupFrame();
        setupBackground();
        setupButtons();
    }

    private void setupFrame() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setResizable(false);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setTitle("Welcome Screen");
        frame.setVisible(true);
        frame.setLayout(new BorderLayout());
    }

    private void setupBackground() {

        ImageIcon icon = new ImageIcon(getClass().getResource("/cover.jpeg"));

        Image image = icon.getImage();
        icon = new ImageIcon(image);

        label = new JLabel(icon);
        label.setLayout(new BorderLayout());

        frame.add(label, BorderLayout.CENTER);

        frame.revalidate();
        frame.repaint();
    }

    private void setupButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setOpaque(false);

        JButton managerLogin = new JButton("MANAGER LOGIN");
        JButton managerSignup = new JButton("MANAGER SIGNUP");
        JButton exitButton = new JButton("EXIT");


        managerLogin.setBounds(100, 700, 200, 60);
        managerSignup.setBounds(100, 800, 200, 60);
        exitButton.setBounds(100, 900, 200, 60);

        managerLogin.setFont(new Font("Noto Sans Mono", Font.BOLD, 18));
        managerSignup.setFont(new Font("Noto Sans Mono", Font.BOLD, 18));
        exitButton.setFont(new Font("Noto Sans Mono", Font.BOLD, 18));

        managerLogin.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        managerSignup.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        exitButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        managerLogin.setBackground(new Color(190, 183, 223));
        managerSignup.setBackground(new Color(190, 183, 223));
        exitButton.setBackground(new Color(190, 183, 223));

        managerLogin.setForeground(new Color(52, 17, 63));
        managerSignup.setForeground(new Color(52, 17, 63));
        exitButton.setForeground(new Color(52, 17, 63));

        managerLogin.setFocusable(false);
        managerSignup.setFocusable(false);
        exitButton.setFocusable(false);

        managerLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        managerSignup.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        managerLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                managerLogin.setBackground(new Color(220, 220, 220));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                managerLogin.setBackground(new Color(190, 183, 223));
            }
        });

        managerSignup.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                managerSignup.setBackground(new Color(220, 220, 220));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                managerSignup.setBackground(new Color(190, 183, 223));
            }
        });

        exitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exitButton.setBackground(new Color(220, 220, 220));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exitButton.setBackground(new Color(190, 183, 223));
            }
        });

        managerLogin.addActionListener(e -> {
            if (e.getActionCommand().equals("MANAGER LOGIN")) {
                frame.dispose();
                new loginManager();
            }
        });

        managerSignup.addActionListener(e -> {
            if (e.getActionCommand().equals("MANAGER SIGNUP")) {
                frame.dispose();
                new signupManager();
            }
        });

        exitButton.addActionListener(e -> {
            int userResponse = JOptionPane.showConfirmDialog(null, "Do you want to EXIT?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
            if (userResponse == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        buttonPanel.add(managerLogin);
        buttonPanel.add(managerSignup);
        buttonPanel.add(exitButton);

        label.add(buttonPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HotelManagementSystem::new);
    }
}