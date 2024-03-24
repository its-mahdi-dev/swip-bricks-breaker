package panels;

import java.awt.*;
import java.util.LinkedHashMap;
import java.awt.event.MouseAdapter;
import java.util.Map;
import javax.swing.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import Data.JsonHelper;
import java.awt.event.MouseEvent;

public class HistoryPanel extends JPanel {
    CardLayout cardLayout;
    JPanel panel;
    Graphics graphics;
    JPanel contentPanel;
    JSONArray history = new JSONArray();

    public HistoryPanel(CardLayout cardLayout, JPanel panel) {
        super(new BorderLayout());
        this.cardLayout = cardLayout;
        this.panel = panel;

        JLabel backButton = new JLabel("back");
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(panel, "menu");
            }
        });

        JLabel titleLabel = new JLabel("History Panel");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        this.cardLayout = cardLayout;
        this.panel = panel;

        setHistory();

        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(history.size() * 2 - 1, 1));

        addHistories();

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setPreferredSize(new Dimension(250, 150));
        add(scrollPane);
    }

    private void setHistory() {
        JsonHelper jsonHelper = new JsonHelper("Data/history.json");
        JSONObject jsonObject = jsonHelper.readJsonFromFile();
        history = (JSONArray) jsonObject.get("history");
    }

    private void addHistories() {
        for (int i = 0; i < history.size(); i++) {
            JSONObject historyObject = (JSONObject) history.get(i);

            JPanel recordPanel = new JPanel(new BorderLayout());
            recordPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
            recordPanel.setBackground(new Color(255, 255, 255, 200));

            JPanel labelPanel = new JPanel(new GridLayout(4, 1, 0, 2));
            labelPanel.setOpaque(false);

            JLabel recordLabel = new JLabel("Record: " + historyObject.get("record"));
            JLabel nameLabel = new JLabel("Name: " + historyObject.get("name"));
            JLabel dateLabel = new JLabel("Date: " + historyObject.get("date"));
            JLabel timeLabel = new JLabel("Time: " + historyObject.get("time"));

            Font font = new Font("Arial", Font.BOLD, 14);
            recordLabel.setFont(font);
            nameLabel.setFont(font);
            dateLabel.setFont(font);
            timeLabel.setFont(font);

            recordLabel.setForeground(new Color(50, 150, 255));
            nameLabel.setForeground(new Color(255, 100, 100));
            dateLabel.setForeground(new Color(100, 200, 100));
            timeLabel.setForeground(new Color(255, 150, 50));

            labelPanel.add(recordLabel);
            labelPanel.add(nameLabel);
            labelPanel.add(dateLabel);
            labelPanel.add(timeLabel);

            recordPanel.add(labelPanel, BorderLayout.CENTER);
            contentPanel.add(recordPanel);

            if (i < history.size() - 1) {
                JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
                contentPanel.add(separator);
            }
        }
    }

}
