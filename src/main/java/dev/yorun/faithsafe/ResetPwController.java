package dev.yorun.faithsafe;

import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class ResetPwController {
  @FXML
  private PasswordField passwordField;
  @FXML
  private PasswordField confirmPasswordField;
  @FXML
  private Label passwordWarning;
  @FXML
  private Button backButton;
  @FXML
  private Button finishButton;

  public void finishReset() {
    if (confirmPassword()) {

    }
  }

  public void abortReset() {
    backButton.getScene().getWindow().hide();
  }

  private boolean confirmPassword() {
    if (Objects.equals(passwordField.getText(), confirmPasswordField.getText())) {
      passwordWarning.setText("");
      return true;
    } else {
      passwordWarning.setText("Passwords do not match.");
      return false;
    }
  }
}
