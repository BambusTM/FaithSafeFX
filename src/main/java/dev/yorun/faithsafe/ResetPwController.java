package dev.yorun.faithsafe;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

public class ResetPwController {
  @FXML
  private PasswordField passwordField;
  @FXML
  private PasswordField confirmPasswordField;
  @FXML
  private Button backButton;
  @FXML
  private Button finishButton;

  public void resetPassword() {
    passwordField.clear();
    confirmPasswordField.clear();
  }
}
