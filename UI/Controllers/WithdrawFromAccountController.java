import Account_Classes.Account;
import Account_Classes.CDAccount;
import Account_Classes.GDAccount;
import Account_Classes.SavingsAccount;
import Account_Classes.TMBAccount;
import Account_Classes.CheckingsAccount;
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

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class WithdrawFromAccountController {

    @FXML
    private ComboBox<String> customerIdComboBox;

    @FXML
    private ComboBox<String> accountComboBox;

    @FXML
    private TextField withdrawAmountField;

    @FXML
    private Label statusLabel;
    @FXML
    private Label previousBalanceLabel;

    @FXML
    private Label withdrawalAmountLabel;

    @FXML
    private Label newBalanceLabel;

    // maps dropdown text back to real account object
    private final Map<String, Account> accountMap = new HashMap<>();

    // maps customer name back to real customer object
    private final Map<String, Customer> customerMap = new HashMap<>();

    @FXML
    public void initialize() {
        if (customerIdComboBox != null) {
            customerIdComboBox.getItems().clear();
            customerMap.clear();

            if (AppState.customers != null) {
                for (int i = 0; i < AppState.customers.getMcount(); i++) {
                    Customer c = AppState.customers.getValue(i);

                    //  show first and last name instead of customer ID
                    if (c != null && c.firstName != null && c.lastName != null) {
                        String displayName = c.firstName + " " + c.lastName;
                        customerIdComboBox.getItems().add(displayName);
                        customerMap.put(displayName, c);
                    }
                }
            }
        }
    }

    @FXML
    private void onCustomerChosen(ActionEvent event) {
        String selectedCustomerName = customerIdComboBox == null ? "" : customerIdComboBox.getValue();

        accountMap.clear();

        if (accountComboBox != null) {
            accountComboBox.getItems().clear();
        }

        if (selectedCustomerName == null || selectedCustomerName.isBlank()) {
            if (statusLabel != null) {
                statusLabel.setText("Please select a customer.");
            }
            return;
        }

        // find customer from name map
        Customer customer = customerMap.get(selectedCustomerName);
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
    private void withdrawPressed(ActionEvent event) {
        String selectedCustomerName = customerIdComboBox == null ? "" : customerIdComboBox.getValue();
        String accountSelection = accountComboBox == null ? "" : accountComboBox.getValue();
        String amountText = withdrawAmountField == null ? "" : withdrawAmountField.getText();

        if (selectedCustomerName == null || selectedCustomerName.isBlank()) {
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
            if (statusLabel != null) statusLabel.setText("Please enter a valid withdrawal amount.");
            return;
        }

        if (amount <= 0) {
            if (statusLabel != null) statusLabel.setText("Amount must be greater than 0.");
            return;
        }

        Account account = accountMap.get(accountSelection);
        if (account == null) {
            if (statusLabel != null) statusLabel.setText("Selected account could not be found.");
            return;
        }

        if (account instanceof CDAccount cdAccount) {
            double needed = amount;
            if (cdAccount.maturityDate != null && LocalDate.now().isBefore(cdAccount.maturityDate)) {
                needed += cdAccount.earlyPenalty;
            }
            if (needed > account.getBalance()) {
                if (statusLabel != null) {
                    statusLabel.setText("Insufficient funds. Balance: $" + String.format("%.2f", account.getBalance()));
                }
                return;
            }
        }

        // prevent over-withdrawing from savings
        if (account instanceof SavingsAccount && amount > account.getBalance()) {
            if (statusLabel != null) {
                statusLabel.setText("Insufficient funds. Balance: $" + String.format("%.2f", account.getBalance()));
            }
            return;
        }

        double previousBalance = account.getBalance();

        account.withdraw(amount);
        if (account instanceof CheckingsAccount checking) {
            checking.handleOverdraft();
        }

        // update TMB/GD account type immediately after withdrawal
        CsvManager.updateCheckingAccountTypes(AppState.customers, AppState.timeline);

        double newBalance = account.getBalance();

        CsvManager.writeCustomersToCsv(AppState.customers, AppState.timeline);

        if (previousBalanceLabel != null) {
            previousBalanceLabel.setText("Previous Balance: $" + String.format("%.2f", previousBalance));
        }

        if (withdrawalAmountLabel != null) {
            withdrawalAmountLabel.setText("Withdrawal Amount: $" + String.format("%.2f", amount));
        }

        if (newBalanceLabel != null) {
            newBalanceLabel.setText("New Balance: $" + String.format("%.2f", newBalance));
        }

        if (statusLabel != null) {
            statusLabel.setText("Withdrawal completed successfully.");
        }

        // refresh dropdown text so new balance shows
        onCustomerChosen(null);
    }

    // dropdown text
    private String buildAccountDisplay(Account account) {
        return getAccountType(account) + " - $" + String.format("%.2f", account.getBalance());
    }

    // account type label
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
