package org.example.theadnan;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.theadnan.services.AuthService;

import java.sql.ResultSet;

public class DashboardController {

    @FXML
    private Label info;

    private String currentUserEmail;

    // called after login
    public void loadUser(String email) {
        this.currentUserEmail = email;

        try {
            ResultSet rs = AuthService.getUser(email);

            info.setText(
                    "Name: " + rs.getString("name") + "\n" +
                            "Email: " + rs.getString("email") + "\n" +
                            "Age: " + rs.getInt("age") + "\n" +
                            "Profession: " + rs.getString("profession") + "\n" +
                            "Hobby: " + rs.getString("hobby")
            );

        } catch (Exception e) {
            info.setText("Failed to load user data");
            e.printStackTrace();
        }
    }

    @FXML
    public void openNotes() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/theadnan/notes.fxml")
            );

            Scene scene = new Scene(loader.load());

            NotesController controller = loader.getController();
            controller.setUserEmail(currentUserEmail);

            Stage stage = (Stage) info.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("My Notes");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
