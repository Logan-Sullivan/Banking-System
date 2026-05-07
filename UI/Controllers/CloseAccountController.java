import Account_Classes.Account;
import Account_Classes.CDAccount;
import Account_Classes.GDAccount;
import Account_Classes.SavingsAccount;
import Account_Classes.TMBAccount;
import Loan_Classes.CreditCard;
import Loan_Classes.Loan;
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
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class CloseAccountController {

    @FXML
    private ComboBox<String> customerComboBox;

    @FXML
    private ComboBox<String> accountComboBox;

    @FXML
    private TextArea resultArea;

    @FXML
    private Label statusLabel;

    private final Map<String, Customer> customerMap = new HashMap<>();
    private final Map<String, CloseChoice> closeChoiceMap = new HashMap<>();

    private Customer selectedCustomer;

    @FXML
    public void initialize() {
        loadCustomers();
        clearResult();
    }

    private void loadCustomers() {
        if (customerComboBox == null) {
            return;
        }

        customerComboBox.getItems().clear();
        customerMap.clear();

        if (AppState.customers != null) {
            for (int i = 0; i < AppState.customers.getMcount(); i++) {
                Customer customer = AppState.customers.getValue(i);

                if (customer != null && customer.firstName != null && customer.lastName != null) {
                    String displayText = customer.firstName + " " + customer.lastName + " (" + customer.customerId + ")";
                    customerComboBox.getItems().add(displayText);
                    customerMap.put(displayText, customer);
                }
            }
        }
    }

    @FXML
    private void onCustomerChosen(ActionEvent event) {
        String selectedCustomerText = customerComboBox == null ? "" : customerComboBox.getValue();

        clearResult();
        selectedCustomer = null;
        closeChoiceMap.clear();

        if (accountComboBox != null) {
            accountComboBox.getItems().clear();
        }

        if (selectedCustomerText == null || selectedCustomerText.isBlank()) {
            setStatus("Please select a customer.");
            return;
        }

        selectedCustomer = customerMap.get(selectedCustomerText);

        if (selectedCustomer == null) {
            setStatus("Customer not found.");
            return;
        }

        loadCustomerAccountsAndLoans(selectedCustomer);
        setStatus("Customer loaded.");
    }

    private void loadCustomerAccountsAndLoans(Customer customer) {
        if (accountComboBox == null) {
            return;
        }

        accountComboBox.getItems().clear();
        closeChoiceMap.clear();

        if (customer.accountList != null) {
            for (int i = 0; i < customer.accountList.size(); i++) {
                Account account = customer.accountList.get(i);
                String displayText = "ACCOUNT | " + getAccountType(account)
                        + " | " + account.accountNumber
                        + " | Balance: " + formatMoney(account.getBalance());

                accountComboBox.getItems().add(displayText);
                closeChoiceMap.put(displayText, new CloseChoice(false, i));
            }
        }

        if (customer.payoffList != null) {
            for (int i = 0; i < customer.payoffList.size(); i++) {
                Loan loan = customer.payoffList.get(i);
                String displayText = "LOAN | " + getLoanType(loan)
                        + " | " + loan.id
                        + " | Due: " + formatMoney(loan.closeAccount());

                accountComboBox.getItems().add(displayText);
                closeChoiceMap.put(displayText, new CloseChoice(true, i));
            }
        }

        if (accountComboBox.getItems().isEmpty()) {
            setStatus("This customer has no accounts or loans.");
        }
    }

    @FXML
    private void closeSelectedPressed(ActionEvent event) {
        if (selectedCustomer == null) {
            setStatus("Please select a customer first.");
            return;
        }

        String selectedAccountText = accountComboBox == null ? "" : accountComboBox.getValue();

        if (selectedAccountText == null || selectedAccountText.isBlank()) {
            setStatus("Please select an account or loan to close.");
            return;
        }

        CloseChoice choice = closeChoiceMap.get(selectedAccountText);

        if (choice == null) {
            setStatus("Selected item could not be found.");
            return;
        }

        double amount;

        if (choice.isLoan) {
            // close selected loan/credit card
            amount = selectedCustomer.closeOneLoan(choice.index);

            resultArea.setText(
                    "Closed selected loan/credit card.\n\n" +
                            "Customer owes: " + formatMoney(Math.abs(amount))
            );
        } else {
            // close selected deposit/checking/CD/savings account
            amount = selectedCustomer.closeOneAccount(choice.index);

            resultArea.setText(
                    "Closed selected account.\n\n" +
                            "Return to customer: " + formatMoney(amount)
            );
        }

        CsvManager.writeCustomersToCsv(AppState.customers, AppState.timeline);

        loadCustomerAccountsAndLoans(selectedCustomer);
        setStatus("Selected account/loan closed successfully.");
    }

    @FXML
    private void closeEntireCustomerPressed(ActionEvent event) {
        if (selectedCustomer == null) {
            setStatus("Please select a customer first.");
            return;
        }

        double netAmount = 0.0;

        //  removing index 0
        while (selectedCustomer.accountList != null && !selectedCustomer.accountList.isEmpty()) {
            netAmount += selectedCustomer.closeOneAccount(0);
        }

        //  removing index 0
        while (selectedCustomer.payoffList != null && !selectedCustomer.payoffList.isEmpty()) {
            netAmount += selectedCustomer.closeOneLoan(0);
        }

        String customerName = selectedCustomer.firstName + " " + selectedCustomer.lastName;

        removeCustomerFromAppState(selectedCustomer);

        CsvManager.writeCustomersToCsv(AppState.customers, AppState.timeline);

        loadCustomers();

        if (accountComboBox != null) {
            accountComboBox.getItems().clear();
        }

        selectedCustomer = null;

        if (netAmount >= 0) {
            resultArea.setText(
                    "Closed entire customer: " + customerName + "\n\n" +
                            "Final amount returned to customer: " + formatMoney(netAmount)
            );
        } else {
            resultArea.setText(
                    "Closed entire customer: " + customerName + "\n\n" +
                            "Final amount customer owes: " + formatMoney(Math.abs(netAmount))
            );
        }

        setStatus("Customer closed and removed from system.");
    }

    private void removeCustomerFromAppState(Customer customerToRemove) {
        if (AppState.customers == null || customerToRemove == null) {
            return;
        }

        for (int i = 0; i < AppState.customers.getMcount(); i++) {
            Customer customer = AppState.customers.getValue(i);

            if (customer == customerToRemove) {
                AppState.customers.removeM(i);
                return;
            }
        }
    }

    private String getAccountType(Account account) {
        if (account instanceof SavingsAccount) {
            return "Savings";
        }
        if (account instanceof TMBAccount) {
            return "TMB Checking";
        }
        if (account instanceof GDAccount) {
            return "Gold/Diamond";
        }
        if (account instanceof CDAccount) {
            return "CD";
        }
        return "Unknown Account";
    }

    private String getLoanType(Loan loan) {
        if (loan instanceof CreditCard) {
            return "Credit Card";
        }
        if (loan instanceof MortgageLoan) {
            return "Mortgage Loan";
        }
        if (loan instanceof ShortTermLoan) {
            return "Short Term Loan";
        }
        return "Unknown Loan";
    }

    private String formatMoney(double amount) {
        return String.format("$%.2f", amount);
    }

    private void clearResult() {
        if (resultArea != null) {
            resultArea.setText("");
        }
    }

    private void setStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }

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

    private static class CloseChoice {
        boolean isLoan;
        int index;

        CloseChoice(boolean isLoan, int index) {
            this.isLoan = isLoan;
            this.index = index;
        }
    }
}
