package items;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Score extends Rectangle {
    static int GAME_WIDTH;
    static int GAME_HEIGHT;
    public int score = 1;
    public int time = 0;

    int scorePosition = (GAME_WIDTH / 2);
    int timePosition = (GAME_WIDTH / 2) + (GAME_WIDTH / 10);

    public Score(int GAME_WIDTH, int GAME_HEIGHT) {
        Score.GAME_WIDTH = GAME_WIDTH;
        Score.GAME_HEIGHT = GAME_HEIGHT;
    }

    public static String convertSecondsToTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.setFont(new Font("Consolas", Font.PLAIN, 20));
        g.drawLine(0, 50, GAME_WIDTH, 50);
        g.drawString("score: ", (GAME_WIDTH / 2) - (GAME_WIDTH / 3), 30);
        g.drawString(String.valueOf(score), (GAME_WIDTH / 2) - (GAME_WIDTH / 3) + 70, 30);
        g.drawString("time: ", (GAME_WIDTH / 2), 30);
        g.drawString(convertSecondsToTime(time), (GAME_WIDTH / 2) + 70, 30);
    }
}
