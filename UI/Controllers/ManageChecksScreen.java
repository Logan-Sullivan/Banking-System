import Account_Classes.*;
import User_Classes.Customer;
import Utils.AppState;
import Utils.Check;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.*;

public class ManageChecksScreen {
    public TableColumn<checkRow, String> recStatus;
    public TableColumn<checkRow, String> recAmount;
    public TableColumn<checkRow, String> recSender;
    public TableColumn<checkRow, String> sentStatus;
    public TableColumn<checkRow, String> sentRecipientName;
    public TableColumn<checkRow, String> sentAmount;
    public ComboBox<String> CheckSelectorBox;
    public TableView<checkRow> sentChecks;
    public TableView<checkRow> ReceivedChecks;
    public Button cancelCheckButton;
    @FXML
    private ComboBox<String> customerIdComboBox;
    @FXML
    private Label statusLabel;
    // maps dropdown text back to real account object
    private final List<Account> myAccounts = new ArrayList<>();
    // maps customer name back to real customer object
    private final Map<String, Customer> customerMap = new HashMap<>();
    // maps checks to real check object
    private final Map<String, Check> checkMap = new HashMap<>();
    @FXML
    public void initialize() {
        if (customerIdComboBox != null) {
            CheckSelectorBox.getItems().clear();
            customerIdComboBox.getItems().clear();
            customerMap.clear();
            cancelCheckButton.setDisable(true);

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
        recSender.setCellValueFactory(new PropertyValueFactory<>("identifier"));
        recAmount.setCellValueFactory(new PropertyValueFactory<>("balance"));
        recStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        sentRecipientName.setCellValueFactory(new PropertyValueFactory<>("identifier"));
        sentStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        sentAmount.setCellValueFactory(new PropertyValueFactory<>("balance"));

    }


    public void onSelectCheck(ActionEvent actionEvent) {
        String selectedCheck = CheckSelectorBox == null ? "" : CheckSelectorBox.getValue();
            if (selectedCheck == null || selectedCheck.isBlank()){
                if (statusLabel != null){
                    statusLabel.setText("Please select a check to modify");
                }
                return;
            }
            Check check = checkMap.get(selectedCheck);
            if (check == null){
                if (statusLabel != null){
                    statusLabel.setText("check not found");
                }
                return;
            }
            cancelCheckButton.setDisable(false);

    }

    public void onCustomerChosen(ActionEvent actionEvent) {
        String selectedCustomerName = customerIdComboBox == null ? "" : customerIdComboBox.getValue();
        CheckSelectorBox.getItems().clear();
        cancelCheckButton.setDisable(true);
        myAccounts.clear();
        sentChecks.getItems().clear();
        ReceivedChecks.getItems().clear();

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

        //get customer accounts
        if (customer.accountList != null) {
            for (Account account : customer.accountList) {
                myAccounts.add(account);
            }
        }
        CheckSelectorBox.getItems().clear();
        //get customers checks
        if (AppState.checks != null) {
            for (int i = 0; i < AppState.checks.size(); i++) {
                Check c = AppState.checks.get(i);
                //check for sent checks
                if (myAccounts.contains(c.getSender())){
                    String text = buildCheckDisplay(c,"To: "+c.getReceiver().lastName);
                    checkMap.put(text,c);
                    if (c.getStatus().equals("Pending")){
                        System.out.println(c.getStatus());
                        CheckSelectorBox.getItems().add(text);
                    }
                }
                //check for received checks
                if (c.getReceiver() == customer){
                    String text = buildCheckDisplay(c,"From: "+ Objects.requireNonNull(getCustFromAccount(c.getSender())).lastName);
                    checkMap.put(text,c);
                    if (c.getStatus().equals("Pending")){
                        CheckSelectorBox.getItems().add(text);
                    }
                }

            }
        }
        ObservableList<checkRow> rowsSent = FXCollections.observableArrayList();
        ObservableList<checkRow> rowsReceived = FXCollections.observableArrayList();
        String Name;
        for (String key : checkMap.keySet()){
            Check chk = checkMap.get(key);
            if (key.startsWith("To:")){
                Name = chk.getReceiver().firstName + " " + chk.getReceiver().lastName;
                rowsSent.add(new checkRow(
                        Name,
                        Double.toString(chk.getAmount()),
                        chk.getStatus()
                ));
            } else {
                Name = getCustFromAccount(chk.getSender()).firstName + " "+ getCustFromAccount(chk.getSender()).lastName;
                rowsReceived.add(new checkRow(
                        Name,
                        Double.toString(chk.getAmount()),
                        chk.getStatus()
                ));
            }
        }

        ReceivedChecks.setItems(rowsReceived);
        sentChecks.setItems(rowsSent);
    }

    public static Customer getCustFromAccount(Account a){
        for (int i = 0; i < AppState.customers.getMcount(); i++) {
            Customer c = AppState.customers.getValue(i);
            if (c.accountList != null) {
                for (Account account : c.accountList) {
                    if (account == a){
                        return c;
                    }
                }
            }
        }
        return null;
    }

    private String buildCheckDisplay(Check check, String prefix) {
        return prefix + " - $" + String.format("%.2f", check.getAmount());
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

    public void cancelCheck(ActionEvent actionEvent) {
        String selectedCheck = CheckSelectorBox == null ? "" : CheckSelectorBox.getValue();
        String selectedCustomerName = customerIdComboBox == null ? "" : customerIdComboBox.getValue();
        Customer customer = customerMap.get(selectedCustomerName);

        if (selectedCheck == null || selectedCheck.isBlank()){
            if (statusLabel != null){
                statusLabel.setText("Please select a check to modify");
            }
            return;
        }
        Check check = checkMap.get(selectedCheck);
        if (check == null){
            if (statusLabel != null){
                statusLabel.setText("check not found");
            }
            return;
        }
        check.cancelCheck();
        Utils.CsvManager.writeChecksToCSV(AppState.checks);

        CheckSelectorBox.getItems().clear();
        if (AppState.checks != null) {
            for (int i = 0; i < AppState.checks.size(); i++) {
                Check c = AppState.checks.get(i);
                //check for sent checks
                if (myAccounts.contains(c.getSender())){
                    String text = buildCheckDisplay(c,"To: "+c.getReceiver().lastName);
                    checkMap.put(text,c);
                    if (c.getStatus().equals("Pending")){
                        CheckSelectorBox.getItems().add(text);
                    }
                }
                //check for received checks
                if (c.getReceiver() == customer){
                    String text = buildCheckDisplay(c,"From: "+ Objects.requireNonNull(getCustFromAccount(c.getSender())).lastName);
                    checkMap.put(text,c);
                    if (c.getStatus().equals("Pending")){
                        CheckSelectorBox.getItems().add(text);
                    }                }
            }
        }
        ObservableList<checkRow> rowsSent = FXCollections.observableArrayList();
        ObservableList<checkRow> rowsReceived = FXCollections.observableArrayList();
        String Name;
        for (String key : checkMap.keySet()){
            Check chk = checkMap.get(key);
            if (key.startsWith("To:")){
                Name = chk.getReceiver().firstName + " " + chk.getReceiver().lastName;
                rowsSent.add(new checkRow(
                        Name,
                        Double.toString(chk.getAmount()),
                        chk.getStatus()
                ));
            } else {
                Name = getCustFromAccount(chk.getSender()).firstName + " "+ getCustFromAccount(chk.getSender()).lastName;
                rowsReceived.add(new checkRow(
                        Name,
                        Double.toString(chk.getAmount()),
                        chk.getStatus()
                ));
            }
        }

        ReceivedChecks.setItems(rowsReceived);
        sentChecks.setItems(rowsSent);

    }


    public static class checkRow {
        private final SimpleStringProperty identifier;
        private final SimpleStringProperty balance;
        private final SimpleStringProperty status;

        public checkRow(String identifier, String balance, String status) {
            this.identifier = new SimpleStringProperty(identifier);
            this.balance = new SimpleStringProperty(balance);
            this.status = new SimpleStringProperty(status);
        }


        public String getIdentifier() {
            return identifier.get();
        }

        public String getBalance() {
            return balance.get();
        }

        public String getStatus() {
            return status.get();
        }
    }
}


