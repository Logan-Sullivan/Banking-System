import Account_Classes.Account;
import Account_Classes.CDAccount;
import Account_Classes.GDAccount;
import Account_Classes.SavingsAccount;
import Account_Classes.TMBAccount;
import User_Classes.Customer;
import Utils.AppState;
import Utils.CsvManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class DepositToAccountController {

    @FXML
    private ComboBox<String> customerIdComboBox;

    @FXML
    private ComboBox<String> accountComboBox;

    @FXML
    private TextField depositAmountField;

    @FXML
    private Label statusLabel;

    // maps dropdown text back to real account object
    private final Map<String, Account> accountMap = new HashMap<>();

    @FXML
    public void initialize() {
        if (customerIdComboBox != null) {
            customerIdComboBox.getItems().clear();

            if (AppState.customers != null) {
                for (int i = 0; i < AppState.customers.getMcount(); i++) {
                    Customer c = AppState.customers.getValue(i);
                    if (c != null && c.customerId != null && !c.customerId.isBlank()) {
                        customerIdComboBox.getItems().add(c.customerId);
                    }
                }
            }
        }
    }

    @FXML
    private void onCustomerChosen(ActionEvent event) {
        String customerId = customerIdComboBox == null ? "" : customerIdComboBox.getValue();

        accountMap.clear();

        if (accountComboBox != null) {
            accountComboBox.getItems().clear();
        }

        if (customerId == null || customerId.isBlank()) {
            if (statusLabel != null) {
                statusLabel.setText("Please select a customer.");
            }
            return;
        }

        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            if (statusLabel != null) {
                statusLabel.setText("Customer not found.");
            }
            return;
        }

        if (customer.accountList != null) {
            for (Account account : customer.accountList) {
                String displayText = buildAccountDisplay(account);
                accountMap.put(displayText, account);

                if (accountComboBox != null) {
                    accountComboBox.getItems().add(displayText);
                }
            }
        }

        if (statusLabel != null) {
            statusLabel.setText("Customer accounts loaded.");
        }
    }

    @FXML
    private void depositToAccount(ActionEvent event) {
        String customerId = customerIdComboBox == null ? "" : customerIdComboBox.getValue();
        String accountSelection = accountComboBox == null ? "" : accountComboBox.getValue();
        String amountText = depositAmountField == null ? "" : depositAmountField.getText();

        if (customerId == null || customerId.isBlank()) {
            if (statusLabel != null) statusLabel.setText("Please select a customer.");
            return;
        }

        if (accountSelection == null || accountSelection.isBlank()) {
            if (statusLabel != null) statusLabel.setText("Please select an account.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (Exception e) {
            if (statusLabel != null) statusLabel.setText("Please enter a valid deposit amount.");
            return;
        }

        if (amount <= 0) {
            if (statusLabel != null) statusLabel.setText("Deposit amount must be greater than zero.");
            return;
        }

        Account account = accountMap.get(accountSelection);
        if (account == null) {
            if (statusLabel != null) statusLabel.setText("Selected account could not be found.");
            return;
        }

        account.deposit(amount);

        // save updated balances back to CSV
        CsvManager.writeCustomersToCsv(AppState.customers);

        if (statusLabel != null) {
            statusLabel.setText("Deposit completed successfully.");
        }

        // refresh account display so balance updates in dropdown
        onCustomerChosen(null);
    }

    // find selected customer
    private Customer findCustomerById(String customerId) {
        if (AppState.customers == null) {
            return null;
        }

        for (int i = 0; i < AppState.customers.getMcount(); i++) {
            Customer c = AppState.customers.getValue(i);
            if (c != null && customerId.equals(c.customerId)) {
                return c;
            }
        }
        return null;
    }

    // account text for dropdown display
    private String buildAccountDisplay(Account account) {
        return getAccountType(account) + " - $" + String.format("%.2f", account.getBalance());
    }

    // account type names
    private String getAccountType(Account account) {
        if (account instanceof SavingsAccount) {
            return "Savings";
        } else if (account instanceof TMBAccount) {
            return "TMB Checking";
        } else if (account instanceof GDAccount) {
            return "Gold/Diamond";
        } else if (account instanceof CDAccount) {
            return "CD";
        }
        return "Unknown";
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
