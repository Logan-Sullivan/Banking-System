package Utils;

import User_Classes.Customer;

/**
 * Minimal shared in-memory state for the JavaFX UI.
 * Keeps the UI unblocked while the team decides on a real persistence/service layer.
 */
public final class AppState {
    public static ArrayListManager<Customer> customers = new ArrayListManager<>();
    public static ArrayListManager<Check> checks = new ArrayListManager<>();
    private AppState() {
    }
}

