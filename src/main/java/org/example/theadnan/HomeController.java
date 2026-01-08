package org.example.theadnan;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HomeController {

    private void switchScene(ActionEvent event, String fxml) {
        try {
            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/theadnan/" + fxml)
            );
            stage.setScene(new Scene(loader.load()));
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

    public void exitApp() {
        System.exit(0);
    }
}
