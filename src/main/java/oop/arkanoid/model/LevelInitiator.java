package oop.arkanoid.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import oop.arkanoid.model.barriers.CollisionPlace;

public class LevelInitiator {

    private final JsonObject paramsForLevel;

    LevelInitiator(JsonObject jsonObject) {
        this.paramsForLevel = jsonObject;
    }

    public GameLevel initLevel() throws GeneratingGameException {
        JsonObject ball = paramsForLevel.getAsJsonObject("ball");
        JsonObject platform = paramsForLevel.getAsJsonObject("platform");
        JsonObject scene = paramsForLevel.getAsJsonObject("scene");
        double sceneWidth = scene.get("width").getAsDouble();
        double sceneHeight = scene.get("height").getAsDouble();
        GameLevel.Builder builder = new GameLevel.Builder(new Point(sceneWidth, sceneHeight)).ball(createPoint(ball.get("x").getAsDouble(), ball.get("y").getAsDouble()),
                        ball.get("radius").getAsDouble())
                .platform(createPoint(platform.get("x").getAsDouble(), platform.get("y").getAsDouble()),
                        createPoint(platform.get("width").getAsDouble(), platform.get("height").getAsDouble()));
        setBricks(builder);
        setWalls(builder, sceneWidth, sceneHeight);
        return builder.build();
    }

    private void setWalls(GameLevel.Builder builder, double sceneWidth, double sceneHeight) throws GeneratingGameException {
        builder.addWall(createPoint(0, 0), createPoint(0, sceneHeight), CollisionPlace.LEFT);
        builder.addWall(createPoint(sceneWidth, 0), createPoint(0, sceneHeight), CollisionPlace.RIGHT);
        builder.addWall(createPoint(0, 0), createPoint(sceneWidth, 0), CollisionPlace.BOTTOM);
    }

    private void setBricks(GameLevel.Builder builder) throws GeneratingGameException {
        JsonObject bricks = paramsForLevel.getAsJsonObject("bricks");
        double brickWidth = bricks.get("width").getAsDouble();
        double brickHeight = bricks.get("height").getAsDouble();
        JsonArray bricksArray = bricks.getAsJsonArray("bricks");
        for (JsonElement elem : bricksArray) {
            JsonObject brick = elem.getAsJsonObject();
            switch (brick.get("health").getAsInt()) {
                case -1 -> builder.addBrick(createPoint(brick.get("x").getAsDouble(), brick.get("y").getAsDouble()),
                        createPoint(brickWidth, brickHeight), -1);
                case 1 -> builder.addBrick(createPoint(brick.get("x").getAsDouble(), brick.get("y").getAsDouble()),
                        createPoint(brickWidth, brickHeight), 1);
                case 2 -> builder.addBrick(createPoint(brick.get("x").getAsDouble(), brick.get("y").getAsDouble()),
                        createPoint(brickWidth, brickHeight), 2);
            }
        }
    }

    private Point createPoint(double x, double y) {
        return new Point(x, y);
    }
}
