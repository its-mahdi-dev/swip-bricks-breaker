
package panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GameMenu extends JPanel {
    CardLayout cardLayout;
    JPanel panel;
    Graphics graphics;

    public GameMenu(CardLayout cardLayout, JPanel panel) {
        this.cardLayout = cardLayout;
        this.panel = panel;

        addButtons();
        // addComponentListener(new ComponentAdapter() {
        // @Override
        // public void componentResized(ComponentEvent e) {
        // int width = getWidth();
        // System.out.println(width);
        // // Do something with the width
        // }
        // });
        setPreferredSize(panel.getPreferredSize());
        setLayout(null);
    }

    private void addButtons() {
        System.out.println(MainPage.GAME_WIDTH);
        JButton backButton = new JButton("Back to Start Page");
        backButton.setBounds(panel.getWidth() / 2 - 100, 100, 200, 30);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panel, "game");
                System.out.println("Switching to game panel");
            }
        });
        add(backButton);

    }

}
