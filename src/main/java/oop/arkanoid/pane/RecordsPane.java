package oop.arkanoid.pane;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import oop.arkanoid.notifications.EventType;
import oop.arkanoid.notifications.Notifications;

public class RecordsPane extends Pane {
    private final Button backButton = new Button("Back");
    private final Text label = new Text("Records");
    private final Text firstLevelScoreText = new Text("The first level:");
    private final Text secondLevelScoreText = new Text("The second level:");
    private final Text thirdLevelScoreText = new Text("The third level:");

    public RecordsPane() {

        backButton.setOnAction(ae -> Notifications.getInstance().publish(EventType.BACK));
        backButton.setLayoutX(233.0);
        backButton.setLayoutY(677.0);
        backButton.setPrefSize(150.0, 70.0);
        backButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #ffffff; -fx-font-size: 19.0");
        backButton.setFont(Font.font("Droid Sans Mono"));

        label.setLayoutX(234.0);
        label.setLayoutY(125.0);
        label.setStyle("-fx-fill: RED; -fx-text-alignment: CENTER; -fx-font-size: 36.0;");
        label.setFont(Font.font("Droid Sans Mono"));

        firstLevelScoreText.setLayoutX(116.0);
        firstLevelScoreText.setLayoutY(220.0);
        firstLevelScoreText.setTextAlignment(TextAlignment.CENTER);
        firstLevelScoreText.setWrappingWidth(400);
        firstLevelScoreText.setStyle("-fx-fill: RED; -fx-text-alignment: CENTER; -fx-font-size: 23.0;");
        firstLevelScoreText.setFont(Font.font("Droid Sans Mono"));

        secondLevelScoreText.setLayoutX(116.0);
        secondLevelScoreText.setLayoutY(341.0);
        secondLevelScoreText.setTextAlignment(TextAlignment.CENTER);
        secondLevelScoreText.setWrappingWidth(400);
        secondLevelScoreText.setStyle("-fx-fill: RED; -fx-text-alignment: CENTER; -fx-font-size: 23.0;");
        secondLevelScoreText.setFont(Font.font("Droid Sans Mono"));

        thirdLevelScoreText.setLayoutX(116.0);
        thirdLevelScoreText.setLayoutY(462.0);
        thirdLevelScoreText.setTextAlignment(TextAlignment.CENTER);
        thirdLevelScoreText.setWrappingWidth(400);
        thirdLevelScoreText.setStyle("-fx-fill: RED; -fx-text-alignment: CENTER; -fx-font-size: 23.0;");
        thirdLevelScoreText.setFont(Font.font("Droid Sans Mono"));

        this.getChildren().addAll(backButton, label, firstLevelScoreText, secondLevelScoreText, thirdLevelScoreText);
        this.setPrefSize(600, 900);
        this.setOpacity(0.5);
        this.setStyle("-fx-background-color: #FFE4B5");
    }
}
