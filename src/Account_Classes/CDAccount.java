package Account_Classes;

import java.time.LocalDate;

public class CDAccount extends Account {
    public double fixedRate;
    public LocalDate maturityDate;
    public double earlyPenalty;

    public CDAccount(double balance, double fixedRate, LocalDate maturityDate, double earlyPenalty){
        this.deposit(balance);
        this.fixedRate = fixedRate;
        this.maturityDate = maturityDate;
        this.earlyPenalty = earlyPenalty;
    } // CS: awful.
    public CDAccount(String id, double balance, double fixedRate, LocalDate maturityDate, double earlyPenalty){
        this.accountNumber = id;
        this.deposit(balance);
        this.fixedRate = fixedRate;
        this.maturityDate = maturityDate;
        this.earlyPenalty = earlyPenalty;
    }

    public void rollover(){
        double rate = fixedRate;
        if (rate > 1.0) {
            rate = rate / 100.0;
        }
        if (rate > 0) {
            double interest = this.getBalance() * rate;
            this.deposit(interest);
        }
        if (maturityDate != null) {
            maturityDate = maturityDate.plusMonths(12);
        }
    }

    
    public void updateTime(LocalDate currentDate, int daysPassed){
        if (currentDate == null || maturityDate == null) {
            return;
        }
        LocalDate nextDate = currentDate.plusDays(1);
        while (!nextDate.isBefore(maturityDate)) {
            rollover();
        }
    }

    public void earlyWithdraw(){
        if (earlyPenalty <= 0) {
            return;
        }
        if (earlyPenalty >= this.getBalance()) {
            super.withdraw(this.getBalance());
            return;
        }
        super.withdraw(earlyPenalty);
    }

    @Override
    public void withdraw(double amount){
        if (amount <= 0) {
            System.out.println("Denied, Non-value");
            return;
        }
        if (maturityDate != null && LocalDate.now().isBefore(maturityDate)) {
            if (amount + earlyPenalty > getBalance()) {
                System.out.println("Insufficient funds");
                return;
            }
            earlyWithdraw();
            super.withdraw(amount);
            return;
        }
        if (amount > getBalance()) {
            System.out.println("Insufficient funds");
            return;
        }
        super.withdraw(amount);
    } // CS: going off of "Ability to credit any account besides saving CDs (remove cash from account)"
} // and assuming that earlyWithdraw will be used instead
