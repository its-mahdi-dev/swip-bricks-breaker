package items;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Brick extends Rectangle {

    static int GAME_WIDTH;
    static int GAME_HEIGHT;
    public int score = 1;
    public Color color;

    public Brick(int x, int y, int GAME_WIDTH, int GAME_HEIGHT, int BRICK_WIDTH, int BRICK_HEIGHT, Color color,
            int score) {
        super(x, y, BRICK_WIDTH, BRICK_HEIGHT);
        Brick.GAME_WIDTH = GAME_WIDTH;
        Brick.GAME_HEIGHT = GAME_HEIGHT;
        this.score = score;
        this.color = color;
    }

    

    public void draw(Graphics g) {
        g.setColor(color);

        g.fillRect(x, y, width, height);
        g.setColor(Color.white);
        g.setFont(new Font("Consolas", Font.PLAIN, 20));
        g.drawString(String.valueOf(score), x + (width / 2) - 7, y + (height / 2) + 7);
    }
}
