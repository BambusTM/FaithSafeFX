package dev.yorun.faithsafe;

import static dev.yorun.faithsafe.LoginController.findUserByUsername;
import static dev.yorun.faithsafe.service.Variables.USER_PATH;

import dev.yorun.faithsafe.objects.UserObject;
import dev.yorun.faithsafe.service.JsonMapper;
import dev.yorun.faithsafe.service.JsonPath;
import dev.yorun.faithsafe.service.StageService;

import java.security.spec.KeySpec;
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
import javafx.stage.Stage;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;

public class ResetPwController {
    private final StageService stageService = new StageService();
    private final JsonMapper<UserObject> jsonMapper = new JsonMapper<>(JsonPath.User);

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label passwordWarning;
    @FXML
    private Button abortButton;
    @FXML
    private Button finishButton;

    public void finishReset() {
        if (confirmPassword() && findUserByUsername(usernameField.getText(), jsonMapper) == null) {
            try {

                var object = new UserObject(0, usernameField.getText(), passwordField.getText());
                Variables.currentUserPassword = object.getPassword();
                object.setPassword(UserObject.getHashedPassword(object.getPassword()));

                jsonMapper.saveToJson(object);
                Parent root = FXMLLoader.load(
                        Objects.requireNonNull(getClass().getResource("login-view.fxml")));
                Stage stage = (Stage) finishButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                System.err.println("Failed to reset password: " + e.getMessage());
                e.printStackTrace();
            }
        } else if (findUserByUsername(usernameField.getText(), jsonMapper) != null) {
            passwordWarning.setText("Username already exists.");
        }
    }

    public void abortReset(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("login-view.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.err.println("Failed to cancel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean confirmPassword() {
        if (usernameField.getText().isEmpty()) {
            passwordWarning.setText("Please enter a username.");
            return false;
        } else if (passwordField.getText().isEmpty() || confirmPasswordField.getText().isEmpty()) {
            passwordWarning.setText("Please enter a password.");
            return false;
        } else if (Objects.equals(passwordField.getText(), confirmPasswordField.getText())) {
            passwordWarning.setText("");
            return true;
        } else {
            passwordWarning.setText("Passwords do not match.");
            return false;
        }
    }
}
