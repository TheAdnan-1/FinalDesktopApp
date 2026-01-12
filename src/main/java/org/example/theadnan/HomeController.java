package org.example.theadnan;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;

public class HomeController {

    private void loadScene(String fxml, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/theadnan/" + fxml)
            );
            Scene scene = new Scene(loader.load());
            ThemeService.initScene(scene);

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openCurrency(ActionEvent event) {
        loadScene("currency.fxml", event);
    }

    public void openLogin(ActionEvent event) {
        loadScene("login.fxml", event);
    }

    public void openRegister(ActionEvent event) {
        loadScene("register.fxml", event);
    }

    public void openWeather(ActionEvent event) {
        loadScene("weather.fxml", event);
    }

    public void toggleTheme(ActionEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();
        ThemeService.toggle(scene);
    }

    public void exitApp() {
        System.exit(0);
    }
}