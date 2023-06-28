package oop.arkanoid;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import oop.arkanoid.model.Ball;
import oop.arkanoid.model.GameLevel;
import oop.arkanoid.model.GeneratingGameException;
import oop.arkanoid.model.Point;
import oop.arkanoid.model.barrier.Barrier;
import oop.arkanoid.model.barrier.Brick;
import oop.arkanoid.model.barrier.CollisionPlace;
import oop.arkanoid.model.barrier.Platform;
import oop.arkanoid.view.*;

import java.util.List;

class LevelInitiator {
    private final JsonObject levelJsonObject;
    final Integer numLevel;

    LevelInitiator(int numLevel, JsonObject levelJsonObject) {
        this.numLevel = numLevel;
        this.levelJsonObject = levelJsonObject;
    }

    GameLevel initLevelModel() throws GeneratingGameException {

        if (levelJsonObject == null) {
            return null;
        }
        JsonObject ball = levelJsonObject.getAsJsonObject("ball");
        JsonObject platform = levelJsonObject.getAsJsonObject("platform");
        JsonObject scene = levelJsonObject.getAsJsonObject("scene");
        double sceneWidth = scene.get("width").getAsDouble();
        double sceneHeight = scene.get("height").getAsDouble();
        GameLevel.Builder builder = new GameLevel.Builder(new Point(sceneWidth, sceneHeight)).
                ball(createPoint(ball.get("x").getAsDouble(), ball.get("y").getAsDouble()), ball.get("radius").getAsDouble())
                .platform(createPoint(platform.get("x").getAsDouble(), platform.get("y").getAsDouble()), createPoint(platform.get("width").getAsDouble(), platform.get("height").getAsDouble()));
        setBricksForModel(builder, levelJsonObject);
        setWallsForModel(builder, sceneWidth, sceneHeight);
        return builder.build();
    }

    LevelView initLevelView(GameLevel model, ScoresManager scoresManager) {

        LevelView.Builder builder = new LevelView.Builder();

        setBarriersForView(model, builder);
        setBallForView(model, builder);
        setSceneForView(model, builder);
        setHighScoreForView(builder, scoresManager);
        setScoreForView(builder);
        setPauseButtonForView(builder);

        return builder.build();

    }

    private void setPauseButtonForView(LevelView.Builder builder) {
        JsonObject button = levelJsonObject.getAsJsonObject("pauseButton");
        String pauseButtonFontSize = "-fx-font-size: " + button.get("fontSize").getAsString();
        Font pauseButtonFont = Font.font(button.get("font").getAsString());
        Color pauseButtonTextColor = Color.valueOf(button.get("textColor").getAsString());
        String pauseButtonBackgroundStyle = "-fx-background-color: " + button.get("color").getAsString();
        double x = button.get("x").getAsDouble();
        double y = button.get("y").getAsDouble();
        double width = button.get("width").getAsDouble();
        double height = button.get("height").getAsDouble();

        builder.pauseButton(new Point(x, y), new Point(width, height), pauseButtonFont, pauseButtonFontSize, pauseButtonTextColor, pauseButtonBackgroundStyle);
    }

    private void setScoreForView(LevelView.Builder builder) {
        JsonObject score = levelJsonObject.getAsJsonObject("score");
        Font scoreFont = Font.font(score.get("font").getAsString());
        String scoreFontSize = "-fx-font-size: " + score.get("fontSize").getAsString();
        double x = score.get("x").getAsDouble();
        double y = score.get("y").getAsDouble();
        builder.score(new Point(x, y), scoreFont, scoreFontSize);
    }

    private void setHighScoreForView(LevelView.Builder builder, ScoresManager scoresManager) {
        JsonObject highScore = levelJsonObject.getAsJsonObject("highScore");
        Font highScoreFont = Font.font(highScore.get("font").getAsString());
        String highScoreFontSIze = "-fx-font-size: " + highScore.get("fontSize").getAsString();
        double x = highScore.get("x").getAsDouble();
        double y = highScore.get("y").getAsDouble();
        builder.highScore(new Point(x, y), (int) scoresManager.getScoreForLevel("level" + numLevel), highScoreFont, highScoreFontSIze);
    }

