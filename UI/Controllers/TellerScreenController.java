import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TellerScreenController {

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
    private void goToCreateNewAccount(ActionEvent event) {
        switchScreen(event, "/Teller/CreateNewAccount.fxml", "Create New Account");
    }

    @FXML
    private void goToCreateNewCustomer(ActionEvent event) {
        switchScreen(event, "/Teller/CreateNewCustomer.fxml", "Create New Customer");
    }

    @FXML
    private void goToLinkAccounts(ActionEvent event) {
        switchScreen(event, "/Teller/LinkAccounts.fxml", "Link Accounts");
    }

    @FXML
    private void goToWithdrawFromAccount(ActionEvent event) {
        switchScreen(event, "/Teller/WithdrawFromAccount.fxml", "Withdraw from Account");
    }

    @FXML
    private void goToStopPayment(ActionEvent event) {
        switchScreen(event, "/Teller/StopPayment.fxml", "Stop Payment");
    }

    @FXML
    private void goToReviewCustomerAccounts(ActionEvent event) {
        switchScreen(event, "/Teller/ReviewCustomerAccounts.fxml", "Review Customer Accounts");
    }

    @FXML
    private void returnToSystemController(ActionEvent event) {
        switchScreen(event, "/SystemController.fxml", "System Controller");
    }
}