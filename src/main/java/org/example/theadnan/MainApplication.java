package org.example.theadnan;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                MainApplication.class.getResource("home.fxml")
        );
        Scene scene = new Scene(loader.load());
        stage.setTitle("Currency Converter and Journal App");

        // Ensure the first scene has theme applied
        ThemeService.init(scene);
        // Optionally set default: (ThemeService.applyDark(scene) or applyLight)
        // ThemeService.applyDark(scene);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}