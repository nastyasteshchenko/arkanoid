package oop.arkanoid.pane;

import javafx.scene.control.Button;

import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import oop.arkanoid.notifications.EventType;
import oop.arkanoid.notifications.Notifications;

public class GameWinPane extends Pane {
    private final Button restartGameButton = new Button("Next level");
    private final Button exitButton = new Button("Exit");
    private final Text label = new Text("Game Win");

    public GameWinPane() {
        restartGameButton.setOnAction(ae -> Notifications.getInstance().publish(EventType.RESTART_LEVEL));
        restartGameButton.setLayoutX(183.0);
        restartGameButton.setLayoutY(380.0);
        restartGameButton.setPrefSize(233.0, 70.0);
        restartGameButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #ffffff; -fx-font-size: 21.0");
        restartGameButton.setFont(Font.font("Droid Sans Mono"));

        exitButton.setOnAction(ae -> Notifications.getInstance().publish(EventType.EXIT));
        exitButton.setLayoutX(183.0);
        exitButton.setLayoutY(495.0);
        exitButton.setPrefSize(233.0, 70.0);
        exitButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #ffffff; -fx-font-size: 21.0");
        exitButton.setFont(Font.font("Droid Sans Mono"));

        label.setLayoutX(103.0);
        label.setLayoutY(235.0);
        label.setWrappingWidth(393);
        label.setStyle("-fx-fill: RED; -fx-text-alignment: CENTER; -fx-font-size: 70.0;");
        label.setFont(Font.font("Droid Sans Mono"));

        this.getChildren().addAll(exitButton, restartGameButton, label);
        this.setPrefSize(600, 900);
        this.setOpacity(0.5);
        this.setStyle("-fx-background-color: #FFE4B5");
    }
}
