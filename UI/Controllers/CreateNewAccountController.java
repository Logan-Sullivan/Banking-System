//NOTE: this is a very basic implementation of the file
//It can only handle making Savings accounts, and does not link them to the customers
//It instead stores the saving accounts in a csv to be read later.
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import User_Classes.Customer;
import Utils.AppState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateNewAccountController {

    @FXML
    private ComboBox accountTypeCombo;
    @FXML
    private Label interestRateLabel;
    @FXML
    private TextField interestRateField;
    @FXML
    private Label compoundFrequencyLabel;
    @FXML
    private TextField compoundFrequencyField;
    @FXML
    private CheckBox flexibleRateCheck;
    @FXML
    private ComboBox<String> customerIdComboBox;
    @FXML
    private TextField accountNumberField;
    @FXML
    private Label statusLabel;
    @FXML
    private TextField initialDepositField;

    @FXML
    public void initialize() {
        if (customerIdComboBox == null) return;

        customerIdComboBox.getItems().clear();

        if (AppState.customers != null) {
            for (int i = 0; i < AppState.customers.getMcount(); i++) {
                Customer c = AppState.customers.getValue(i);
                if (c != null && c.customerId != null && !c.customerId.isBlank()) {
                    customerIdComboBox.getItems().add(c.customerId);
                }
            }
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

    //This function is for the account type being chosen using the combo box.
    //This reveals the extra fields that each account type might need so the teller can input the information
    @FXML
    private void onAccountTypeChosen(ActionEvent event){
        if(accountTypeCombo.getValue().equals("Savings Account")){
            interestRateField.setVisible(true);
            interestRateLabel.setVisible(true);
            compoundFrequencyLabel.setVisible(true);
            compoundFrequencyField.setVisible(true);
            flexibleRateCheck.setVisible(false);
        }
        if(accountTypeCombo.getValue().equals("Gold Diamond Checking Account")){
            interestRateField.setVisible(false);
            interestRateLabel.setVisible(false);
            compoundFrequencyLabel.setVisible(false);
            compoundFrequencyField.setVisible(false);
            flexibleRateCheck.setVisible(true);
        }
        if(accountTypeCombo.getValue().equals("That's My Bank Checking Account")){
            interestRateField.setVisible(false);
            interestRateLabel.setVisible(false);
            compoundFrequencyLabel.setVisible(false);
            compoundFrequencyField.setVisible(false);
            flexibleRateCheck.setVisible(false);
        }
        if(accountTypeCombo.getValue().equals("CD Account")){
            interestRateField.setVisible(false);
            interestRateLabel.setVisible(false);
            compoundFrequencyLabel.setVisible(false);
            compoundFrequencyField.setVisible(false);
            flexibleRateCheck.setVisible(false);
        }
    }
    /**
     * This is currently in it's VERY early stages, and can only handle saving accounts.
     * It also does not link the accoutns to the customers
     * It writes the account information to a csv to be read.
     * @param event the "create account" button being pressed
     */
    @FXML
    private void createAccountPressed(ActionEvent event){
        String customerID = customerIdComboBox == null ? "" : customerIdComboBox.getValue();
        String accountNumber = accountNumberField == null ? "" : accountNumberField.getText();
        double interestRate = 0.0;
        String compoundFrequency = "";
        Boolean flexibleRate;
        double balance = 0.0;
        try{
            balance = initialDepositField == null ? 0.0 : Double.parseDouble(initialDepositField.getText());
        }catch(Exception e){
            if (statusLabel != null){
                statusLabel.setText("Please enter a valid balance number");
                statusLabel.setVisible(true);
            }
        }
        if(accountTypeCombo.getValue().equals("Savings Account")){
            try{
                interestRate = interestRateField == null ? 0.0 : Double.parseDouble(interestRateField.getText());
                compoundFrequency = compoundFrequencyField == null ? "" : compoundFrequencyField.getText();
            }catch(Exception e){
                statusLabel.setText("Please enter a valid interest rate");
                statusLabel.setVisible(true);
                return;
            }
            if(interestRate==0.0){
                if(statusLabel != null){
                    statusLabel.setText("interest rate is required");
                    return;
                }
            }
        }else if(accountTypeCombo.getValue().equals("Gold Diamond Checking Account")){
            flexibleRate = flexibleRateCheck == null ? null : flexibleRateCheck.isSelected();
        }else if(accountTypeCombo.getValue().equals("That's My Bank Checking Account")){

        }else if(accountTypeCombo.getValue().equals("CD Account")){

        }else{

        }
        if(customerID == null || customerID.isBlank() || accountNumber.isBlank() || compoundFrequency.isBlank()){
            if(statusLabel != null){
                statusLabel.setText("customer ID, account number, interest rate, and compound frequency is required");
                return;
            }
        }
        File outputFile = new File("src/testAccounts.csv");
        try{
            PrintWriter writer = new PrintWriter(outputFile);
            writer.write(String.format("%s, %s, %f, %s, %f", customerID, accountNumber, interestRate, compoundFrequency, balance));
            writer.close();
        }catch(FileNotFoundException e){
            statusLabel.setText("Problem with writer");
            statusLabel.setVisible(true);
        }
    }
}
