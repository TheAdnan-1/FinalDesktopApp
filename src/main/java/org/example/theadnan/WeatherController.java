package org.example.theadnan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class WeatherController {

    @FXML private TextField cityField;
    @FXML private Button fetchButton;
    @FXML private Label locationLabel;
    @FXML private Label tempLabel;
    @FXML private Label condLabel;
    @FXML private Label windLabel;
    @FXML private Button backButton;
    @FXML private Label statusLabel;

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @FXML
    public void initialize() {
        cityField.setPromptText("Enter city (e.g., London)");
        // optional default
        cityField.setText("New York");
        // you can pre-run fetchWeather() here or not
    }

    // NOTE: method must be public (or annotated @FXML) and signature must match FXML onAction
    @FXML
    public void fetchWeather(ActionEvent event) {
        fetchWeather();
    }

    // programmatic version used by the button handler above
    private void fetchWeather() {
        statusLabel.setText("");
        String city = cityField.getText().trim();
        if (city.isEmpty()) {
            statusLabel.setText("Please enter a city name");
            return;
        }

        fetchButton.setDisable(true);

        new Thread(() -> {
            try {
                Optional<double[]> coords = geocodeCity(city);
                if (coords.isEmpty()) {
                    runOnFx(() -> {
                        statusLabel.setText("City not found");
                        fetchButton.setDisable(false);
                    });
                    return;
                }
                double lat = coords.get()[0];
                double lon = coords.get()[1];

                String weatherUrl = String.format(
                        "https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&current_weather=true&timezone=auto",
                        lat, lon
                );

                HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(weatherUrl))
                        .GET()
                        .build();

                HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
                JsonNode root = mapper.readTree(resp.body());

                JsonNode current = root.path("current_weather");
                if (current.isMissingNode()) {
                    runOnFx(() -> {
                        statusLabel.setText("Weather data unavailable");
                        fetchButton.setDisable(false);
                    });
                    return;
                }

                double temp = current.path("temperature").asDouble(Double.NaN);
                double wind = current.path("windspeed").asDouble(Double.NaN);
                int weatherCode = current.path("weathercode").asInt(-1);

                String cond = weatherDescription(weatherCode);
                String displayLocation = city + " (" + String.format("%.4f, %.4f", lat, lon) + ")";

                runOnFx(() -> {
                    locationLabel.setText(displayLocation);
                    tempLabel.setText(String.format("%.1f °C", temp));
                    condLabel.setText(cond);
                    windLabel.setText(String.format("Wind: %.1f km/h", wind));
                    fetchButton.setDisable(false);
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnFx(() -> {
                    statusLabel.setText("Failed to fetch weather");
                    fetchButton.setDisable(false);
                });
            }
        }).start();
    }

    private Optional<double[]> geocodeCity(String city) throws Exception {
        String url = "https://geocoding-api.open-meteo.com/v1/search?name=" + java.net.URLEncoder.encode(city, "UTF-8") + "&count=1";
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        JsonNode root = mapper.readTree(resp.body());
        JsonNode results = root.path("results");
        if (results.isArray() && results.size() > 0) {
            JsonNode r = results.get(0);
            double lat = r.path("latitude").asDouble();
            double lon = r.path("longitude").asDouble();
            return Optional.of(new double[]{lat, lon});
        }
        return Optional.empty();
    }

    private String weatherDescription(int code) {
        return switch (code) {
            case 0 -> "Clear sky";
            case 1, 2, 3 -> "Mainly clear / partly cloudy";
            case 45, 48 -> "Fog / depositing rime fog";
            case 51, 53, 55 -> "Drizzle";
            case 56, 57 -> "Freezing drizzle";
            case 61, 63, 65 -> "Rain";
            case 66, 67 -> "Freezing rain";
            case 71, 73, 75 -> "Snow fall";
            case 77 -> "Snow grains";
            case 80, 81, 82 -> "Rain showers";
            case 85, 86 -> "Snow showers";
            case 95, 96, 99 -> "Thunderstorm";
            default -> "Unknown";
        };
    }

    private void runOnFx(Runnable r) {
        javafx.application.Platform.runLater(r);
    }

    @FXML
    public void goBack(ActionEvent event) {
        try {
            boolean loggedIn = Session.isLoggedIn();
            String fxml = loggedIn ? "/org/example/theadnan/dashboard.fxml" : "/org/example/theadnan/home.fxml";

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Scene scene = new Scene(loader.load());

            ThemeService.initScene(scene);

            if (loggedIn) {
                DashboardController controller = loader.getController();
                controller.loadUser(Session.getCurrentUser());
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Failed to go back");
        }
    }
}