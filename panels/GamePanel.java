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
    private Score score;
    private Point mousePosition;
    Brick removeBrick;
    boolean isMoving = false;
    Integer finalX = null;

    public GamePanel() {
        score = new Score(GAME_WIDTH, GAME_HEIGHT);
        newBall();
        newbrick();
        this.setFocusable(true);
        mouseAdapter = new AL(balls, 10); // Initialize AL instance
        this.addMouseListener(mouseAdapter); // Use the same AL instance throughout
        this.setPreferredSize(SCREEN_SIZE);
        mousePosition = new Point(0, 0); // Initialize mouse position
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mousePosition = e.getPoint(); // Update mouse position on mouse movement
                repaint(); // Repaint the panel on mouse movement
            }
        });
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
        bricks.add(new Brick(50, 280, GAME_WIDTH, GAME_HEIGHT, BRICK_WIDTH, BRICK_HEIGHT, score.score + 1));
        // bricks.add(new Brick(150, 120, GAME_WIDTH, GAME_HEIGHT, BRICK_WIDTH,
        // BRICK_HEIGHT));
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
        if (!isMoving) {
            Ball ball = balls.get(0);
            int ballCenterX = ball.x + ball.width / 2;
            int ballCenterY = ball.y + ball.height / 2;
            g.drawLine(ballCenterX, ballCenterY, mousePosition.x, mousePosition.y);
        }
    }

    public void move() {
        for (Ball ball : balls) {
            ball.move();
        }
    }

    public void checkCollision() {
        // System.out.println(isMoving);
        int count = 0;
        for (int i = 0; i < balls.size(); i++) {
            Ball ball = balls.get(i);
            ball.isMoving = true;
            if (ball.y <= 50) {
                ball.setYDirection(-ball.yVelocity);
            }
            if (ball.y >= GAME_HEIGHT - BALL_DIAMETER && isMoving) {
                if (finalX == null)
                    finalX = ball.x;
                ball.setY(GAME_HEIGHT - BALL_DIAMETER);
                ball.setXDirection(0);
            }
            if (ball.y >= GAME_HEIGHT - BALL_DIAMETER) {
                ball.setY(GAME_HEIGHT - BALL_DIAMETER);
                ball.setXDirection(0);
                if (finalX != null)
                    ball.setX(finalX);
                ball.isMoving = false;
            }

            if (ball.x <= 0) {
                ball.setXDirection(-ball.xVelocity);
            }
            if (ball.x >= GAME_WIDTH - BALL_DIAMETER) {
                ball.setXDirection(-ball.xVelocity);
            }
            if (!ball.isMoving)
                count++;

            // BRICKS
            for (Brick brick : bricks) {
                boolean checkX = ball.x + (BALL_DIAMETER / 2) >= brick.x
                        && ball.x + (BALL_DIAMETER / 2) <= brick.x + BRICK_WIDTH;
                boolean checkY = ball.y + (BALL_DIAMETER / 2) >= brick.y
                        && ball.y + (BALL_DIAMETER / 2) <= brick.y + BRICK_HEIGHT;
                if (ball.y - (brick.y + BRICK_HEIGHT) <= 0 && ball.y - (brick.y + BRICK_HEIGHT) >= -BRICK_HEIGHT / 5
                        && checkX) {
                    ball.setYDirection(-ball.yVelocity);
                    brick.score--;
                    score.score++;
                }
                if (brick.y - (ball.y + BALL_DIAMETER) <= 0 && brick.y - (ball.y + BALL_DIAMETER) >= -BRICK_HEIGHT / 5
                        && checkX) {
                    ball.setYDirection(-ball.yVelocity);
                    brick.score--;
                    score.score++;
                }

                if (ball.x - (brick.x + BRICK_WIDTH) <= 0 && ball.x - (brick.x + BRICK_WIDTH) >= -BRICK_WIDTH / 5
                        && checkY) {
                    ball.setXDirection(-ball.xVelocity);
                    brick.score--;
                    score.score++;
                }
                if (brick.x - (ball.x + BALL_DIAMETER) <= 0 && brick.x - (ball.x + BALL_DIAMETER) >= -BRICK_WIDTH / 5
                        && checkY) {
                    ball.setXDirection(-ball.xVelocity);
                    brick.score--;
                    score.score++;
                }
                if (brick.score <= 0)
                    removeBrick = brick;
            }
            if (removeBrick != null) {
                bricks.remove(removeBrick);
                removeBrick = null;
            }
        }
        if (count == balls.size())
            isMoving = false;
    }

    public class AL extends MouseAdapter {
        private List<Ball> balls;
        private double speed;

        public AL(List<Ball> balls, double speed) {
            this.balls = balls;
            this.speed = speed;
        }

        public void setDirection(int mouseX, int mouseY) {
            for (Ball ball : balls) {
                int ballX = ball.x + ball.width / 2;
                int ballY = ball.y + ball.height / 2;

                double angle = Math.atan2(mouseY - ballY, mouseX - ballX);

                double xVelocity = Math.cos(angle) * speed;
                double yVelocity = Math.sin(angle) * speed;

                ball.xVelocity = xVelocity;
                ball.yVelocity = yVelocity;

                repaint();

                try {
                    Thread.sleep(400 / (int) speed);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (!isMoving) {
                setDirection(e.getX(), e.getY());
                isMoving = true;
                finalX = null;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the balls
        for (Ball ball : balls) {
            ball.draw(g);
        }

        // Draw line from ball to mouse pointer
        for (Ball ball : balls) {
            int ballCenterX = ball.x + ball.width / 2;
            int ballCenterY = ball.y + ball.height / 2;
            g.drawLine(ballCenterX, ballCenterY, mousePosition.x, mousePosition.y);
        }
    }

}
