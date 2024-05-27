package dev.yorun.faithsafe;

import dev.yorun.faithsafe.service.JsonMapper;
import dev.yorun.faithsafe.service.StageService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class EditPwController {

    private final StageService stageService = new StageService();
    private final JsonMapper jsonMapper = new JsonMapper();
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


    public void setOnEditSave(Consumer<Void> onEditSave) {
        this.onEditSave = onEditSave;
    }

    public void cancelEditPw(ActionEvent event) {
        try {
            Stage currentStage = (Stage) createPwConfirmPassword.getScene().getWindow();
        }
    }
}
