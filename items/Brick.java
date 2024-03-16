package items;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Brick extends Rectangle {

    static int GAME_WIDTH;
    static int GAME_HEIGHT;

    public Brick(int x, int y, int GAME_WIDTH, int GAME_HEIGHT, int BRICK_WIDTH, int BRICK_HEIGHT) {
        super(x, y, BRICK_WIDTH, BRICK_HEIGHT);
        Brick.GAME_WIDTH = GAME_WIDTH;
        Brick.GAME_HEIGHT = GAME_HEIGHT;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);

        g.fillRect(x, y, width, height);
        g.setColor(Color.white);
        g.setFont(new Font("Consolas", Font.PLAIN, 20));
        g.drawString(String.valueOf(Score.score), x + (width / 2), y + (height / 2));
    }
}
