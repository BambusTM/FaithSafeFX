package dev.yorun.faithsafe;

import dev.yorun.faithsafe.objects.UserObject;
import dev.yorun.faithsafe.service.*;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class LoginController {
    private final StageService stageService = new StageService();
    private final JsonMapper<UserObject> jsonMapper = new JsonMapper<UserObject>(JsonPath.User);

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label loginWarning;

    public static UserObject findUserByUsername(String username, JsonMapper<UserObject> jsonMapper) {
        return jsonMapper.loadFromJson().stream()
                .filter(entry -> entry.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public void login(ActionEvent event) {
        try {
            if (checkCredentials()) {
                Variables.currentUserId = findUserByUsername(usernameField.getText(), jsonMapper)
                                .getId();
                Variables.currentUserPassword = passwordField.getText();
                Variables.DATA_PATH = usernameField.getText() + "-data.json";
                JsonPath.Data.updatePath(Variables.DATA_PATH);
                System.out.println(Variables.DATA_PATH);
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

    public void handleImport(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Exported Data");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ZIP files (*.zip)", "*.zip"));
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try {
                ImportPasswords importPw = new ImportPasswords();
                File userFile = new File(JsonPath.User.path);
                File dataFile = new File(JsonPath.Data.path);
                importPw.importPasswords(file, userFile, dataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkCredentials() {
        UserObject user = findUserByUsername(usernameField.getText(), jsonMapper);
        if (user == null) {
            loginWarning.setText("Username not found.");
            System.out.println("Username not found.");
            return false;
        }
        try {
            if (UserObject.getHashedPassword(passwordField.getText()).equals(user.getPassword())) {
                System.out.println("Login successful.");
                return true;
            } else {
                loginWarning.setText("Invalid credentials.");
                System.out.println("Invalid credentials.");
                return false;
            }
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {

            return false;
        }
    }
}
