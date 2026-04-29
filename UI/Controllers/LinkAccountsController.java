import Account_Classes.Account;
import Account_Classes.CheckingsAccount;
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
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class LinkAccountsController {

    @FXML
    private ComboBox<String> customerIdComboBox;

    @FXML
    private ComboBox<String> primaryAccountComboBox;

    @FXML
    private ComboBox<String> linkedAccountComboBox;

    @FXML
    private ComboBox<String> linkTypeComboBox;

    @FXML
    private Label statusLabel;

    private final Map<String, Customer> customerMap = new HashMap<>();
    private final Map<String, Account> primaryAccountMap = new HashMap<>();
    private final Map<String, Account> linkedAccountMap = new HashMap<>();

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

    @FXML
    private void onCustomerChosen(ActionEvent event) {
        String selectedCustomerName = customerIdComboBox == null ? "" : customerIdComboBox.getValue();

        primaryAccountMap.clear();
        linkedAccountMap.clear();

        if (primaryAccountComboBox != null) {
            primaryAccountComboBox.getItems().clear();
        }
        if (linkedAccountComboBox != null) {
            linkedAccountComboBox.getItems().clear();
        }

        if (selectedCustomerName == null || selectedCustomerName.isBlank()) {
            if (statusLabel != null) {
                statusLabel.setText("Select a customer first.");
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

        for (Account account : customer.accountList) {
            String displayText = buildAccountDisplay(account);

            // primary account choices are checking accounts
            if (account instanceof TMBAccount || account instanceof GDAccount) {
                primaryAccountMap.put(displayText, account);
                if (primaryAccountComboBox != null) {
                    primaryAccountComboBox.getItems().add(displayText);
                }
            }

            // linked account choices are savings accounts
            if (account instanceof SavingsAccount) {
                linkedAccountMap.put(displayText, account);
                if (linkedAccountComboBox != null) {
                    linkedAccountComboBox.getItems().add(displayText);
                }
            }
        }

        if (statusLabel != null) {
            statusLabel.setText("Customer accounts loaded for linking.");
        }
    }

    @FXML
    private void linkAccounts(ActionEvent event) {
        String selectedCustomerName = customerIdComboBox == null ? "" : customerIdComboBox.getValue();
        String primarySelection = primaryAccountComboBox == null ? "" : primaryAccountComboBox.getValue();
        String linkedSelection = linkedAccountComboBox == null ? "" : linkedAccountComboBox.getValue();
        String linkType = linkTypeComboBox == null ? "" : linkTypeComboBox.getValue();

        if (selectedCustomerName == null || selectedCustomerName.isBlank()) {
            if (statusLabel != null) statusLabel.setText("Please select a customer.");
            return;
        }

        if (primarySelection == null || primarySelection.isBlank()) {
            if (statusLabel != null) statusLabel.setText("Please select a primary account.");
            return;
        }

        if (linkedSelection == null || linkedSelection.isBlank()) {
            if (statusLabel != null) statusLabel.setText("Please select a linked account.");
            return;
        }

        if (linkType == null || linkType.isBlank()) {
            if (statusLabel != null) statusLabel.setText("Please select a link type.");
            return;
        }

        Account primaryAccount = primaryAccountMap.get(primarySelection);
        Account linkedAccount = linkedAccountMap.get(linkedSelection);

        if (primaryAccount == null || linkedAccount == null) {
            if (statusLabel != null) statusLabel.setText("Could not find selected accounts.");
            return;
        }

        if ("Overdraft Backup".equals(linkType)) {
            if (!(primaryAccount instanceof CheckingsAccount checking)) {
                if (statusLabel != null) statusLabel.setText("Primary account must be a checking account.");
                return;
            }

            if (!(linkedAccount instanceof SavingsAccount savings)) {
                if (statusLabel != null) statusLabel.setText("Linked account must be a savings account.");
                return;
            }

            checking.setOverdraftProtAccount(savings);

            CsvManager.writeCustomersToCsv(AppState.customers);

            if (statusLabel != null) {
                statusLabel.setText("Overdraft backup linked successfully.");
            }
            return;
        }

        if ("Transfer Pair".equals(linkType)) {
            if (statusLabel != null) {
                statusLabel.setText("Transfer Pair is not wired yet.");
            }
            return;
        }
    }

    // dropdown display text
    private String buildAccountDisplay(Account account) {
        return getAccountType(account) + " - $" + String.format("%.2f", account.getBalance());
    }

    // account type label helper
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
