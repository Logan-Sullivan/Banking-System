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

        this.currentPaymentDue = monthlyPayment*this.term;
        this.principal = principal - (monthlyPayment-monthlyRate);//This is the leftover amount from the payment
    }

    public void flagIfMissed(){
        this.isProblemAccount = true;
    }

    public void disableFlag(){
        this.isProblemAccount = false;
    }

    /**
     * This function applies a late fee if the loan has not been paid by its due date
     */
    public void applyLateFee(){
        if(LocalDate.now().compareTo(dueDate)>0){
            this.currentPaymentDue = currentPaymentDue + lateFee;
        }
    }


}
