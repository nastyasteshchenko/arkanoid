package oop.arkanoid.pane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import oop.arkanoid.SingleScore;
import oop.arkanoid.notifications.EventType;
import oop.arkanoid.notifications.Notifications;

import java.util.Collection;

public class ScoresPane extends GridPane {
    private final Button backButton = new Button("Back");
    private final Label scoresTitleLabel = new Label("Scores");
    private ListView<GridPane> scoresListView = new ListView<>();
    private ObservableList<GridPane> scoresObservableList = FXCollections.observableArrayList();

    public ScoresPane() {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100.);

        RowConstraints scoresLabelRowConstraints = new RowConstraints();
        RowConstraints scoresViewRowConstraints = new RowConstraints();
        RowConstraints backButtonRowConstraints = new RowConstraints();

        scoresLabelRowConstraints.setPercentHeight(20.);
        scoresViewRowConstraints.setPercentHeight(60.);
        backButtonRowConstraints.setPercentHeight(20.);

        GridPane.setConstraints(scoresTitleLabel, 0, 0);
        GridPane.setConstraints(scoresListView, 0, 1);
        GridPane.setConstraints(backButton, 0, 2);

        GridPane.setHalignment(scoresTitleLabel, HPos.CENTER);
        GridPane.setHalignment(scoresListView, HPos.CENTER);
        GridPane.setHalignment(backButton, HPos.CENTER);

        GridPane.setValignment(scoresTitleLabel, VPos.CENTER);
        GridPane.setValignment(scoresListView, VPos.CENTER);
        GridPane.setValignment(backButton, VPos.CENTER);

        backButton.setOnAction(ae -> Notifications.getInstance().publish(EventType.BACK));
        backButton.setPrefSize(150.0, 70.0);

        backButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #ffffff; -fx-font-size: 19.0");
        backButton.setFont(Font.font("Droid Sans Mono"));

        scoresTitleLabel.setStyle("-fx-text-fill: RED; -fx-text-alignment: CENTER; -fx-font-size: 36.0;");
        scoresTitleLabel.setFont(Font.font("Droid Sans Mono"));


        this.getChildren().addAll(backButton, scoresTitleLabel, scoresListView);
        this.setPrefSize(600, 900);
        this.setOpacity(0.5);
        this.setStyle("-fx-background-color: #FFE4B5");
        this.getColumnConstraints().add(columnConstraints);
        this.getRowConstraints().addAll(scoresLabelRowConstraints, scoresViewRowConstraints, backButtonRowConstraints);
    }

    public void updateScores(Collection<SingleScore> scoreList) {
        scoresObservableList.clear();
        for (SingleScore singleScore : scoreList) {
            Label levelNameLabel = new Label(singleScore.levelName());
            Label authorLabel = new Label(singleScore.author());
            Label scoreLabel = new Label(String.valueOf(singleScore.score()));

            levelNameLabel.setFont(Font.font("Droid Sans Mono", 20.));
            authorLabel.setFont(Font.font("Droid Sans Mono", 20.));
            scoreLabel.setFont(Font.font("Droid Sans mono", 20.));

            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.);

            ColumnConstraints levelNameLabelColumnConstraints = new ColumnConstraints();
            ColumnConstraints authorLabelColumnConstraints = new ColumnConstraints();
            ColumnConstraints scoreLabelColumnConstraints = new ColumnConstraints();

            levelNameLabelColumnConstraints.setPercentWidth(30.);
            authorLabelColumnConstraints.setPercentWidth(40.);
            scoreLabelColumnConstraints.setPercentWidth(30.);

            GridPane itemGrid = new GridPane();

            itemGrid.getColumnConstraints().addAll(levelNameLabelColumnConstraints, authorLabelColumnConstraints, scoreLabelColumnConstraints);
            itemGrid.getRowConstraints().add(rowConstraints);

            GridPane.setConstraints(levelNameLabel, 0, 0);
            GridPane.setConstraints(authorLabel, 1, 0);
            GridPane.setConstraints(scoreLabel, 2, 0);

            GridPane.setHalignment(levelNameLabel, HPos.LEFT);
            GridPane.setHalignment(authorLabel, HPos.CENTER);
            GridPane.setHalignment(scoreLabel, HPos.RIGHT);

            GridPane.setValignment(levelNameLabel, VPos.CENTER);
            GridPane.setValignment(authorLabel, VPos.CENTER);
            GridPane.setValignment(scoreLabel, VPos.CENTER);

            itemGrid.getChildren().addAll(levelNameLabel, authorLabel, scoreLabel);

            scoresObservableList.add(itemGrid);
        }

        scoresListView.setItems(scoresObservableList);
    }
}
