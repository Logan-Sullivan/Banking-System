package Account_Classes;

import java.util.Date;

public class CDAccount extends SavingsAccount { // Will change this all if need be
    public double fixedRate;
    public Date maturityDate;
    public double earlyPenalty;

    public CDAccount(double intrestRate, String compoundFreq, boolean overdraftBackup,
                     double balance, double fixedRate, Date maturityDate, double earlyPenalty){
        super(intrestRate, compoundFreq, false, balance);
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