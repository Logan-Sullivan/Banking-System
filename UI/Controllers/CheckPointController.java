import Utils.AppState;
import Utils.ArrayListManager;
import Utils.CsvManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CheckPointController {

    @FXML
    private Label statusLabel;

    @FXML
    private void beginCheckPoint(ActionEvent event) {
        try {
            CsvManager.writeCustomersToCsv(AppState.customers, AppState.timeline);

            if (statusLabel != null) {
                statusLabel.setText("Checkpoint completed successfully.");
            }
        } catch (Exception e) {
            if (statusLabel != null) {
                statusLabel.setText("Checkpoint failed.");
            }
            e.printStackTrace();
        }
    }

    @FXML
    private void restoreSavedState(ActionEvent event) {
        try {
            AppState.customers = new ArrayListManager<>();
            CsvManager.fetchCustsAndAccountsFromCSV(AppState.customers, AppState.timeline);

            if (statusLabel != null) {
                statusLabel.setText("Saved state restored successfully.");
            }
        } catch (Exception e) {
            if (statusLabel != null) {
                statusLabel.setText("Restore failed.");
            }
            e.printStackTrace();
        }
    }

    @FXML
    private void shutDownSystem(ActionEvent event) {
        try {
            CsvManager.writeCustomersToCsv(AppState.customers, AppState.timeline);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            if (statusLabel != null) {
                statusLabel.setText("Shutdown failed.");
            }
            e.printStackTrace();
        }
    }

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
