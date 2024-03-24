package items;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Ball extends Rectangle {
    public double xVelocity;
    public double yVelocity;
    public boolean isMoving;
    Color color;

    Random random;
    public int speed = 10;

    public Ball(int x, int y, int WIDTH, int HEIGHT, Color color) {
        super(x, y, WIDTH, HEIGHT);
        this.color = color;
        random = new Random();
        int randomXDirection = random.nextInt(2);
        if (randomXDirection == 0) {
            randomXDirection--;
        }

    }

    public void setYDirection(double yDirection) {
        yVelocity = yDirection;
    }

    public void setXDirection(double xDirection) {
        xVelocity = xDirection;
    }

    public void setX(int newX) {
        x = newX;
    }

    public void setY(int newY) {
        y = newY;
    }

    public void move() {
        x += xVelocity;
        y += yVelocity;

        // System.out.println(x + " " + y);
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, width, height);

    }
}
