import Account_Classes.*;
import User_Classes.*;
import Utils.*;

public class Main {
    public static void main(String[] args) {
        ArrayListManager<Customer> CustomerList = new ArrayListManager<>();
        CheckpointManager Checkpoint = new CheckpointManager();
        CsvManager.fetchCustsAndAccountsFromCSV(CustomerList);

        for (int i = 0; i < CustomerList.getMcount(); i++) {
            Customer cust = CustomerList.getValue(i);
            cust.accountList.add(new SavingsAccount());
            cust.accountList.add(new CheckingsAccount(1567.3+(int)(Math.random()*1000)));
        }

        Checkpoint.EXEC_CHECKPOINT(CustomerList);

        // example of customer object to save the data
       /*Customer newCustomer = new Customer();
        SavingsAccount newSavings = new SavingsAccount();
        CheckingsAccount newCheckings = new CheckingsAccount( 100);
        SavingsAccount blankSavings = new SavingsAccount(1, "Test for Overdraft", false, 0);
        newCustomer.accountList.add(newCheckings);
        newCustomer.accountList.add(newSavings);
        String extraData = "";
        PrintUtil.saveCustomerData(newCustomer, newSavings, extraData);
        System.out.println("----------Transferring first amount----------");
        newCheckings.transfer(newSavings, 25);
        System.out.println("----------Transfer over---------- \nsetting overdraft account, and overdrafting");
        newCheckings.setOverdraftProtAccount(newSavings);
        newCheckings.transfer(blankSavings, 76);*/

    }
}