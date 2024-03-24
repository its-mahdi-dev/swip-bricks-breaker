package settings;

import java.awt.*;
import java.util.ArrayList;
import java.util.*;
import java.util.List;

import items.Brick;

public class GameSetting {
    public static Color generateRandomColor() {
        int r = (int) (Math.random() * 256); // Red component
        int g = (int) (Math.random() * 256); // Green component
        int b = (int) (Math.random() * 256); // Blue component
        return new Color(r, g, b);
    }

    public static List<Brick> getBricksAround(Brick targetBrick , List<Brick> bricks) {
        List<Brick> bricksAround = new ArrayList<>();

        Point targetCenter = new Point(targetBrick.x + targetBrick.width / 2, targetBrick.y + targetBrick.height / 2);

        for (Brick brick : bricks) {
            if (brick != targetBrick) {
                Point brickCenter = new Point(brick.x + brick.width / 2, brick.y + brick.height / 2);

                double distance = targetCenter.distance(brickCenter);

                if (distance <= 150) {
                    bricksAround.add(brick);
                }
            }
        }

        return bricksAround;
    }
}
