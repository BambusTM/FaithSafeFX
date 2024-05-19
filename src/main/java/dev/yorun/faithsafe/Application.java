package dev.yorun.faithsafe;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void logoutPopup(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("Press OK to logout, or Cancel to stay logged in.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("Logging out...");
            stage.close();
        } else {
            System.out.println("Failed.");
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), Color.rgb(31, 31, 31, 1));

            // Image icon = new Image("src/main/resources/Safe.png");
            // stage.getIcons().add();

            stage.setTitle("FaithSafe");
            stage.setResizable(true);
            stage.setFullScreen(false);
            stage.setFullScreenExitHint("Press 'ESC' to exit full screen");
            stage.setFullScreenExitKeyCombination(KeyCombination.valueOf("ESC"));

            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(event -> logoutPopup(stage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
