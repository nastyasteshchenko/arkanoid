package oop.arkanoid.pane;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import oop.arkanoid.notifications.EventType;
import oop.arkanoid.notifications.Notifications;

public class AboutPane extends GridPane {
    public AboutPane() {

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100.);

        RowConstraints aboutTitleRowConstraints = new RowConstraints();
        RowConstraints aboutRowConstraints = new RowConstraints();
        RowConstraints backRowConstraints = new RowConstraints();

        aboutTitleRowConstraints.setPercentHeight(30.);
        aboutRowConstraints.setPercentHeight(50.);
        backRowConstraints.setPercentHeight(20.);

        Label aboutTitleText = new Label("How to play Arkanoid");
        GridPane.setConstraints(aboutTitleText, 0, 0);

        AnchorPane aboutTextAnchorPane = new AnchorPane();
        GridPane.setConstraints(aboutTextAnchorPane, 0, 1);

        Button backButton = new Button("Back");
        GridPane.setConstraints(backButton, 0, 2);

        GridPane.setHalignment(aboutTitleText, HPos.CENTER);
        GridPane.setHalignment(aboutTextAnchorPane, HPos.CENTER);
        GridPane.setHalignment(backButton, HPos.CENTER);

        GridPane.setValignment(aboutTitleText, VPos.CENTER);
        GridPane.setValignment(aboutTextAnchorPane, VPos.TOP);
        GridPane.setValignment(backButton, VPos.TOP);

        Label aboutText = new Label("The player controls the platform, which prevents a ball from falling from the playing field, and attempts to bounce the ball against a number of bricks. The ball striking a brick causes the brick to disappear. When all the bricks are gone, the player advances to the next level, where another pattern of bricks appears.");
        AnchorPane.setLeftAnchor(aboutText, 15.);
        AnchorPane.setRightAnchor(aboutText, 15.);

        aboutTextAnchorPane.getChildren().add(aboutText);

        backButton.setOnAction(ae -> Notifications.getInstance().publish(EventType.BACK));
        backButton.setPrefSize(150., 70.);
        backButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #ffffff; -fx-font-size: 19");
        backButton.setFont(Font.font("Droid Sans Mono"));

        aboutText.setStyle("-fx-text-fill: #680707; -fx-text-alignment: CENTER; -fx-font-size: 23;");
        aboutText.setFont(Font.font("Droid Sans Mono"));
        aboutText.setWrapText(true);

        aboutTitleText.setTextFill(Color.valueOf("RED"));
        aboutTitleText.setStyle("-fx-text-alignment: CENTER; -fx-font-size: 36");
        aboutTitleText.setFont(Font.font("Droid Sans Mono"));

        this.getChildren().addAll(backButton, aboutTextAnchorPane, aboutTitleText);
        this.setPrefSize(600, 900);
        this.setOpacity(0.5);
        this.setStyle("-fx-background-color: #FFE4B5");
        this.getColumnConstraints().add(columnConstraints);
        this.getRowConstraints().addAll(aboutTitleRowConstraints, aboutRowConstraints, backRowConstraints);
    }
}

