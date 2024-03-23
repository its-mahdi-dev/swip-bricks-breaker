package items;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Brick extends Rectangle {

    static int GAME_WIDTH;
    static int GAME_HEIGHT;
    public int score = 1;
    public int finalScore = 1;
    public Color color;
    public String specialItem;
    public int widthChange;

    public Brick(int x, int y, int GAME_WIDTH, int GAME_HEIGHT, int BRICK_WIDTH, int BRICK_HEIGHT, Color color,
            int score) {
        super(x, y, BRICK_WIDTH, BRICK_HEIGHT);
        Brick.GAME_WIDTH = GAME_WIDTH;
        Brick.GAME_HEIGHT = GAME_HEIGHT;
        this.score = score;
        this.color = color;
        this.finalScore = score;
        Random random = new Random();
        int randomNumber = random.nextInt(101);
        if (randomNumber % 7 == 0) {
            this.specialItem = "color";
        } else if (randomNumber % 8 == 0) {
            this.specialItem = "earthQuake";
            System.out.println("earth");
        } else if (randomNumber % 9 == 0) {
            this.specialItem = "bomb";
        }
    }

    private Color generateRandomColor() {
        int r = (int) (Math.random() * 256); // Red component
        int g = (int) (Math.random() * 256); // Green component
        int b = (int) (Math.random() * 256); // Blue component
        return new Color(r, g, b);
    }

    public void draw(Graphics g) {
        if (specialItem == "color") {
            color = generateRandomColor();
        }
        g.setColor(color);
        g.fillRect(x, y, width, height);
        g.setColor(Color.white);
        if (specialItem == "earthQuake") {
            g.setColor(Color.red);
            g.fillRect(x + width / 4, y + height / 4, width - width / 2, height - height / 2);
            g.setColor(Color.white);
        }
        Font font = new Font("Consolas", Font.PLAIN, 20);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        String text = String.valueOf(score);
        int textWidth = metrics.stringWidth(text);
        int textHeight = metrics.getHeight();

        int centerX = x + (width - textWidth) / 2;
        int centerY = y + (height - textHeight) / 2 + metrics.getAscent() + 1;

        g.drawString(text, centerX, centerY);
    }
}
