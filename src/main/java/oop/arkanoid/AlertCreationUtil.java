package oop.arkanoid;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AlertCreationUtil {

    static void alert(Stage stage, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error initializing resources");
        alert.setContentText("Error: " + e.getMessage());
        alert.showAndWait();
        stage.close();
    }
}
