import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ManagerScreenController {

    private void switchScreen(ActionEvent event, String fxmlFile, String title) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource(fxmlFile)));
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToCreateNewLoan(ActionEvent event) {
        switchScreen(event, "/Manager/CreateNewLoan.fxml", "Create New Loan");
    }

    @FXML
    private void goToSetLoanInterestRates(ActionEvent event) {
        switchScreen(event, "/Manager/SetLoanInterestRates.fxml", "Set Interest Rates on Loans");
    }

    @FXML
    private void goToSetCDAndCheckingRates(ActionEvent event) {
        switchScreen(event, "/Manager/SetCDAndCheckingRates.fxml", "Set Interest Rates on CDs / Checking");
    }

    @FXML
    private void goToEstablishCDs(ActionEvent event) {
        switchScreen(event, "/Manager/EstablishCDs.fxml", "Establish CDs");
    }

    @FXML
    private void goToProcessChecks(ActionEvent event) {
        switchScreen(event, "/Manager/ProcessChecks.fxml", "Process Checks");
    }

    @FXML
    private void returnToSystemController(ActionEvent event) {
        switchScreen(event, "/SystemController.fxml", "System Controller");
    }
}
