package dev.yorun.faithsafe;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Application extends javafx.application.Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login-view.fxml")));
            Scene scene = new Scene(root);

            // Image icon = new Image("src/main/resources/Safe.png");
            // stage.getIcons().add();

            stage.setTitle("FaithSafe");
            stage.setResizable(true);
            stage.setFullScreen(false);
            stage.setFullScreenExitHint("Press 'ESC' to exit full screen");
            stage.setFullScreenExitKeyCombination(KeyCombination.valueOf("ESC"));

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Failed to load main-view.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
