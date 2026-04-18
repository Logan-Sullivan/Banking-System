package Loan_Classes;

import java.time.LocalDate;

public class MortgageLoan extends TermBasedLoan{

    public MortgageLoan(int term,double interestRate,double principle){
        if(term == 15 && term == 30){//Terms can only be 15 or 30 years for mortgage
            this.term = term;
        }else{
            System.out.println("Term is not of a valid duration");
            this.term = 15;//Default to 15
        }
        this.interest_rate = interestRate;
        this.principal = principle;
        this.dueDate = LocalDate.now().plusMonths(1);//Loans are paid monthly
        this.calculateLoanRepayment();
    }

}
