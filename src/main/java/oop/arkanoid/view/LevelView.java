package oop.arkanoid.view;

import com.google.gson.JsonObject;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import oop.arkanoid.model.*;
import oop.arkanoid.notifications.ButtonEventNotifications;
import oop.arkanoid.notifications.ButtonEventType;
import oop.arkanoid.notifications.MovePlatformNotifications;

import java.util.ArrayList;
import java.util.List;

public class LevelView {

    private final List<Rectangle> bricks;
    private final Rectangle platform;
    private final Circle ball;
    private final Scene gameScene;
    private final Pane gamePane;
    private final Label score;

    LevelView(List<Rectangle> bricks, Rectangle platform, Circle ball, Scene gameScene, Pane gamePane, Label score) {
        this.ball = ball;
        this.bricks = bricks;
        this.platform = platform;
        this.gameScene = gameScene;
        this.gamePane = gamePane;
        this.score = score;
    }

    public static class Builder {
        private final List<Rectangle> bricks = new ArrayList<>();
        private Rectangle platform;
        private Circle ball;
        private final JsonObject paramsForLevel;
        private final Pane gamePane = new Pane();
        private Scene gameScene;
        private Label score;
        private int highScore;

        public Builder(JsonObject jsonObject) {
            this.paramsForLevel = jsonObject;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder highScore(int score) {
            highScore = score;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder gameScene(Point size) {
            gamePane.setOpacity(0.5);
            gameScene = new Scene(gamePane, size.x(), size.y(), Color.valueOf(paramsForLevel.getAsJsonObject("scene").get("color").getAsString()));
            return this;
        }

        public Builder platform(Point position, Point size) {
            platform = new Rectangle(position.x(), position.y(), size.x(), size.y());
            setPlatformParams();
            gamePane.getChildren().add(platform);
            return this;
        }

        public Builder ball(Point position, double radius) {
            ball = new Circle(position.x(), position.y(), radius);
            setBallParams();
            gamePane.getChildren().add(ball);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder addStandardBrick(Point position, Point size) {
            Rectangle brick = new Rectangle(position.x(), position.y(), size.x(), size.y());
            setStandardBrickParams(brick);
            bricks.add(brick);
            gamePane.getChildren().add(brick);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder addDoubleHitBrick(Point position, Point size) {
            Rectangle brick = new Rectangle(position.x(), position.y(), size.x(), size.y());
            setDoubleHitBrickParams(brick);
            bricks.add(brick);
            gamePane.getChildren().add(brick);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder addImmortalBrick(Point position, Point size) {
            Rectangle brick = new Rectangle(position.x(), position.y(), size.x(), size.y());
            setImmortalBrickParams(brick);
            bricks.add(brick);
            gamePane.getChildren().add(brick);
            return this;
        }

        public LevelView build() {
            score = new Label("Score: 0");
            setScoreParams();
            gamePane.getChildren().add(score);

            Label highScoreLabel = new Label("High score: " + highScore);
            setHighScoreLabelParams(highScoreLabel);
            gamePane.getChildren().add(highScoreLabel);

            Button pauseButton = new Button();
            setPauseButtonParams(pauseButton);
            gamePane.getChildren().add(pauseButton);

            gameScene.setOnMouseClicked(event -> ButtonEventNotifications.getInstance().publish(ButtonEventType.START_GAME));
            gameScene.setOnMouseMoved(event -> MovePlatformNotifications.getInstance().publish(event.getX()));

            return new LevelView(bricks, platform, ball, gameScene, gamePane, score);

        }

        private void setPlatformParams() {
            platform.setFill(Color.valueOf(paramsForLevel.getAsJsonObject("platform").get("color").getAsString()));
        }

        private void setHighScoreLabelParams(Label highScoreLabel) {
            highScoreLabel.setTranslateX(10);
            highScoreLabel.setTranslateY(gameScene.getHeight() - 40);
            JsonObject score = paramsForLevel.getAsJsonObject("score");
            highScoreLabel.setFont(Font.font(score.get("font").getAsString()));
            highScoreLabel.setStyle("-fx-font-size: " + score.get("fontSize").getAsString());
        }

        private void setPauseButtonParams(Button pauseButton) {
            pauseButton.setOnMouseClicked(event -> ButtonEventNotifications.getInstance().publish(ButtonEventType.PAUSE));
            pauseButton.setText("Pause");
            pauseButton.setTranslateX(gameScene.getWidth() - 75);
            pauseButton.setTranslateY(gameScene.getHeight() - 50);
            pauseButton.setPrefSize(60, 40);
            JsonObject button = paramsForLevel.getAsJsonObject("pauseButton");
            pauseButton.setStyle("-fx-font-size: " + button.get("fontSize").getAsString());
            pauseButton.setFont(Font.font(button.get("font").getAsString()));
            pauseButton.setTextFill(Color.valueOf(button.get("textColor").getAsString()));
            pauseButton.setStyle("-fx-background-color: " + button.get("color").getAsString());
        }

        private void setImmortalBrickParams(Rectangle brick) {
            JsonObject immortalBrick = paramsForLevel.getAsJsonObject("bricks").getAsJsonObject("immortalBrick");
            brick.setFill(Color.valueOf(immortalBrick.get("color").getAsString()));
            brick.setStroke(Color.valueOf(immortalBrick.get("strokeColor").getAsString()));
            brick.setStyle("-fx-stroke-width: " + immortalBrick.get("strokeWidth").getAsString());
        }

        private void setDoubleHitBrickParams(Rectangle brick) {
            JsonObject doubleHitBrick = paramsForLevel.getAsJsonObject("bricks").getAsJsonObject("doubleHitBrick");
            brick.setFill(Color.valueOf(doubleHitBrick.get("color").getAsString()));
            brick.setStroke(Color.valueOf(doubleHitBrick.get("strokeColor").getAsString()));
            brick.setStyle("-fx-stroke-width: " + doubleHitBrick.get("strokeWidth").getAsString());
        }

        private void setStandardBrickParams(Rectangle brick) {
            JsonObject standardBrick = paramsForLevel.getAsJsonObject("bricks").getAsJsonObject("standardBrick");
            brick.setFill(Color.valueOf(standardBrick.get("color").getAsString()));
            brick.setStroke(Color.valueOf(standardBrick.get("strokeColor").getAsString()));
            brick.setStyle("-fx-stroke-width: " + standardBrick.get("strokeWidth").getAsString());
        }

        private void setBallParams() {
            JsonObject ball = paramsForLevel.getAsJsonObject("ball");
            this.ball.setFill(Color.valueOf(ball.get("color").getAsString()));
            this.ball.setStroke(Color.valueOf(ball.get("strokeColor").getAsString()));
            this.ball.setStyle("-fx-stroke-width: " + ball.get("strokeWidth").getAsString());
        }

        private void setScoreParams() {
            score.setTranslateX(200);
            score.setTranslateY(gameScene.getHeight() - 40);
            JsonObject score = paramsForLevel.getAsJsonObject("score");
            this.score.setFont(Font.font(score.get("font").getAsString()));
            this.score.setStyle("-fx-font-size: " + score.get("fontSize").getAsString());
        }
    }

    public void drawScore(int value) {
        score.setText("Score: " + value);
    }

    public void drawPlatform(double x) {
        platform.setX(x);
    }

    public void drawBall(Point point) {
        ball.setCenterX(point.x());
        ball.setCenterY(point.y());
    }

    public void deleteBrick(Point point) {
        removeBrick(bricks.stream().filter(i -> point.x() == i.getX() && point.y() == i.getY()).findFirst().get());
    }

    public Scene getGameScene() {
        return gameScene;
    }

    private void removeBrick(Rectangle brick) {
        gamePane.getChildren().remove(brick);
        bricks.remove(brick);
    }

}
