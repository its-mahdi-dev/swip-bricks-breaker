package items;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class ItemBall extends Rectangle {
    public ItemBall(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void draw(Graphics g) {
        g.setColor(Color.blue);
        g.fillOval(x, y, width, height);
        Image image = new ImageIcon("images/ball.png").getImage();
        g.drawImage(image, x, y, width, height, null); // Corrected to draw the image with correct dimensions
    }
}
