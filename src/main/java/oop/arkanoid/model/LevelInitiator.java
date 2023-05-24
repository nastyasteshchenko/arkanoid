package oop.arkanoid.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class LevelInitiator {

    private final JsonObject paramsForLevel;

    LevelInitiator(JsonObject jsonObject) {
        this.paramsForLevel = jsonObject;
    }

    public GameLevel initLevel() {
        JsonObject ball = paramsForLevel.getAsJsonObject("ball");
        JsonObject platform = paramsForLevel.getAsJsonObject("platform");
        GameLevel.Builder builder = new GameLevel.Builder().ball(createPoint(ball.get("x").getAsDouble(), ball.get("y").getAsDouble()),
                        ball.get("radius").getAsDouble())
                .platform(createPoint(platform.get("x").getAsDouble(), platform.get("y").getAsDouble()),
                        createPoint(platform.get("width").getAsDouble(), platform.get("height").getAsDouble()));
        setBricks(builder);
        setWalls(builder);
        return builder.build();
    }

    private void setWalls(GameLevel.Builder builder) {
        JsonObject scene = paramsForLevel.getAsJsonObject("scene");
        double sceneWidth = scene.get("width").getAsDouble();
        double sceneHeight = scene.get("height").getAsDouble();
        builder.scene(new Point(sceneWidth, sceneHeight));
        builder.addWall(createPoint(0, 0), createPoint(0, sceneHeight), WallType.LEFT);
        builder.addWall(createPoint(sceneWidth - 0, 0), createPoint(0, sceneHeight), WallType.RIGHT);
        builder.addWall(createPoint(0, 0), createPoint(sceneWidth, 0), WallType.TOP);
    }

    private void setBricks(GameLevel.Builder builder) {
        JsonObject bricks = paramsForLevel.getAsJsonObject("bricks");
        double brickWidth = bricks.get("width").getAsDouble();
        double brickHeight = bricks.get("height").getAsDouble();
        JsonArray bricksArray = bricks.getAsJsonArray("bricks");
        bricksArray.forEach(elem -> {
            JsonObject brick = elem.getAsJsonObject();
            switch (brick.get("type").getAsString()) {
                case "dh" ->
                        builder.addDestroyableBrick(createPoint(brick.get("x").getAsDouble(), brick.get("y").getAsDouble()),
                                createPoint(brickWidth, brickHeight), 2);
                case "s" ->
                        builder.addDestroyableBrick(createPoint(brick.get("x").getAsDouble(), brick.get("y").getAsDouble()),
                                createPoint(brickWidth, brickHeight), 1);
                case "im" ->
                        builder.addImmortalBrick(createPoint(brick.get("x").getAsDouble(), brick.get("y").getAsDouble()),
                                createPoint(brickWidth, brickHeight));
            }
        });
    }

    private Point createPoint(double x, double y) {
        return new Point(x, y);
    }
}
