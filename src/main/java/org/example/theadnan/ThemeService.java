package org.example.theadnan;

import javafx.scene.Scene;

/**
 * Utility to manage dark/light themes by adding/removing stylesheet paths.
 * Provides both init(scene) and initScene(scene) for compatibility with different callers.
 */
public final class ThemeService {

    private static final String DARK_CSS = "/org/example/theadnan/dark.css";
    private static final String LIGHT_CSS = "/org/example/theadnan/light.css";

    // default theme
    private static boolean dark = true;

    private ThemeService() {}

    /**
     * Initialize a scene for theme handling: remove any existing theme entries and
     * apply the current theme.
     */
    public static void init(Scene scene) {
        if (scene == null) return;
        scene.getStylesheets().remove(DARK_CSS);
        scene.getStylesheets().remove(LIGHT_CSS);
        applyCurrent(scene);
    }

    /**
     * Compatibility alias used by some controllers.
     */
    public static void initScene(Scene scene) {
        init(scene);
    }

    public static void applyDark(Scene scene) {
        if (scene == null) return;
        scene.getStylesheets().remove(LIGHT_CSS);
        if (!scene.getStylesheets().contains(DARK_CSS)) {
            scene.getStylesheets().add(DARK_CSS);
        }
        dark = true;
    }

    public static void applyLight(Scene scene) {
        if (scene == null) return;
        scene.getStylesheets().remove(DARK_CSS);
        if (!scene.getStylesheets().contains(LIGHT_CSS)) {
            scene.getStylesheets().add(LIGHT_CSS);
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