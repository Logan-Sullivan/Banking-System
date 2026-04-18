package Loan_Classes;

import java.time.LocalDate;

public class ShortTermLoan extends TermBasedLoan{

    public ShortTermLoan(double interestRate,double principle){
        this.term = 5;
        this.interest_rate = interestRate;
        this.principal = principle;
        this.dueDate = LocalDate.now().plusMonths(1);//Loans are paid montly
        this.calculateLoanRepayment();
    }
}
