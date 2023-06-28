package oop.arkanoid.view;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
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
    private final Pane gamePane;
    private final GridPane gamePaneHolderGridPane = new GridPane();
    private final Label score;

    LevelView(List<Rectangle> bricks, Rectangle platform, Circle ball, Pane gamePane, Label score) {
        this.ball = ball;
        this.bricks = bricks;
        this.platform = platform;
        this.gamePane = gamePane;
        this.score = score;

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(100.);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100.);

        GridPane.setValignment(this.gamePane, VPos.CENTER);
        GridPane.setHalignment(this.gamePane, HPos.CENTER);
        GridPane.setFillHeight(this.gamePane, false);
        GridPane.setFillWidth(this.gamePane, false);

        gamePaneHolderGridPane.getColumnConstraints().add(columnConstraints);
        gamePaneHolderGridPane.getRowConstraints().add(rowConstraints);

        gamePaneHolderGridPane.getChildren().add(this.gamePane);
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

    public Pane getGamePane() {
        return gamePaneHolderGridPane;
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
        private Label score;

        public Builder() {
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder gameScene(Point size, Color color, double opacity) {
            gamePane.setOpacity(opacity);
            gamePane.setPrefSize(size.x(), size.y());
            String fxColor = String.format("#%2x%2x%2x", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
            gamePane.setStyle("-fx-background-color: " + fxColor);
            gamePane.setOnMouseClicked(event -> Notifications.getInstance().publish(EventType.START_PLAYING_GAME));
            gamePane.setOnMouseMoved(event -> Notifications.getInstance().publish(EventType.MOVE_PLATFORM, event.getX()));
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
            return new LevelView(bricks, platform, ball, gamePane, score);
        }

    }

}
