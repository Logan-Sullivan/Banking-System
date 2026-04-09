package Loan_Classes;

import Utils.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreditCard extends Loan {
    double creditLimit;
    List<Transaction> transactions;
    public CreditCard(double principal, double interest_rate, double balance, String loanStatus, boolean isProblemAccount,double creditLimit) {
        this.principal = principal;
        this.interest_rate = interest_rate;
        this.balance = balance;
        LoanStatus = loanStatus;
        this.isProblemAccount = isProblemAccount;
        this.creditLimit = creditLimit;
        this.dueDate = LocalDate.now().plusMonths(1).withDayOfMonth(21);
    }


    /**
     * checks if charge being made would exceed credit limit if it does return false otherwise return true
     * */
    public boolean authorizeCharge(double Charge){
        if(Charge + this.balance>this.creditLimit){
            System.out.println("Insufficient funds!");
            return false;
        } else return true;
    }

    /**
     * applies interest from our apr turned into a monthly interest rate
     * */
    public void applyInterest(){
     this.balance = this.balance +(this.balance * ((this.interest_rate/100)/12));
    }

    /**
     * makes a transaction for our payment
     * */
    @Override
    public void makePayment(double amount) {
        super.makePayment(amount);
        this.transactions.add(new Transaction(amount, transactions.size(), "Balance Payment"));
    }
    /**
     * First authorizes charge then generates a transaction to add to our transaction list
     * */
    public void makeTransaction(double amount,String TransactionDesc){
         if (authorizeCharge(amount)){
             this.balance = balance + amount;
             this.transactions.add(new Transaction(amount,transactions.size(),TransactionDesc));
             System.out.println("New Balance: "+this.balance);
         }
    }
}
