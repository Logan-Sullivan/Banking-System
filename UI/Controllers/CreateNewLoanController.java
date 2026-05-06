import Loan_Classes.MortgageLoan;
import Loan_Classes.ShortTermLoan;
import User_Classes.Customer;
import Utils.AppState;
import Utils.CsvManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.Period;
import java.util.HashMap;
import java.util.Map;

public class CreateNewLoanController {

    @FXML
    private ComboBox<String> customerComboBox;

    @FXML
    private ComboBox<String> loanTypeComboBox;

    @FXML
    private TextField principalField;

    @FXML
    private TextField interestRateField;

    @FXML
    private Label termLabel;

    @FXML
    private ComboBox<String> termComboBox;

    @FXML
    private TextField dueDateIntervalField;

    @FXML
    private Label statusLabel;

    private final Map<String, Customer> customerMap = new HashMap<>();

    @FXML
    public void initialize() {
        if (customerComboBox != null) {
            customerComboBox.getItems().clear();
            customerMap.clear();

            if (AppState.customers != null) {
                for (int i = 0; i < AppState.customers.getMcount(); i++) {
                    Customer c = AppState.customers.getValue(i);

                    if (c != null && c.firstName != null && c.lastName != null) {
                        String displayName = c.firstName + " " + c.lastName;
                        customerComboBox.getItems().add(displayName);
                        customerMap.put(displayName, c);
                    }
                }
            }
        }
    }

    @FXML
    private void onLoanTypeChosen(ActionEvent event) {
        String type = loanTypeComboBox == null ? "" : loanTypeComboBox.getValue();

        if ("Mortgage Loan".equals(type)) {
            termLabel.setVisible(true);
            termComboBox.setVisible(true);
        } else {
            termLabel.setVisible(false);
            termComboBox.setVisible(false);
        }
    }

    @FXML
    private void createLoanPressed(ActionEvent event) {
        String customerName = customerComboBox == null ? "" : customerComboBox.getValue();
        String loanType = loanTypeComboBox == null ? "" : loanTypeComboBox.getValue();

        if (customerName == null || customerName.isBlank()) {
            statusLabel.setText("Please select a customer.");
            return;
        }

        Customer selectedCustomer = customerMap.get(customerName);
        if (selectedCustomer == null) {
            statusLabel.setText("Customer not found.");
            return;
        }

        if (loanType == null || loanType.isBlank()) {
            statusLabel.setText("Please select a loan type.");
            return;
        }

        double principal;
        double interestRate;
        int graceDays;

        try {
            principal = Double.parseDouble(principalField.getText());

            //use manager-set loan rate if interest field is blank
            String rateText = interestRateField.getText();

            if (rateText == null || rateText.isBlank()) {
                interestRate = AppState.loanRates.getOrDefault(loanType, 0.05);
            } else {
                interestRate = Double.parseDouble(rateText);
            }

            graceDays = Integer.parseInt(dueDateIntervalField.getText());
        } catch (Exception e) {
            statusLabel.setText("Please enter valid numeric loan data.");
            return;
        }

        if (principal <= 0 || interestRate <= 0 || graceDays <= 0) {
            statusLabel.setText("Loan amount, interest rate, and grace period must be greater than zero.");
            return;
        }

        if ("Mortgage Loan".equals(loanType)) {
            String termText = termComboBox == null ? "" : termComboBox.getValue();

            if (termText == null || termText.isBlank()) {
                statusLabel.setText("Please select a mortgage term.");
                return;
            }

            int term = Integer.parseInt(termText);

            // creates mortgage loan object
            MortgageLoan loan = new MortgageLoan(term, interestRate, principal, Period.ofDays(graceDays));

            // attach loan to selected customer
            selectedCustomer.payoffList.add(loan);

            // save loan data to CSV
            CsvManager.writeCustomersToCsv(AppState.customers, AppState.timeline);

            statusLabel.setText("Mortgage loan created and saved.");
            return;
        }

        if ("Short Term Loan".equals(loanType)) {
            // creates short term loan object
            ShortTermLoan loan = new ShortTermLoan(interestRate, principal, Period.ofDays(graceDays));

            //attach loan to selected customer
            selectedCustomer.payoffList.add(loan);

            // save loan data to CSV
            CsvManager.writeCustomersToCsv(AppState.customers, AppState.timeline);

            statusLabel.setText("Short term loan created and saved.");
            return;
        }

        statusLabel.setText("Loan type not supported yet.");
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
