package Loan_Classes;

import java.time.LocalDate;

public abstract class Loan {
    double principal,interest_rate,balance;
    boolean isProblemAccount = false;
    String LoanStatus;
    LocalDate dueDate;

    public void makePayment(double amount){
        this.balance = this.balance - amount;
        System.out.println("Balance is now " + this.balance);
    }
    public double getBalance(){return balance;}
}
