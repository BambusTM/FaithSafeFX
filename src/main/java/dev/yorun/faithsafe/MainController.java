package dev.yorun.faithsafe;

import dev.yorun.faithsafe.service.DataObject;
import dev.yorun.faithsafe.service.JsonMapper;
import dev.yorun.faithsafe.service.StageService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements javafx.fxml.Initializable {
    private final JsonMapper jsonMapper = new JsonMapper();
    private final StageService stageService = new StageService();

    @FXML
    private ListView<String> pwListView;
    @FXML
    private Label pwPreviewLabel;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        refreshPasswordList();
        pwListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            pwPreviewLabel.setText(newValue);
        });
    }

    private void refreshPasswordList() {
        pwListView.getItems().clear();
        List<DataObject> passwordEntries = jsonMapper.loadFromJson();
        for (DataObject entry : passwordEntries) {
            pwListView.getItems().add(entry.getId() + ". -" + entry.getUsername() + " - " + entry.getDomain() + " - " + entry.getEmail());
        }
    }

    public void switchToCreatePwView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("create-pw-view.fxml"));
            Parent root = loader.load();

            CreatePwController createPwController = loader.getController();
            createPwController.setOnPasswordCreated(aVoid -> refreshPasswordList());

            Stage popupStage = new Stage();
            Scene scene = new Scene(root);

            popupStage.setTitle("Create Password");
            popupStage.setResizable(false);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(scene);

            popupStage.setOnCloseRequest(e -> {
                e.consume();
                stageService.conformationPopup(popupStage,
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
            System.err.println("Failed to switch to create-pw-view.fxml: " + e.getMessage());
        }
    }

    public void deletePassword(ActionEvent event) {
        String selectedEntry = pwListView.getSelectionModel().getSelectedItem();
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (selectedEntry != null) {
            String[] parts = selectedEntry.split("\\.");
            int id = Integer.parseInt(parts[0].trim());

            stageService.conformationPopup(currentStage,
                    "Delete Password",
                    "Delete Confirmation",
                    "Are you sure you want delete this Password? You won't be able to recover it.",
                    confirmed -> {
                        if (confirmed) {
                            jsonMapper.deleteEntry(id);
                            refreshPasswordList();
                        }
                    });
        }
    }
}
