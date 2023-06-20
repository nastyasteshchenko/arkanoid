package oop.arkanoid;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import oop.arkanoid.model.Ball;
import oop.arkanoid.model.GameLevel;
import oop.arkanoid.model.GeneratingGameException;
import oop.arkanoid.model.Point;
import oop.arkanoid.model.barrier.Barrier;
import oop.arkanoid.model.barrier.Brick;
import oop.arkanoid.model.barrier.CollisionPlace;
import oop.arkanoid.model.barrier.Platform;
import oop.arkanoid.view.LevelView;

import java.util.List;

public class LevelInitiator {
    private final int numLevel;

    LevelInitiator(int numLevel) {
        this.numLevel = numLevel;
    }

    String getLevelName() {
        return "level" + numLevel;
    }

    LevelView initLevelView(GameLevel model) {
        JsonObject paramsForLevel = LevelsManager.getLevelJsonObject(getLevelName());

        LevelView.Builder builder = new LevelView.Builder(paramsForLevel);

        List<Barrier> barriers = model.getBarriers();
        for (Barrier barrier : barriers) {
            if (barrier instanceof Platform platform) {
                builder.platform(platform.position(), platform.size);
                continue;
            }
            if (barrier instanceof Brick brick) {
                if (brick.isImmortal()) {
                    builder.addImmortalBrick(brick.position(), brick.size);
                    continue;
                }
                if (brick.health() == 1) {
                    builder.addStandardBrick(brick.position(), brick.size);
                    continue;
                }
                if (brick.health() == 2) {
                    builder.addDoubleHitBrick(brick.position(), brick.size);
                }
            }
        }

        Ball ball = model.getBall();
        builder.ball(ball.position(), ball.radius).gameScene(model.getSceneSize());

        builder.highScore(ScoresManager.getScoreForLevel(getLevelName()));

        return builder.build();

    }

    GameLevel initLevelModel() throws GeneratingGameException {

        JsonObject paramsForLevel = LevelsManager.getLevelJsonObject(getLevelName());
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
            builder.addBrick(createPoint(brick.get("x").getAsDouble(), brick.get("y").getAsDouble()),
                    createPoint(brickWidth, brickHeight), brick.get("health").getAsInt());
        }
    }

    private Point createPoint(double x, double y) {
        return new Point(x, y);
    }
}
