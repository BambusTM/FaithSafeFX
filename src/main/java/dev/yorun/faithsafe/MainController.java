package dev.yorun.faithsafe;

import dev.yorun.faithsafe.objects.DataObject;
import dev.yorun.faithsafe.objects.ListObject;
import dev.yorun.faithsafe.service.ExportPasswords;
import dev.yorun.faithsafe.service.JsonMapper;
import dev.yorun.faithsafe.service.JsonPath;
import dev.yorun.faithsafe.service.StageService;
import dev.yorun.faithsafe.service.Variables;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController implements javafx.fxml.Initializable {
  private final JsonMapper<DataObject> jsonMapper = new JsonMapper<>(JsonPath.Data);
  private final StageService stageService = new StageService();

  @FXML
  private TableView<ListObject> pwTableView;
  @FXML
  private TableColumn<ListObject, String> usernameColumn;
  @FXML
  private TableColumn<ListObject, String> domainColumn;
  @FXML
  private TableColumn<ListObject, String> emailColumn;
  @FXML
  private TableColumn<ListObject, String> passwordColumn;
  @FXML
  private Label pwPreviewLabel1;
  @FXML
  private Label pwPreviewLabel2;
  @FXML
  private Label pwPreviewLabel3;

  public JsonPath path;


  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {
    usernameColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
    domainColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getDomain()));
    emailColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
    passwordColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getPassword()));

    refreshPasswordList();

    pwTableView.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> {
          pwPreviewLabel1.setText(newValue == null ? "" : usernameColumn.getCellData(newValue));
          pwPreviewLabel2.setText(newValue == null ? "" : emailColumn.getCellData(newValue));
          pwPreviewLabel3.setText(newValue == null ? "" : passwordColumn.getCellData(newValue));
        });
  }

  public void switchToCreatePwView() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("create-pw-view.fxml"));
      Parent root = loader.load();

      CreatePwController createPwController = loader.getController();
      createPwController.setOnPasswordCreated(aVoid -> refreshPasswordList());

      Stage popupStage = new Stage();
      Scene scene = new Scene(root);

      popupStage.setTitle("Create Password");
      popupStage.setResizable(false);
      popupStage.initModality(Modality.APPLICATION_MODAL);
      popupStage.setScene(scene);

      popupStage.setOnCloseRequest(e -> {
        e.consume();
        stageService.conformationPopup(popupStage,
            "Cancel",
            "Cancel Confirmation",
            "Are you sure you want to cancel your creation? All data will be lost.",
            confirmed -> {
              if (confirmed) {
                popupStage.close();
              }
            });
      });
      popupStage.showAndWait();
    } catch (Exception e) {
      System.err.println("Failed to switch to create-pw-view.fxml: " + e.getMessage());
    }
  }

  public void switchToEditPwView() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("edit-pw-view.fxml"));
      Parent root = loader.load();

      EditPwController editPwController = loader.getController();
      if (pwTableView.getSelectionModel().getSelectedItem() == null) {
        return;
      }
      ListObject item = pwTableView.getSelectionModel().getSelectedItem();
      DataObject data = jsonMapper.findById(item.getId());
      editPwController.init(
          item.getId(),
          data.getUsername(),
          data.getDomain(),
          data.getEmail(),
          data.getPassword(),
          "",
          data.getDescription()
      );
      editPwController.setOnEditSave(aVoid -> refreshPasswordList());

      Stage popupStage = new Stage();
      Scene scene = new Scene(root);

      popupStage.setTitle("Edit Password");
      popupStage.setResizable(false);
      popupStage.initModality(Modality.APPLICATION_MODAL);
      popupStage.setScene(scene);

      popupStage.setOnCloseRequest(e -> {
        e.consume();
        stageService.conformationPopup(popupStage,
            "Edit",
            "Cancel Confirmation",
            "Are you sure you want to cancel your creation? All data will be lost.",
            confirmed -> {
              if (confirmed) {
                popupStage.close();
              }
            });
      });
      popupStage.showAndWait();
    } catch (Exception e) {
      System.err.println("Failed to switch to edit-pw-view.fxml: " + e.getMessage());
    }
  }

  public void deletePassword(ActionEvent event) {
    ListObject selectedEntry = pwTableView.getSelectionModel().getSelectedItem();
    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

    if (selectedEntry != null) {
      int id = selectedEntry.getId();

      stageService.conformationPopup(currentStage,
          "Delete Password",
          "Delete Confirmation",
          "Are you sure you want delete this Password? You won't be able to recover it.",
          confirmed -> {
            if (confirmed) {
              jsonMapper.deleteEntry(id);
              refreshPasswordList();
            }
          });
    }
  }

  public void logout(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
      Parent root = loader.load();

      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      Scene scene = new Scene(root);

      stage.setTitle("FaithSafe - Login");
      stage.setResizable(false);
      stage.setFullScreen(false);
      stage.setScene(scene);
      stage.show();
    } catch (Exception e) {
      System.err.println("Failed to switch to login-view.fxml: " + e.getMessage());
    }
  }

  public void copyPassword() {
    Clipboard.getSystemClipboard().setContent(Map.of(DataFormat.PLAIN_TEXT, pwPreviewLabel3.getText()));
  }

  public void handleExport(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
    Parent root = loader.load();
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Exported Data");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ZIP files (*.zip)", "*.zip"));
    File file = fileChooser.showSaveDialog(stage);

    if (file != null) {
      try {
        ExportPasswords exportPw = new ExportPasswords();
        File userFile = new File(JsonPath.User.path);
        File dataFile = new File(JsonPath.Data.path);
        exportPw.exportPasswords(userFile, dataFile, file);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void refreshPasswordList() {
    pwTableView.getItems().clear();
    List<DataObject> passwordEntries = jsonMapper.loadFromJson();
    for (DataObject entry : passwordEntries) {
      if (entry.getOwnerId() != Variables.currentUserId) {
        continue;
      }
      pwTableView.getItems().add(
          new ListObject(entry.getId(), entry.getUsername(), entry.getDomain(),
              entry.getEmail(), entry.getPassword()));
    }
  }
}
