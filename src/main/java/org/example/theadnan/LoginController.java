package org.example.theadnan;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.theadnan.services.AuthService;

import java.util.Optional;

public class LoginController {

    @FXML private TextField email;
    @FXML private PasswordField password;
    @FXML private Label status;

    // Admin credentials (kept for reference). The actual admin routing uses the isAdmin flag
    // stored in the users table (the Database initializer ensures admin@example.app.com exists).
    private static final String ADMIN_EMAIL = "admin@example.app.com";
    private static final String ADMIN_PASSWORD = "AdminIsTheKing";

    public void loginUser() {
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

        // 2) Auth succeeded. Determine whether the user is admin (use DB flag).
        try {
            Optional<User> opt = AuthService.getUser(inputEmail);
            if (opt.isPresent() && opt.get().isAdmin()) {
                // Admin -> open admin panel
                try {
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/org/example/theadnan/admin.fxml"));
                    Scene scene = new Scene(loader.load());
                    Stage stage = (Stage) email.getScene().getWindow();
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

                    DashboardController controller = loader.getController();
                    controller.loadUser(inputEmail);

                    Stage stage = (Stage) email.getScene().getWindow();
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
}