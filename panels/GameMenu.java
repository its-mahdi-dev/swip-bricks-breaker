package panels;

import javax.swing.*;
import javax.swing.border.Border;

import org.json.simple.JSONObject;

import Data.JsonHelper;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class GameMenu extends JPanel {
    CardLayout cardLayout;
    JPanel panel;
    Graphics graphics;
    JPanel contentPanel;
    GridBagConstraints gbc;
    Map<String, JButton> buttons = new LinkedHashMap<>();

    public GameMenu(CardLayout cardLayout, JPanel panel) {
        this.cardLayout = cardLayout;
        this.panel = panel;

        contentPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridy = 1;
        addLabels();
        addButtons();

        setPreferredSize(panel.getPreferredSize());
        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
    }

    private void addButtons() {
        // start button
        gbc.insets = new Insets(10, 5, 20, 5);
        gbc.gridx = 0;
        gbc.gridy++;
        JButton starButton = new JButton("Start New Game");
        starButton.setBackground(Color.green);
        starButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panel, "game");
                System.out.println("Switching to game panel");
            }
        });
        buttons.put("start", starButton);

        JButton historyButton = new JButton("Game History");
        historyButton.setBackground(Color.blue);
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panel, "history");
                System.out.println("Switching to history panel");
            }
        });
        buttons.put("history", historyButton);

        JButton settingsButton = new JButton("Game settings");
        settingsButton.setBackground(Color.cyan);
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panel, "game");
                System.out.println("Switching to game panel");
            }
        });
        buttons.put("settings", settingsButton);

        JButton exitButton = new JButton("Game exit");
        exitButton.setBackground(Color.red);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panel, "game");
                System.out.println("Switching to game panel");
            }
        });
        buttons.put("exit", exitButton);

        for (Map.Entry<String, JButton> button : buttons.entrySet()) {
            button.getValue().setFont(new Font("Arial", Font.BOLD, 20));
            button.getValue().setFocusable(false);
            button.getValue().setPreferredSize(new Dimension(300, 50));
            button.getValue().setForeground(Color.white);
            Border border = BorderFactory.createLineBorder(Color.black, 3);
            button.getValue().setBorder(border);
            gbc.gridx = 0;
            gbc.gridy++;
            gbc.insets = new Insets(10, 5, 5, 5);
            contentPanel.add(button.getValue(), gbc);
        }

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

        JsonHelper jsonHelper = new JsonHelper("Data/history.json");
        JSONObject jsonObject = jsonHelper.readJsonFromFile();
        JLabel record = new JLabel("game Record: " + jsonObject.get("record"));
        record.setFont(new Font("Cascadia Code", Font.BOLD, 20));
        record.setForeground(Color.black);
        record.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy++;
        gbc.insets = new Insets(10, 5, 5, 5);
        contentPanel.add(record, gbc);
    }

}
