import Account_Classes.CDAccount;
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

public class EstablishCDsController {

    @FXML
    private ComboBox<String> customerComboBox;

    @FXML
    private ComboBox<String> cdTermComboBox;

    @FXML
    private TextField initialDepositField;

    @FXML
    private TextField interestRateField;

    @FXML
    private TextField earlyPenaltyField;

    @FXML
    private Label statusLabel;

    // maps customer name back to real customer object
    private final Map<String, Customer> customerMap = new HashMap<>();

    @FXML
    public void initialize() {
        if (customerComboBox != null) {
            customerComboBox.getItems().clear();
            customerMap.clear();

            if (AppState.customers != null) {
                for (int i = 0; i < AppState.customers.getMcount(); i++) {
                    Customer c = AppState.customers.getValue(i);

                    if (c != null && c.firstName != null && c.lastName != null) {
                        String displayName = c.firstName + " " + c.lastName;
                        customerComboBox.getItems().add(displayName);
                        customerMap.put(displayName, c);
                    }
                }
            }
        }
    }

    @FXML
    private void establishCDPressed(ActionEvent event) {
        String selectedCustomerName = customerComboBox == null ? "" : customerComboBox.getValue();
        String cdTerm = cdTermComboBox == null ? "" : cdTermComboBox.getValue();

        if (selectedCustomerName == null || selectedCustomerName.isBlank()) {
            statusLabel.setText("Please select a customer.");
            return;
        }

        Customer selectedCustomer = customerMap.get(selectedCustomerName);
        if (selectedCustomer == null) {
            statusLabel.setText("Customer not found.");
            return;
        }

        if (cdTerm == null || cdTerm.isBlank()) {
            statusLabel.setText("Please select a CD term.");
            return;
        }

        double initialDeposit;
        double interestRate;
        double earlyPenalty;

        try {
            initialDeposit = Double.parseDouble(initialDepositField.getText());
            interestRate = Double.parseDouble(interestRateField.getText());
            earlyPenalty = Double.parseDouble(earlyPenaltyField.getText());
        } catch (Exception e) {
            statusLabel.setText("Please enter valid numeric CD data.");
            return;
        }

        if (initialDeposit <= 0) {
            statusLabel.setText("Initial deposit must be greater than zero.");
            return;
        }

        if (interestRate < 0 || earlyPenalty < 0) {
            statusLabel.setText("Interest rate and penalty cannot be negative.");
            return;
        }

        // create CD account using current CD constructor
        CDAccount cd = new CDAccount(initialDeposit, interestRate, null, earlyPenalty);

        // attach CD to selected customer
        selectedCustomer.accountList.add(cd);

        // save CD to CSV
        CsvManager.writeCustomersToCsv(AppState.customers);

        statusLabel.setText("CD established for " + selectedCustomer.firstName + " " + selectedCustomer.lastName + ".");
    }

    @FXML
    private void returnToManagerScreen(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/ManagerScreen.fxml")));
            stage.setScene(scene);
            stage.setTitle("Manager Screen");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
