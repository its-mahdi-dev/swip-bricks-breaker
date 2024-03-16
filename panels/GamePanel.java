package panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import items.*;

public class GamePanel extends JPanel implements Runnable {

    static final int GAME_HEIGHT = 700;
    static final int GAME_WIDTH = 560;
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int BRICK_WIDTH = 70;
    static final int BRICK_HEIGHT = 40;
    Thread gameThread;
    Graphics graphics;
    public int ball_count = 0;
    public int brick_number = 0;
    List<Ball> balls = new ArrayList<>();
    Set<Brick> bricks = new HashSet<>();
    public AL mouseAdapter;
    Score score;

    public GamePanel() {
        newBall();
        newbrick();
        score = new Score(GAME_WIDTH, GAME_HEIGHT);
        this.setFocusable(true);
        mouseAdapter = new AL(balls, 10); // Initialize AL instance
        this.addMouseListener(mouseAdapter); // Use the same AL instance throughout
        this.setPreferredSize(SCREEN_SIZE);
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void newBall() {
        balls.add(
                new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2), (GAME_HEIGHT) - (BALL_DIAMETER), BALL_DIAMETER,
                        BALL_DIAMETER));
        balls.add(
                new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2), (GAME_HEIGHT) - (BALL_DIAMETER), BALL_DIAMETER,
                        BALL_DIAMETER));
        balls.add(
                new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2), (GAME_HEIGHT) - (BALL_DIAMETER), BALL_DIAMETER,
                        BALL_DIAMETER));
        balls.add(
                new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2), (GAME_HEIGHT) - (BALL_DIAMETER), BALL_DIAMETER,
                        BALL_DIAMETER));
        balls.add(
                new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2), (GAME_HEIGHT) - (BALL_DIAMETER), BALL_DIAMETER,
                        BALL_DIAMETER));
    }

    public void newbrick() {
        bricks.add(new Brick(50, 280, GAME_WIDTH, GAME_HEIGHT, BRICK_WIDTH, BRICK_HEIGHT));
        bricks.add(new Brick(150, 120, GAME_WIDTH, GAME_HEIGHT, BRICK_WIDTH, BRICK_HEIGHT));
    }

    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }
    }

    public void paint(Graphics g) {
        Image image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }

    public void draw(Graphics g) {
        for (Ball ball : balls) {
            ball.draw(g);
        }
        for (Brick brick : bricks)
            brick.draw(g);
        score.draw(g);
    }

    public void move() {
        for (Ball ball : balls) {
            ball.move();
        }
    }

    public void checkCollision() {
        for (Ball ball : balls) {
            for (Brick brick : bricks) {
                // boolean checkX = ball.x >= brick.x && ball.x <= brick.x + BRICK_WIDTH;
                if (ball.y <= 50) {
                    System.out.println("llll");
                    ball.setYDirection(-ball.yVelocity);
                }
                if (ball.y >= GAME_HEIGHT - BALL_DIAMETER) {
                    ball.setY(GAME_HEIGHT - BALL_DIAMETER);
                    ball.setXDirection(0);
                }

                if (ball.x <= 0) {
                    ball.setXDirection(-ball.xVelocity);
                }
                if (ball.x >= GAME_WIDTH - BALL_DIAMETER) {
                    ball.setXDirection(-ball.xVelocity);
                }

                // BRICKS
                // if (ball.y <= brick.y + BRICK_HEIGHT && checkX) {
                // ball.setYDirection(-ball.yVelocity);
                // }
                // if (ball.y + BALL_DIAMETER <= brick.y && checkX) {
                // ball.setYDirection(-ball.yVelocity);
                // }

                // if (ball.x < brick.x + BRICK_WIDTH) {
                // ball.setXDirection(-ball.xVelocity);
                // }
                // if (ball.x + BALL_DIAMETER >= brick.x) {
                // ball.setXDirection(-ball.xVelocity);
                // }
            }
        }
    }

    public class AL extends MouseAdapter {
        private List<Ball> balls;
        private int speed;

        public AL(List<Ball> balls, int speed) {
            this.balls = balls;
            this.speed = speed;
        }

        public void setDirection(int mouseX, int mouseY) {
            for (int i = 0; i < balls.size(); i++) {
                Ball ball = balls.get(i);
                int ballX = ball.x + ball.width / 2;
                int ballY = ball.y + ball.width / 2;

                double angle = Math.atan2(mouseY - ballY, mouseX - ballX);

                double xVelocity = Math.cos(angle) * speed;
                double yVelocity = Math.sin(angle) * speed;

                ball.xVelocity = (int) xVelocity;
                ball.yVelocity = (int) yVelocity;

                repaint();

                try {
                    Thread.sleep(400 / speed);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            setDirection(e.getX(), e.getY());
        }
    }

}
