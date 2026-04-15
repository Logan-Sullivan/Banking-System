package Account_Classes;
import java.util.List;
import java.util.UUID;

public class CheckingsAccount extends Account{
    SavingsAccount overdraftProtAccount;
    List<Integer> stopPayments;

//,SavingsAccount overdraftAccount
    public CheckingsAccount(double balance){
        this.accountNumber = UUID.randomUUID().toString();
        //this.overdraftProtAccount = overdraftAccount;
        this.deposit(balance);
    }

    //constructor for writing from csv
    public CheckingsAccount(String accountNumber,double balance){
        this.accountNumber = accountNumber;
        this.deposit(balance);
        this.overdraftProtAccount = null;
    }

    public void stopPayment(){

    }

    public void handleOverdraft(){
        if (this.getBalance() < 0 && overdraftProtAccount != null && overdraftProtAccount.getBalance() >= 0){
            System.out.println("Charging " + overdraftProtAccount.accountNumber + " for " + this.getBalance());
            overdraftProtAccount.withdraw(-this.getBalance());
            this.deposit(-this.getBalance()); // CS: dumb way to do it, but who cares
        } else if (this.getBalance() < 0){
            this.withdraw(25);
            System.out.println("No overdraft, charging 25$");
        } // Charge overdraft fee, if not below zero, who cares.
    }

    public void transfer(Account account, double amount){ // CS: could probably make it a fromAccount, toAccount
        this.withdraw(amount);
        account.deposit(amount);
        this.handleOverdraft();
        System.out.println("Transfers " + amount + " to account " + account.accountNumber);
    }

    public void setOverdraftProtAccount(SavingsAccount overdraftProtAccount) {
        this.overdraftProtAccount = overdraftProtAccount;
        overdraftProtAccount.toggleOverdraftBackup();
    }

    @Override
    public String toString() {
        return this.accountNumber+","+this.getBalance();
    }

    public SavingsAccount getOverdraftProtAccount() {
        return overdraftProtAccount;
    }
}