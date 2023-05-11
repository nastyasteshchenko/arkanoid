package oop.arkanoid.model;

import java.util.Properties;

public class LevelInitiator {

    private final Properties properties;

    LevelInitiator(Properties properties) {
        this.properties = properties;
    }

    private Double getPropertyInDouble(String key) {
        return Double.parseDouble(properties.getProperty(key));
    }

    private Integer getPropertyInInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    private String getPropertyInString(String key) {
        return properties.getProperty(key);
    }

    public Game initLevel() {
        Game.Builder builder = new Game.Builder().ball(createPoint(getPropertyInDouble("ball.x"), getPropertyInDouble("ball.y")),
                        getPropertyInDouble("ball.radius"))
                .platform(createPoint(getPropertyInDouble("platform.x"), getPropertyInDouble("platform.y")),
                        createPoint(getPropertyInDouble("platform.width"), getPropertyInDouble("platform.height")));
        int amountOfLines = getPropertyInInt("amount.of.lines");
        int amountOfColumns = getPropertyInInt("amount.of.columns");
        double brickWidth = getPropertyInDouble("brick.width");
        double brickHeight = getPropertyInDouble("brick.height");
        double sceneWidth = getPropertyInDouble("scene.width");
        double sceneHeight = getPropertyInDouble("scene.height");
        builder.scene(new Point(sceneWidth, sceneHeight));
        for (int i = 0; i < amountOfLines; i++) {
            for (int j = 0; j < amountOfColumns; j++) {
                String brick = "b[" + i + "," + j + "]";
                String type = getPropertyInString(brick + ".type");
                switch (type) {
                    case "dh" ->
                            builder.addDestroyableBrick(createPoint(getPropertyInDouble(brick + ".x"), getPropertyInDouble(brick + ".y")),
                                    createPoint(brickWidth, brickHeight), 2);
                    case "s" ->
                            builder.addDestroyableBrick(createPoint(getPropertyInDouble(brick + ".x"), getPropertyInDouble(brick + ".y")),
                                    createPoint(brickWidth, brickHeight), 1);
                    case "im" ->
                            builder.addImmortalBrick(createPoint(getPropertyInDouble(brick + ".x"), getPropertyInDouble(brick + ".y")),
                                    createPoint(brickWidth, brickHeight));
                }
            }
        }
        builder.addWall(createPoint(0, 0), createPoint(0.5, sceneHeight));
        builder.addWall(createPoint(sceneWidth-0.5, 0), createPoint(0.5, sceneHeight));
        builder.addWall(createPoint(0, 0), createPoint(sceneWidth, 0.5));

        return builder.build();
    }

    private Point createPoint(double x, double y) {
        return new Point(x, y);
    }
}