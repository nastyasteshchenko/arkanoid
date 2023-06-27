package oop.arkanoid.pane;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import oop.arkanoid.notifications.EventType;
import oop.arkanoid.notifications.Notifications;

public class GameOverPane extends Pane {
    private final Button restartGameButton = new Button("Restart game");
    private final Button exitButton = new Button("Exit");
    private final Text text = new Text("Game Over");

    public GameOverPane() {
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

        text.setLayoutX(103.0);
        text.setLayoutY(235.0);
        text.setWrappingWidth(393);
        text.setStyle("-fx-fill: RED; -fx-text-alignment: CENTER; -fx-font-size: 70.0;");
        text.setFont(Font.font("Droid Sans Mono"));

        this.getChildren().addAll(exitButton, restartGameButton, text);
        this.setPrefSize(600, 900);
        this.setOpacity(0.5);
        this.setStyle("-fx-background-color: #FFE4B5");
    }
}
