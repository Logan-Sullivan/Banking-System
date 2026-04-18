package Loan_Classes;

import java.time.LocalDate;

public abstract class Loan {
    double principal,interest_rate,currentPaymentDue;
    boolean isProblemAccount = false;
    String LoanStatus;
    LocalDate dueDate;
    LocalDate dateNotifiedOfPayment;//Not sure what this would be used for in our program, but it was listed as a requirement in one of Pickett's documents
    LocalDate dateSinceLastPayment;

    /**
     * This function makes a payment, first clearing the payment that is due at the end of the month, and if there is some left over it pays the the principal
     * If there is any leftover after the principal, it returns that amount (presumably to a user if at something like an ATM)
     * Otherwise it returns 0.0
     * @param amount amount paid
     * @return returns the amount leftover, if any
     */
    public double makePayment(double amount){
        if(amount > currentPaymentDue){
            amount -= currentPaymentDue;
            currentPaymentDue = 0;
            if(amount > principal){
                amount -= principal;
                principal = 0;
                dateSinceLastPayment = LocalDate.now();
                return amount;
            }else{
                principal -= amount;
            }
        }else{
            currentPaymentDue -= amount;
        }
        dateSinceLastPayment = LocalDate.now();
        System.out.println("Balance is now " + this.currentPaymentDue);
        return 0;
        
    }
    public double getBalance(){return currentPaymentDue;}
}
