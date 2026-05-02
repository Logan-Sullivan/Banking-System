import User_Classes.Customer;
import Utils.AppState;
import Utils.ArrayListManager;
import Utils.CsvManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.Scanner;

public class LoadBasicDataController {

    @FXML
    private TextField dataSourceField;

    @FXML
    private Label statusLabel;

    @FXML
    private void loadData(ActionEvent event) {
        String path = dataSourceField == null ? "" : dataSourceField.getText();
        if (path == null || path.isBlank()) {
            path = "src/data.csv";
            if (dataSourceField != null) {
                dataSourceField.setText(path);
            }
        }

        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            if (statusLabel != null) statusLabel.setText("File not found: " + path);
            return;
        }

        // Reset in-memory state, then load from CSV.
        AppState.customers = new ArrayListManager<>();
        CsvManager.fetchCustsAndAccountsFromCSV(AppState.customers, path);

        if (statusLabel != null) {
            statusLabel.setText("Loaded " + AppState.customers.getMcount() + " customers from " + path);
        }
    }

    @FXML
    private void validateData(ActionEvent event) {
        String path = dataSourceField == null ? "" : dataSourceField.getText();
        if (path == null || path.isBlank()) {
            path = "src/data.csv";
        }

        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            if (statusLabel != null) statusLabel.setText("File not found: " + path);
            return;
        }

        int lines = 0;
        int invalid = 0;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                if (line.isBlank()) {
                    continue;
                }

                lines++;

                // customer row
                if (line.contains(",")) {
                    String[] parts = line.split(",", -1);

                    if (parts.length < 7) {
                        invalid++;
                        continue;
                    }

                    if (parts[0].isBlank() || parts[5].isBlank() || parts[6].isBlank()) {
                        invalid++;
                    }

                    continue;
                }

                // account / loan / transaction row
                if (line.contains("|")) {
                    String[] parts = line.split("\\|", -1);
                    String type = parts[0];

                    switch (type) {
                        case "SavingsAccount" -> {
                            if (parts.length < 6) invalid++;
                        }
                        case "TMBAccount" -> {
                            if (parts.length < 3) invalid++;
                        }
                        case "GDAccount" -> {
                            if (parts.length < 4) invalid++;
                        }
                        case "CDAccount" -> {
                            if (parts.length < 6) invalid++;
                        }
                        case "MortgageLoan" -> {
                            if (parts.length < 5) invalid++;
                        }
                        case "ShortTermLoan" -> {
                            if (parts.length < 4) invalid++;
                        }
                        case "CreditCard" -> {
                            if (parts.length < 6) invalid++;
                        }
                        case "Transaction" -> {
                            if (parts.length < 5) invalid++;
                        }
                        default -> invalid++;
                    }

                    continue;
                }

                invalid++;
            }
        } catch (Exception e) {
            if (statusLabel != null) statusLabel.setText("Validation error: " + e.getMessage());
            return;
        }

        if (statusLabel != null) {
            statusLabel.setText(invalid == 0
                    ? ("Validation OK: " + lines + " data row(s)")
                    : ("Validation found " + invalid + " invalid row(s) out of " + lines));
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
