package Account_Classes;

public class GDAccount extends CheckingsAccount {
    SavingsAccount interestAccount;
    double minimumBalance  = 5000.0;
    private double interestRate;
    public boolean dailyRateFlexible;

    public GDAccount(SavingsAccount overdraftAccount, double balance, boolean flexibleRate) {
        super(overdraftAccount, balance);
        this.dailyRateFlexible = flexibleRate;
    }

    public void setInterestAccount(SavingsAccount account){
        this.interestAccount = account;
    }

    public void applyInterest(){
        this.interestRate = 0.5 * interestAccount.getBalance();
    } // CS: Unsure if this is supposed to be a += or otherwise cause it says changes daily, but I assume it's called daily

    public void checkMinBalance(){

    } // CS: Sorry not a clue what this is for? is it just if balance is lower than minimum? or returning minimum?

    public void chargeBelowMinFee(double tsFee){
        if (this.getBalance() < minimumBalance) this.withdraw(tsFee);
    }
    @Override
    public void transfer(Account account, double amount){
        chargeBelowMinFee(0.75);
        super.transfer(account, amount);
    }
    @Override
    public void makeMonthlyTransfer(Account account, double amount){
        chargeBelowMinFee(1.25);
        super.makeMonthlyTransfer(account, amount);
    }
}