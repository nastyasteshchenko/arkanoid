package oop.arkanoid;

import javafx.scene.control.Alert;

public class AlertCreationUtil {

    static void alert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error initializing resources");
        alert.setContentText("Error: " + msg);
        alert.showAndWait();
        Arkanoid.getStage().close();
    }

}
