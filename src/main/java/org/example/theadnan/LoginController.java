package org.example.theadnan;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import org.example.theadnan.services.AuthService;

import java.util.Optional;

public class LoginController {

    @FXML private TextField email;
    @FXML private PasswordField password;
    @FXML private Label status;

    public void loginUser(ActionEvent event) {
        String inputEmail = email.getText().trim();
        String inputPassword = password.getText();

        // 1) Authenticate and check blocked state
        String st;
        try {
            st = AuthService.loginStatus(inputEmail, inputPassword);
        } catch (Exception e) {
            e.printStackTrace();
            status.setText("Login error (auth). See console.");
            return;
        }

        if ("BLOCKED".equals(st)) {
            status.setText("Account blocked. Contact admin.");
            return;
        }

        if (!"OK".equals(st)) {
            status.setText("Invalid email or password");
            return;
        }

        // Set session
        Session.setCurrentUser(inputEmail);

        // 2) Auth succeeded. Determine whether the user is admin (use DB flag).
        try {
            Optional<org.example.theadnan.User> opt = org.example.theadnan.services.AuthService.getUser(inputEmail);
            if (opt.isPresent() && opt.get().isAdmin()) {
                // Admin -> open admin panel
                try {
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/org/example/theadnan/admin.fxml"));
                    Scene scene = new Scene(loader.load());
                    ThemeService.initScene(scene);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    status.setText("Failed to open admin UI. See console.");
                    return;
                }
            } else {
                // Normal user -> open dashboard
                try {
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/org/example/theadnan/dashboard.fxml"));
                    Scene scene = new Scene(loader.load());
                    ThemeService.initScene(scene);

                    DashboardController controller = loader.getController();
                    controller.loadUser(inputEmail);

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    status.setText("Failed to open dashboard. See console.");
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            status.setText("Login error (loading user). See console.");
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