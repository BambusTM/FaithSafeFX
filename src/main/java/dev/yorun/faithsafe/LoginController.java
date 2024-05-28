package dev.yorun.faithsafe;

import static dev.yorun.faithsafe.service.Variables.USER_PATH;

import dev.yorun.faithsafe.service.JsonMapper;
import dev.yorun.faithsafe.service.StageService;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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


  public void login() {
    checkPassword();
  }

  public void switchToResetPwView(ActionEvent event) {
    try {
      Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login-reset-view.fxml")));
      Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    } catch (Exception e) {
      System.err.println("Failed to switch to login-reset-view.fxml: " + e.getMessage());
    }
  }

  private void checkPassword() {
  }
}
