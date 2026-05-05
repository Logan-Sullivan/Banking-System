//NOTE: this is a very basic implementation of the file. 
//It can only handle making Savings accounts, and does not link them to the customers
//It instead stores the saving accounts in a csv to be read later.
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import Account_Classes.SavingsAccount;
import Account_Classes.GDAccount;
import Account_Classes.TMBAccount;
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

    private final Map<String, Customer> customerMap = new HashMap<>();

    @FXML
    public void initialize() {
        if (customerIdComboBox == null) return;

        customerIdComboBox.getItems().clear();
        customerMap.clear();

        if (AppState.customers != null) {
            for (int i = 0; i < AppState.customers.getMcount(); i++) {
                Customer c = AppState.customers.getValue(i);

                // display first and last name instead of ID
                if (c != null && c.firstName != null && c.lastName != null) {
                    String displayName = c.firstName + " " + c.lastName;

                    customerIdComboBox.getItems().add(displayName);
                    customerMap.put(displayName, c);
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

        // now getting selected name instead of ID
        String selectedCustomerName = customerIdComboBox.getValue();

        if (selectedCustomerName == null || selectedCustomerName.isBlank()) {
            statusLabel.setText("Please select a customer");
            statusLabel.setVisible(true);
            return;
        }

        // get actual customer from map
        Customer selectedCustomer = customerMap.get(selectedCustomerName);

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
                    // use manager-set savings rate if field is blank
                    String rateText = interestRateField.getText();

                    if (rateText == null || rateText.isBlank()) {
                        rate = AppState.accountRates.getOrDefault("Savings Account", 0.01);
                    } else {
                        rate = Double.parseDouble(rateText);
                    }

                    freq = compoundFrequencyField.getText();
                } catch (Exception e) {
                    statusLabel.setText("Invalid savings input");
                    statusLabel.setVisible(true);
                    return;
                }

                SavingsAccount savings = new SavingsAccount(rate, freq, false, balance);
                selectedCustomer.accountList.add(savings);
                // add new savings account to timeline
                AppState.timeline.addServices(savings);
            }

            case "Gold Diamond Checking Account" -> {
                boolean flexible = flexibleRateCheck.isSelected();

                GDAccount gd = new GDAccount(null, balance, flexible);
                selectedCustomer.accountList.add(gd);
                // add new GD account to timeline
                AppState.timeline.addServices(gd);
            }

            case "That's My Bank Checking Account" -> {
                TMBAccount tmb = new TMBAccount(null, balance);
                selectedCustomer.accountList.add(tmb);
                // add new TMB account to timeline
                AppState.timeline.addServices(tmb);
            }
        }

        Utils.CsvManager.writeCustomersToCsv(AppState.customers);

        statusLabel.setText("Account created successfully!");
        statusLabel.setVisible(true);
    }
}


