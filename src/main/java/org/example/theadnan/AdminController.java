package org.example.theadnan;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.theadnan.services.ReportService;

import java.util.List;

public class AdminController {

    @FXML private ListView<Report> reportsList;
    @FXML private TextField reporterField;
    @FXML private TextField targetField;
    @FXML private TextArea messageArea;
    @FXML private TextField statusField;
    @FXML private Label adminStatus;

    private final ObservableList<Report> reports = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        reportsList.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            showReport(newV);
        });
        loadReports();
    }

    public void loadReports() {
        adminStatus.setText("");
        try {
            List<Report> list = ReportService.getAllReports();
            reports.setAll(list);
            reportsList.setItems(reports);
        } catch (Exception e) {
            adminStatus.setText("Failed to load reports");
            e.printStackTrace();
        }
    }

    private void showReport(Report r) {
        adminStatus.setText("");
        if (r == null) {
            reporterField.clear();
            targetField.clear();
            messageArea.clear();
            statusField.clear();
            return;
        }
        reporterField.setText(r.getReporterEmail());
        targetField.setText(r.getTargetEmail());
        messageArea.setText(r.getMessage());
        statusField.setText(r.getStatus());
    }

    @FXML
    public void markReviewed() {
        adminStatus.setText("");
        Report r = reportsList.getSelectionModel().getSelectedItem();
        if (r == null) { adminStatus.setText("Select a report"); return;}
        String err = ReportService.reviewReport(r.getId(), "admin", false);
        if (err == null) {
            adminStatus.setStyle("-fx-text-fill: green;");
            adminStatus.setText("Marked reviewed");
            loadReports();
        } else {
            adminStatus.setStyle("-fx-text-fill: red;");
            adminStatus.setText(err);
        }
    }

    @FXML
    public void blockUser() {
        adminStatus.setText("");
        Report r = reportsList.getSelectionModel().getSelectedItem();
        if (r == null) { adminStatus.setText("Select a report"); return;}
        String err = ReportService.reviewReport(r.getId(), "admin", true);
        if (err == null) {
            adminStatus.setStyle("-fx-text-fill: green;");
            adminStatus.setText("User blocked and report actioned");
            loadReports();
        } else {
            adminStatus.setStyle("-fx-text-fill: red;");
            adminStatus.setText(err);
        }
    }
    @FXML
    public void goBack() {
        try {
            Scene scene = SceneHelper.loadScene("home.fxml");
            Stage stage = (Stage) reportsList.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }