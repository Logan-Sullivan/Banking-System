package Loan_Classes;

import Utils.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;


public class CreditCard extends Loan {
    double creditLimit;
    ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    double averageDue;
    double financeCharge = 0.0;

    //I'm not going to put the starting balance as the principal, because the credit card balance is expected to flucuate as it is used
    public CreditCard(double currentPaymentDue, double interest_rate, String loanStatus, boolean isProblemAccount,double creditLimit, LocalDate currentDate) {
        this.currentPaymentDue = currentPaymentDue;
        this.interest_rate = interest_rate;
        LoanStatus = loanStatus;
        this.isProblemAccount = isProblemAccount;
        this.creditLimit = creditLimit;
        this.loanRepaymentDate = currentDate.plusMonths(1).withDayOfMonth(10);//This isn't actually used because credit cards have set dates that actions occur
        this.averageDue = currentPaymentDue;
        this.dateSinceLastBalanceChange = currentDate;
    }

    public void updateTime(LocalDate currentDate, int days){
        generateBill(currentDate);
        checkBillStatus(currentDate);

    }


    /**
     * checks if charge being made would exceed credit limit if it does return false otherwise return true
     * */
    public boolean authorizeCharge(double charge){
        if(charge + this.currentPaymentDue>this.creditLimit){
            System.out.println("Insufficient funds!");
            return false;
        } else return true;
    }

    /**
     * This function checks if it is the first day of the month. If so, it checks if the credit card has charges on it
     * If there is charges on the card, then it creates a bill of both the charges and a finance charge
     * The finance charge is calculated as the average amount of charge on the credit card for the month
     */
    public void generateBill(LocalDate currentDay){
        if(currentDay.getDayOfMonth()==1){
            if(currentPaymentDue>0 && dateSinceLastBalanceChange != null){
                averageDue += currentPaymentDue * (currentDay.toEpochDay()-dateSinceLastBalanceChange.toEpochDay()-1);
                //Now for a bit of a monster of a line: this calculates the finance charge, which is the average amount of charge of the credit card for the month
                //This line takes the total amounts over all of the days and divides it by the number of days of the previous month
                int numDaysInLastMonth = (LocalDate.of(currentDay.getYear(), currentDay.minusMonths(1).getMonth(), 1).getMonth().length(currentDay.isLeapYear()));
                financeCharge = averageDue/numDaysInLastMonth;
            }else{
                averageDue = 0.0;
            }
        }else{
            System.out.println("Is not the beginning of the month!");
        }
    }

    /**
     * This function checks if the bill has not been paid by the 10th of this month or later. This funciton is used for most of the system
     * If so, it marks this card as a problem account
     * @param currentDate the current date
     */
    public void checkBillStatus(LocalDate currentDate){
        if(currentDate.getDayOfMonth()==10 && financeCharge > 0){
            isProblemAccount = true;
        }
    }
    /**
     * This function checks if the bill has not been paid by the 10th of the month or later
     * If so, it marks this card as a problem account
     */
    public void checkBillStatus(){
        if(LocalDate.now().getDayOfMonth()>=10 && financeCharge > 0){
            isProblemAccount = true;
        }
    }
    /**
     * applies interest from our apr turned into a monthly interest rate
     * */
    public void applyInterest(){
     this.currentPaymentDue = this.currentPaymentDue +(this.currentPaymentDue * ((this.interest_rate/100)/12));
    }

    /**
     * makes a transaction for our payment
     * */
    @Override
    public double makePayment(double amount) {
        this.transactions.add(new Transaction(amount, transactions.size(), "Balance Payment"));
        averageDue += currentPaymentDue * (LocalDate.now().toEpochDay()-dateSinceLastBalanceChange.toEpochDay());
        dateSinceLastBalanceChange = LocalDate.now();
        if(amount>financeCharge){
            amount -= financeCharge;
            financeCharge = 0;
            return super.makePayment(amount);
        }else{
            financeCharge -= amount;
            return 0;
        }
    }
    /**
     * Makes a transaction for our payment. This function is for testing/presentation, as it treats it as if it is the passed day
     */
    public double makePayment(double amount, LocalDate currentDate) {
        this.transactions.add(new Transaction(amount, transactions.size(), "Balance Payment"));
        averageDue += currentPaymentDue * (currentDate.toEpochDay()-dateSinceLastBalanceChange.toEpochDay());
        dateSinceLastBalanceChange = currentDate;
        if(amount>financeCharge){
            amount -= financeCharge;
            financeCharge = 0;
            return super.makePayment(amount, currentDate);
        }else{
            financeCharge -= amount;
            return 0;
        }
    }
    /**
     * This function makes a transaction for the card being used for a payment
     * @param amount the amount of money to be charged to the card
     * @param TransactionDesc the description for the transaction
     */
    public void makeTransaction(double amount,String TransactionDesc){
         if (authorizeCharge(amount)){
            averageDue += currentPaymentDue * (LocalDate.now().toEpochDay()-dateSinceLastBalanceChange.toEpochDay());
            dateSinceLastBalanceChange = LocalDate.now();
            this.currentPaymentDue = currentPaymentDue + amount;
            this.transactions.add(new Transaction(amount,transactions.size(),TransactionDesc));
            System.out.println("New Balance: "+this.currentPaymentDue);
         }
    }
    /**
     * See above, except the current date is passed to the function. Primarily for testing/presentation
     * @param amount the amount of money charged to the card
     * @param TransactionDesc the description for the transaction
     * @param transactionDate the date that this is occuring
     */
    public void makeTransaction(double amount,String TransactionDesc, LocalDate transactionDate){
         if (authorizeCharge(amount)){
            averageDue += currentPaymentDue * (transactionDate.toEpochDay()-dateSinceLastBalanceChange.toEpochDay());
            dateSinceLastBalanceChange = transactionDate;
            this.currentPaymentDue = currentPaymentDue + amount;
            this.transactions.add(new Transaction(amount,transactions.size(),TransactionDesc));
            System.out.println("New Balance: "+this.currentPaymentDue);
         }
    }
    public double getFinanceCharge(){
        return financeCharge;
    }
}
