package org.example.theadnan;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.theadnan.ThemeService;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                MainApplication.class.getResource("home.fxml")
        );
        Scene scene = new Scene(loader.load());
        stage.setTitle("Utility Desktop Application");

        // Initialize theme handling (default: dark)
        ThemeService.init(scene);
        ThemeService.applyDark(scene);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}