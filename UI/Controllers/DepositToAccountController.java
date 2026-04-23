import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DepositToAccountController {

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

