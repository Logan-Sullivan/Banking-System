package Loan_Classes;

import Utils.Transaction;

import java.time.LocalDate;

public abstract class TermBasedLoan extends Loan{
    int term;
    double monthlyPayment;
    double lateFee = 75.00;

    /**
     * Generates total repayment amount and monthly payment
     * */
    public void calculateLoanRepayment(){
        //monthly payment formula is | Principle * interest *(1 + interest)^term / (1+interest)^term - 1
        double monthlyRate = ((interest_rate/12)/100);
        this.monthlyPayment = (
                ( (principal*monthlyRate) * ( Math.pow(1+monthlyRate,this.term) ) ) )
                /( 1+( Math.pow(1+monthlyRate,this.term)-1 ) );

        this.balance = monthlyPayment*this.term;
    }

    public void flagIfMissed(){
        this.isProblemAccount = true;
    }

    public void disableFlag(){
        this.isProblemAccount = false;
    }

    public void applyLateFee(){
        this.balance = balance + lateFee;
    }


}
