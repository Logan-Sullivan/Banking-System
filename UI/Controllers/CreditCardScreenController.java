import Account_Classes.Account;
import Account_Classes.CDAccount;
import Account_Classes.GDAccount;
import Account_Classes.SavingsAccount;
import Account_Classes.TMBAccount;
import Loan_Classes.CreditCard;
import Loan_Classes.Loan;
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

import java.util.HashMap;
import java.util.Map;

public class CreditCardScreenController {

    @FXML private TextField customerIdField;
    @FXML private ComboBox<String> creditCardComboBox;
    @FXML private ComboBox<String> payFromAccountComboBox;
    @FXML private TextField currentBalanceField;
    @FXML private TextField paymentAmountField;
    @FXML private TextField chargeAmountField;
    @FXML private TextField chargeDescriptionField;
    @FXML private Label statusLabel;

    private final Map<String, CreditCard> creditCardMap = new HashMap<>();
    private final Map<String, Account> payFromAccountMap = new HashMap<>();
    private CreditCard selectedCard;

    @FXML
    private void searchCustomerPressed(ActionEvent event) {
        String customerId = customerIdField.getText();

        creditCardComboBox.getItems().clear();
        payFromAccountComboBox.getItems().clear();
        creditCardMap.clear();
        payFromAccountMap.clear();
        selectedCard = null;
        currentBalanceField.clear();

        if (customerId == null || customerId.isBlank()) {
            showStatus("Please enter a customer ID.");
            return;
        }

        Customer customer = findCustomer(customerId);

        if (customer == null) {
            showStatus("Customer not found.");
            return;
        }

        for (Loan loan : customer.payoffList) {
            if (loan instanceof CreditCard card) {
                String displayText = card.id + " | Balance: $" + card.getBalance();
                creditCardComboBox.getItems().add(displayText);
                creditCardMap.put(displayText, card);
            }
        }

        for (Account account : customer.accountList) {
            if (account instanceof SavingsAccount || account instanceof TMBAccount || account instanceof GDAccount) {
                String displayText = buildAccountDisplay(account);
                payFromAccountComboBox.getItems().add(displayText);
                payFromAccountMap.put(displayText, account);
            }
        }

        showStatus("Customer loaded. Select a credit card and payment account.");
    }

    @FXML
    private void makePaymentPressed(ActionEvent event) {
        if (!selectCardFromDropdown()) return;

        String selectedAccountText = payFromAccountComboBox.getValue();

        if (selectedAccountText == null || selectedAccountText.isBlank()) {
            showStatus("Please select an account to pay from.");
            return;
        }

        Account payFromAccount = payFromAccountMap.get(selectedAccountText);

        if (payFromAccount == null) {
            showStatus("Selected payment account not found.");
            return;
        }

        double paymentAmount;

        try {
            paymentAmount = Double.parseDouble(paymentAmountField.getText());
        } catch (Exception e) {
            showStatus("Invalid payment amount.");
            return;
        }

        if (paymentAmount <= 0) {
            showStatus("Payment amount must be greater than 0.");
            return;
        }

        if (payFromAccount.getBalance() < paymentAmount) {
            showStatus("Insufficient funds in selected account.");
            return;
        }
        
        payFromAccount.withdraw(paymentAmount);
        
        selectedCard.makePayment(paymentAmount, AppState.timeline.getLastUpdatedDate());

        currentBalanceField.setText(String.valueOf(selectedCard.getBalance()));
        CsvManager.writeCustomersToCsv(AppState.customers, AppState.timeline);

        refreshCardDropdown();
        showStatus("Payment made successfully.");
    }

    @FXML
    private void chargeCreditCardPressed(ActionEvent event) {
        if (!selectCardFromDropdown()) return;

        double chargeAmount;

        try {
            chargeAmount = Double.parseDouble(chargeAmountField.getText());
        } catch (Exception e) {
            showStatus("Invalid charge amount.");
            return;
        }

        if (chargeAmount <= 0) {
            showStatus("Charge amount must be greater than 0.");
            return;
        }

        String description = chargeDescriptionField.getText();

        if (description == null || description.isBlank()) {
            description = "Credit Card Charge";
        }
        
        selectedCard.makeTransaction(chargeAmount, description, AppState.timeline.getLastUpdatedDate());

        currentBalanceField.setText(String.valueOf(selectedCard.getBalance()));
        CsvManager.writeCustomersToCsv(AppState.customers, AppState.timeline);

        refreshCardDropdown();
        showStatus("Credit card charged successfully.");
    }

    @FXML
    private void creditCardChosen(ActionEvent event) {
        selectCardFromDropdown();
    }

    private boolean selectCardFromDropdown() {
        String selectedCardText = creditCardComboBox.getValue();

        if (selectedCardText == null || selectedCardText.isBlank()) {
            showStatus("Please select a credit card.");
            return false;
        }

        selectedCard = creditCardMap.get(selectedCardText);

        if (selectedCard == null) {
            showStatus("Selected credit card not found.");
            return false;
        }

        currentBalanceField.setText(String.valueOf(selectedCard.getBalance()));
        return true;
    }

    private void refreshCardDropdown() {
        if (selectedCard == null) return;

        String oldSelection = creditCardComboBox.getValue();

        String oldKey = null;
        for (String key : creditCardMap.keySet()) {
            if (creditCardMap.get(key) == selectedCard) {
                oldKey = key;
                break;
            }
        }

        if (oldKey != null) {
            creditCardMap.remove(oldKey);
        }

        String newDisplayText = selectedCard.id + " | Balance: $" + selectedCard.getBalance();
        creditCardMap.put(newDisplayText, selectedCard);

        int index = creditCardComboBox.getItems().indexOf(oldSelection);
        if (index >= 0) {
            creditCardComboBox.getItems().set(index, newDisplayText);
            creditCardComboBox.setValue(newDisplayText);
        }
    }

    private String buildAccountDisplay(Account account) {
        return getAccountType(account) + " - $" + String.format("%.2f", account.getBalance());
    }

    private String getAccountType(Account account) {
        if (account instanceof SavingsAccount) return "Savings";
        if (account instanceof TMBAccount) return "TMB Checking";
        if (account instanceof GDAccount) return "Gold/Diamond";
        if (account instanceof CDAccount) return "CD";
        return "Unknown";
    }

    private Customer findCustomer(String customerId) {
        for (int i = 0; i < AppState.customers.getMcount(); i++) {
            Customer customer = AppState.customers.getValue(i);

            if (customer != null && customer.customerId.equals(customerId)) {
                return customer;
            }
        }

        return null;
    }

    private void showStatus(String message) {
        statusLabel.setText(message);
        statusLabel.setVisible(true);
    }

    @FXML
    private void returnToCustomerScreen(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/CustomerScreen.fxml")));
            stage.setScene(scene);
            stage.setTitle("Customer Screen");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
