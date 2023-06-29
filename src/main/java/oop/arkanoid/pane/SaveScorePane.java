package oop.arkanoid.pane;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import oop.arkanoid.notifications.EventType;
import oop.arkanoid.notifications.NotificationsManager;

public class SaveScorePane extends GridPane {
    private final Label scoreLabel = new Label();
    private final TextField authorNameTextField = new TextField();
    private final Button saveButton = new Button("Save");

    public SaveScorePane() {
        AnchorPane authorNameWrapper = new AnchorPane();
        authorNameWrapper.getChildren().add(authorNameTextField);
        AnchorPane.setRightAnchor(authorNameTextField, 15.);
        AnchorPane.setLeftAnchor(authorNameTextField, 15.);

        ColumnConstraints leftColumnConstraints = new ColumnConstraints();
        ColumnConstraints rightColumnConstraints = new ColumnConstraints();

        leftColumnConstraints.setPercentWidth(50.);
        rightColumnConstraints.setPercentWidth(50.);

        RowConstraints titleRowConstraints = new RowConstraints();
        RowConstraints scoreRowConstraints = new RowConstraints();
        RowConstraints textRowConstraints = new RowConstraints();
        RowConstraints textFieldRowConstraints = new RowConstraints();
        RowConstraints spaceRowConstraints = new RowConstraints();
        RowConstraints buttonsRowConstraints = new RowConstraints();

        titleRowConstraints.setPercentHeight(20.);
        scoreRowConstraints.setPercentHeight(15.);
        textRowConstraints.setPercentHeight(15.);
        textFieldRowConstraints.setPercentHeight(20.);
        spaceRowConstraints.setPercentHeight(20.);
        buttonsRowConstraints.setPercentHeight(10.);

        Label newScoreTitleLabel = new Label("New score");
        GridPane.setConstraints(newScoreTitleLabel, 0, 0);
        GridPane.setConstraints(scoreLabel, 0, 1);

        Label newScoreLabel = new Label("You have set a new score!\nEnter your nickname to save it");
        GridPane.setConstraints(newScoreLabel, 0, 2);
        GridPane.setConstraints(authorNameWrapper, 0, 3);
        GridPane.setConstraints(saveButton, 0, 5);

        Button dontSaveButton = new Button("Don't save");
        GridPane.setConstraints(dontSaveButton, 1, 5);

        GridPane.setValignment(newScoreTitleLabel, VPos.CENTER);
        GridPane.setValignment(scoreLabel, VPos.CENTER);
        GridPane.setValignment(newScoreLabel, VPos.CENTER);
        GridPane.setValignment(authorNameWrapper, VPos.CENTER);
        GridPane.setValignment(saveButton, VPos.CENTER);
        GridPane.setValignment(dontSaveButton, VPos.CENTER);

        GridPane.setHalignment(newScoreTitleLabel, HPos.CENTER);
        GridPane.setHalignment(scoreLabel, HPos.CENTER);
        GridPane.setHalignment(newScoreLabel, HPos.CENTER);
        GridPane.setHalignment(authorNameWrapper, HPos.CENTER);
        GridPane.setHalignment(saveButton, HPos.CENTER);
        GridPane.setHalignment(dontSaveButton, HPos.CENTER);

        GridPane.setColumnSpan(newScoreTitleLabel, 2);
        GridPane.setColumnSpan(scoreLabel, 2);
        GridPane.setColumnSpan(newScoreLabel, 2);
        GridPane.setColumnSpan(authorNameWrapper, 2);

        newScoreTitleLabel.setFont(Font.font("Droid Sans Mono", 70.));
        newScoreTitleLabel.setTextFill(Color.valueOf("RED"));
        newScoreTitleLabel.setTextAlignment(TextAlignment.CENTER);

        scoreLabel.setFont(Font.font("Droid Sans Mono", 40.));
        scoreLabel.setTextFill(Color.valueOf("RED"));
        scoreLabel.setTextAlignment(TextAlignment.CENTER);

        newScoreLabel.setFont(Font.font("Droid Sans Mono", 23.));
        newScoreLabel.setTextFill(Color.valueOf("RED"));
        newScoreLabel.setTextAlignment(TextAlignment.CENTER);

        authorNameTextField.setFont(Font.font("Droid Sans Mono", 30.));
        authorNameTextField.setStyle("-fx-border-color: #ff0000; -fx-border-radius: 10; -fx-border-width: 2; -fx-background-insets: 0; -fx-background-radius: 10; -fx-highlight-fill: #ff0000");
        authorNameTextField.setOnKeyTyped(ae -> saveButton.setDisable(authorNameTextField.getText().isEmpty()));

        saveButton.setPrefSize(150., 70.);
        saveButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #ffffff");
        saveButton.setFont(Font.font("Droid Sans Mono", 19.));
        saveButton.setDisable(true);
        saveButton.setOnAction(ae -> NotificationsManager.getInstance().publish(EventType.SAVE_SCORE, authorNameTextField.getText()));

        dontSaveButton.setPrefSize(150., 70.);
        dontSaveButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #ffffff");
        dontSaveButton.setFont(Font.font("Droid Sans Mono", 19.));
        dontSaveButton.setOnAction(ae -> NotificationsManager.getInstance().publish(EventType.DONT_SAVE_SCORE));

        this.getChildren().addAll(newScoreTitleLabel, scoreLabel, newScoreLabel, authorNameWrapper, saveButton, dontSaveButton);
        this.setPrefSize(600, 900);
        this.setOpacity(0.5);
        this.setStyle("-fx-background-color: #FFE4B5");
        this.getColumnConstraints().addAll(leftColumnConstraints, rightColumnConstraints);
        this.getRowConstraints().addAll(titleRowConstraints, scoreRowConstraints, textRowConstraints, textFieldRowConstraints, spaceRowConstraints, buttonsRowConstraints);
    }

    public void setShowingScore(int score, double time) {
        this.scoreLabel.setText(String.format("%d for %.2f sec", score, time));
    }
}
