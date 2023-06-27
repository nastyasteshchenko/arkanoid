package oop.arkanoid.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import oop.arkanoid.model.*;
import oop.arkanoid.notifications.*;

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

    @SuppressWarnings("OptionalGetWithoutIsPresent")
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

    public static class Builder {
        private final List<Rectangle> bricks = new ArrayList<>();
        private Rectangle platform;
        private Circle ball;
        private final Pane gamePane = new Pane();
        private Scene gameScene;
        private Label score;

        public Builder() {
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder gameScene(Point size, Color color, double opacity) {
            gamePane.setOpacity(opacity);
            gameScene = new Scene(gamePane, size.x(), size.y(), color);
            gameScene.setOnMouseClicked(event -> Notifications.getInstance().publish(EventType.START_PLAYING_GAME));
            gameScene.setOnMouseMoved(event -> Notifications.getInstance().publish(EventType.MOVE_PLATFORM, event.getX()));
            return this;
        }

        public Builder platform(Point position, Point size, Color color) {
            platform = new Rectangle(position.x(), position.y(), size.x(), size.y());
            platform.setFill(color);
            gamePane.getChildren().add(platform);
            return this;
        }

        public Builder ball(Point position, double radius, Color color, Color strokeColor, String strokeWidth) {
            ball = new Circle(position.x(), position.y(), radius);
            ball.setStyle(strokeWidth);
            ball.setStroke(strokeColor);
            ball.setFill(color);
            gamePane.getChildren().add(ball);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder addBrick(Point position, Point size, Color color, Color strokeColor, String strokeWidth) {
            Rectangle brick = new Rectangle(position.x(), position.y(), size.x(), size.y());
            brick.setFill(color);
            brick.setStroke(strokeColor);
            brick.setStyle(strokeWidth);
            bricks.add(brick);
            gamePane.getChildren().add(brick);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder highScore(Point position, int score, Font font, String fontSize) {
            Label highScore = new Label("High score: " + score);
            highScore.setTranslateX(position.x());
            highScore.setTranslateY(position.y());
            highScore.setFont(font);
            highScore.setStyle(fontSize);
            gamePane.getChildren().add(highScore);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder score(Point position, Font font, String fontSize) {
            this.score = new Label("Score: 0");
            this.score.setTranslateX(position.x());
            this.score.setTranslateY(position.y());
            this.score.setFont(font);
            this.score.setStyle(fontSize);
            gamePane.getChildren().add(this.score);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder pauseButton(Point position, Point size, Font font, String fontSize, Color textColor, String backgroundStyle) {
            Button pauseButton = new Button();
            pauseButton.setOnMouseClicked(event -> Notifications.getInstance().publish(EventType.PAUSE));
            pauseButton.setText("Pause");
            pauseButton.setTranslateX(position.x());
            pauseButton.setTranslateY(position.y());
            pauseButton.setPrefSize(size.x(), size.y());

            pauseButton.setStyle(fontSize);
            pauseButton.setFont(font);
            pauseButton.setTextFill(textColor);
            pauseButton.setStyle(backgroundStyle);

            gamePane.getChildren().add(pauseButton);
            return this;
        }

        public LevelView build() {
            return new LevelView(bricks, platform, ball, gameScene, gamePane, score);
        }

    }

}
