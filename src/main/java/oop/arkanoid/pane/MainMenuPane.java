package oop.arkanoid.pane;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import oop.arkanoid.notifications.EventType;
import oop.arkanoid.notifications.Notifications;

public class MainMenuPane extends Pane {
    private final Button startButton = new Button("Start game");
    private final Button exitButton = new Button("Exit");
    private final Button aboutButton = new Button("About");
    private final Button recordsButton = new Button("Records");
    private final Text arkanoidText = new Text("Arkanoid");

    public MainMenuPane() {
        arkanoidText.setLayoutX(132.0);
        arkanoidText.setLayoutY(158.0);
        arkanoidText.setFont(Font.font("Droid Sans Mono"));
        arkanoidText.setFill(Color.valueOf("RED"));
        arkanoidText.setStyle("-fx-font-size: 70.0");
        arkanoidText.setTextAlignment(TextAlignment.CENTER);

        startButton.setOnAction(ae -> Notifications.getInstance().publish(EventType.START_GAME));
        startButton.setLayoutX(226.0);
        startButton.setLayoutY(305.0);
        startButton.setPrefSize(150.0, 70.0);
        startButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #ffffff; -fx-font-size: 19.0");
        startButton.setFont(Font.font("Droid Sans Mono"));

        aboutButton.setOnAction(ae -> Notifications.getInstance().publish(EventType.ABOUT));
        aboutButton.setLayoutX(226.0);
        aboutButton.setLayoutY(513.0);
        aboutButton.setPrefSize(150.0, 70.0);
        aboutButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #ffffff; -fx-font-size: 19.0");
        aboutButton.setFont(Font.font("Droid Sans Mono"));

        exitButton.setOnAction(ae -> Notifications.getInstance().publish(EventType.EXIT));
        exitButton.setLayoutX(226.0);
        exitButton.setLayoutY(623.0);
        exitButton.setPrefSize(150.0, 70.0);
        exitButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #ffffff; -fx-font-size: 19.0");
        exitButton.setFont(Font.font("Droid Sans Mono"));

        recordsButton.setOnAction(ae -> Notifications.getInstance().publish(EventType.RECORDS));
        recordsButton.setLayoutX(226.0);
        recordsButton.setLayoutY(409.0);
        recordsButton.setPrefSize(150.0, 70.0);
        recordsButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #ffffff; -fx-font-size: 19.0");
        recordsButton.setFont(Font.font("Droid Sans Mono"));


        this.getChildren().addAll(startButton, exitButton, recordsButton, aboutButton, arkanoidText);
        this.setPrefSize(600, 900);
        this.setOpacity(0.5);
        this.setStyle("-fx-background-color: #FFE4B5");
    }
}
