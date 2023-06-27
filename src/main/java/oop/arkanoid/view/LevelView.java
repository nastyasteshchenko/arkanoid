package oop.arkanoid.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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
        public Builder gameScene(GameSceneView gameSceneView) {
            gamePane.setOpacity(gameSceneView.opacity());
            gameScene = new Scene(gamePane, gameSceneView.size().x(), gameSceneView.size().y(), gameSceneView.color());
            gameScene.setOnMouseClicked(event -> Notifications.getInstance().publish(EventType.START_PLAYING_GAME));
            gameScene.setOnMouseMoved(event -> Notifications.getInstance().publish(EventType.MOVE_PLATFORM, event.getX()));
            return this;
        }

        public Builder platform(PlatformView platformView) {
            platform = new Rectangle(platformView.position().x(), platformView.position().y(), platformView.size().x(), platformView.size().y());
            platform.setFill(platformView.color());
            gamePane.getChildren().add(platform);
            return this;
        }

        public Builder ball(BallView ballView) {
            ball = new Circle(ballView.position().x(), ballView.position().y(), ballView.radius());
            ball.setStyle(ballView.strokeWidth());
            ball.setStroke(ballView.strokeColor());
            ball.setFill(ballView.color());
            gamePane.getChildren().add(ball);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder addBrick(BrickView brickView) {
            Rectangle brick = new Rectangle(brickView.position().x(), brickView.position().y(), brickView.size().x(), brickView.size().y());
            brick.setFill(brickView.color());
            brick.setStroke(brickView.strokeColor());
            brick.setStyle(brickView.strokeWidth());
            bricks.add(brick);
            gamePane.getChildren().add(brick);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder highScore(ScoreLabel highScoreLabel) {
            Label highScore = new Label("High score: " + highScoreLabel.score());
            highScore.setTranslateX(highScoreLabel.position().x());
            highScore.setTranslateY(highScoreLabel.position().y());
            highScore.setFont(highScoreLabel.font());
            highScore.setStyle(highScoreLabel.fontSize());
            gamePane.getChildren().add(highScore);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder score(ScoreLabel scoreLabel) {
            score = new Label("Score: 0");
            score.setTranslateX(scoreLabel.position().x());
            score.setTranslateY(scoreLabel.position().y());
            score.setFont(scoreLabel.font());
            score.setStyle(scoreLabel.fontSize());
            gamePane.getChildren().add(score);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder pauseButton(PauseButtonView pauseButtonView) {
            Button pauseButton = new Button();
            pauseButton.setOnMouseClicked(event -> Notifications.getInstance().publish(EventType.PAUSE));
            pauseButton.setText("Pause");
            pauseButton.setTranslateX(pauseButtonView.position().x());
            pauseButton.setTranslateY(pauseButtonView.position().y());
            pauseButton.setPrefSize(pauseButtonView.size().x(), pauseButtonView.size().y());

            pauseButton.setStyle(pauseButtonView.fontSize());
            pauseButton.setFont(pauseButtonView.font());
            pauseButton.setTextFill(pauseButtonView.textColor());
            pauseButton.setStyle(pauseButtonView.backgroundStyle());

            gamePane.getChildren().add(pauseButton);
            return this;
        }

        public LevelView build() {
            return new LevelView(bricks, platform, ball, gameScene, gamePane, score);
        }

    }

}
