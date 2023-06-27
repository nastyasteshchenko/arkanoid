package oop.arkanoid.pane;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import oop.arkanoid.notifications.EventType;
import oop.arkanoid.notifications.Notifications;

public class GamePassedPane extends Pane {
    private final Button backToMenuButton = new Button("Back to menu");
    private final Button exitButton = new Button("Exit");
    private final Text label = new Text("Game Passed");

    public GamePassedPane() {
        backToMenuButton.setOnAction(ae -> Notifications.getInstance().publish(EventType.RESTART_GAME));
        backToMenuButton.setLayoutX(183.0);
        backToMenuButton.setLayoutY(380.0);
        backToMenuButton.setPrefSize(233.0, 70.0);
        backToMenuButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #ffffff; -fx-font-size: 21.0");
        backToMenuButton.setFont(Font.font("Droid Sans Mono"));

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

        this.getChildren().addAll(exitButton, backToMenuButton, label);
        this.setPrefSize(600, 900);
        this.setOpacity(0.5);
        this.setStyle("-fx-background-color: #FFE4B5");
    }
}
