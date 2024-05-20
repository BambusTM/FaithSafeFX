package dev.yorun.faithsafe;

import dev.yorun.faithsafe.service.StageService;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class CreatePwController {
    private final StageService stageService = new StageService();

    public void finishCreatePw(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("create-pw-view.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }


    public void cancelCreatePw(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stageService.logoutPopup(currentStage,
                    "Cancel",
                    "Cancel Confirmation",
                    "Are you sure you want to cancel your creation? All data will be lost.",
                    confirmed -> {
                        if (confirmed) {
                            currentStage.close();
                        }
                    });
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
