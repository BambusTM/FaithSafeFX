package dev.yorun.faithsafe;

import dev.yorun.faithsafe.service.JsonMapper;
import dev.yorun.faithsafe.service.StageService;
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
import static dev.yorun.faithsafe.service.Variables.DATA_PATH;

import java.util.Objects;
import java.util.function.Consumer;

public class EditPwController {

    private final StageService stageService = new StageService();
    private final JsonMapper jsonMapper = new JsonMapper(DATA_PATH);
    private Consumer<Void> onEditSave;
    private int id;

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

    public void init(int id, String username, String domain, String password, String warning, String description) {
        this.id = id;
        this.createPwUsername.setText(username);
        this.createPwDomain.setText(domain);
        this.createPwPassword.setText(password);
        this.createPwConfirmPassword.setText(password);
        this.createPwWarning.setText(warning);
        this.createPwDescription.setText(description);
        
    }


    public void setOnEditSave(Consumer<Void> onEditSave) {
        this.onEditSave = onEditSave;
    }

    public void cancelEditPw(ActionEvent event) {

        try {
            Stage currentStage = (Stage) createPwConfirmPassword.getScene().getWindow();

            stageService.conformationPopup(currentStage,
                    "Cancel",
                    "Cancel Confirmation",
                    "Are you sure you want to cancel your creation? All data will be lost.",
                    aBoolean -> {
                        if (aBoolean) { currentStage.close(); }
                    }
                    );
        } catch (Exception e) {
            System.err.println("Failed to cancel: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void finishEditPw(ActionEvent event) {
        String username = createPwUsername.getText();
        String domain = createPwDomain.getText();
        String email = createPwEmail.getText();
        String password = createPwPassword.getText();
        String description = createPwDescription.getText();

        if (confirmPw()) {
            jsonMapper.updateEntry(DATA_PATH, id, username, domain, email, password, description);

            if (onEditSave != null) {
                onEditSave.accept(null);
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
        } else {
            System.err.println("Error: Passwords do not match.");
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
