package dev.yorun.faithsafe;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements javafx.fxml.Initializable {
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
}
