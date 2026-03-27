package src;

import java.util.UUID;

class SavingsAccount extends Account{
    double intrestRate;
    String compoundFreq;
    boolean overdraftBackup;

    public SavingsAccount(double intrestRate, String compoundFreq, boolean overdraftBackup,double balance){
        this.accountNumber = UUID.randomUUID().toString();
        this.intrestRate = intrestRate;
        this.compoundFreq = compoundFreq;
        this.overdraftBackup = overdraftBackup;
        this.deposit(balance);
    }

    public void applyDailyInterest(){
        System.out.println("Daily interest applied: " + this.getBalance());
        this.deposit(this.getBalance()*intrestRate);
        System.out.print(" → "+ this.getBalance() +"\n");
    }

    public void toggleOverdraftBackup(){
        this.overdraftBackup = !this.overdraftBackup; // toggles between true and false
        if (this.overdraftBackup) {
            System.out.println("Account set as backup");
        } else {
            System.out.println("Account set as non-backup");
        }
    }
}