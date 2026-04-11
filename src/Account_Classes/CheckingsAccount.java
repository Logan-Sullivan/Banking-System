package Account_Classes;
import java.util.List;
import java.util.UUID;

public abstract class CheckingsAccount extends Account{
    SavingsAccount overdraftProtAccount;
    List<Integer> monthlyPayments;
    int checkNum = 0;
    double minimumBalance;

    public CheckingsAccount(SavingsAccount overdraftAccount, double balance){
        this.accountNumber = UUID.randomUUID().toString();
        this.overdraftProtAccount = overdraftAccount;
        this.deposit(balance);
    }

    public void stopPayment(int checknumber){

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

    public void transfer(Account account, double amount){
        this.withdraw(amount);
        account.deposit(amount);
        this.handleOverdraft();
        System.out.println("Transfers " + amount + " to account " + account.accountNumber);
    }

    public void makeMonthlyTransfer(Account account, double amount){
        this.withdraw(amount);
        account.deposit(amount);
        this.handleOverdraft();
        System.out.println("Monthly Transfer " + amount + " made to account " + account.accountNumber);
    } // CS: same thing, just called monthly, could be done way better, but separated

    public boolean requestFeeWaiver(double checkAmt){
        return this.getBalance() - checkAmt >= 5000.0;
    }

    public void setOverdraftProtAccount(SavingsAccount overdraftProtAccount) {
        this.overdraftProtAccount = overdraftProtAccount;
        overdraftProtAccount.toggleOverdraftBackup();
    }

    public SavingsAccount getOverdraftProtAccount() {
        return overdraftProtAccount;
    }
}