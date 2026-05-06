import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CustomerScreenController {

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
    private void goToCreditCardScreen(ActionEvent event) {
        switchScreen(event, "/Customer/CreditCardScreen.fxml", "Credit Card Screen");
    }

    @FXML
    private void goToReviewAccountStatusScreen(ActionEvent event) {
        switchScreen(event, "/Customer/ReviewAccountStatus.fxml", "Review Account Status Screen");
    }

    @FXML
    private void goToInputCheckScreen(ActionEvent event) {
        switchScreen(event, "/Customer/InputCheckScreen.fxml", "Input Check Screen");
    }

    @FXML
    private void goToATMWithdrawScreen(ActionEvent event) {
        switchScreen(event, "/Customer/ATMWithdraw.fxml", "ATM Withdraw");
    }

    @FXML
    private void returnToSystemController(ActionEvent event) {
        switchScreen(event, "/SystemController.fxml", "System Controller");
    }
}
