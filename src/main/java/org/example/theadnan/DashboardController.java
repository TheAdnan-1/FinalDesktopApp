package org.example.theadnan;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.theadnan.services.AuthService;

import java.util.Optional;

public class DashboardController {

    @FXML
    private Label info;

    private String currentUserEmail;

    // called after login
    public void loadUser(String email) {
        this.currentUserEmail = email;

        try {
            Optional<User> opt = AuthService.getUser(email);
            if (opt.isPresent()) {
                User u = opt.get();
                info.setText(
                        "Name: " + u.getName() + "\n" +
                                "Email: " + u.getEmail() + "\n" +
                                "Age: " + u.getAge() + "\n" +
                                "Profession: " + u.getProfession() + "\n" +
                                "Hobby: " + u.getHobby()
                );
            } else {
                info.setText("Failed to load user data");
            }

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