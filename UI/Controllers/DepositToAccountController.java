import Account_Classes.Account;
import Account_Classes.CDAccount;
import Account_Classes.GDAccount;
import Account_Classes.SavingsAccount;
import Account_Classes.TMBAccount;
import User_Classes.Customer;
import Utils.AppState;
import Utils.Check;
import Utils.CsvManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.*;

public class DepositToAccountController {

    @FXML
    public ComboBox<String> depositTypeBox;
    public Label checkChoiceLabel;
    public ComboBox<String> chooseCheck;
    public Group checkGroup;
    public Group cashGroup;
    private final List<Account> myAccounts = new ArrayList<>();
    private final Map<String, Check> checkMap = new HashMap<>();

    @FXML
    private ComboBox<String> customerIdComboBox;

    @FXML
    private ComboBox<String> accountComboBox;

    @FXML
    private TextField depositAmountField;

    @FXML
    private Label statusLabel;
    @FXML
    private Label previousBalanceLabel;

    @FXML
    private Label depositAmountLabel;

    @FXML
    private Label newBalanceLabel;

    // maps dropdown text back to real account object
    private final Map<String, Account> accountMap = new HashMap<>();

    // maps customer name back to real customer object
    private final Map<String, Customer> customerMap = new HashMap<>();

    @FXML
    public void initialize() {
        checkGroup.setVisible(false);
        cashGroup.setVisible(false);
        depositTypeBox.getItems().add("Cash");
        depositTypeBox.getItems().add("Check");
        if (customerIdComboBox != null) {
            customerIdComboBox.getItems().clear();
            customerMap.clear();

            if (AppState.customers != null) {
                for (int i = 0; i < AppState.customers.getMcount(); i++) {
                    Customer c = AppState.customers.getValue(i);

                    // show first and last name instead of customer ID
                    if (c != null && c.firstName != null && c.lastName != null) {
                        String displayName = c.firstName + " " + c.lastName;
                        customerIdComboBox.getItems().add(displayName);
                        customerMap.put(displayName, c);
                    }
                }
            }
        }
    }
    private static void getCustomerChecks(){

    }
    @FXML
    private void onCustomerChosen(ActionEvent event) {
        String selectedCustomerName = customerIdComboBox == null ? "" : customerIdComboBox.getValue();
        checkGroup.setVisible(false);
        cashGroup.setVisible(false);
        chooseCheck.getItems().clear();
        depositTypeBox.getItems().clear();
        depositTypeBox.getItems().add("Cash");
        depositTypeBox.getItems().add("Check");
        accountMap.clear();
        myAccounts.clear();

        if (accountComboBox != null) {
            accountComboBox.getItems().clear();
        }

        if (selectedCustomerName == null || selectedCustomerName.isBlank()) {
            if (statusLabel != null) {
                statusLabel.setText("Please select a customer.");
            }
            return;
        }

        // find customer from name map
        Customer customer = customerMap.get(selectedCustomerName);
        if (customer == null) {
            if (statusLabel != null) {
                statusLabel.setText("Customer not found.");
            }
            return;
        }

        if (customer.accountList != null) {
            for (Account account : customer.accountList) {
                String displayText = buildAccountDisplay(account);
                accountMap.put(displayText, account);

                if (accountComboBox != null) {
                    accountComboBox.getItems().add(displayText);
                }
            }
        }

        //get customers checks
        if (AppState.checks != null) {
            for (int i = 0; i < AppState.checks.size(); i++) {
                Check c = AppState.checks.get(i);
                //check for received checks
                if (c.getReceiver() == customer && c.getStatus().equals("Pending")){
                    String text = buildCheckDisplay(c,"From: "+ Objects.requireNonNull(ManageChecksScreen.getCustFromAccount(c.getSender())).lastName);
                    checkMap.put(text,c);
                    chooseCheck.getItems().add(text);
                }
            }
        }

        if (statusLabel != null) {
            statusLabel.setText("Customer accounts loaded.");
        }
    }

    @FXML
    public void onChooseDepositType(ActionEvent actionEvent) {
        checkGroup.setVisible(false);
        cashGroup.setVisible(false);
        if (depositTypeBox.getValue() != null){
            if (depositTypeBox.getValue().equals("Check")){
                checkGroup.setVisible(true);
            } else if(depositTypeBox.getValue().equals("Cash")){
                cashGroup.setVisible(true);
            }
        }

    }

