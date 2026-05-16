package GraphicalUserInterface;

import javax.swing.*;
import java.awt.*;

public class Dashboard {
    public Dashboard() {
        setupGUIafter app = new setupGUIafter("DASHBOARD");

        JPanel mainPanel = new JPanel();

        JLabel mainLabel = new JLabel("Welcome Back Manager. You can manage the whole hotel from Left-Side Navigation Bar.");
        mainPanel.setLayout(new BorderLayout());
        mainLabel.setFont(new Font("Noto Sans Mono", Font.BOLD, 22));
        mainLabel.setHorizontalAlignment(JLabel.CENTER);
        mainLabel.setForeground(Color.white);
        mainPanel.add(mainLabel);
        mainPanel.setBackground(new Color(33, 26, 30));

        app.setCenterPanel(mainPanel);
    }

}