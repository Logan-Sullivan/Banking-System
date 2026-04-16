import User_Classes.Customer;
import Utils.AppState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ReviewCustomerAccountsController {

    @FXML
    private TextField customerIdField;

    @FXML
    private Label statusLabel;

    @FXML
    private void searchAccounts(ActionEvent event) {
        String customerId = customerIdField == null ? "" : customerIdField.getText();
        if (customerId == null || customerId.isBlank()) {
            if (statusLabel != null) statusLabel.setText("Enter a Customer ID.");
            return;
        }

        if (AppState.customers == null || AppState.customers.getMcount() == 0) {
            if (statusLabel != null) statusLabel.setText("No customers loaded. Go to System Controller → Load Basic Data → Load Data.");
            return;
        }

        for (int i = 0; i < AppState.customers.getMcount(); i++) {
            Customer c = AppState.customers.getValue(i);
            if (c != null && customerId.equals(c.customerId)) {
                int accounts = c.accountList == null ? 0 : c.accountList.size();
                if (statusLabel != null) statusLabel.setText("Found: " + c.firstName + " " + c.lastName + " | Accounts: " + accounts);
                return;
            }
        }

        if (statusLabel != null) statusLabel.setText("Customer not found: " + customerId);
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
