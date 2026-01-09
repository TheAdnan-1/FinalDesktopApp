package org.example.theadnan;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.theadnan.services.AuthService;

public class LoginController {

    @FXML private TextField email;
    @FXML private PasswordField password;
    @FXML private Label status;

    public void loginUser() {
        try {
            if (!AuthService.login(email.getText(), password.getText())) {
                status.setText("Invalid email or password");
                return;
            }

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/theadnan/dashboard.fxml"));
            Scene scene = new Scene(loader.load());

            DashboardController controller = loader.getController();
            controller.loadUser(email.getText());

            Stage stage = (Stage) email.getScene().getWindow();
            stage.setScene(scene);

        } catch (Exception e) {
            status.setText("Login error");
        }
    }
}
