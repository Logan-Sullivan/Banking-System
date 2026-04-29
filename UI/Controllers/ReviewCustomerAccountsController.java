import Account_Classes.*;
import User_Classes.Customer;
import Utils.AppState;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class ReviewCustomerAccountsController {

    @FXML
    private ComboBox<String> customerIdComboBox;

    @FXML
    private Label statusLabel;

    @FXML
    private TableView<AccountRow> accountTableView;

    @FXML
    private TableColumn<AccountRow, String> accountNumberColumn;

    @FXML
    private TableColumn<AccountRow, String> typeColumn;

    @FXML
    private TableColumn<AccountRow, String> balanceColumn;

    @FXML
    private TableColumn<AccountRow, String> recentDebitColumn;

    @FXML
    private TableColumn<AccountRow, String> statusColumn;

    @FXML
    private TableColumn<AccountRow, String> notesColumn;

    // map display name to actual customer
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

        // connects table columns to row fields
        if (accountNumberColumn != null) {
            accountNumberColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        }
        if (typeColumn != null) {
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        }
        if (balanceColumn != null) {
            balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        }
        if (recentDebitColumn != null) {
            recentDebitColumn.setCellValueFactory(new PropertyValueFactory<>("recentDebit"));
        }
        if (statusColumn != null) {
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        }
        if (notesColumn != null) {
            notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
        }
    }

    @FXML
    private void searchAccounts(ActionEvent event) {
        // selected value is now customer name
        String selectedCustomerName = customerIdComboBox == null ? "" : customerIdComboBox.getValue();

        if (selectedCustomerName == null || selectedCustomerName.isBlank()) {
            if (statusLabel != null) statusLabel.setText("Select a Customer Name.");
            return;
        }

        if (AppState.customers == null || AppState.customers.getMcount() == 0) {
            if (statusLabel != null) statusLabel.setText("No customers loaded. Go to System Controller → Load Basic Data → Load Data.");
            return;
        }

        // finds customer from map instead of searching by ID
        Customer c = customerMap.get(selectedCustomerName);

        if (c != null) {
            ObservableList<AccountRow> rows = FXCollections.observableArrayList();

            int accounts = c.accountList == null ? 0 : c.accountList.size();

            if (c.accountList != null) {
                for (Account account : c.accountList) {
                    String type = getAccountType(account);
                    String balance = String.format("$%.2f", account.getBalance());
                    String recentDebit = "N/A";
                    String status = "Current";
                    String notes = getNotes(account);

                    rows.add(new AccountRow(
                            account.accountNumber,
                            type,
                            balance,
                            recentDebit,
                            status,
                            notes
                    ));
                }
            }

            if (accountTableView != null) {
                accountTableView.setItems(rows);
            }

            if (statusLabel != null) {
                statusLabel.setText("Found: " + c.firstName + " " + c.lastName + " | Accounts: " + accounts);
            }
            return;
        }

        if (accountTableView != null) {
            accountTableView.getItems().clear();
        }

        if (statusLabel != null) statusLabel.setText("Customer not found: " + selectedCustomerName);
    }

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

    // simple notes text for summary table
    private String getNotes(Account account) {
        if (account instanceof SavingsAccount) {
            return "Interest";
        } else if (account instanceof TMBAccount) {
            return "Txn Fees";
        } else if (account instanceof GDAccount) {
            return "Min $5000";
        } else if (account instanceof CDAccount) {
            return "Time Deposit";
        }
        return "";
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

    // row model for table display
    public static class AccountRow {
        private final SimpleStringProperty accountNumber;
        private final SimpleStringProperty type;
        private final SimpleStringProperty balance;
        private final SimpleStringProperty recentDebit;
        private final SimpleStringProperty status;
        private final SimpleStringProperty notes;

        public AccountRow(String accountNumber, String type, String balance, String recentDebit, String status, String notes) {
            this.accountNumber = new SimpleStringProperty(accountNumber);
            this.type = new SimpleStringProperty(type);
            this.balance = new SimpleStringProperty(balance);
            this.recentDebit = new SimpleStringProperty(recentDebit);
            this.status = new SimpleStringProperty(status);
            this.notes = new SimpleStringProperty(notes);
        }

        public String getAccountNumber() {
            return accountNumber.get();
        }

        public String getType() {
            return type.get();
        }

        public String getBalance() {
            return balance.get();
        }

        public String getRecentDebit() {
            return recentDebit.get();
        }

        public String getStatus() {
            return status.get();
        }

        public String getNotes() {
            return notes.get();
        }
    }
}

