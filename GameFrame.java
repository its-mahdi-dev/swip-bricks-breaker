
import javax.swing.*;

import panels.GamePanel;
import panels.MainPage;

import java.awt.*;
import java.awt.event.*;

public class GameFrame extends JFrame {
    MainPage panel;

    public GameFrame() {
        panel = new MainPage();
        this.add(panel);
        this.setTitle("swip brick breakers");
        this.setResizable(false);
        this.setBackground(Color.WHITE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
