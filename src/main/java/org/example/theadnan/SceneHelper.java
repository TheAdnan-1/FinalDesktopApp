package org.example.theadnan;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public final class SceneHelper {
    private SceneHelper() {}

    /**
     * Load an FXML from the classpath under /org/example/theadnan/...
     * Returns a Scene already initialized with the active ThemeService theme.
     * Use FXMLLoader#getController() on the passed loader if you need the controller.
     */
    public static Scene loadScene(String fxmlPath) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                SceneHelper.class.getResource("/org/example/theadnan/" + fxmlPath)
        );
        Parent root = loader.load();
        Scene scene = new Scene(root);
        ThemeService.initScene(scene);
        return scene;
    }

    /**
     * Convenience: load scene and return loader (to access controller).
     * Use like: FXMLLoader loader = SceneHelper.loadFxml("dashboard.fxml"); Scene scene = new Scene(loader.load()); ThemeService.initScene(scene);
     */
    public static FXMLLoader loadFxml(String fxmlPath) {
        return new FXMLLoader(SceneHelper.class.getResource("/org/example/theadnan/" + fxmlPath));
    }
}