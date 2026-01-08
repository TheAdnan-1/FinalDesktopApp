package org.example.theadnan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.theadnan.services.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class CurrencyController {

    @FXML
    private TextField amountField;

    @FXML
    private ComboBox<String> fromCurrency;

    @FXML
    private ComboBox<String> toCurrency;

    @FXML
    private Label resultLabel;

    private Map<String, Double> rates;

    @FXML
    public void initialize() {
        loadCurrencies();
    }

    private void loadCurrencies() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.exchangerate-api.com/v4/latest/USD"))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            rates = JsonParser.parseRates(response.body());

            fromCurrency.getItems().addAll(rates.keySet());
            toCurrency.getItems().addAll(rates.keySet());

            fromCurrency.setValue("USD");
            toCurrency.setValue("EUR");

        } catch (Exception e) {
            resultLabel.setText("Failed to load currency data.");
        }
    }

    @FXML
    public void convertCurrency() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String from = fromCurrency.getValue();
            String to = toCurrency.getValue();

            double usdAmount = amount / rates.get(from);
            double converted = usdAmount * rates.get(to);

            resultLabel.setText(
                    String.format("%.2f %s = %.2f %s",
                            amount, from, converted, to)
            );
        } catch (Exception e) {
            resultLabel.setText("Invalid input or conversion error.");
        }
    }

    @FXML
    public void goBack(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/theadnan/home.fxml")
            );
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
