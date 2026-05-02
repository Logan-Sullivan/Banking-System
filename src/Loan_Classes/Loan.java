package Loan_Classes;

import java.time.LocalDate;
import java.util.UUID;
import Utils.TimeService;

public abstract class Loan extends TimeService{
    public String id = UUID.randomUUID().toString();
    public double principal;
    public double interest_rate;
    double currentPaymentDue;
    boolean isProblemAccount = false;
    String LoanStatus;
    LocalDate loanRepaymentDate;
    LocalDate paymentDueDate;
    LocalDate dateNotifiedOfPayment;//Not sure what this would be used for in our program, but it was listed as a requirement in one of Pickett's documents
    LocalDate dateSinceLastBalanceChange;

    /**
     * This function makes a payment, first clearing the payment that is due at the end of the month, and if there is some left over it pays the the principal
     * If there is any leftover after the principal, it returns that amount (presumably to a user if at something like an ATM)
     * Otherwise it returns 0.0
     * it also sets the date that the account has changed to the current date
     * @param amount amount paid to the loan account
     * @return returns the amount leftover, if any
     */
    public double makePayment(double amount){
        if(amount > currentPaymentDue){
            amount -= currentPaymentDue;
            currentPaymentDue = 0;
            if(amount > principal){
                amount -= principal;
                principal = 0;
                dateSinceLastBalanceChange = LocalDate.now();
                return amount;
            }else{
                principal -= amount;
            }
        }else{
            currentPaymentDue -= amount;
        }
        dateSinceLastBalanceChange = LocalDate.now();
        System.out.println("Balance is now " + this.currentPaymentDue);
        return 0;
    }
    /**
     * This function works the same as the above, but the date is set by the parameters.
     * @param amount amount paid to the loan account
     * @param paymentDate the date that the payment took place
     * @return returns the amount leftover, if any
     */
    public double makePayment(double amount, LocalDate paymentDate){
        if(amount > currentPaymentDue){
            amount -= currentPaymentDue;
            currentPaymentDue = 0;
            if(amount > principal){
                amount -= principal;
                principal = 0;
                dateSinceLastBalanceChange = LocalDate.now();
                return amount;
            }else{
                principal -= amount;
            }
        }else{
            currentPaymentDue -= amount;
        }
        dateSinceLastBalanceChange = paymentDate;
        System.out.println("Balance is now " + this.currentPaymentDue);
        return 0;

    }
    public double closeAccount(){
        return currentPaymentDue+principal;
    }
    public double getBalance(){return currentPaymentDue;}
    public boolean getIsProblemAccount(){return isProblemAccount;}
}
