package panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameReadyPanel extends JPanel {
    private JComboBox<String> levelComboBox;
    private JTextField nameTextField;
    private JButton startButton;
    private Color selectedColor = Color.BLACK;
    private JPanel colorPreviewPanel;

    private StartButtonClickListener listener;

    public interface StartButtonClickListener {
        void onStartButtonClicked(String level, Color color, String name);
    }

    public void setStartButtonClickListener(StartButtonClickListener listener) {
        this.listener = listener;
    }

    public GameReadyPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        // Title Label
        JLabel titleLabel = new JLabel("Game Ready");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Level Selection
        JLabel levelLabel = new JLabel("Select Difficulty:");
        levelComboBox = new JComboBox<>(new String[] { "Easy", "Medium", "Hard" });
        formPanel.add(levelLabel, gbc);

        gbc.gridy++;
        formPanel.add(levelComboBox, gbc);

        // Color Selection Label
        gbc.gridy++;
        JLabel colorSelectionLabel = new JLabel("Select Color:");
        formPanel.add(colorSelectionLabel, gbc);

        // Color Selection Button
        gbc.gridx = 1;
        gbc.gridy++;
        JButton colorButton = new JButton("Choose Color");
        colorButton.setBackground(selectedColor);
        colorButton.setForeground(Color.white);
        colorButton.setPreferredSize(new Dimension(120, 30));
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(null, "Choose Color", selectedColor);
                if (newColor != null) {
                    selectedColor = newColor;
                    colorButton.setBackground(selectedColor);
                    colorButton.setForeground(selectedColor);
                    colorPreviewPanel.setBackground(selectedColor);
                }
            }
        });
        formPanel.add(colorButton, gbc);

        // Color Preview Panel
        gbc.gridx = 0;
        gbc.gridy++;
        colorPreviewPanel = new JPanel();
        colorPreviewPanel.setBackground(selectedColor);
        colorPreviewPanel.setPreferredSize(new Dimension(30, 30));
        // formPanel.add(colorPreviewPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel nameLabel = new JLabel("Enter Your Name:");
        formPanel.add(nameLabel, gbc);

        // Name Input Field
        gbc.gridx = 0;
        gbc.gridy++;
        nameTextField = new JTextField();
        nameTextField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(nameTextField, gbc);

        // Start Button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setBackground(new Color(100, 200, 100));
        startButton.setForeground(Color.WHITE);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String level = (String) levelComboBox.getSelectedItem();
                String name = nameTextField.getText();
                if (listener != null) {
                    listener.onStartButtonClicked(level, selectedColor, name);
                }
            }
        });
        formPanel.add(startButton, gbc);
        add(formPanel, BorderLayout.CENTER);
    }
}
