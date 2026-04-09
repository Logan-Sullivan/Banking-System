package Loan_Classes;

import java.time.LocalDate;

public class MortgageLoan extends TermBasedLoan{

    public MortgageLoan(int term,double interestRate,double principle){
        this.term = term;
        this.interest_rate = interestRate;
        this.principal = principle;
        this.dueDate = LocalDate.now().plusMonths(term);
        this.calculateLoanRepayment();
    }

}
