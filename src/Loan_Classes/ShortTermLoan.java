package Loan_Classes;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAmount;


/**
 * This class is for short term loans. These are term based loans, and funciton as such with no modifications
 */
public class ShortTermLoan extends TermBasedLoan{

    //The main difference between these constructors is that they take the due date interval and current date as arguments. If none is provided, they default
    //This is to let us test/demonstrate the system while also have functionality for a real world environment

    public ShortTermLoan(double interestRate,double principle, Period dueDateInterval, LocalDate currentDate){
        this.term = 5;
        termMonthsLeft = term*12;
        this.interest_rate = interestRate;
        this.principal = principle;
        this.dueDateInterval = dueDateInterval;
        this.loanRepaymentDate = currentDate;
        this.paymentDueDate = loanRepaymentDate.plus(dueDateInterval);
        this.calculateLoanRepayment();
    }

    public ShortTermLoan(double interestRate,double principle, Period dueDateInterval){
        this.term = 5;
        termMonthsLeft = term*12;
        this.interest_rate = interestRate;
        this.principal = principle;
        this.dueDateInterval = dueDateInterval;
        this.loanRepaymentDate = LocalDate.now();
        this.paymentDueDate = loanRepaymentDate.plus(dueDateInterval);//Loans are paid at a set interval
        this.calculateLoanRepayment();
    }

    public ShortTermLoan(double interestRate,double principle, LocalDate currentDate){
        this.term = 5;
        termMonthsLeft = term*12;
        this.interest_rate = interestRate;
        this.principal = principle;
        this.dueDateInterval = Period.ofDays(10);
        this.loanRepaymentDate = currentDate;
        this.paymentDueDate = loanRepaymentDate.plus(dueDateInterval);//Loans are paid at a set interval
        this.calculateLoanRepayment();
    }

    public ShortTermLoan(double interestRate,double principle){
        this.term = 5;
        termMonthsLeft = term*12;
        this.interest_rate = interestRate;
        this.principal = principle;
        this.dueDateInterval = Period.ofDays(10);
        this.loanRepaymentDate = LocalDate.now();
        this.paymentDueDate = loanRepaymentDate.plus(dueDateInterval);//at default they are paid monthly
        this.calculateLoanRepayment();
    }

    public void updateTime(LocalDate currentDate, int days){
        if(currentDate == loanRepaymentDate || (loanRepaymentDate.lengthOfMonth() > currentDate.lengthOfMonth() && currentDate.getDayOfMonth() == currentDate.lengthOfMonth())){
            flagIfMissed(currentDate);
            loanPaymentCycle();
        }
        if(currentDate == paymentDueDate || (paymentDueDate.lengthOfMonth() > currentDate.lengthOfMonth() && currentDate.getDayOfMonth() == currentDate.lengthOfMonth())){
            applyLateFee(currentDate);
        }
    }
}
