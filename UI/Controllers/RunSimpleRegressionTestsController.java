import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import tests.AccountTests;
import tests.LoanTests;
import tests.MainTests;
import tests.TimelineIntegrationTests;

public class RunSimpleRegressionTestsController {

    @FXML
    private TextArea resultsArea;

    @FXML
    private void runMainTests(ActionEvent event) {
        runTestClass(MainTests.class);
    }

    @FXML
    private void runAccountTests(ActionEvent event) {
        runTestClass(AccountTests.class);
    }

    @FXML
    private void runLoanTests(ActionEvent event) {
        runTestClass(LoanTests.class);
    }

    @FXML
    private void runTimelineIntegrationTests(ActionEvent event) {
        runTestClass(TimelineIntegrationTests.class);
    }

    private void runTestClass(Class<?> testClass) {
        StringBuilder output = new StringBuilder();

        output.append("Running: ").append(testClass.getSimpleName()).append("\n\n");

        try {
            Result result = JUnitCore.runClasses(testClass);

            output.append("Tests Run: ").append(result.getRunCount()).append("\n");
            output.append("Failures: ").append(result.getFailureCount()).append("\n");
            output.append("Ignored: ").append(result.getIgnoreCount()).append("\n");
            output.append("Passed: ").append(result.wasSuccessful()).append("\n\n");

            if (!result.wasSuccessful()) {
                output.append("Failure Details:\n");

                for (Failure failure : result.getFailures()) {
                    output.append("----------------------------------------\n");
                    output.append(failure.getTestHeader()).append("\n");
                    output.append(failure.getMessage()).append("\n");
                }
            } else {
                output.append("All tests passed successfully.");
            }

        } catch (Exception e) {
            output.append("Error running test class.\n");
            output.append(e.getMessage());
            e.printStackTrace();
        }

        if (resultsArea != null) {
            resultsArea.setText(output.toString());
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
