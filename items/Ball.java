package items;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Ball extends Rectangle {
    public int xVelocity;
    public int yVelocity;

    Random random;
    public int speed = 10;

    public Ball(int x, int y, int WIDTH, int HEIGHT) {
        super(x, y, WIDTH, HEIGHT);
        random = new Random();
        int randomXDirection = random.nextInt(2);
        if (randomXDirection == 0) {
            randomXDirection--;
        }

    }

    public void setYDirection(int yDirection) {
        yVelocity = yDirection;
    }

    public void setXDirection(int xDirection) {
        xVelocity = xDirection;
    }

    public void setX(int newX) {
        x = newX;
    }

    public void setY(int newY) {
        y = newY;
    }

    public void move() {
        x = x + xVelocity;
        y = y + yVelocity;

        // System.out.println(x + " " + y);
    }

    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.fillOval(x, y, width, height);

    }
}
