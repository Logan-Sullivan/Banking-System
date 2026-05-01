package Account_Classes;
import java.time.LocalDate;
import java.util.UUID;

public class SavingsAccount extends Account{
    double intrestRate;
    String compoundFreq;
    boolean overdraftBackup;


    //default constructor for testing
    public SavingsAccount(){
        this.accountNumber = UUID.randomUUID().toString();;
        this.intrestRate = .01+(99.99-.01)*Math.random();
        this.compoundFreq = "31";
        this.overdraftBackup = true;
        this.deposit((int)(Math.random()*1000));
    }

    /**
     * This function applies the interest for the specified number of days that have passed
     * @param currentTime the current time of the system
     * @param daysPassed the time passed between the previous time and the current time
     */
    public void updateTime(LocalDate currentTime, int daysPassed){
        applyDailyInterest();
    }

    /**
     *
     * @param intrestRate Rate of interest in percentage ie: 25.6 = %25.6 <br>expected value should be above 0% and below 100%<br>
     * @param compoundFreq Rate at which interest is accrued in days expected value 1-365<br>
     * @param overdraftBackup Weather or not account is used for overdraft bacup<br>
     * @param balance The balance of the savings account<br>
     */
    public SavingsAccount(double intrestRate, String compoundFreq, boolean overdraftBackup,double balance){
        this.accountNumber = UUID.randomUUID().toString();
        this.intrestRate = intrestRate;
        this.compoundFreq = compoundFreq;
        this.overdraftBackup = overdraftBackup;
        this.deposit(balance);
    }

    public void applyDailyInterest(){
        System.out.println("Daily interest applied: " + this.getBalance());
        this.deposit(this.getBalance()*(intrestRate/100));
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

    // Ike: getter for interest rate (CSV writing)
    public double getInterestRate(){
        return intrestRate;
    }

    // Ike: getter for compound frequency (CSV writing)
    public String getCompoundFreq(){
        return compoundFreq;
    }

    // Ike: getter for overdraft flag (CSV writing)
    public boolean isOverdraftBackup(){
        return overdraftBackup;
    }
}
