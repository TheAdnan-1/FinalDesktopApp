package org.example.theadnan;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import org.example.theadnan.services.AuthService;

public class RegisterController {

    @FXML private TextField name, email, age, profession, hobby;
    @FXML private PasswordField password;
    @FXML private Label status;

    public void registerUser() {
        try {
            boolean success = AuthService.register(
                    email.getText(),
                    name.getText(),
                    Integer.parseInt(age.getText()),
                    profession.getText(),
                    hobby.getText(),
                    password.getText()
            );

            status.setText(success
                    ? "Registration successful!"
                    : "Email already exists!");

        } catch (Exception e) {
            status.setText("Invalid input!");
        }
    }

    @FXML
    public void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/theadnan/home.fxml"));
            Scene scene = new Scene(loader.load());
            ThemeService.initScene(scene);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            status.setText("Failed to go back");
        }
    }
}