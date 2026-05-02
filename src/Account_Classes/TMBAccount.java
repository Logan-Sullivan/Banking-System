package Account_Classes;

import java.time.LocalDate;

public class TMBAccount extends CheckingsAccount {
    double tsFee = 0.75;
    double monthlyTransferFee = 1.25;
    public int overdraftIdentifier = -1;

    public TMBAccount(SavingsAccount overdraftAccount, double balance) {
        super(overdraftAccount, balance);
    }

    public TMBAccount(String id, SavingsAccount overdraftAccount, double balance) {
        super(id, overdraftAccount, balance);
    }

    public void updateTime(LocalDate currentTime, int daysPassed){
        //There is no daily changes for the TMB accounts, so nothing to do here
    }

    @Override
    public void transfer(Account account, double amount){
        this.withdraw(tsFee);
        super.transfer(account, amount);
    }
    @Override
    public void makeMonthlyTransfer(Account account, double amount){
        this.withdraw(monthlyTransferFee);
        super.makeMonthlyTransfer(account, amount);
    } // CS: This sucks.
}