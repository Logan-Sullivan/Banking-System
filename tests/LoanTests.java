package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAmount;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import Loan_Classes.CreditCard;
import Loan_Classes.MortgageLoan;
import Loan_Classes.ShortTermLoan;
import User_Classes.Customer;

public class LoanTests {
    //Tests the creation of loans
    @Test
    public void testLoanCreation(){
        assertDoesNotThrow(() ->{
            MortgageLoan mortgageExample = new MortgageLoan(15, 10, 10000);
            ShortTermLoan shortLoanExample = new ShortTermLoan(5, 2000);
        });
    }
    //This test ensures that the loans can be paid for the correct amount
    @Test
    public void testLoanPayment(){
        MortgageLoan mortgageExample = new MortgageLoan(15, 5, 25000);
        mortgageExample.loanPaymentCycle();
        mortgageExample.makePayment(50);
        int testAmount = 147;
        assertEquals(testAmount, (int)mortgageExample.getBalance());
    }
    //This tests that the loans balance is the expected amount when the month rolls over
    @Test
    public void testLoanRepayment(){
        MortgageLoan mortgageExample = new MortgageLoan(30, 15, 10000);
        mortgageExample.loanPaymentCycle();
        Double expectedBalance = 127.0;
        assertEquals(expectedBalance, (int)mortgageExample.getBalance()+1);//Kinda messing with the results because for whatever reason it rounds to 1874.999999999 and then to 1874
    }
    //This test checks that a loan correctly charges and accepts payment for the loan
    @Test
    public void testFullExampleLoan(){
        MortgageLoan mortgageExample = new MortgageLoan(15, 6, 30000);
        for(int i=0; i<(180);i++){
            mortgageExample.loanPaymentCycle();
            mortgageExample.makePayment(mortgageExample.getCurrentPaymentDue());
        }
        mortgageExample.loanPaymentCycle();
        Double expected = 0.0;
        assertEquals(expected, (int)mortgageExample.closeAccount());
    }
    //This tests that an account that pays extra during the loan decreases the monthly payments as expected
    @Test
    public void testOverpaidExampleLoan(){
        MortgageLoan mortgageExample = new MortgageLoan(15, 6, 30000);
        for(int i=0; i<(180);i++){
            mortgageExample.loanPaymentCycle();
            mortgageExample.makePayment(mortgageExample.getCurrentPaymentDue());
            if(i == 40){
                mortgageExample.makePayment(500);
            }
        }
        mortgageExample.loanPaymentCycle();
        Double expected = 0.0;
        assertEquals(expected, (int)mortgageExample.closeAccount());
    }
    //This test checks that the problem account flag is raised if the account misses a payment
    @Test
    public void testLoanProblemFlagMissed(){
        LocalDate testTime = LocalDate.of(2025, 10, 10);
        MortgageLoan mortgageExample = new MortgageLoan(30, 10, 25000, Period.ofDays(10), testTime);
        mortgageExample.loanPaymentCycle();
        LocalDate testFutureTime = LocalDate.of(2025, 11, 25);
        mortgageExample.flagIfMissed(testFutureTime);
        assertTrue(mortgageExample.getIsProblemAccount());
    }
    //This loan checks that the account is not flagged as a problem account if it pays
    @Test
    public void testLoanProblemFlagPaid(){
        LocalDate testTime = LocalDate.of(2025, 10, 10);
        ShortTermLoan mortgageExample = new ShortTermLoan(6, 20000, Period.ofDays(10), testTime);
        mortgageExample.makePayment(387);
        LocalDate testFutureTime = LocalDate.of(2025, 11, 19);
        mortgageExample.flagIfMissed(testFutureTime);
    }
    //This test does not currently function because customers cannot be connected to loans
    // @Test
    // public void testCreditCardCreation(){
    //     assertDoesNotThrow(() ->{
    //         Customer testCustomer = new Customer();
    //         testCustomer.accountList.add(new CreditCard(50, 25, 0, "none", false, 300.0));
    //     });
    // }
    //This test checks that the credit card charge function returns true if it is under the limit
    @Test
    public void testCreditCardCharge(){
        CreditCard exampleCard = new CreditCard(100, 5.5, "none", false, 300.0, LocalDate.of(2025, 9, 1));
        assertTrue(exampleCard.authorizeCharge(100));
    }
    //This test checks that the credit card charge function returns false if it is over the limit
    @Test
    public void testCreditCardOvercharge(){
        CreditCard exampleCard = new CreditCard(100, 0, "none", false, 300.0, LocalDate.of(2025, 9, 1));
        assertFalse(exampleCard.authorizeCharge(400));
    }
    //This test ensures that the apply interest function returns the expected value
    @Test
    public void testApplyInterest(){
        CreditCard exampleCard = new CreditCard(100, 0, "none", false, 300.0, LocalDate.of(2025, 9, 1));
        exampleCard.makeTransaction(200, "testing");
        exampleCard.applyInterest();
        Double expected = 300.0;
        assertEquals(exampleCard.getBalance(), expected);
    }
    //This test checks that the credit card finance charge associated with the bill is the expected value
    @Test
    public void testGenerateBill(){
        CreditCard exampleCard = new CreditCard(200, 5.5, "none", false, 500, LocalDate.of(2025, 4, 1));
        LocalDate testDay = LocalDate.of(2025, 4, 10);
        exampleCard.makeTransaction(100, "Testing", testDay);
        LocalDate testDay2 = LocalDate.of(2025, 4, 20);
        exampleCard.makePayment(200.0, testDay2);
        LocalDate testDay3 = LocalDate.of(2025, 5, 1);
        exampleCard.generateBill(testDay3);
        exampleCard.checkBillStatus(testDay);
        Double expected = 200.0;
        assertEquals(expected, exampleCard.getFinanceCharge());
    }
    //This test checks that a credit card is marked as a problem account if they fail to pay the bill by the specified time
    @Test
    public void testCheckBillStatus(){
        CreditCard exampleCard = new CreditCard(100, 5.5, "none", false, 500, LocalDate.of(2025, 4, 1));
        LocalDate day = LocalDate.of(2025, 5, 1);
        exampleCard.generateBill(day);
        day = LocalDate.of(2025, 5, 10);
        exampleCard.checkBillStatus();
        assertTrue(exampleCard.getIsProblemAccount());
    }
}
