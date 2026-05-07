package Utils;

import User_Classes.Customer;
import java.util.HashMap;

/**
 * Minimal shared in-memory state for the JavaFX UI.
 * Keeps the UI unblocked while the team decides on a real persistence/service layer.
 */
public final class AppState {
    public static ArrayListManager<Customer> customers = new ArrayListManager<>();
    public static HashMap<String, Double> loanRates = new HashMap<>();
    public static HashMap<String, String> loanEffectiveDates = new HashMap<>();
    public static HashMap<String, Double> accountRates = new HashMap<>();
    public static HashMap<String, String> accountEffectiveDates = new HashMap<>();
    public static Timeline timeline = new Timeline();

    private AppState() {
    }
    public double closeCustomerAccount(int customerIndex){
        double amount = 0.0;
        amount = customers.getValue(customerIndex).closeCustomer();
        customers.removeM(customerIndex);
        return amount;
    }
}
