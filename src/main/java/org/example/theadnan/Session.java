package org.example.theadnan;

/**
 * Simple in-memory session holder for the currently logged-in user.
 */
public final class Session {
    private static String currentUserEmail = null;

    private Session() {}

    public static void setCurrentUser(String email) {
        currentUserEmail = email;
    }

    public static String getCurrentUser() {
        return currentUserEmail;
    }

    public static void clear() {
        currentUserEmail = null;
    }

    public static boolean isLoggedIn() {
        return currentUserEmail != null && !currentUserEmail.isBlank();
    }
}