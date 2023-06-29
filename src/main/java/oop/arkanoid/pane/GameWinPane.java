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

public class GameWinPane extends GridPane {

    public GameWinPane() {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100.);

        RowConstraints gameOverRowConstraints = new RowConstraints();
        RowConstraints restartRowConstraints = new RowConstraints();
        RowConstraints exitRowConstraints = new RowConstraints();
        RowConstraints spaceRowConstraints = new RowConstraints();

        gameOverRowConstraints.setPercentHeight(40.);
        restartRowConstraints.setPercentHeight(20.);
        exitRowConstraints.setPercentHeight(20.);
        spaceRowConstraints.setPercentHeight(20.);

        Label gameWinLabel = new Label("Game Win");
        GridPane.setConstraints(gameWinLabel, 0, 0);

        Button nextLevelButton = new Button("Next level");
        GridPane.setConstraints(nextLevelButton, 0, 1);

        Button exitButton = new Button("Exit");
        GridPane.setConstraints(exitButton, 0, 2);

        GridPane.setHalignment(gameWinLabel, HPos.CENTER);
        GridPane.setHalignment(nextLevelButton, HPos.CENTER);
        GridPane.setHalignment(exitButton, HPos.CENTER);

        GridPane.setValignment(gameWinLabel, VPos.CENTER);
        GridPane.setValignment(nextLevelButton, VPos.CENTER);
        GridPane.setValignment(exitButton, VPos.TOP);


        nextLevelButton.setOnAction(ae -> NotificationsManager.getInstance().publish(EventType.RESTART_LEVEL));
        nextLevelButton.setPrefSize(233., 70.);
        nextLevelButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #ffffff; -fx-font-size: 21");
        nextLevelButton.setFont(Font.font("Droid Sans Mono"));

        exitButton.setOnAction(ae -> NotificationsManager.getInstance().publish(EventType.EXIT));
        exitButton.setPrefSize(233., 70.);
        exitButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #ffffff; -fx-font-size: 21");
        exitButton.setFont(Font.font("Droid Sans Mono"));

        gameWinLabel.setStyle("-fx-text-fill: RED; -fx-text-alignment: CENTER; -fx-font-size: 70;");
        gameWinLabel.setFont(Font.font("Droid Sans Mono"));

        this.getChildren().addAll(exitButton, nextLevelButton, gameWinLabel);
        this.setPrefSize(600, 900);
        this.setOpacity(0.5);
        this.setStyle("-fx-background-color: #FFE4B5");
        this.getColumnConstraints().add(columnConstraints);
        this.getRowConstraints().addAll(gameOverRowConstraints, restartRowConstraints, exitRowConstraints, spaceRowConstraints);
    }
}
