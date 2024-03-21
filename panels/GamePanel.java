package panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import items.*;
import panels.GamePanel.AL;

public class GamePanel extends JPanel implements Runnable {

    static final int GAME_HEIGHT = 700;
    static final int GAME_WIDTH = 560;
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 15;
    static final int BRICK_WIDTH = 70;
    static final int BRICK_HEIGHT = 40;
    Thread gameThread;
    Graphics graphics;
    public int ball_count = 0;
    public int brick_number = 0;
    List<Ball> balls = new ArrayList<>();
    List<Brick> bricks = new ArrayList<>();
    public AL mouseAdapter;
    private Score score;
    private Point mousePosition;
    Brick removeBrick;
    boolean isMoving = false;
    Integer finalX = null;
    private Timer timer;
    public int timeCount = 0;
    int brickScore = 1;
    List<ItemBall> itemBalls = new ArrayList<>();
    List<Integer> generatedXPositions = new ArrayList<>();
    ItemBall removedItemBall;
    boolean addBall = false;

    public GamePanel() {
        score = new Score(GAME_WIDTH, GAME_HEIGHT);
        newBall();
        generateRandomBrick();
        generateItemBall();
        this.setFocusable(true);
        mouseAdapter = new AL(balls, 10);
        this.addMouseListener(mouseAdapter);
        this.setPreferredSize(SCREEN_SIZE);
        mousePosition = new Point(0, 0);
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mousePosition = e.getPoint();
                repaint();
            }
        });
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void newBall() {
        balls.add(
                new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2), (GAME_HEIGHT) - (BALL_DIAMETER), BALL_DIAMETER,
                        BALL_DIAMETER));
    }

    public void newbrick() {
        bricks.add(new Brick(70, 280, GAME_WIDTH, GAME_HEIGHT, BRICK_WIDTH, BRICK_HEIGHT, generateRandomColor(),
                score.score + 1));
        bricks.add(new Brick(420, 120, GAME_WIDTH, GAME_HEIGHT, BRICK_WIDTH,
                BRICK_HEIGHT, generateRandomColor(), score.score));
    }

    public void newItemBall() {
        itemBalls.add(new ItemBall(200, 200, 20, 20));
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
        for (ItemBall itemBall : itemBalls) {
            itemBall.draw(g);
        }
        if (!isMoving && mousePosition.y < GAME_HEIGHT - 50) {
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

    private void moveBricksDown(double dy) {
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            brick.y += dy;
        }
        for (int i = 0; i < itemBalls.size(); i++) {
            ItemBall itemBall = itemBalls.get(i);
            itemBall.y += dy;
        }
    }

    private Color generateRandomColor() {
        int r = (int) (Math.random() * 256); // Red component
        int g = (int) (Math.random() * 256); // Green component
        int b = (int) (Math.random() * 256); // Blue component
        return new Color(r, g, b);
    }

    private void generateRandomBrick() {
        generatedXPositions = new ArrayList<>();
        int random = (int) (Math.random() * 8);
        if (random == 0) {
            random++;
        }
        for (int i = 0; i < random; i++) {
            int brickX = (int) (Math.random() * 8) * BRICK_WIDTH;
            generatedXPositions.add(brickX);
            int brickY = 50;
            bricks.add(new Brick(brickX, brickY, GAME_WIDTH, GAME_HEIGHT, BRICK_WIDTH, BRICK_HEIGHT,
                    generateRandomColor(), brickScore));
        }

    }

    private void generateItemBall() {
        int itemBallX = (int) (Math.random() * 8) * BRICK_WIDTH;
        while (generatedXPositions.contains(itemBallX)) {
            itemBallX = (int) (Math.random() * 8) * BRICK_WIDTH;
        }
        itemBallX += BRICK_WIDTH / 2 - 10;
        int itemBallY = 50 + BRICK_HEIGHT / 2 - 10;
        itemBalls.add(new ItemBall(itemBallX, itemBallY, 20, 20));

    }

    public void checkCollision() {
        int count = 0;
        for (int i = 0; i < balls.size(); i++) {
            Ball ball = balls.get(i);
            ball.isMoving = true;
            if (ball.y <= 50 && ball.yVelocity < 0) {
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
            if (ball.xVelocity <= 10.1 && ball.xVelocity >= 9.95) {
                ball.xVelocity--;
            }
            if (!ball.isMoving)
                count++;

            // BRICKS
            for (Brick brick : bricks) {
                if (brick.y + BRICK_HEIGHT >= GAME_HEIGHT) {
                    // System.out.println("finished");
                }
                // boolean checkX = ball.x + BALL_DIAMETER >= brick.x && ball.x <= brick.x +
                // BRICK_WIDTH;
                // boolean checkY = ball.y + BALL_DIAMETER >= brick.y && ball.y <= brick.y +
                // BRICK_HEIGHT;

                boolean checkX = ball.x + (BALL_DIAMETER / 2) >= brick.x
                        && ball.x + (BALL_DIAMETER / 2) <= brick.x + BRICK_WIDTH;
                boolean checkY = ball.y + (BALL_DIAMETER / 2) >= brick.y
                        && ball.y + (BALL_DIAMETER / 2) <= brick.y + BRICK_HEIGHT;

                boolean checkTop = ball.y + BALL_DIAMETER >= brick.y
                        && ball.y + BALL_DIAMETER < brick.y + BRICK_HEIGHT / 2
                        && ball.yVelocity > 0;
                boolean checkBottom = ball.y <= brick.y + BRICK_HEIGHT && ball.y > brick.y + BRICK_HEIGHT / 2
                        && ball.yVelocity < 0;
                if ((checkTop || checkBottom)
                        && checkX) {
                    ball.setYDirection(-ball.yVelocity);
                    brick.score--;
                }

                boolean checkLeft = ball.x + BALL_DIAMETER >= brick.x
                        && ball.x + BALL_DIAMETER < brick.x + BRICK_WIDTH / 2 && ball.xVelocity > 0;
                boolean checkRight = ball.x <= brick.x + BRICK_WIDTH && ball.x > brick.x + BRICK_WIDTH / 2
                        && ball.xVelocity < 0;

                if ((checkLeft || checkRight)
                        && checkY) {
                    ball.setXDirection(-ball.xVelocity);
                    brick.score--;
                }

                if (brick.score <= 0) {
                    score.score += brick.finalScore - (int) score.time / 10;
                    removeBrick = brick;
                }
            }
            if (removeBrick != null) {
                bricks.remove(removeBrick);
                removeBrick = null;
            }

            // itemBalls
            for (ItemBall itemBall : itemBalls) {
                if (ball.x + BALL_DIAMETER >= itemBall.x && ball.x <= itemBall.x + 20
                        && ball.y + BALL_DIAMETER >= itemBall.y
                        && ball.y <= itemBall.y + 20) {
                    removedItemBall = itemBall;
                    addBall = true;

                }
            }

            if (removedItemBall != null) {
                itemBalls.remove(removedItemBall);
                removedItemBall = null;
            }
        }
        if (count == balls.size()) {
            if (isMoving) {
                moveBricksDown(BRICK_HEIGHT);
                brickScore += (int) (Math.random() * 10);
                generateRandomBrick();
                generateItemBall();
                balls.add(
                        new Ball(finalX, (GAME_HEIGHT) - (BALL_DIAMETER), BALL_DIAMETER,
                                BALL_DIAMETER));
                if (addBall) {
                    balls.add(
                            new Ball(finalX, (GAME_HEIGHT) - (BALL_DIAMETER), BALL_DIAMETER,
                                    BALL_DIAMETER));
                    addBall = false;
                }
            }
            isMoving = false;
            moveBricksDown(1);
            count = 0;
        }

        if (timer == null) {
            timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    score.time++;
                }
            });
        }
        timer.start();
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
                if (e.getY() < GAME_HEIGHT - 50) {
                    setDirection(e.getX(), e.getY());
                    isMoving = true;
                    finalX = null;
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Ball ball : balls) {
            ball.draw(g);
        }

        for (Ball ball : balls) {
            int ballCenterX = ball.x + ball.width / 2;
            int ballCenterY = ball.y + ball.height / 2;

            if (mousePosition.y < GAME_HEIGHT - 50) {
                // g drawLine(ballCenterX, ballCenterY, mousePosition.x, mousePosition.y);
            }
        }
    }

}
