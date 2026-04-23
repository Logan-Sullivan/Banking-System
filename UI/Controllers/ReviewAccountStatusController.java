import Account_Classes.Account;
import Account_Classes.CDAccount;
import Account_Classes.GDAccount;
import Account_Classes.SavingsAccount;
import Account_Classes.TMBAccount;
import User_Classes.Customer;
import Utils.AppState;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ReviewAccountStatusController {

    @FXML
    private TextField customerIdField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TableView<AccountRow> accountTableView;

    @FXML
    private TableColumn<AccountRow, String> accountNumberColumn;

    @FXML
    private TableColumn<AccountRow, String> typeColumn;

    @FXML
    private TableColumn<AccountRow, String> balanceColumn;

    @FXML
    private TableColumn<AccountRow, String> statusColumn;

    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        // connect table columns to row fields
        if (accountNumberColumn != null) {
            accountNumberColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        }
        if (typeColumn != null) {
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        }
        if (balanceColumn != null) {
            balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        }
        if (statusColumn != null) {
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        }
    }

    @FXML
    private void refreshAccounts(ActionEvent event) {
        String customerId = customerIdField == null ? "" : customerIdField.getText().trim();
        String firstName = firstNameField == null ? "" : firstNameField.getText().trim();
        String lastName = lastNameField == null ? "" : lastNameField.getText().trim();

        if (customerId.isBlank() || firstName.isBlank() || lastName.isBlank()) {
            if (statusLabel != null) {
                statusLabel.setText("Please enter customer ID, first name, and last name.");
            }
            return;
        }

        if (AppState.customers == null || AppState.customers.getMcount() == 0) {
            if (statusLabel != null) {
                statusLabel.setText("No customer data is loaded.");
            }
            return;
        }

        for (int i = 0; i < AppState.customers.getMcount(); i++) {
            Customer c = AppState.customers.getValue(i);

            if (c != null
                    && customerId.equals(c.customerId)
                    && firstName.equalsIgnoreCase(c.firstName)
                    && lastName.equalsIgnoreCase(c.lastName)) {

                ObservableList<AccountRow> rows = FXCollections.observableArrayList();

                if (c.accountList != null) {
                    for (Account account : c.accountList) {
                        rows.add(new AccountRow(
                                account.accountNumber,
                                getAccountType(account),
                                String.format("$%.2f", account.getBalance()),
                                "Current"
                        ));
                    }
                }

                if (accountTableView != null) {
                    accountTableView.setItems(rows);
                }

                if (statusLabel != null) {
                    statusLabel.setText("Accounts found for " + c.firstName + " " + c.lastName + ".");
                }
                return;
            }
        }

        if (accountTableView != null) {
            accountTableView.getItems().clear();
        }

        if (statusLabel != null) {
            statusLabel.setText("Customer information did not match our records.");
        }
    }

    // helper for account type names
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

    // row model for customer account table
    public static class AccountRow {
        private final SimpleStringProperty accountNumber;
        private final SimpleStringProperty type;
        private final SimpleStringProperty balance;
        private final SimpleStringProperty status;

        public AccountRow(String accountNumber, String type, String balance, String status) {
            this.accountNumber = new SimpleStringProperty(accountNumber);
            this.type = new SimpleStringProperty(type);
            this.balance = new SimpleStringProperty(balance);
            this.status = new SimpleStringProperty(status);
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

        public String getStatus() {
            return status.get();
        }
    }
}
