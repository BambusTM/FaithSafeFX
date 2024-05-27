package dev.yorun.faithsafe;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginController {

  @FXML
  private PasswordField passwordField;
  @FXML
  private Button loginButton;
  @FXML
  private Button resetButton;

  public void login() {
      checkPassword();
  }

  public void switchToResetPwView() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("reset-pw-view.fxml"));
      Parent root = loader.load();

      ResetPwController resetPwController = loader.getController();

      Stage popupStage = new Stage();
      Scene scene = new Scene(root);

      popupStage.setTitle("Reset Password");
      popupStage.setResizable(false);
      popupStage.initModality(Modality.APPLICATION_MODAL);
      popupStage.setScene(scene);

      popupStage.showAndWait();
    } catch (Exception e) {
      System.err.println("Failed to switch to create-pw-view.fxml: " + e.getMessage());
    }
  }

  private void checkPassword() {

  }
}
