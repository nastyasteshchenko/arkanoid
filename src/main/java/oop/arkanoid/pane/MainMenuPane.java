package oop.arkanoid.pane;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import oop.arkanoid.notifications.EventType;
import oop.arkanoid.notifications.NotificationsManager;

public class MainMenuPane extends GridPane {

    public MainMenuPane() {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100.);


        RowConstraints textRowConstraints = new RowConstraints();
        RowConstraints startGameRowConstraints = new RowConstraints();
        RowConstraints recordsRowConstraints = new RowConstraints();
        RowConstraints aboutRowConstraints = new RowConstraints();
        RowConstraints exitRowConstraints = new RowConstraints();
        RowConstraints spaceRowConstraints = new RowConstraints();

        textRowConstraints.setPercentHeight(30.);
        startGameRowConstraints.setPercentHeight(10.);
        recordsRowConstraints.setPercentHeight(10.);
        aboutRowConstraints.setPercentHeight(10.);
        exitRowConstraints.setPercentHeight(10.);
        spaceRowConstraints.setPercentHeight(30.);

        Text arkanoidText = new Text("Arkanoid");
        GridPane.setConstraints(arkanoidText, 0, 0);

        Button startButton = new Button("Start game");
        GridPane.setConstraints(startButton, 0, 1);

        Button recordsButton = new Button("Records");
        GridPane.setConstraints(recordsButton, 0, 2);

        Button aboutButton = new Button("About");
        GridPane.setConstraints(aboutButton, 0, 3);

        Button exitButton = new Button("Exit");
        GridPane.setConstraints(exitButton, 0, 4);

        GridPane.setHalignment(arkanoidText, HPos.CENTER);
        GridPane.setHalignment(startButton, HPos.CENTER);
        GridPane.setHalignment(recordsButton, HPos.CENTER);
        GridPane.setHalignment(aboutButton, HPos.CENTER);
        GridPane.setHalignment(exitButton, HPos.CENTER);

        GridPane.setValignment(arkanoidText, VPos.CENTER);
        GridPane.setValignment(startButton, VPos.CENTER);
        GridPane.setValignment(recordsButton, VPos.CENTER);
        GridPane.setValignment(aboutButton, VPos.CENTER);
        GridPane.setValignment(exitButton, VPos.CENTER);

        arkanoidText.setFont(Font.font("Droid Sans Mono"));
        arkanoidText.setFill(Color.valueOf("RED"));
        arkanoidText.setStyle("-fx-font-size: 70");
        arkanoidText.setTextAlignment(TextAlignment.CENTER);

        startButton.setOnAction(ae -> NotificationsManager.getInstance().publish(EventType.START_GAME));
        startButton.setPrefSize(150., 70.);
        startButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #ffffff; -fx-font-size: 19");
        startButton.setFont(Font.font("Droid Sans Mono"));

        aboutButton.setOnAction(ae -> NotificationsManager.getInstance().publish(EventType.ABOUT));
        aboutButton.setPrefSize(150., 70.);
        aboutButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #ffffff; -fx-font-size: 19");
        aboutButton.setFont(Font.font("Droid Sans Mono"));

        exitButton.setOnAction(ae -> NotificationsManager.getInstance().publish(EventType.EXIT));
        exitButton.setPrefSize(150., 70.);
        exitButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #ffffff; -fx-font-size: 19");
        exitButton.setFont(Font.font("Droid Sans Mono"));

        recordsButton.setOnAction(ae -> NotificationsManager.getInstance().publish(EventType.RECORDS));
        recordsButton.setPrefSize(150., 70.);
        recordsButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #ffffff; -fx-font-size: 19");
        recordsButton.setFont(Font.font("Droid Sans Mono"));


        this.getChildren().addAll(startButton, exitButton, recordsButton, aboutButton, arkanoidText);
        this.setPrefSize(600, 900);
        this.setOpacity(0.5);
        this.setStyle("-fx-background-color: #FFE4B5");
        this.getColumnConstraints().add(columnConstraints);
        this.getRowConstraints().addAll(textRowConstraints, startGameRowConstraints, recordsRowConstraints, aboutRowConstraints, exitRowConstraints, spaceRowConstraints);
    }
}
