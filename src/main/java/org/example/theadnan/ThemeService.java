package org.example.theadnan;

import javafx.scene.Scene;

/**
 * Theme manager: exposes init(scene), initScene(scene), applyDark/applyLight/toggle/isDark.
 */
public final class ThemeService {
    private static boolean dark = true;

    private ThemeService() {}

    public static void init(Scene scene) {
        if (scene == null) return;
        scene.getStylesheets().remove(ThemeService.class.getResource("dark.css").toExternalForm());
        scene.getStylesheets().remove(ThemeService.class.getResource("light.css").toExternalForm());
        applyCurrent(scene);
    }

    // compatibility alias (some controllers call initScene)
    public static void initScene(Scene scene) {
        init(scene);
    }

    public static void applyDark(Scene scene) {
        if (scene == null) return;
        scene.getStylesheets().remove(ThemeService.class.getResource("light.css").toExternalForm());
        if (!scene.getStylesheets().contains(ThemeService.class.getResource("dark.css").toExternalForm())) {
            scene.getStylesheets().add(ThemeService.class.getResource("dark.css").toExternalForm());
        }
        dark = true;
    }

    public static void applyLight(Scene scene) {
        if (scene == null) return;
        scene.getStylesheets().remove(ThemeService.class.getResource("dark.css").toExternalForm());
        if (!scene.getStylesheets().contains(ThemeService.class.getResource("light.css").toExternalForm())) {
            scene.getStylesheets().add(ThemeService.class.getResource("light.css").toExternalForm());
        }
        dark = false;
    }

    public static void toggle(Scene scene) {
        if (scene == null) return;
        if (dark) applyLight(scene); else applyDark(scene);
    }

    public static boolean isDark() {
        return dark;
    }

    private static void applyCurrent(Scene scene) {
        if (dark) applyDark(scene); else applyLight(scene);
    }
}