package Account_Classes;
import java.util.List;
import java.util.UUID;

public class CheckingsAccount extends Account{
    SavingsAccount overdraftProtAccount;
    List<Integer> stopPayments;
    SavingsAccount interestAccount;
    private Type type;
    double txFee = 0.75;
    double monthlyFee = 1.25;
    double minimumBalance  = 5000.0;
    private double interestRate;
    enum Type{
        ThatsMyBank,
        GoldDiamond
    }

    public CheckingsAccount(SavingsAccount overdraftAccount, Type type, double balance){
        this.accountNumber = UUID.randomUUID().toString();
        this.type = type;
        this.overdraftProtAccount = overdraftAccount;
        this.deposit(balance);
    }
    public CheckingsAccount(SavingsAccount overdraftAccount, double balance){
        this.accountNumber = UUID.randomUUID().toString();
        this.type = Type.ThatsMyBank;
        this.overdraftProtAccount = overdraftAccount;
        this.deposit(balance);
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

    public void transfer(Account account, double amount){
        this.withdraw(amount);
        if (type == Type.ThatsMyBank || this.getBalance() < minimumBalance)
            this.withdraw(txFee);
        account.deposit(amount);
        this.handleOverdraft();
        System.out.println("Transfers " + amount + " to account " + account.accountNumber);
    }

    public void makeMonthlyTransfer(Account account, double amount){
        this.withdraw(amount);
        if (type == Type.ThatsMyBank || this.getBalance() < minimumBalance)
            this.withdraw(monthlyFee);
        account.deposit(amount);
        this.handleOverdraft();
        System.out.println("Monthly Transfer " + amount + " made to account " + account.accountNumber);
    } // CS: Probably a better way to do this, by just calling transfer and changing the fee.

    public void setInterestAccount(SavingsAccount account){
        this.interestAccount = account;
    }
    public void applyInterest(){
        if (type == Type.GoldDiamond){
            this.interestRate = 0.5 * interestAccount.getBalance();
        } else System.out.println("Wrong account Type");
    }

    public void setOverdraftProtAccount(SavingsAccount overdraftProtAccount) {
        this.overdraftProtAccount = overdraftProtAccount;
        overdraftProtAccount.toggleOverdraftBackup();
    }

    public SavingsAccount getOverdraftProtAccount() {
        return overdraftProtAccount;
    }
}