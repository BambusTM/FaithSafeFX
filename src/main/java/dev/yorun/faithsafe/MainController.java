package dev.yorun.faithsafe;

import dev.yorun.faithsafe.service.StageService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements javafx.fxml.Initializable {
    private final StageService stageService = new StageService();
    @FXML
    private ListView<String> pwListView;
    @FXML
    private Label pwPreviewLabel;
    String[] passwords = {"password1", "password2", "password3"};
    String currentPassword;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        pwListView.getItems().addAll(passwords);
        pwListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            currentPassword = newValue;
            pwPreviewLabel.setText(currentPassword);
        });
    }

    public void switchToCreatePwView() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("create-pw-view.fxml")));
            Stage popupStage = new Stage();
            Scene scene = new Scene(root);

            popupStage.setTitle("Create Password");
            popupStage.setResizable(false);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(scene);

            popupStage.setOnCloseRequest(e -> {
                e.consume();
                stageService.logoutPopup(popupStage,
                        "Cancel",
                        "Cancel Confirmation",
                        "Are you sure you want to cancel your creation? All data will be lost.",
                        confirmed -> {
                            if (confirmed) {
                                popupStage.close();
                            }
                        });
            });
            popupStage.showAndWait();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
