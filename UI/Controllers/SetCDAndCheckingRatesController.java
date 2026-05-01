import Utils.AppState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SetCDAndCheckingRatesController {

    @FXML
    private ComboBox<String> accountTypeCombo;
    @FXML
    private TextField rateField;
    @FXML
    private TextField effectiveDateField;
    @FXML
    private Label statusLabel;

    @FXML
    private void updateAccountRate(ActionEvent event) {
        String accountType = accountTypeCombo == null ? null : accountTypeCombo.getValue();
        String rateText = rateField == null ? "" : rateField.getText().trim();
        String effectiveDate = effectiveDateField == null ? "" : effectiveDateField.getText().trim();

        if (accountType == null || accountType.isBlank()) {
            statusLabel.setText("Select an account type.");
            return;
        }

        if (effectiveDate.isBlank()) {
            statusLabel.setText("Enter an effective date.");
            return;
        }

        double rate;
        try {
            rate = Double.parseDouble(rateText);
        } catch (Exception e) {
            statusLabel.setText("Enter a valid interest rate.");
            return;
        }

        if (rate < 0) {
            statusLabel.setText("Rate cannot be negative.");
            return;
        }

        // stores the global manager-set rate
        AppState.accountRates.put(accountType, rate);

        // stores the effective date for the global rate
        AppState.accountEffectiveDates.put(accountType, effectiveDate);

        // status message for global rate behavior
        if (accountType.equals("CD Account")) {
            statusLabel.setText("CD rate updated for newly established CDs.");
        } else if (accountType.equals("Savings Account")) {
            statusLabel.setText("Savings rate updated for new savings accounts.");
        } else if (accountType.equals("Gold Diamond Checking Account")) {
            statusLabel.setText("Gold/Diamond checking rate stored for future calculations.");
        } else {
            statusLabel.setText("Account rate updated.");
        }
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
