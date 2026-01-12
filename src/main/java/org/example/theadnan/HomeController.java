package org.example.theadnan;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;

public class HomeController {

    private void switchScene(ActionEvent event, String fxml) {
        try {
            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/theadnan/" + fxml)
            );
            Scene scene = new Scene(loader.load());
            // initialize theme for the newly created scene
            ThemeService.initScene(scene);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openCurrency(ActionEvent event) {
        switchScene(event, "currency.fxml");
    }

    public void openLogin(ActionEvent event) {
        switchScene(event, "login.fxml");
    }

    public void openRegister(ActionEvent event) {
        switchScene(event, "register.fxml");
    }

    public void openWeather(ActionEvent event) {
        switchScene(event, "weather.fxml");
    }

    public void toggleTheme(ActionEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();
        ThemeService.toggle(scene);
    }

    public void exitApp() {
        System.exit(0);
    }
}