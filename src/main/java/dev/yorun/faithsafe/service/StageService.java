package dev.yorun.faithsafe.service;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.function.Consumer;

public class StageService {
    public void logoutPopup(Stage owner, String title, String header, String content, Consumer<Boolean> callback) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(owner);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        boolean confirmed = result.isPresent() && result.get() == ButtonType.OK;
        callback.accept(confirmed);
    }
}
