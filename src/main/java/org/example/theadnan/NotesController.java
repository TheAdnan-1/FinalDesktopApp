package org.example.theadnan;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.theadnan.services.NotesService;

import java.sql.ResultSet;

public class NotesController {

    @FXML private ListView<String> notesList;
    @FXML private TextField titleField;
    @FXML private TextArea contentArea;

    private String userEmail;
    private int selectedNoteId = -1;

    public void setUserEmail(String email) {
        this.userEmail = email;
        loadNotes();
    }

    private void loadNotes() {
        try {
            notesList.getItems().setAll(
                    NotesService.getNotesTitles(userEmail));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        notesList.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, selected) -> {
                    if (selected != null) {
                        loadNote(selected);
                    }
                });
    }

    private void loadNote(String selected) {
        try {
            selectedNoteId =
                    Integer.parseInt(selected.split(" - ")[0]);

            ResultSet rs =
                    NotesService.getNoteById(selectedNoteId);

            titleField.setText(rs.getString("title"));
            contentArea.setText(rs.getString("content"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void saveNote() {
        try {
            NotesService.addNote(
                    userEmail,
                    titleField.getText(),
                    contentArea.getText()
            );
            clearFields();
            loadNotes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void updateNote() {
        if (selectedNoteId == -1) return;

        try {
            NotesService.updateNote(
                    selectedNoteId,
                    titleField.getText(),
                    contentArea.getText()
            );
            loadNotes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteNote() {
        if (selectedNoteId == -1) return;

        try {
            NotesService.deleteNote(selectedNoteId);
            clearFields();
            loadNotes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        titleField.clear();
        contentArea.clear();
        selectedNoteId = -1;
    }

    @FXML
    public void goBack() throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/theadnan/dashboard.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) notesList.getScene().getWindow();
        stage.setScene(scene);
    }
}
