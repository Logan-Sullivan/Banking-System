package Loan_Classes;

import java.time.LocalDate;
import java.time.Period;


/**
 * This class is for mortgage loans. These are term based loans, and funciton as such with no modifications
 */
public class MortgageLoan extends TermBasedLoan{

    //The main difference between these constructors is that they take the due date interval and current date as arguments. If none is provided, they default
    //This is to let us test/demonstrate the system while also have functionality for a real world environment

    public MortgageLoan(int term,double interestRate,double principle, Period dueDateInterval, LocalDate currentDate){
        if(term == 15 || term == 30){//Terms can only be 15 or 30 years for mortgage
            this.term = term;
        }else{
            System.out.println("Term is not of a valid duration");
            this.term = 15;//Default to 15
        }
        this.termMonthsLeft = term*12;
        this.interest_rate = interestRate;
        this.principal = principle;
        this.dueDateInterval = dueDateInterval;
        this.loanRepaymentDate = currentDate;
        this.paymentDueDate = loanRepaymentDate.plus(dueDateInterval);
        this.calculateLoanRepayment();
    }

    public MortgageLoan(int term,double interestRate,double principle, Period dueDateInterval){
        if(term == 15 || term == 30){//Terms can only be 15 or 30 years for mortgage
            this.term = term;
        }else{
            System.out.println("Term is not of a valid duration");
            this.term = 15;//Default to 15
        }
        this.termMonthsLeft = term*12;
        this.interest_rate = interestRate;
        this.principal = principle;
        this.dueDateInterval = dueDateInterval;
        this.loanRepaymentDate = LocalDate.now();
        this.paymentDueDate = loanRepaymentDate.plus(dueDateInterval);
        this.calculateLoanRepayment();
    }

    public MortgageLoan(int term,double interestRate,double principle, LocalDate currentDate){
        if(term == 15 || term == 30){//Terms can only be 15 or 30 years for mortgage
            this.term = term;
        }else{
            System.out.println("Term is not of a valid duration");
            this.term = 15;//Default to 15
        }
        this.termMonthsLeft = term*12;
        this.interest_rate = interestRate;
        this.principal = principle;
        this.dueDateInterval = Period.ofDays(10);
        this.loanRepaymentDate = currentDate;
        this.paymentDueDate = loanRepaymentDate.plus(dueDateInterval);
        this.calculateLoanRepayment();
    }

    public MortgageLoan(int term,double interestRate,double principle){
        if(term == 15 || term == 30){//Terms can only be 15 or 30 years for mortgage
            this.term = term;
        }else{
            System.out.println("Term is not of a valid duration");
            this.term = 15;//Default to 15
        }
        this.termMonthsLeft = term*12;
        this.interest_rate = interestRate;
        this.principal = principle;
        this.dueDateInterval = Period.ofDays(10);
        this.loanRepaymentDate = LocalDate.now();
        this.paymentDueDate = loanRepaymentDate.plus(dueDateInterval);
        this.calculateLoanRepayment();
    }
    
    public MortgageLoan(String id, int term,double interestRate,double principle, Period dueDateInterval, LocalDate currentDate){
        if(term == 15 || term == 30){//Terms can only be 15 or 30 years for mortgage
            this.term = term;
        }else{
            System.out.println("Term is not of a valid duration");
            this.term = 15;//Default to 15
        }
        this.id = id;
        this.termMonthsLeft = term*12;
        this.interest_rate = interestRate;
        this.principal = principle;
        this.dueDateInterval = dueDateInterval;
        this.loanRepaymentDate = currentDate;
        this.calculateLoanRepayment();
    }

    public void updateTime(LocalDate currentDate, int days){
        if(currentDate.compareTo(loanRepaymentDate) == 0){
            flagIfMissed(currentDate);
            loanPaymentCycle();
        }
        if(currentDate.compareTo(paymentDueDate) == 0){
            applyLateFee(currentDate);
        }
    }
}
