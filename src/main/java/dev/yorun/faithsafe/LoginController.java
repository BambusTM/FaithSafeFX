package dev.yorun.faithsafe;

import static dev.yorun.faithsafe.service.Variables.USER_PATH;

import dev.yorun.faithsafe.service.JsonMapper;
import dev.yorun.faithsafe.service.StageService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class LoginController {
  private final StageService stageService = new StageService();
  private final JsonMapper jsonMapper = new JsonMapper(USER_PATH);

  @FXML
  private PasswordField passwordField;
  @FXML
  private Button loginButton;
  @FXML
  private Button resetButton;

  private Scene loginScene;

  @FXML
  public void initialize() {
    loginScene = loginButton.getScene();
  }

  public void login() {
    checkPassword();
  }

  public void switchToResetPwView(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("login-reset-view.fxml"));
      Parent root = loader.load();
      ResetPwController resetPwController = loader.getController();
      resetPwController.setPreviousScene(loginScene);
      Stage currentStage = (Stage) loginButton.getScene().getWindow();
      Scene scene = new Scene(root);
      currentStage.setScene(scene);
      currentStage.setTitle("Reset Password");
    } catch (Exception e) {
      System.err.println("Failed to switch to login-reset-view.fxml: " + e.getMessage());
    }
  }

  private void checkPassword() {
  }
}
