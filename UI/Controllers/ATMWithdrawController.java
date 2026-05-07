import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import Account_Classes.ATMCard;
import Account_Classes.Account;
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

public class ATMWithdrawController {
    
    @FXML
    private ComboBox<String> customerIdComboBox;


    @FXML
    private ComboBox<String> accountComboBox;

    @FXML
    private TextField withdrawAmountField;

    @FXML
    private Label statusLabel;

    @FXML
    private Label withdrawsLabel;

    @FXML
    private Label previousBalanceLabel;

    @FXML
    private Label withdrawalAmountLabel;

    @FXML
    private Label newBalanceLabel;

    private Customer cus;

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

        if (selectedCustomerName == null || selectedCustomerName.isBlank()) {
            if (statusLabel != null) {
                statusLabel.setText("Please select a customer.");
            }
            return;
        }

        // find customer from name map
        cus = customerMap.get(selectedCustomerName);
        if (cus == null) {
            if (statusLabel != null) {
                statusLabel.setText("Customer not found.");
            }
            return;
        }

        if (statusLabel != null) {
            statusLabel.setText("Customer loaded.");
        }
    }

    //Modified from generic withdraw to cater to ATM withdraws
    @FXML
    private void withdrawPressed(ActionEvent event) {
        String amountText = withdrawAmountField == null ? "" : withdrawAmountField.getText();
        
        //Check that we have a customer selected, then get the atm card
        if (cus == null) {
            if (statusLabel != null) statusLabel.setText("Please select a customer.");
            return;
        }
        ATMCard atm = cus.atm;

        //Verify that amount is valid
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

        //Find account and check it
        if(atm.findATMAccount(amount) != 0){
            statusLabel.setText(String.format("Customer %s %s has no compatible account!\n",
            cus.firstName, cus.lastName));
            return;
        }

        //Get balance amounts and perform withdraw
        double previousBalance = atm.getAccount().getBalance();
        int result = atm.ATMWithdraw(amount);
        switch (result){
            case 0: statusLabel.setText("Withdraw successful."); break;
            case 1: statusLabel.setText("Customer has already made 2 withdraws!"); return;
            case 2: statusLabel.setText("Customer does not have an account with sufficient balance!"); return;
            default: return;
        }
        double newBalance = atm.getAccount().getBalance();

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

        if (withdrawsLabel != null) {
            withdrawsLabel.setText("Withdraws remaining: " + (2 -atm.getWithdraws()));
        }

        // refresh dropdown text so new balance shows
        onCustomerChosen(null);
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
