package panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private ScheduledExecutorService brickExecutor = Executors.newSingleThreadScheduledExecutor();
    private double brickMoveSpeed = 0.1;
    private boolean isBrickExecutorRunning = false;
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
    List<ItemSpeed> itemSpeeds = new ArrayList<>();
    List<ItemPower> itemPowers = new ArrayList<>();
    List<ItemConfused> itemConfuseds = new ArrayList<>();
    List<Integer> generatedXPositions = new ArrayList<>();
    ItemBall removedItemBall;
    ItemConfused removedItemConfused;
    ItemPower removedItemPower;
    ItemSpeed removedItemSpeed;
    Integer speedTimer;
    boolean addBall = false;
    private double brickDownDY = 1;
    int powerScore = 1;
    Integer powerTimer;
    boolean isConfused = false;
    Integer colorTimer;
    Integer earthQuakeTimer;

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

        startMovingBricks();

    }

    private void startMovingBricks() {
        if (!isBrickExecutorRunning) {
            brickExecutor = Executors.newSingleThreadScheduledExecutor();
            brickExecutor.scheduleAtFixedRate(this::moveBricksDown, 0, 100, TimeUnit.MILLISECONDS);
            isBrickExecutorRunning = true;
        }
    }

    public void stopMovingBricks() {
        if (isBrickExecutorRunning) {
            brickExecutor.shutdown(); // Shutdown the executor service
            isBrickExecutorRunning = false;
        }
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
        List<Ball> copyOfBalls = new ArrayList<>(balls); // Create a copy of the balls list
        for (Ball ball : copyOfBalls) {
            ball.draw(g);
        }
        List<Brick> copyOfBricks = new ArrayList<>(bricks); // Create a copy of the bricks list
        for (Brick brick : copyOfBricks) {
            brick.draw(g);
        }
        score.draw(g);
        for (ItemBall itemBall : itemBalls) {
            itemBall.draw(g);
        }
        for (ItemSpeed itemSpeed : itemSpeeds) {
            itemSpeed.draw(g);
        }
        for (ItemPower itemPower : itemPowers) {
            itemPower.draw(g);
        }
        for (ItemConfused itemConfused : itemConfuseds) {
            itemConfused.draw(g);
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

    private void moveBricksDown() {
        double dy = brickDownDY;
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            brick.y += dy;
        }
        for (int i = 0; i < itemBalls.size(); i++) {
            ItemBall itemBall = itemBalls.get(i);
            itemBall.y += dy;
        }
        for (int i = 0; i < itemSpeeds.size(); i++) {
            ItemSpeed itemSpeed = itemSpeeds.get(i);
            itemSpeed.y += dy;
        }
        for (int i = 0; i < itemPowers.size(); i++) {
            ItemPower itemPower = itemPowers.get(i);
            itemPower.y += dy;
        }
        for (int i = 0; i < itemConfuseds.size(); i++) {
            ItemConfused itemConfused = itemConfuseds.get(i);
            itemConfused.y += dy;
        }

        // repaint();
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
        generatedXPositions.add(itemBallX);
        itemBallX += BRICK_WIDTH / 2 - 10;

        int itemBallY = 50 + BRICK_HEIGHT / 2 - 10;
        itemBalls.add(new ItemBall(itemBallX, itemBallY, 20, 20));

    }

    private void generateOtherItems() {
        System.out.println(score.time);
        if (score.time % 6 == 0) {
            int itemBallX = (int) (Math.random() * 8) * BRICK_WIDTH;
            while (generatedXPositions.contains(itemBallX)) {
                itemBallX = (int) (Math.random() * 8) * BRICK_WIDTH;
            }
            generatedXPositions.add(itemBallX);
            itemBallX += BRICK_WIDTH / 2 - 10;
            int itemBallY = 50 + BRICK_HEIGHT / 2 - 10;
            itemSpeeds.add(new ItemSpeed(itemBallX, itemBallY, 20, 20));
        } else if (score.time % 5 == 0) {
            int itemBallX = (int) (Math.random() * 8) * BRICK_WIDTH;
            while (generatedXPositions.contains(itemBallX)) {
                itemBallX = (int) (Math.random() * 8) * BRICK_WIDTH;
            }
            generatedXPositions.add(itemBallX);
            itemBallX += BRICK_WIDTH / 2 - 10;
            int itemBallY = 50 + BRICK_HEIGHT / 2 - 10;
            itemPowers.add(new ItemPower(itemBallX, itemBallY, 20, 20));
        } else if (score.time % 2 == 0) {
            int itemBallX = (int) (Math.random() * 8) * BRICK_WIDTH;
            while (generatedXPositions.contains(itemBallX)) {
                itemBallX = (int) (Math.random() * 8) * BRICK_WIDTH;
            }
            generatedXPositions.add(itemBallX);
            itemBallX += BRICK_WIDTH / 2 - 10;
            int itemBallY = 50 + BRICK_HEIGHT / 2 - 10;
            itemConfuseds.add(new ItemConfused(itemBallX, itemBallY, 20, 20));
        }
    }

    private void setSpecialItems(String specialItem) {
        if (specialItem != null) {
            switch (specialItem) {
                case "color":
                    colorTimer = score.time + 10;
                    break;
                case "earthQuake":
                    earthQuakeTimer = score.time + 10;
                    break;
                default:
                    break;
            }
        }
    }

    public void checkCollision() {
        int count = 0;
        if (speedTimer != null) {
            if (speedTimer <= score.time) {
                speedTimer = null;
                mouseAdapter.speed /= 2;
            }
        }
        if (powerTimer != null) {
            if (powerTimer <= score.time) {
                powerTimer = null;
                powerScore = 1;
            }
        }
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
                if (colorTimer != null) {
                    if (colorTimer >= score.time) {
                        brick.color = generateRandomColor();
                    } else {
                        colorTimer = null;
                    }
                }
                if (earthQuakeTimer != null) {
                    if (earthQuakeTimer >= score.time) {
                        int newWidth = (int) (Math.sin(System.currentTimeMillis() * 0.003) * 20) + BRICK_WIDTH;
                        int newHeight = (int) (Math.cos(System.currentTimeMillis() * 0.003) * 20) + BRICK_HEIGHT;
                        brick.width = newWidth;
                        brick.height = newHeight;
                    } else {
                        brick.width = BRICK_WIDTH;
                        brick.height = BRICK_HEIGHT;
                        earthQuakeTimer = null;
                    }
                } else {

                    brick.width = BRICK_WIDTH;
                    brick.height = BRICK_HEIGHT;
                }

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
                    int sss = brick.score;
                    // System.out.println("t1 -> " + sss);
                    brick.score -= powerScore;
                    // System.out.println("t22 -> " + brick.score);
                    if (sss == brick.score)
                        System.out.println("top or bottom");
                }

                boolean checkLeft = ball.x + BALL_DIAMETER >= brick.x
                        && ball.x + BALL_DIAMETER < brick.x + BRICK_WIDTH / 2 && ball.xVelocity > 0;
                boolean checkRight = ball.x <= brick.x + BRICK_WIDTH && ball.x > brick.x + BRICK_WIDTH / 2
                        && ball.xVelocity < 0;

                if ((checkLeft || checkRight)
                        && checkY) {
                    ball.setXDirection(-ball.xVelocity);
                    int sss = brick.score;
                    // System.out.println("r1 -> " + sss);
                    brick.score -= powerScore;
                    // System.out.println("r22 -> " + brick.score);
                    if (sss == brick.score)
                        System.out.println("left or right");

                }

                if (brick.score <= 0) {
                    score.score += brick.finalScore - (int) score.time / 10;
                    removeBrick = brick;
                }

            }
            if (removeBrick != null) {
                setSpecialItems(removeBrick.specialItem);
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

            // itemSpeed
            for (ItemSpeed itemSpeed : itemSpeeds) {
                if (ball.x + BALL_DIAMETER >= itemSpeed.x && ball.x <= itemSpeed.x + 20
                        && ball.y + BALL_DIAMETER >= itemSpeed.y
                        && ball.y <= itemSpeed.y + 20) {

                    if (speedTimer == null) {
                        speedTimer = score.time + 10;
                        mouseAdapter.speed *= 2;
                        removedItemSpeed = itemSpeed;
                    }
                }
            }
            if (removedItemSpeed != null) {
                itemSpeeds.remove(removedItemSpeed);
                removedItemSpeed = null;
            }

            // itemPower
            for (ItemPower itemPower : itemPowers) {
                if (ball.x + BALL_DIAMETER >= itemPower.x && ball.x <= itemPower.x + 20
                        && ball.y + BALL_DIAMETER >= itemPower.y
                        && ball.y <= itemPower.y + 20) {
                    if (powerTimer == null) {
                        powerTimer = score.time + 15;
                        powerScore = 2;
                        removedItemPower = itemPower;
                    }
                }
            }
            if (removedItemPower != null) {
                itemPowers.remove(removedItemPower);
                removedItemPower = null;
            }

            // item confused
            for (ItemConfused itemConfused : itemConfuseds) {
                if (ball.x + BALL_DIAMETER >= itemConfused.x && ball.x <= itemConfused.x + 20
                        && ball.y + BALL_DIAMETER >= itemConfused.y
                        && ball.y <= itemConfused.y + 20) {
                    if (!isConfused) {
                        isConfused = true;
                        removedItemConfused = itemConfused;
                    }
                }
            }
            if (removedItemConfused != null) {
                itemConfuseds.remove(removedItemConfused);
                removedItemConfused = null;
            }
        }
        if (count == balls.size()) {
            if (isMoving) {
                brickScore += (int) (Math.random() * 10);
                startMovingBricks();
                brickDownDY = BRICK_HEIGHT;
                moveBricksDown();
                generateRandomBrick();
                generateItemBall();
                generateOtherItems();
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
            brickDownDY = 1;
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
        public double speed;

        public AL(List<Ball> balls, double speed) {
            this.balls = balls;
            this.speed = speed;
        }

        public void setDirection(int mouseX, int mouseY) {
            int selectedY = mouseY;
            int selectedX = mouseX;
            if (isConfused) {
                Random random = new Random();
                selectedX = random.nextInt(GAME_WIDTH - 10) + 5;
                selectedY = random.nextInt(GAME_HEIGHT - 30) + 100;
            }
            int ballX = balls.get(0).x + balls.get(0).width / 2;
            int ballY = balls.get(0).y + balls.get(0).height / 2;
            double angle = Math.atan2(selectedY - ballY, selectedX - ballX);
            double xVelocity = Math.cos(angle) * speed;
            double yVelocity = Math.sin(angle) * speed;
            for (Ball ball : balls) {

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
                    stopMovingBricks();
                }
            }
        }
    }

    // @Override
    // protected void paintComponent(Graphics g) {
    // super.paintComponent(g);

    // for (Ball ball : balls) {
    // ball.draw(g);
    // }

    // for (Ball ball : balls) {
    // int ballCenterX = ball.x + ball.width / 2;
    // int ballCenterY = ball.y + ball.height / 2;

    // if (mousePosition.y < GAME_HEIGHT - 50) {
    // // g drawLine(ballCenterX, ballCenterY, mousePosition.x, mousePosition.y);
    // }
    // }
    // }

}
