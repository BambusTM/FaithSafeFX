package dev.yorun.faithsafe;

import dev.yorun.faithsafe.service.JsonMapper;
import dev.yorun.faithsafe.service.StageService;
import dev.yorun.faithsafe.service.Variables;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

public class CreatePwController {
    private final StageService stageService = new StageService();
    private final JsonMapper jsonMapper = new JsonMapper();

    @FXML
    private TextField createPwUsername;
    @FXML
    private TextField createPwDomain;
    @FXML
    private TextField createPwEmail;
    @FXML
    private TextField createPwPassword;
    @FXML
    private TextField createPwConfirmPassword;
    @FXML
    private Label createPwWarning;
    @FXML
    private TextArea createPwDescription;

    public void finishCreatePw(ActionEvent event) {
        String username = createPwUsername.getText();
        String domain = createPwDomain.getText();
        String email = createPwEmail.getText();
        String password = createPwPassword.getText();
        String description = createPwDescription.getText();

        if (confirmPw()) {
            jsonMapper.saveToJson(username, domain, email, password, description);

            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("create-pw-view.fxml")));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.close();
            } catch (Exception e) {
                System.err.println("Failed to close create-pw-view.fxml: " + e.getMessage());
            }
        } else {
            System.err.println("Error: Passwords do not match.");
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
            System.err.println("Failed to cancel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean confirmPw() {
        if (Objects.equals(createPwPassword.getText(), createPwConfirmPassword.getText())) {
            createPwWarning.setText("");
            return true;
        } else {
            createPwWarning.setText("Passwords do not match.");
            return false;
        }
    }
}
