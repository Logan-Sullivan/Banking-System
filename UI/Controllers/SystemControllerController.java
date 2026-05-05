import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SystemControllerController {

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
    private void goToCheckPoint(ActionEvent event) {
        switchScreen(event, "/SystemController/CheckPoint.fxml", "Check Point");
    }

    @FXML
    private void goToLoadBasicData(ActionEvent event) {
        switchScreen(event, "/SystemController/LoadBasicData.fxml", "Load Basic Data");
    }

    @FXML
    private void goToRunSimpleRegressionTests(ActionEvent event) {
        switchScreen(event, "/SystemController/RunSimpleRegressionTests.fxml", "Run Simple Regression Tests");
    }
    @FXML
    private void openProgressTimeScreen(ActionEvent event) {
        switchScreen(event, "/SystemController/ProgressTime.fxml", "Progress Time");
    }

    @FXML
    private void goToTellerScreen(ActionEvent event) {
        switchScreen(event, "/TellerScreen.fxml", "Teller Screen");
    }

    @FXML
    private void goToManagerScreen(ActionEvent event) {
        switchScreen(event, "/ManagerScreen.fxml", "Manager Screen");
    }

    @FXML
    private void goToCustomerScreen(ActionEvent event) {
        switchScreen(event, "/CustomerScreen.fxml", "Customer Screen");
    }
}
