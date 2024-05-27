package dev.yorun.faithsafe;

import static dev.yorun.faithsafe.service.Variables.DATA_PATH;

import dev.yorun.faithsafe.objects.DataObject;
import dev.yorun.faithsafe.service.JsonMapper;
import dev.yorun.faithsafe.objects.ListObject;
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
import java.util.ResourceBundle;

public class MainController implements javafx.fxml.Initializable {
    private final JsonMapper jsonMapper = new JsonMapper(DATA_PATH);
    private final StageService stageService = new StageService();

    @FXML
    private ListView<ListObject> pwListView;
    @FXML
    private Label pwPreviewLabel;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        refreshPasswordList();
        pwListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            pwPreviewLabel.setText(newValue == null ? "No password selected..." : newValue.toString()); // source: Togira
        });
    }

    private void refreshPasswordList() {
        pwListView.getItems().clear();
        List<DataObject> passwordEntries = jsonMapper.loadFromJson(DATA_PATH);
        for (DataObject entry : passwordEntries) {
            pwListView.getItems().add(new ListObject(entry.getId(), entry.getUsername() + " - " + entry.getDomain() + " - " + entry.getEmail()));
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
        ListObject selectedEntry = pwListView.getSelectionModel().getSelectedItem();
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (selectedEntry != null) {
            int id = selectedEntry.getId();

            stageService.conformationPopup(currentStage,
                    "Delete Password",
                    "Delete Confirmation",
                    "Are you sure you want delete this Password? You won't be able to recover it.",
                    confirmed -> {
                        if (confirmed) {
                            jsonMapper.deleteEntry(DATA_PATH, id);
                            refreshPasswordList();
                        }
                    });
        }
    }
}
