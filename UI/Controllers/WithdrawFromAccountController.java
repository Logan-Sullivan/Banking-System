import Account_Classes.Account;
import Account_Classes.SavingsAccount;
import User_Classes.Customer;
import Utils.AppState;
import Utils.CsvManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class WithdrawFromAccountController {

    @FXML
    private TextField customerIdField;

    @FXML
    private TextField withdrawAmountField;

    @FXML
    private Label statusLabel;

    @FXML
    private void withdrawPressed(ActionEvent event) {
        String customerId = customerIdField == null ? "" : customerIdField.getText();
        if (customerId == null || customerId.isBlank()) {
            if (statusLabel != null) statusLabel.setText("Enter customer ID.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(withdrawAmountField == null ? "" : withdrawAmountField.getText());
        } catch (Exception e) {
            if (statusLabel != null) statusLabel.setText("Invalid withdrawal amount.");
            return;
        }

        if (amount <= 0) {
            if (statusLabel != null) statusLabel.setText("Amount must be greater than 0.");
            return;
        }

        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            if (statusLabel != null) statusLabel.setText("Customer not found: " + customerId);
            return;
        }

        SavingsAccount savings = findFirstSavings(customer);
        if (savings == null) {
            if (statusLabel != null) statusLabel.setText("No savings account found for customer.");
            return;
        }

        // Minimal savings rule for demo: do not allow withdrawing more than the current savings balance.
        if (amount > savings.getBalance()) {
            if (statusLabel != null) statusLabel.setText("Insufficient funds. Balance: $" + String.format("%.2f", savings.getBalance()));
            return;
        }

        savings.withdraw(amount);
        CsvManager.writeCustomersToCsv(AppState.customers);

        if (statusLabel != null) {
            statusLabel.setText("Withdrew $" + String.format("%.2f", amount) + ". New balance: $" + String.format("%.2f", savings.getBalance()));
        }
    }

    private Customer findCustomerById(String customerId) {
        if (AppState.customers == null) return null;
        for (int i = 0; i < AppState.customers.getMcount(); i++) {
            Customer c = AppState.customers.getValue(i);
            if (c != null && customerId.equals(c.customerId)) return c;
        }
        return null;
    }

    private SavingsAccount findFirstSavings(Customer customer) {
        if (customer == null || customer.accountList == null) return null;
        for (Account account : customer.accountList) {
            if (account instanceof SavingsAccount savings) return savings;
        }
        return null;
    }

    @FXML
    private void returnToTellerScreen(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/TellerScreen.fxml")));
            stage.setScene(scene);
            stage.setTitle("Teller Screen");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
