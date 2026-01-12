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

import java.util.List;
import java.util.Optional;

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

        try {
            List<String> titles = NotesService.getNotes(userEmail);
            notes.addAll(titles);
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
        try {
            Optional<String> contentOpt = NotesService.getNoteContent(userEmail, title);
            if (contentOpt.isPresent()) {
                titleField.setText(title);
                contentArea.setText(contentOpt.get());
            } else {
                titleField.setText(title);
                contentArea.clear();
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
            FXMLLoader loader = SceneHelper.loadFxml("dashboard.fxml");
            Scene scene = new Scene(loader.load());
            ThemeService.initScene(scene); // already done by SceneHelper if you use loadScene, but kept here for clarity
            DashboardController controller = loader.getController();
            controller.loadUser(userEmail);
            Stage stage = (Stage) notesList.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}