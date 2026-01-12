package org.example.theadnan;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.theadnan.services.AuthService;
import org.example.theadnan.services.MoneyRequestService;

import java.util.List;
import java.util.Optional;

public class DashboardController {

    @FXML
    private Label info;

    @FXML
    private TextField balanceEmail;

    @FXML
    private PasswordField balancePassword;

    @FXML
    private TextField balanceAmount;

    @FXML
    private TextField requestEmailField;

    @FXML
    private TextField requestAmountField;

    @FXML
    private ListView<MoneyRequest> requestsList;

    @FXML
    private Label statusLabel;

    private String currentUserEmail;

    private final ObservableList<MoneyRequest> requests = FXCollections.observableArrayList();

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
                                "Hobby: " + u.getHobby() + "\n" +
                                String.format("Balance: %.2f", u.getBalance())
                );
                // pre-fill balanceEmail (editable as requested)
                balanceEmail.setText(u.getEmail());

                loadRequests();
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

    // ---------------- Balance update ----------------
    @FXML
    public void addBalance() {
        changeBalance(true);
    }

    @FXML
    public void deductBalance() {
        changeBalance(false);
    }

    private void changeBalance(boolean add) {
        statusLabel.setText("");
        String email = balanceEmail.getText().trim();
        String password = balancePassword.getText();
        double amount;
        try {
            amount = Double.parseDouble(balanceAmount.getText().trim());
            if (amount <= 0) { statusLabel.setText("Amount must be > 0"); return; }
        } catch (Exception e) {
            statusLabel.setText("Invalid amount");
            return;
        }

        if (!email.equals(currentUserEmail)) {
            statusLabel.setText("You can only modify your own balance");
            return;
        }

        double delta = add ? amount : -amount;
        String err = AuthService.updateBalanceWithAuth(email, password, delta);
        if (err == null) {
            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Balance updated");
            // refresh user info and requests
            loadUser(currentUserEmail);
        } else {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText(err);
        }
    }

    // ---------------- Request money ----------------
    @FXML
    public void sendRequest() {
        statusLabel.setText("");
        String toEmail = requestEmailField.getText().trim();
        double amount;
        try {
            amount = Double.parseDouble(requestAmountField.getText().trim());
            if (amount <= 0) { statusLabel.setText("Amount must be > 0"); return; }
        } catch (Exception e) {
            statusLabel.setText("Invalid amount");
            return;
        }

        try {
            MoneyRequestService.createRequest(currentUserEmail, toEmail, amount);
            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Request sent");
            loadRequests();
        } catch (Exception e) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Failed to send request");
            e.printStackTrace();
        }
    }

    private void loadRequests() {
        try {
            List<MoneyRequest> list = MoneyRequestService.getRequestsForUser(currentUserEmail);
            requests.setAll(list);
            requestsList.setItems(requests);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void acceptSelectedRequest() {
        statusLabel.setText("");
        MoneyRequest sel = requestsList.getSelectionModel().getSelectedItem();
        if (sel == null) { statusLabel.setText("Select a request"); return; }
        try {
            String err = MoneyRequestService.acceptRequest(sel.getId(), currentUserEmail);
            if (err == null) {
                statusLabel.setStyle("-fx-text-fill: green;");
                statusLabel.setText("Request accepted");
                loadRequests();
                loadUser(currentUserEmail);
            } else {
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setText(err);
            }
        } catch (Exception e) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Error accepting request");
            e.printStackTrace();
        }
    }

    @FXML
    public void cancelSelectedRequest() {
        statusLabel.setText("");
        MoneyRequest sel = requestsList.getSelectionModel().getSelectedItem();
        if (sel == null) { statusLabel.setText("Select a request"); return; }
        try {
            String err = MoneyRequestService.cancelRequest(sel.getId(), currentUserEmail);
            if (err == null) {
                statusLabel.setStyle("-fx-text-fill: green;");
                statusLabel.setText("Request cancelled");
                loadRequests();
            } else {
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setText(err);
            }
        } catch (Exception e) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Error cancelling request");
            e.printStackTrace();
        }
    }
}