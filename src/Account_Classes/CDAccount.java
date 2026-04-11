package Account_Classes;

import java.util.Date;

public class CDAccount extends Account {
    public double fixedRate;
    public Date maturityDate;
    public double earlyPenalty;

    public CDAccount(double balance, double fixedRate, Date maturityDate, double earlyPenalty){
        this.deposit(balance);
        this.fixedRate = fixedRate;
        this.maturityDate = maturityDate;
        this.earlyPenalty = earlyPenalty;
    } // CS: awful.

    public void rollover(){
        System.out.println("Account Rollover Notice");
    }

    public void earlyWithdraw(){

    }

    @Override
    public void withdraw(double amount){
        System.out.println("No");
    } // CS: going off of "Ability to credit any account besides saving CDs (remove cash from account)"
} // and assuming that earlyWithdraw will be used instead