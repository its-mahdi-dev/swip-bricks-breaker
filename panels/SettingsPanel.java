package panels;

import javax.swing.*;

import org.json.simple.JSONObject;

import Data.JsonHelper;

import java.awt.*;
import java.awt.event.*;

public class SettingsPanel extends JPanel {
    private JButton applyButton;

    private CardLayout cardLayout;
    private JPanel panel;

    public SettingsPanel(CardLayout cardLayout, JPanel panel) {
        super(new BorderLayout());
        this.cardLayout = cardLayout;
        this.panel = panel;

        setBackground(Color.WHITE); // Set background color

        JLabel backButton = new JLabel("back");
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(panel, "menu");
            }
        });

        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(4, 1, 10, 10));
        JPanel option1Panel = createOptionPanel("marking", Color.RED, new Font("Arial", Font.PLAIN, 25));
        JPanel option2Panel = createOptionPanel("background music", Color.GREEN, new Font("Arial", Font.BOLD, 25));
        JPanel option3Panel = createOptionPanel("save game in history", Color.BLUE, new Font("Arial", Font.ITALIC, 25));

        contentPanel.add(option1Panel);
        contentPanel.add(option2Panel);
        contentPanel.add(option3Panel);
        applyButton = new JButton("Apply Settings");
        applyButton.setBackground(new Color(0, 150, 136));
        applyButton.setForeground(Color.WHITE);
        applyButton.setFocusPainted(false);
        applyButton.setFont(new Font("Arial", Font.BOLD, 20));
        applyButton.setSize(350, 50);
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applySettings();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(applyButton);

        contentPanel.add(buttonPanel);
        add(contentPanel);
    }

    private JPanel createOptionPanel(String optionText, Color color, Font font) {
        JPanel optionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        optionPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        optionPanel.setPreferredSize(new Dimension(300, 40));

        JCheckBox checkBox = new JCheckBox(optionText);
        checkBox.setFont(font.deriveFont(28f));
        checkBox.setForeground(Color.WHITE);
        checkBox.setBackground(color);
        checkBox.setFocusPainted(false);

        optionPanel.add(checkBox);
        return optionPanel;
    }

    @SuppressWarnings("unchecked")
    private void applySettings() {
        boolean marking = ((JCheckBox) ((JPanel) getComponent(0)).getComponent(0)).isSelected();
        boolean music = ((JCheckBox) ((JPanel) getComponent(1)).getComponent(0)).isSelected();
        boolean save = ((JCheckBox) ((JPanel) getComponent(2)).getComponent(0)).isSelected();

        JsonHelper jsonHelper = new JsonHelper("Data/settings.json");
        JSONObject jsonObject = jsonHelper.readJsonFromFile();

        jsonObject.put("marking", marking);
        jsonObject.put("music", music);
        jsonObject.put("save", save);

        jsonHelper.writeJsonToFile(jsonObject);

    }
}
