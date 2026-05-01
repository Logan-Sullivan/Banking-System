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

public class SetLoanInterestRatesController {

    @FXML
    private ComboBox<String> loanTypeCombo;
    @FXML
    private TextField rateField;
    @FXML
    private TextField effectiveDateField;
    @FXML
    private Label statusLabel;

    @FXML
    private void updateLoanRate(ActionEvent event) {
        String loanType = loanTypeCombo == null ? null : loanTypeCombo.getValue();
        String rateText = rateField == null ? "" : rateField.getText().trim();
        String effectiveDate = effectiveDateField == null ? "" : effectiveDateField.getText().trim();

        if (loanType == null || loanType.isBlank()) {
            statusLabel.setText("Select a loan type.");
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

        AppState.loanRates.put(loanType, rate);
        AppState.loanEffectiveDates.put(loanType, effectiveDate);
        statusLabel.setText("Loan rate updated.");
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
