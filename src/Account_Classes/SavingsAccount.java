package Account_Classes;
import java.time.LocalDate;
import java.util.UUID;

public class SavingsAccount extends Account{
    double intrestRate;
    String compoundFreq;
    boolean overdraftBackup;
    int daysSinceInterest;


    //default constructor for testing
    public SavingsAccount(){
        this.accountNumber = UUID.randomUUID().toString();;
        this.intrestRate = .01+(99.99-.01)*Math.random();
        this.compoundFreq = "31";
        this.overdraftBackup = true;
        this.daysSinceInterest = 0;
        this.deposit((int)(Math.random()*1000));
    }

    /**
     * This function applies the interest for the specified number of days that have passed
     * @param currentTime the current time of the system
     * @param daysPassed the time passed between the previous time and the current time
     */
    public void updateTime(LocalDate currentTime, int daysPassed){
        int compoundDays = getCompoundDays();
        daysSinceInterest++;
        if (daysSinceInterest >= compoundDays) {
            applyInterestForPeriod(currentTime, compoundDays);
            daysSinceInterest = 0;
        }
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
        this.daysSinceInterest = 0;
        this.deposit(balance);
    }

    public SavingsAccount(String id, double intrestRate, String compoundFreq, boolean overdraftBackup,double balance){
        this.accountNumber = id;
        this.intrestRate = intrestRate;
        this.compoundFreq = compoundFreq;
        this.overdraftBackup = overdraftBackup;
        this.daysSinceInterest = 0;
        this.deposit(balance);
    }

    private int getCompoundDays(){
        if (compoundFreq == null) {
            return 1;
        }
        String value = compoundFreq.trim();
        if (value.isEmpty()) {
            return 1;
        }
        String lower = value.toLowerCase();
        if (lower.equals("daily")) {
            return 1;
        }
        if (lower.equals("weekly")) {
            return 7;
        }
        if (lower.equals("biweekly")) {
            return 14;
        }
        if (lower.equals("monthly")) {
            return 30;
        }
        try {
            int days = Integer.parseInt(value);
            if (days > 0) {
                return days;
            }
        } catch (Exception e) {
            return 1;
        }
        return 1;
    }

    public void applyDailyInterest(){
        System.out.println("Daily interest applied: " + this.getBalance());
        applyInterestForPeriod(LocalDate.now(), 1);
        System.out.print(" → "+ this.getBalance() +"\n");
    }

    private void applyInterestForPeriod(LocalDate currentTime, int periodDays){
        if (periodDays <= 0) {
            return;
        }
        int daysInYear = 365;
        if (currentTime != null && currentTime.isLeapYear()) {
            daysInYear = 366;
        }
        double annualRate = intrestRate / 100.0;
        double periodRate = annualRate * ((double) periodDays / daysInYear);
        if (periodRate <= 0) {
            return;
        }
        this.deposit(this.getBalance() * periodRate);
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
