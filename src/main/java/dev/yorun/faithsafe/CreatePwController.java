package dev.yorun.faithsafe;

import static dev.yorun.faithsafe.service.Variables.DATA_PATH;
import static dev.yorun.faithsafe.service.Variables.USER_PATH;

import dev.yorun.faithsafe.objects.DataObject;
import dev.yorun.faithsafe.service.JsonMapper;
import dev.yorun.faithsafe.service.JsonPath;
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
import java.util.function.Consumer;

public class CreatePwController {
    private final StageService stageService = new StageService();
    private final JsonMapper<DataObject> jsonMapper = new JsonMapper<DataObject>(JsonPath.Data);
    private Consumer<Void> onPasswordCreated;

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

    public void setOnPasswordCreated(Consumer<Void> onPasswordCreated) {
        this.onPasswordCreated = onPasswordCreated;
    }

    public void finishCreatePw(ActionEvent event) {
        String username = createPwUsername.getText();
        String domain = createPwDomain.getText();
        String email = createPwEmail.getText();
        String password = createPwPassword.getText();
        String description = createPwDescription.getText();

        if (confirmPw()) {
            try {
                jsonMapper.saveToJson(new DataObject(0, Variables.currentUserId, username, domain, email, password, description));

                if (onPasswordCreated != null) {
                    onPasswordCreated.accept(null);
                }

                try {
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("create-pw-view.fxml")));
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.close();
                } catch (Exception e) {
                    System.err.println("Failed to close create-pw-view.fxml: " + e.getMessage());
                }
            } catch (Exception e) {
                System.err.println("Failed to save password: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("Error: Passwords do not match.");
        }
    }

    public void cancelCreatePw(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stageService.conformationPopup(currentStage,
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
