package Loan_Classes;

import Utils.Transaction;

import java.time.LocalDate;
import java.time.Period;


/**
 * This class is for term based loans
 * These loans last for a set amount of time (unless the customer closes them early by paying the remaining principal) in years called terms
 * These terms have a monthly payment that is expected of the customer. The payment is calculated as follows: (monthly interest + principal amount)
 * As the loan is paid, the monthly interest decreases and more of the payment goes to the principal amount
 * So, the monthly amount is calculated to ensure that the loan terminates at the expected term
 */
public abstract class TermBasedLoan extends Loan{
    public int term;
    double monthlyPayment;
    int termMonthsLeft;
    double lateFee = 75.00;
    Period dueDateInterval;
    boolean lateFeeApplied;
    LocalDate dateCreated;

    /**
     * Generates the total repayment amount required for each monthly payment due date.
     * This function is used during the creation of the class to get the expected payment amount
     * additionally it is used whenever customers pay extra to account for the change in principal that will affect the expected payment for the future months
     * */
    public void calculateLoanRepayment(){
        //monthly payment formula is | Principle * interest *(1 + interest)^term / (1+interest)^term - 1
        double monthlyRate = ((interest_rate/12)/100);
        this.monthlyPayment = principal * (monthlyRate * Math.pow((1 + monthlyRate), (termMonthsLeft)))/((Math.pow(1+monthlyRate, (termMonthsLeft)))-1);
        lateFeeApplied = false;
        // double interestPayment = monthlyRate * (principal+currentPaymentDue);
        // this.currentPaymentDue = monthlyPayment;
        // paymentDueDate = loanRepaymentDate.plus(dueDateInterval);
        // loanRepaymentDate = loanRepaymentDate.plusMonths(1);
        // this.principal = principal - (monthlyPayment-interestPayment);//This is the leftover amount from the payment
    }

    /**
     * This function sets the current monthly expected payment, which is: (monthly interest) + (amount deducted from principal to terminate the loan on schedule)
     */
    public void loanPaymentCycle(){
        double monthlyRate = ((interest_rate/12)/100);
        double interestPayment = monthlyRate * (principal+currentPaymentDue);
        currentPaymentDue = monthlyPayment;
        paymentDueDate = loanRepaymentDate.plus(dueDateInterval);
        loanRepaymentDate = loanRepaymentDate.plusMonths(1);
        this.principal = principal - (monthlyPayment - interestPayment);
        lateFeeApplied = false;
        termMonthsLeft--;
    }

    /**
     * This function makes a payment to the accounts
     * if the customer is paying extra than expected, this function recalculates the monthly payments required to terminate the loan on time
     */
    @Override
    public double makePayment(double amount){
        double leftoverAmount;
        if(amount > currentPaymentDue){
            leftoverAmount = super.makePayment(amount);
            calculateLoanRepayment();
            return leftoverAmount;
        }else{
            leftoverAmount = super.makePayment(amount);
            if(principal <= 0){
                monthlyPayment = 0;
            }
            return leftoverAmount;
        }
    }

    /**
     * This function checks if there is unpaid payments due and it is the next loan payment cycle
     * If so, this account is marked as a problem account
     */
    public void flagIfMissed(){
        if(LocalDate.now().toEpochDay() >= loanRepaymentDate.toEpochDay()){
            if(currentPaymentDue > 0){
                this.isProblemAccount = true;
            }
        }
    }
    /**
     * This function performs the same as above, but with the current date passed as a parameter
     * To be used for presentation/test purposes
     * @param currentDate The current date
     */
    public void flagIfMissed(LocalDate currentDate){
        if(currentDate.toEpochDay() >= loanRepaymentDate.toEpochDay() && currentPaymentDue > 0){
            this.isProblemAccount = true;
        }
    }

    /**
     * This function clears the problem account flag for this account
     */
    public void disableFlag(){
        this.isProblemAccount = false;
    }

    /**
     * This function applies a late fee if the loan has not been paid by its due date
     */
    public void applyLateFee(){
        if(LocalDate.now().compareTo(paymentDueDate)>0 && !lateFeeApplied){
            this.currentPaymentDue = currentPaymentDue + lateFee;
            lateFeeApplied = true;
        }
    }
    /**
     * Performs the same as above, except with the "current" date passed to it
     * To be used for testing/presentation purposes
     * @param currentDate The current date
     */
    public void applyLateFee(LocalDate currentDate){
        if(currentDate.compareTo(paymentDueDate)==0 && !lateFeeApplied){
            this.currentPaymentDue = currentPaymentDue + lateFee;
            lateFeeApplied = true;
        }
    }
    public double getCurrentPaymentDue(){return currentPaymentDue;}

}
