package Account_Classes;

public class TMBAccount extends CheckingsAccount {
    double tsFee = 0.75;
    double monthlyTransferFee = 1.25;
    public int overdraftIdentifier = -1;

    public TMBAccount(SavingsAccount overdraftAccount, double balance) {
        super(overdraftAccount, balance);
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