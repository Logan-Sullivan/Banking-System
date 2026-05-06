import Account_Classes.*;
import User_Classes.Customer;
import Utils.AppState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ManageChecksScreen {
    @FXML
    private ComboBox<String> customerIdComboBox;
    @FXML
    private ComboBox<String> customerIdComboBox2;
    @FXML
    private ComboBox<String> accountComboBox;
    @FXML
    private Label statusLabel;
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

                    // show first and last name instead of customer ID
                    if (c != null && c.firstName != null && c.lastName != null) {
                        String displayName = c.firstName + " " + c.lastName;
                        customerIdComboBox.getItems().add(displayName);
                        customerMap.put(displayName, c);
                    }
                }
            }
        }
    }

    public void onCustomerChosen(ActionEvent actionEvent) {
        String selectedCustomerName = customerIdComboBox == null ? "" : customerIdComboBox.getValue();

        accountMap.clear();

        if (accountComboBox != null) {
            accountComboBox.getItems().clear();
        }

        if (customerIdComboBox2 != null){
            customerIdComboBox2.getItems().clear();
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

    }



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
    private void returnToCustomerScreen(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/CustomerScreen.fxml")));
            stage.setScene(scene);
            stage.setTitle("Customer Screen");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
