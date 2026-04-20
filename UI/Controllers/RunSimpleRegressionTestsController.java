import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RunSimpleRegressionTestsController {

    @FXML
    private void returnToSystemController(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/SystemController.fxml")));
            stage.setScene(scene);
            stage.setTitle("System Controller");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
