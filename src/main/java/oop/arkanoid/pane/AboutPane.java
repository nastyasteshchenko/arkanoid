package oop.arkanoid.pane;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import oop.arkanoid.notifications.EventType;
import oop.arkanoid.notifications.Notifications;

public class AboutPane extends Pane {
    private final Button backButton = new Button("Back");
    private final Text aboutText = new Text("The player controls the platform, which prevents a ball from falling from the playing field, and attempts to bounce the ball against a number of bricks. The ball striking a brick causes the brick to disappear. When all the bricks are gone, the player advances to the next level, where another pattern of bricks appears.");
    private final Text aboutTitleText = new Text("How to play Arkanoid");
    public AboutPane() {

        backButton.setOnAction(ae -> Notifications.getInstance().publish(EventType.BACK));
        backButton.setLayoutX(233.0);
        backButton.setLayoutY(677.0);
        backButton.setPrefSize(150.0, 70.0);
        backButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #ffffff; -fx-font-size: 19.0");
        backButton.setFont(Font.font("Droid Sans Mono"));

        aboutText.setLayoutX(58.0);
        aboutText.setLayoutY(284.0);
        aboutText.setWrappingWidth(484);
        aboutText.setStyle("-fx-text-fill: #680707; -fx-text-alignment: CENTER; -fx-font-size: 23.0;");
        aboutText.setFont(Font.font("Droid Sans Mono"));


        aboutTitleText.setLayoutX(84.0);
        aboutTitleText.setLayoutY(143.0);
        aboutTitleText.setFill(Color.valueOf("RED"));
        aboutTitleText.setStyle("-fx-text-alignment: CENTER; -fx-font-size: 36.0;");
        aboutTitleText.setFont(Font.font("Droid Sans Mono"));

        this.getChildren().addAll(backButton, aboutText, aboutTitleText);
        this.setPrefSize(600, 900);
        this.setOpacity(0.5);
        this.setStyle("-fx-background-color: #FFE4B5");
    }
}

