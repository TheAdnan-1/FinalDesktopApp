package org.example.theadnan;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.theadnan.services.NotesService;

import java.sql.ResultSet;

public class NotesController {

    @FXML
    private ListView<String> notesList;

    @FXML
    private TextField titleField;

    @FXML
    private TextArea contentArea;

    private String userEmail;
    private final ObservableList<String> notes = FXCollections.observableArrayList();

    // called from DashboardController
    public void setUserEmail(String email) {
        this.userEmail = email;
        loadNotes();
        setupSelectionListener();
    }

    // ---------------- LOAD TITLES ----------------
    private void loadNotes() {
        notes.clear();

        try (ResultSet rs = NotesService.getNotes(userEmail)) {
            while (rs.next()) {
                notes.add(rs.getString("title"));
            }
            notesList.setItems(notes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- CLICK TITLE → LOAD CONTENT ----------------
    private void setupSelectionListener() {
        notesList.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        loadNoteContent(newVal);
                    }
                }
        );
    }

    private void loadNoteContent(String title) {
        try (ResultSet rs = NotesService.getNote(userEmail, title)) {
            if (rs.next()) {
                titleField.setText(title);
                contentArea.setText(rs.getString("content"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- SAVE (INSERT OR UPDATE) ----------------
    @FXML
    public void saveNote() throws Exception {
        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();

        if (title.isEmpty() || content.isEmpty()) return;

        NotesService.saveNote(userEmail, title, content);
        loadNotes();
    }

    // ---------------- DELETE ----------------
    @FXML
    public void deleteNote() throws Exception {
        String title = titleField.getText().trim();
        if (title.isEmpty()) return;

        NotesService.deleteNote(userEmail, title);
        titleField.clear();
        contentArea.clear();
        loadNotes();
    }

    // ---------------- BACK ----------------
    @FXML
    public void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/theadnan/dashboard.fxml")
            );
            Scene scene = new Scene(loader.load());

            DashboardController controller = loader.getController();
            controller.loadUser(userEmail);

            Stage stage = (Stage) notesList.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
