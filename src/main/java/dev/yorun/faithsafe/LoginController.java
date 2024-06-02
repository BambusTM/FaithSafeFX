package dev.yorun.faithsafe;

import static dev.yorun.faithsafe.service.Variables.USER_PATH;

import dev.yorun.faithsafe.objects.UserObject;
import dev.yorun.faithsafe.service.JsonMapper;
import dev.yorun.faithsafe.service.StageService;

import java.util.Objects;

import dev.yorun.faithsafe.service.Variables;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class LoginController {
    private final StageService stageService = new StageService();
    private final JsonMapper jsonMapper = new JsonMapper(USER_PATH);

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label loginWarning;
    @FXML
    private Button loginButton;
    @FXML
    private Button resetButton;

    public void login(ActionEvent event) {
        try {
            if (checkCredentials()) {
                Variables.currentUserId = jsonMapper.findUserByUsername(usernameField.getText()).getId();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-view.fxml")));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("FaithSafe");
                stage.setResizable(true);
                stage.setFullScreen(false);
                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            System.err.println("Failed to load data from json: " + e.getMessage());
        }

    }

    public void switchToResetPwView(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login-reset-view.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.err.println("Failed to switch to login-reset-view.fxml: " + e.getMessage());
        }
    }

    private boolean checkCredentials() {
        UserObject user = jsonMapper.findUserByUsername(usernameField.getText());
        if (user == null) {
            loginWarning.setText("Username not found.");
            System.out.println("Username not found.");
            return false;
        }
        if (passwordField.getText().equals(user.getPassword())) {
            System.out.println("Login successful.");
            return true;
        } else {
            loginWarning.setText("Invalid credentials.");
            System.out.println("Invalid credentials.");
            return false;
        }
    }
}
