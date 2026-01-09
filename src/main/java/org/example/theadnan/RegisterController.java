package org.example.theadnan;

import javafx.fxml.FXML;
import javafx.scene.control.*;
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
}