    @FXML
    private void depositToAccount(ActionEvent event) {
        String selectedCustomerName = customerIdComboBox == null ? "" : customerIdComboBox.getValue();
        String accountSelection = accountComboBox == null ? "" : accountComboBox.getValue();
        String amountText = depositAmountField == null ? "" : depositAmountField.getText();
        String depositType = depositTypeBox == null ? "" : depositTypeBox.getValue();
        double amount;
        if (depositType == null || depositType.isBlank()) {
            if (statusLabel != null) statusLabel.setText("Please select a deposit type.");
            return;
        }

        if (selectedCustomerName == null || selectedCustomerName.isBlank()) {
            if (statusLabel != null) statusLabel.setText("Please select a customer.");
            return;
        }

        if (accountSelection == null || accountSelection.isBlank()) {
            if (statusLabel != null) statusLabel.setText("Please select an account.");
            return;
        }

        Account account = accountMap.get(accountSelection);
        if (account == null) {
            if (statusLabel != null) statusLabel.setText("Selected account could not be found.");
            return;
        }

        double previousBalance = account.getBalance();

        if (depositType.equals("Cash")){
            try {
                amount = Double.parseDouble(amountText);
            } catch (Exception e) {
                if (statusLabel != null) statusLabel.setText("Please enter a valid deposit amount.");
                return;
            }

            if (amount <= 0) {
                if (statusLabel != null) statusLabel.setText("Deposit amount must be greater than zero.");
                return;
            }

            account.deposit(amount);

            if (depositAmountLabel != null) {
                depositAmountLabel.setText("Deposit Amount: $" + String.format("%.2f", amount));
            }
        } else if(depositType.equals("Check")){
            String checkchoice = chooseCheck ==null ? "" : chooseCheck.getValue();

            if (checkchoice == null || checkchoice.isBlank()) {
                if (statusLabel != null) statusLabel.setText("Please select a check.");
                return;
            }
            Check check = checkMap.get(checkchoice);
            if (check == null){
                if (statusLabel != null) statusLabel.setText("Selected check could not be found.");
                return;
            }
            if (check.getAmount() <= 0) {
                if (statusLabel != null) statusLabel.setText("Deposit amount must be greater than zero.");
                return;
            }
            if (!check.processCheck(account)){
                if (statusLabel != null) statusLabel.setText("Check was unable to be processed");
                return;
            }
            if (depositAmountLabel != null) {
                depositAmountLabel.setText("Deposit Amount: $" + String.format("%.2f", check.getAmount()));
            }
        }


        // update TMB/GD account type immediately after deposit
        CsvManager.updateCheckingAccountTypes(AppState.customers, AppState.timeline);

        double newBalance = account.getBalance();

        CsvManager.writeCustomersToCsv(AppState.customers, AppState.timeline);
        CsvManager.writeChecksToCSV(AppState.checks);
        if (previousBalanceLabel != null) {
            previousBalanceLabel.setText("Previous Balance: $" + String.format("%.2f", previousBalance));
        }

        if (newBalanceLabel != null) {
            newBalanceLabel.setText("New Balance: $" + String.format("%.2f", newBalance));
        }

        if (statusLabel != null) {
            statusLabel.setText("Deposit completed successfully.");
        }

        // refresh account display so balance updates in dropdown
        onCustomerChosen(null);
    }
    public void onChooseCheck(ActionEvent actionEvent) {
        //validate check
    }

    private String buildCheckDisplay(Check check, String prefix) {
        return prefix + " - $" + String.format("%.2f", check.getAmount());
    }
    // account text for dropdown display
    private String buildAccountDisplay(Account account) {
        return getAccountType(account) + " - $" + String.format("%.2f", account.getBalance());
    }

    // account type names
    private String getAccountType(Account account) {
        if (account instanceof SavingsAccount) {
            return "Savings";
        } else if (account instanceof TMBAccount) {
            return "TMB Checking";
        } else if (account instanceof GDAccount) {
            return "Gold/Diamond";
        } else if (account instanceof CDAccount) {
            return "CD";
        }
        return "Unknown";
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



}
