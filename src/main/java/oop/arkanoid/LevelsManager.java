package oop.arkanoid;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import oop.arkanoid.model.GameLevel;
import oop.arkanoid.model.GeneratingGameException;
import oop.arkanoid.model.Point;
import oop.arkanoid.model.barrier.CollisionPlace;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class LevelsManager {
    private static int currentLevel = 1;
    private final static String pathToLevels = "./oop/arkanoid/Levels";
    private final Map<String, JsonObject> availableLevels = new HashMap<>();

    LevelsManager() throws IOException {

        File levelsDir = new File(Objects.requireNonNull(getClass().getClassLoader().getResource(pathToLevels)).getFile());

        if (!levelsDir.isDirectory()) {
            throw new NotDirectoryException("Expected \"Levels\" directory, but got file");
        }

        Gson gson = new Gson();

        for (File f : Objects.requireNonNull(levelsDir.listFiles())) {
            if (f.isDirectory()) {
                continue;
            }

            try (JsonReader reader = new JsonReader(new FileReader(f))) {
                availableLevels.put(f.getName(), gson.fromJson(reader, JsonObject.class));
            }
        }
    }

    String getCurrentLevel() {
        return "level" + currentLevel;
    }

    void checkGeneratingAllLevels() throws GeneratingGameException {
        for (JsonObject ignored : availableLevels.values()) {
            initLevel();
            currentLevel++;
        }
        currentLevel = 1;
    }

    void increaseLevel() {
        currentLevel++;
    }

    JsonObject getCurrentLevelJsonObject() {
        JsonObject object = availableLevels.get(getCurrentLevel() + ".json");
        if (object == null) {
            currentLevel = 1;
        }
        return object;
    }

    GameLevel initLevel() throws GeneratingGameException {
        JsonObject paramsForLevel = getCurrentLevelJsonObject();
        if (paramsForLevel == null) {
            return null;
        }
        JsonObject ball = paramsForLevel.getAsJsonObject("ball");
        JsonObject platform = paramsForLevel.getAsJsonObject("platform");
        JsonObject scene = paramsForLevel.getAsJsonObject("scene");
        double sceneWidth = scene.get("width").getAsDouble();
        double sceneHeight = scene.get("height").getAsDouble();
        GameLevel.Builder builder = new GameLevel.Builder(new Point(sceneWidth, sceneHeight)).
                ball(createPoint(ball.get("x").getAsDouble(), ball.get("y").getAsDouble()), ball.get("radius").getAsDouble())
                .platform(createPoint(platform.get("x").getAsDouble(), platform.get("y").getAsDouble()), createPoint(platform.get("width").getAsDouble(), platform.get("height").getAsDouble()));
        setBricks(builder, paramsForLevel);
        setWalls(builder, sceneWidth, sceneHeight);
        return builder.build();
    }

    private void setWalls(GameLevel.Builder builder, double sceneWidth, double sceneHeight) throws GeneratingGameException {
        builder.addWall(createPoint(0, 0), createPoint(0, sceneHeight), CollisionPlace.LEFT);
        builder.addWall(createPoint(sceneWidth, 0), createPoint(0, sceneHeight), CollisionPlace.RIGHT);
        builder.addWall(createPoint(0, 0), createPoint(sceneWidth, 0), CollisionPlace.BOTTOM);
    }

    private void setBricks(GameLevel.Builder builder, JsonObject paramsForLevel) throws GeneratingGameException {
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
                default -> throw GeneratingGameException.unsupportedHealth();
            }
        }
    }

    private Point createPoint(double x, double y) {
        return new Point(x, y);
    }
}
