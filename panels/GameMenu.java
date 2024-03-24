package panels;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMenu extends JPanel {
    CardLayout cardLayout;
    JPanel panel;
    Graphics graphics;
    JPanel contentPanel;
    GridBagConstraints gbc;

    public GameMenu(CardLayout cardLayout, JPanel panel) {
        this.cardLayout = cardLayout;
        this.panel = panel;

        contentPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Set insets to provide space between components
        gbc.gridy = 1;
        addLabels();
        addButtons();

        // Add contentPanel to GameMenu
        setPreferredSize(panel.getPreferredSize());
        setLayout(new BorderLayout()); // Use BorderLayout for contentPanel to center its components
        add(contentPanel, BorderLayout.CENTER); // Add contentPanel to the center of GameMenu
    }

    private void addButtons() {
        // start button
        gbc.insets = new Insets(10, 5, 20, 5);
        gbc.gridx = 0;
        gbc.gridy++;
        JButton starButton = new JButton("Start New Game");
        starButton.setFont(new Font("Arial", Font.BOLD, 20));
        starButton.setFocusable(false);
        starButton.setBackground(Color.green);
        starButton.setPreferredSize(new Dimension(300, 50));
        starButton.setForeground(Color.white);
        Border border = BorderFactory.createLineBorder(Color.black, 3);
        starButton.setBorder(border);
        starButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panel, "game");
                System.out.println("Switching to game panel");
            }
        });
        contentPanel.add(starButton, gbc);

        JButton historyButton = new JButton("Game History");
        historyButton.setFont(new Font("Arial", Font.BOLD, 20));
        historyButton.setFocusable(false);
        historyButton.setBackground(Color.blue);
        historyButton.setPreferredSize(new Dimension(300, 50));
        historyButton.setForeground(Color.white);
        Border historyBorder = BorderFactory.createLineBorder(Color.black, 3);
        historyButton.setBorder(historyBorder);
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panel, "history");
                System.out.println("Switching to game panel");
            }
        });
        gbc.gridy++;
        contentPanel.add(historyButton, gbc);

        JButton settingsButton = new JButton("Game settings");
        settingsButton.setFont(new Font("Arial", Font.BOLD, 20));
        settingsButton.setFocusable(false);
        settingsButton.setBackground(Color.gray);
        settingsButton.setPreferredSize(new Dimension(300, 50));
        settingsButton.setForeground(Color.white);
        Border settingsBorder = BorderFactory.createLineBorder(Color.black, 3);
        settingsButton.setBorder(settingsBorder);
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panel, "settings");
                System.out.println("Switching to game panel");
            }
        });
        gbc.gridy++;
        contentPanel.add(settingsButton, gbc);

        JButton exitButton = new JButton("Game exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 20));
        exitButton.setFocusable(false);
        exitButton.setBackground(Color.red);
        exitButton.setPreferredSize(new Dimension(300, 50));
        exitButton.setForeground(Color.white);
        Border exitBorder = BorderFactory.createLineBorder(Color.black, 3);
        exitButton.setBorder(exitBorder);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        gbc.gridy++;
        contentPanel.add(exitButton, gbc);

    }

    private void addLabels() {
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.insets = new Insets(10, 5, 80, 5);
        JLabel title = new JLabel("Swip Brik Breakers Game");
        title.setFont(new Font("Jokerman", Font.BOLD, 30));
        title.setForeground(Color.black);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(title, gbc);

        JLabel record = new JLabel("game Record:");
        record.setFont(new Font("Cascadia Code", Font.BOLD, 20));
        record.setForeground(Color.black);
        record.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy++;
        gbc.insets = new Insets(10, 5, 5, 5);
        contentPanel.add(record, gbc);
    }

}
