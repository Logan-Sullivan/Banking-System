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
import Utils.Transaction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class ReviewAccountStatusController {

    @FXML
    private TextField customerIdField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextArea savingsArea;

    @FXML
    private TextArea tmbArea;

    @FXML
    private TextArea gdArea;

    @FXML
    private TextArea cdArea;

    @FXML
    private TextArea creditCardArea;

    @FXML
    private TextArea loanArea;

    @FXML
    private Label statusLabel;

    @FXML
    private TextArea atmArea;

    @FXML
    public void initialize() {
        clearSections();
    }

    @FXML
    private void refreshAccounts(ActionEvent event) {
        String customerId = customerIdField == null ? "" : customerIdField.getText().trim();
        String firstName = firstNameField == null ? "" : firstNameField.getText().trim();
        String lastName = lastNameField == null ? "" : lastNameField.getText().trim();

        if (customerId.isBlank() || firstName.isBlank() || lastName.isBlank()) {
            setStatus("Please enter customer ID, first name, and last name.");
            return;
        }

        Customer customer = findCustomer(customerId, firstName, lastName);

        if (customer == null) {
            clearSections();
            setStatus("Customer information did not match our records.");
            return;
        }

        loadCustomerAccountSections(customer);
        if (customer.atm != null) {
            atmArea.setText("ATM Withdrawals Today: " + customer.atm.getWithdraws() + " / 2");
        } else {
            atmArea.setText("No ATM card found.");
        }
        setStatus("Account status loaded for " + customer.firstName + " " + customer.lastName + ".");
    }
    private Customer findCustomer(String customerId, String firstName, String lastName) {
        if (AppState.customers == null) {
            return null;
        }

        for (int i = 0; i < AppState.customers.getMcount(); i++) {
            Customer customer = AppState.customers.getValue(i);

            if (customer != null
                    && customerId.equals(customer.customerId)
                    && firstName.equalsIgnoreCase(customer.firstName)
                    && lastName.equalsIgnoreCase(customer.lastName)) {
                return customer;
            }
        }

        return null;
    }

    private void loadCustomerAccountSections(Customer customer) {
        clearSections();

        StringBuilder savingsBuilder = new StringBuilder();
        StringBuilder tmbBuilder = new StringBuilder();
        StringBuilder gdBuilder = new StringBuilder();
        StringBuilder cdBuilder = new StringBuilder();
        StringBuilder creditCardBuilder = new StringBuilder();
        StringBuilder loanBuilder = new StringBuilder();

        if (customer.accountList != null) {
            for (Account account : customer.accountList) {

                if (account instanceof SavingsAccount savings) {
                    savingsBuilder.append("Account Number: ").append(savings.accountNumber).append("\n");
                    savingsBuilder.append("Balance: ").append(formatMoney(savings.getBalance())).append("\n");
                    savingsBuilder.append("Interest Rate: ").append(savings.getInterestRate()).append("%\n");
                    savingsBuilder.append("Compound Frequency: ").append(savings.getCompoundFreq()).append(" days\n");
                    savingsBuilder.append("Overdraft Backup: ").append(savings.isOverdraftBackup()).append("\n");
                    savingsBuilder.append("----------------------------------------\n");
                }

                else if (account instanceof TMBAccount tmb) {
                    tmbBuilder.append("Account Number: ").append(tmb.accountNumber).append("\n");
                    tmbBuilder.append("Balance: ").append(formatMoney(tmb.getBalance())).append("\n");
                    tmbBuilder.append("Transaction Fee: $0.75\n");
                    tmbBuilder.append("Monthly Transfer Fee: $1.25\n");

                    if (tmb.getOverdraftProtAccount() != null) {
                        tmbBuilder.append("Overdraft Backup Account: ")
                                .append(tmb.getOverdraftProtAccount().accountNumber).append("\n");
                    } else {
                        tmbBuilder.append("Overdraft Backup Account: None\n");
                    }

                    tmbBuilder.append("----------------------------------------\n");
                }

                else if (account instanceof GDAccount gd) {
                    gdBuilder.append("Account Number: ").append(gd.accountNumber).append("\n");
                    gdBuilder.append("Balance: ").append(formatMoney(gd.getBalance())).append("\n");
                    gdBuilder.append("Minimum Balance: $5000.00\n");
                    gdBuilder.append("Flexible Daily Rate: ").append(gd.dailyRateFlexible).append("\n");

                    if (gd.getOverdraftProtAccount() != null) {
                        gdBuilder.append("Linked Interest/Savings Account: ")
                                .append(gd.getOverdraftProtAccount().accountNumber).append("\n");
                    } else {
                        gdBuilder.append("Linked Interest/Savings Account: None\n");
                    }

                    gdBuilder.append("----------------------------------------\n");
                }

                else if (account instanceof CDAccount cd) {
                    cdBuilder.append("Account Number: ").append(cd.accountNumber).append("\n");
                    cdBuilder.append("Balance: ").append(formatMoney(cd.getBalance())).append("\n");
                    cdBuilder.append("Fixed Rate: ").append(cd.fixedRate).append("%\n");
                    cdBuilder.append("Maturity Date: ").append(cd.maturityDate).append("\n");
                    cdBuilder.append("Early Withdrawal Penalty: ").append(formatMoney(cd.earlyPenalty)).append("\n");
                    cdBuilder.append("----------------------------------------\n");
                }
            }
        }

        if (customer.payoffList != null) {
            for (Loan loan : customer.payoffList) {

                if (loan instanceof CreditCard card) {
                    creditCardBuilder.append("Card ID: ").append(card.id).append("\n");
                    creditCardBuilder.append("Current Balance: ").append(formatMoney(card.getBalance())).append("\n");
                    creditCardBuilder.append("Interest Rate: ").append(card.interest_rate).append("%\n");
                    creditCardBuilder.append("Problem Account: ").append(card.getIsProblemAccount()).append("\n");
                    creditCardBuilder.append("Credit Limit: ").append(formatMoney(card.creditLimit)).append("\n");
                    creditCardBuilder.append("Available Credit: ")
                            .append(formatMoney(card.creditLimit - card.getBalance())).append("\n");
                    creditCardBuilder.append("Finance Charge: ").append(formatMoney(card.getFinanceCharge())).append("\n");

                    if (card.transactions != null && !card.transactions.isEmpty()) {
                        creditCardBuilder.append("Transactions:\n");

                        for (Transaction transaction : card.transactions) {
                            creditCardBuilder.append("  ").append(transaction.toString()).append("\n");
                        }
                    } else {
                        creditCardBuilder.append("Transactions: None\n");
                    }

                    creditCardBuilder.append("----------------------------------------\n");
                }

                else if (loan instanceof MortgageLoan mortgage) {
                    loanBuilder.append("Loan Type: Mortgage Loan\n");
                    loanBuilder.append("Loan ID: ").append(mortgage.id).append("\n");
                    loanBuilder.append("Term: ").append(mortgage.term).append(" years\n");
                    loanBuilder.append("Principal: ").append(formatMoney(mortgage.principal)).append("\n");
                    loanBuilder.append("Interest Rate: ").append(mortgage.interest_rate).append("%\n");
                    loanBuilder.append("Current Payment Due: ")
                            .append(formatMoney(mortgage.getCurrentPaymentDue())).append("\n");
                    loanBuilder.append("Problem Account: ").append(mortgage.getIsProblemAccount()).append("\n");
                    loanBuilder.append("----------------------------------------\n");
                }

                else if (loan instanceof ShortTermLoan shortLoan) {
                    loanBuilder.append("Loan Type: Short Term Loan\n");
                    loanBuilder.append("Loan ID: ").append(shortLoan.id).append("\n");
                    loanBuilder.append("Term: ").append(shortLoan.term).append(" years\n");
                    loanBuilder.append("Principal: ").append(formatMoney(shortLoan.principal)).append("\n");
                    loanBuilder.append("Interest Rate: ").append(shortLoan.interest_rate).append("%\n");
                    loanBuilder.append("Current Payment Due: ")
                            .append(formatMoney(shortLoan.getCurrentPaymentDue())).append("\n");
                    loanBuilder.append("Problem Account: ").append(shortLoan.getIsProblemAccount()).append("\n");
                    loanBuilder.append("----------------------------------------\n");
                }
            }
        }

        savingsArea.setText(savingsBuilder.length() == 0 ? "No savings accounts." : savingsBuilder.toString());
        tmbArea.setText(tmbBuilder.length() == 0 ? "No TMB checking accounts." : tmbBuilder.toString());
        gdArea.setText(gdBuilder.length() == 0 ? "No Gold/Diamond accounts." : gdBuilder.toString());
        cdArea.setText(cdBuilder.length() == 0 ? "No CD accounts." : cdBuilder.toString());
        creditCardArea.setText(creditCardBuilder.length() == 0 ? "No credit cards." : creditCardBuilder.toString());
        loanArea.setText(loanBuilder.length() == 0 ? "No loan accounts." : loanBuilder.toString());
    }

    private void clearSections() {
        if (savingsArea != null) savingsArea.setText("");
        if (tmbArea != null) tmbArea.setText("");
        if (gdArea != null) gdArea.setText("");
        if (cdArea != null) cdArea.setText("");
        if (creditCardArea != null) creditCardArea.setText("");
        if (loanArea != null) loanArea.setText("");
        if (atmArea != null) atmArea.setText("");
    }

    private String formatMoney(double amount) {
        return String.format("$%.2f", amount);
    }

    private void setStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
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
