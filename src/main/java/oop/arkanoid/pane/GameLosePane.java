package oop.arkanoid.pane;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import oop.arkanoid.notifications.EventType;
import oop.arkanoid.notifications.NotificationsManager;

public class GameLosePane extends GridPane {

    public GameLosePane() {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100.);

        RowConstraints textRowConstraints = new RowConstraints();
        RowConstraints restartRowConstraints = new RowConstraints();
        RowConstraints exitRowConstraints = new RowConstraints();
        RowConstraints spaceRowConstraints = new RowConstraints();

        textRowConstraints.setPercentHeight(40.);
        restartRowConstraints.setPercentHeight(20.);
        exitRowConstraints.setPercentHeight(20.);
        spaceRowConstraints.setPercentHeight(20.);

        Label gameOverLabel = new Label("Game Over");
        GridPane.setConstraints(gameOverLabel, 0, 0);

        Button restartGameButton = new Button("Restart game");
        GridPane.setConstraints(restartGameButton, 0, 1);

        Button exitButton = new Button("Exit");
        GridPane.setConstraints(exitButton, 0, 2);

        GridPane.setHalignment(gameOverLabel, HPos.CENTER);
        GridPane.setHalignment(restartGameButton, HPos.CENTER);
        GridPane.setHalignment(exitButton, HPos.CENTER);

        GridPane.setValignment(gameOverLabel, VPos.CENTER);
        GridPane.setValignment(restartGameButton, VPos.CENTER);
        GridPane.setValignment(exitButton, VPos.TOP);

        restartGameButton.setOnAction(ae -> NotificationsManager.getInstance().publish(EventType.RESTART_LEVEL));
        restartGameButton.setPrefSize(233., 70.);
        restartGameButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #ffffff; -fx-font-size: 21");
        restartGameButton.setFont(Font.font("Droid Sans Mono"));

        exitButton.setOnAction(ae -> NotificationsManager.getInstance().publish(EventType.EXIT));
        exitButton.setPrefSize(233., 70.);
        exitButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #ffffff; -fx-font-size: 21");
        exitButton.setFont(Font.font("Droid Sans Mono"));

        gameOverLabel.setStyle("-fx-text-fill: RED; -fx-text-alignment: CENTER; -fx-font-size: 70;");
        gameOverLabel.setFont(Font.font("Droid Sans Mono"));

        this.getChildren().addAll(exitButton, restartGameButton, gameOverLabel);
        this.setPrefSize(600, 900);
        this.setOpacity(0.5);
        this.setStyle("-fx-background-color: #FFE4B5");
        this.getColumnConstraints().add(columnConstraints);
        this.getRowConstraints().addAll(textRowConstraints, restartRowConstraints, exitRowConstraints, spaceRowConstraints);
    }
}
