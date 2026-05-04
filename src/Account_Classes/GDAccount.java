package tests.Account_Classes;

import java.time.LocalDate;

public class GDAccount extends CheckingsAccount {
    SavingsAccount interestAccount;
    double minimumBalance  = 5000.0;
    private double interestRate;
    public boolean dailyRateFlexible;

    public GDAccount(SavingsAccount overdraftAccount, double balance, boolean flexibleRate) {
        super(overdraftAccount, balance);
        this.dailyRateFlexible = flexibleRate;
    }

    public GDAccount(String id, SavingsAccount overdraftAccount, double balance, boolean flexibleRate) {
        super(id, overdraftAccount, balance);
        this.dailyRateFlexible = flexibleRate;
    }

    public void setInterestAccount(SavingsAccount account){
        this.interestAccount = account;
    }

    public void applyInterest(){
        this.interestRate = 0.5 * interestAccount.getBalance();
    } // CS: Unsure if this is supposed to be a += or otherwise cause it says changes daily, but I assume it's called daily

    public void updateTime(LocalDate currentDate, int daysPassed){
        //Currently the complete functionality of the interest calculation is not implemented. When it does, this function will need to be updated
        applyInterest();
        if(currentDate.getDayOfMonth() == currentDate.lengthOfMonth()){
            //Needs to have the interest get sent to the account
        }
    }

    public void checkMinBalance(){

    } // CS: Sorry not a clue what this is for? is it just if balance is lower than minimum? or returning minimum?

    @Override // Pointed to nothing cause interest is separated
    // should've just changed it to overdraft, but that'd cause more problems.
    public SavingsAccount getOverdraftProtAccount() {
        return interestAccount;
    }

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