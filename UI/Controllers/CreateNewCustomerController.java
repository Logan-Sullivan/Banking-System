import User_Classes.Customer;
import Utils.AppState;
import Utils.CsvManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class CreateNewCustomerController {

    @FXML
    private TextField customerIdField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField stateField;

    @FXML
    private TextField zipcodeField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private Label statusLabel;

    @FXML
    private void createCustomer(ActionEvent event) {
        String customerId = customerIdField == null ? "" : customerIdField.getText();
        String address = addressField == null ? "" : addressField.getText();
        String city = cityField == null ? "" : cityField.getText();
        String state = stateField == null ? "" : stateField.getText();
        String zipcode = zipcodeField == null ? "" : zipcodeField.getText();
        String firstName = firstNameField == null ? "" : firstNameField.getText();
        String lastName = lastNameField == null ? "" : lastNameField.getText();

        customerId = customerId == null ? "" : customerId.trim();
        address = address == null ? "" : address.trim();
        city = city == null ? "" : city.trim();
        state = state == null ? "" : state.trim();
        zipcode = zipcode == null ? "" : zipcode.trim();
        firstName = firstName == null ? "" : firstName.trim();
        lastName = lastName == null ? "" : lastName.trim();

        if (customerId.isBlank() || firstName.isBlank() || lastName.isBlank()) {
            if (statusLabel != null) statusLabel.setText("Customer ID, First Name, and Last Name are required.");
            return;
        }
        if (!zipcode.isBlank() && !zipcode.matches("\\d{5}")) {
            if (statusLabel != null) statusLabel.setText("Zipcode must be 5 digits.");
            return;
        }
        if (!state.isBlank() && state.length() != 2) {
            if (statusLabel != null) statusLabel.setText("State should be 2 letters (ex: MO).");
            return;
        }

        //  load existing CSV data with CsvManager if memory is empty
        if (AppState.customers == null || AppState.customers.getMcount() == 0) {
            File f = new File("src/data.csv");
            if (f.exists() && f.isFile()) {
                AppState.customers = new Utils.ArrayListManager<>();
                CsvManager.fetchCustsAndAccountsFromCSV(AppState.customers, "src/data.csv");
            }
        }

        for (int i = 0; AppState.customers != null && i < AppState.customers.getMcount(); i++) {
            Customer existing = AppState.customers.getValue(i);
            if (existing != null && customerId.equals(existing.customerId)) {
                if (statusLabel != null) statusLabel.setText("Customer ID already exists: " + customerId);
                return;
            }
        }

        Customer customer = new Customer(customerId, address, city, state, zipcode, firstName, lastName);
        AppState.customers.addInOrder(customer);

        //  save customers/accounts/loans with CsvManager
        CsvManager.writeCustomersToCsv(AppState.customers);

        if (statusLabel != null) statusLabel.setText("Created customer " + firstName + " " + lastName + " (" + customerId + ")");
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
