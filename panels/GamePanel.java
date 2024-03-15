package panels;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;;

public class GamePanel extends JPanel implements Runnable {

    static final int GAME_HEIGHT = 700;
    static final int GAME_WIDTH = (int) (GAME_HEIGHT * (0.7777));
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    Thread gameThread;
    public int ball_count = 0;
    public int brick_number = 0;

    public GamePanel() {
        this.setFocusable(true);
        this.addMouseListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void newBall() {

    }

    public void newbrick() {

    }

    public void run() {

    }

    public void paint(Graphics g) {

    }

    public void draw(Graphics g) {

    }

    public void move() {

    }

    public void checkColision() {

    }

    public class AL extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            System.out.println("Mouse Pressed");
        }

        public void mouseReleased(MouseEvent e) {
            System.out.println("Mouse Released");
        }
    }
}
