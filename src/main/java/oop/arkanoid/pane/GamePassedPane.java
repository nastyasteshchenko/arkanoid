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

public class GamePassedPane extends GridPane {

    public GamePassedPane() {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100.);

        RowConstraints gamePassedRowConstraints = new RowConstraints();
        RowConstraints backToMenuRowConstraints = new RowConstraints();
        RowConstraints exitRowConstraints = new RowConstraints();
        RowConstraints spaceRowConstraints = new RowConstraints();

        gamePassedRowConstraints.setPercentHeight(40.);
        backToMenuRowConstraints.setPercentHeight(20.);
        exitRowConstraints.setPercentHeight(20.);
        spaceRowConstraints.setPercentHeight(20.);

        Label gamePassedLabel = new Label("Game Passed");
        GridPane.setConstraints(gamePassedLabel, 0, 0);

        Button backToMenuButton = new Button("Back to menu");
        GridPane.setConstraints(backToMenuButton, 0, 1);

        Button exitButton = new Button("Exit");
        GridPane.setConstraints(exitButton, 0, 2);

        GridPane.setHalignment(gamePassedLabel, HPos.CENTER);
        GridPane.setHalignment(backToMenuButton, HPos.CENTER);
        GridPane.setHalignment(exitButton, HPos.CENTER);

        GridPane.setValignment(gamePassedLabel, VPos.CENTER);
        GridPane.setValignment(backToMenuButton, VPos.CENTER);
        GridPane.setValignment(exitButton, VPos.TOP);

        backToMenuButton.setOnAction(ae -> NotificationsManager.getInstance().publish(EventType.RESTART_GAME));
        backToMenuButton.setPrefSize(233., 70.);
        backToMenuButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #ffffff; -fx-font-size: 21");
        backToMenuButton.setFont(Font.font("Droid Sans Mono"));

        exitButton.setOnAction(ae -> NotificationsManager.getInstance().publish(EventType.EXIT));
        exitButton.setPrefSize(233., 70.);
        exitButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #ffffff; -fx-font-size: 21");
        exitButton.setFont(Font.font("Droid Sans Mono"));

        gamePassedLabel.setStyle("-fx-text-fill: RED; -fx-text-alignment: CENTER; -fx-font-size: 70;");
        gamePassedLabel.setFont(Font.font("Droid Sans Mono"));

        this.getChildren().addAll(exitButton, backToMenuButton, gamePassedLabel);
        this.setPrefSize(600, 900);
        this.setOpacity(0.5);
        this.setStyle("-fx-background-color: #FFE4B5");
        this.getColumnConstraints().add(columnConstraints);
        this.getRowConstraints().addAll(gamePassedRowConstraints, backToMenuRowConstraints, exitRowConstraints, spaceRowConstraints);
    }
}
