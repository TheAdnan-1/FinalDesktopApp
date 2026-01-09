package org.example.theadnan;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.theadnan.services.AuthService;

import java.sql.ResultSet;

public class DashboardController {

    @FXML private Label info;

    public void loadUser(String email) {
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
        }
    }
}
