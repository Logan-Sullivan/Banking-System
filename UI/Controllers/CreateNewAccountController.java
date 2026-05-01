//NOTE: this is a very basic implementation of the file
//It can only handle making Savings accounts, and does not link them to the customers
//It instead stores the saving accounts in a csv to be read later.
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import Account_Classes.CDAccount;
import Account_Classes.GDAccount;
import Account_Classes.SavingsAccount;
import Account_Classes.TMBAccount;
import Utils.CsvManager;
import User_Classes.Customer;
import Utils.AppState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateNewAccountController {

    @FXML
    private ComboBox accountTypeCombo;
    @FXML
    private Label interestRateLabel;
    @FXML
    private TextField interestRateField;
    @FXML
    private Label compoundFrequencyLabel;
    @FXML
    private TextField compoundFrequencyField;
    @FXML
    private CheckBox flexibleRateCheck;
    @FXML
    private ComboBox<String> customerIdComboBox;
    @FXML
    private Label statusLabel;
    @FXML
    private TextField initialDepositField;

    @FXML
    public void initialize() {
        if (customerIdComboBox == null) return;

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

    //This function is for the account type being chosen using the combo box.
    //This reveals the extra fields that each account type might need so the teller can input the information
    @FXML
    private void onAccountTypeChosen(ActionEvent event) {
        if (accountTypeCombo.getValue().equals("Savings Account")) {
            interestRateField.setVisible(true);
            interestRateLabel.setVisible(true);
            compoundFrequencyLabel.setVisible(true);
            compoundFrequencyField.setVisible(true);
            flexibleRateCheck.setVisible(false);
        }
        if (accountTypeCombo.getValue().equals("Gold Diamond Checking Account")) {
            interestRateField.setVisible(false);
            interestRateLabel.setVisible(false);
            compoundFrequencyLabel.setVisible(false);
            compoundFrequencyField.setVisible(false);
            flexibleRateCheck.setVisible(true);
        }
        if (accountTypeCombo.getValue().equals("That's My Bank Checking Account")) {
            interestRateField.setVisible(false);
            interestRateLabel.setVisible(false);
            compoundFrequencyLabel.setVisible(false);
            compoundFrequencyField.setVisible(false);
            flexibleRateCheck.setVisible(false);
        }
        if (accountTypeCombo.getValue().equals("CD Account")) {
            interestRateField.setVisible(false);
            interestRateLabel.setVisible(false);
            compoundFrequencyLabel.setVisible(false);
            compoundFrequencyField.setVisible(false);
            flexibleRateCheck.setVisible(false);
        }
    }
    @FXML
    private void createAccountPressed(ActionEvent event) {

        // get selected customer ID
        String customerID = customerIdComboBox.getValue();

        if (customerID == null || customerID.isBlank()) {
            statusLabel.setText("Please select a customer");
            statusLabel.setVisible(true);
            return;
        }

        // find customer in AppState
        Customer selectedCustomer = null;
        for (int i = 0; i < AppState.customers.getMcount(); i++) {
            Customer c = AppState.customers.getValue(i);
            if (c.customerId.equals(customerID)) {
                selectedCustomer = c;
                break;
            }
        }

        if (selectedCustomer == null) {
            statusLabel.setText("Customer not found");
            statusLabel.setVisible(true);
            return;
        }

        double balance;

        try {
            balance = Double.parseDouble(initialDepositField.getText());
        } catch (Exception e) {
            statusLabel.setText("Invalid deposit amount");
            statusLabel.setVisible(true);
            return;
        }

        String type = (String) accountTypeCombo.getValue();

        if (type == null) {
            statusLabel.setText("Please select account type");
            statusLabel.setVisible(true);
            return;
        }

        switch (type) {

            case "Savings Account" -> {
                double rate;
                String freq;

                try {
                    rate = Double.parseDouble(interestRateField.getText());
                    freq = compoundFrequencyField.getText();
                } catch (Exception e) {
                    statusLabel.setText("Invalid savings input");
                    statusLabel.setVisible(true);
                    return;
                }

                // create savings account
                SavingsAccount savings = new SavingsAccount(rate, freq, false, balance); // auto account number
                selectedCustomer.accountList.add(savings); // attach to customer
            }

            case "Gold Diamond Checking Account" -> {
                boolean flexible = flexibleRateCheck.isSelected();

                GDAccount gd = new GDAccount(null, balance, flexible); // no overdraft yet
                selectedCustomer.accountList.add(gd);
            }

            case "That's My Bank Checking Account" -> {
                TMBAccount tmb = new TMBAccount(null, balance);
                selectedCustomer.accountList.add(tmb);
            }

            case "CD Account" -> {
                double rate = 0.05; // simple default for now

                CDAccount cd = new CDAccount(balance, rate, null, 50.0); // placeholder values
                selectedCustomer.accountList.add(cd);
            }
        }

        // OPTIONAL: save back to CSV using your existing manager
        Utils.CsvManager.writeCustomersToCsv(AppState.customers);

        statusLabel.setText("Account created successfully!");
        statusLabel.setVisible(true);
    }
}
