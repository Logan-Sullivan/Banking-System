package Loan_Classes;

import Utils.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreditCard extends Loan {
    double creditLimit;
    ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    double averageDue;
    double financeCharge = 0.0;

    public CreditCard(double currentPaymentDue, double interest_rate, String loanStatus, boolean isProblemAccount,double creditLimit) {
        this.currentPaymentDue = currentPaymentDue;
        this.interest_rate = interest_rate;
        LoanStatus = loanStatus;
        this.isProblemAccount = isProblemAccount;
        this.creditLimit = creditLimit;
        this.dueDate = LocalDate.now().plusMonths(1).withDayOfMonth(10);
        this.averageDue = currentPaymentDue;
    }


    /**
     * checks if charge being made would exceed credit limit if it does return false otherwise return true
     * */
    public boolean authorizeCharge(double Charge){
        if(Charge + this.currentPaymentDue>this.creditLimit){
            System.out.println("Insufficient funds!");
            return false;
        } else return true;
    }

    /**
     * This function checks if it is the first day of the month. If so, it checks if the credit card has charges on it
     * If there is charges on the card, then it creates a bill of both the charges and a finance charge
     * The finance charge is calculated as the average amount of charge on the credit card for the month
     */
    public void generateBill(){
        if(LocalDate.now().getDayOfMonth()==1){
            if(currentPaymentDue>0){
                averageDue += currentPaymentDue * (LocalDate.now().toEpochDay()-dateSinceLastPayment.toEpochDay());
                //Now for a bit of a monster of a line: this calculates the finance charge, which is the average amount of charge of the credit card for the month
                //This line takes the total amounts over all of the days and divides it by the number of days of the previous month
                financeCharge = averageDue/(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 1).getMonth().length(LocalDate.now().isLeapYear()));
                currentPaymentDue += financeCharge;
            }else{
                averageDue = 0.0;
            }
        }else{
            System.out.println("Is not the beginning of the month!");
        }
    }

    /**
     * This function checks if the bill has not been paid by the 10th of the month (Or later if someone forgets)
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
        averageDue += currentPaymentDue * (LocalDate.now().toEpochDay()-dateSinceLastPayment.toEpochDay());
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
     * First authorizes charge then generates a transaction to add to our transaction list
     * */
    public void makeTransaction(double amount,String TransactionDesc){
         if (authorizeCharge(amount)){
            this.currentPaymentDue = currentPaymentDue + amount;
            this.transactions.add(new Transaction(amount,transactions.size(),TransactionDesc));
            System.out.println("New Balance: "+this.currentPaymentDue);
         }
    }
}