    private void setSceneForView(GameLevel model, LevelView.Builder builder) {
        double sceneOpacity = levelJsonObject.getAsJsonObject("scene").get("opacity").getAsDouble();
        Color sceneColor = Color.valueOf(levelJsonObject.getAsJsonObject("scene").get("color").getAsString());
        builder.gameScene(model.getSceneSize(), sceneColor, sceneOpacity);
    }

    private void setBarriersForView(GameLevel model, LevelView.Builder builder) {
        List<Barrier> barriers = model.getBarriers();
        for (Barrier barrier : barriers) {
            if (barrier instanceof Platform p) {
                Color platformColor = Color.valueOf(levelJsonObject.getAsJsonObject("platform").get("color").getAsString());
                builder.platform(p.position(), p.size, platformColor);
                continue;
            }

            JsonObject immortalBrick = levelJsonObject.getAsJsonObject("bricks").getAsJsonObject("immortalBrick");
            Color immortalBrickColor = Color.valueOf(immortalBrick.get("color").getAsString());
            Color immortalBrickStrokeColor = Color.valueOf(immortalBrick.get("strokeColor").getAsString());
            String immortalBrickStrokeWidth = "-fx-stroke-width: " + immortalBrick.get("strokeWidth").getAsString();

            JsonObject doubleHitBrick = levelJsonObject.getAsJsonObject("bricks").getAsJsonObject("doubleHitBrick");
            Color doubleHitBrickColor = Color.valueOf(doubleHitBrick.get("color").getAsString());
            Color doubleHitBrickStrokeColor = Color.valueOf(doubleHitBrick.get("strokeColor").getAsString());
            String doubleHitBrickStrokeWidth = "-fx-stroke-width: " + doubleHitBrick.get("strokeWidth").getAsString();

            JsonObject standardBrick = levelJsonObject.getAsJsonObject("bricks").getAsJsonObject("standardBrick");
            Color standardBrickColor = Color.valueOf(standardBrick.get("color").getAsString());
            Color standardStrokeColor = Color.valueOf(standardBrick.get("strokeColor").getAsString());
            String standardBrickStrokeWidth = "-fx-stroke-width: " + standardBrick.get("strokeWidth").getAsString();

            if (barrier instanceof Brick brick) {
                if (brick.isImmortal()) {
                    builder.addBrick(brick.position(), brick.size, immortalBrickColor, immortalBrickStrokeColor, immortalBrickStrokeWidth);
                    continue;
                }
                if (brick.health() == 1) {
                    builder.addBrick(brick.position(), brick.size, standardBrickColor, standardStrokeColor, standardBrickStrokeWidth);
                    continue;
                }
                if (brick.health() == 2) {
                    builder.addBrick(brick.position(), brick.size, doubleHitBrickColor, doubleHitBrickStrokeColor, doubleHitBrickStrokeWidth);
                }
            }
        }
    }

    private void setBallForView(GameLevel model, LevelView.Builder builder) {
        JsonObject ballJO = levelJsonObject.getAsJsonObject("ball");
        Color ballColor = Color.valueOf(ballJO.get("color").getAsString());
        Color ballStrokeColor = Color.valueOf(ballJO.get("strokeColor").getAsString());
        String ballStokeWidth = "-fx-stroke-width: " + ballJO.get("strokeWidth").getAsString();

        Ball ball = model.getBall();

        builder.ball(ball.position(), ball.radius, ballColor, ballStrokeColor, ballStokeWidth);
    }

    private void setWallsForModel(GameLevel.Builder builder, double sceneWidth, double sceneHeight) throws GeneratingGameException {
        builder.addWall(createPoint(0, 0), createPoint(0, sceneHeight), CollisionPlace.LEFT);
        builder.addWall(createPoint(sceneWidth, 0), createPoint(0, sceneHeight), CollisionPlace.RIGHT);
        builder.addWall(createPoint(0, 0), createPoint(sceneWidth, 0), CollisionPlace.BOTTOM);
    }

    private void setBricksForModel(GameLevel.Builder builder, JsonObject paramsForLevel) throws GeneratingGameException {
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
