package panels;

import javax.swing.*;
import java.awt.*;

public class MainPage extends JPanel {
    private CardLayout cardLayout;
    private JPanel panel;
    static final int GAME_HEIGHT = 700;
    static final int GAME_WIDTH = 560;
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    Graphics graphics;

    // Main panel
    public MainPage() {
        
        this.setFocusable(true);
        this.setPreferredSize(SCREEN_SIZE);
        cardLayout = new CardLayout();
        panel = new JPanel(cardLayout);

        JPanel menuPanel = new GameMenu(cardLayout, panel);
        JPanel gamePanel = new GamePanel(cardLayout, panel);
        JPanel historyPanel = new HistoryPanel(cardLayout, panel);

        panel.add(menuPanel, "menu");
        panel.add(gamePanel, "game");
        panel.add(historyPanel, "history");

        cardLayout.show(panel, "menu");
        this.add(panel);
    }

}
