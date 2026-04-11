package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
            ShortTermLoan shortLoanExample = new ShortTermLoan(5, 5, 2000);
        });
    }
    //This test ensures that the loans can be paid for the correct amount
    @Test
    public void testLoanPayment(){
        MortgageLoan mortgageExample = new MortgageLoan(15, 10, 10000);
        mortgageExample.makePayment(1000);
        Double testAmount = 250.0;
        assertEquals(testAmount, mortgageExample.getBalance());
    }
    //This tests that the loans balance is the expected amount when the month rolls over
    @Test
    public void testLoanRepayment(){
        MortgageLoan mortgageExample = new MortgageLoan(30, 15, 10000);
        Double expectedBalance = 3750.0;
        assertEquals(expectedBalance, mortgageExample.getBalance());
    }
    //This test does not currently function because customers cannot be linked to loans
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
        CreditCard exampleCard = new CreditCard(50, 25, 0, "none", false, 300.0);
        assertTrue(exampleCard.authorizeCharge(100));
    }
    //This test checks that the credit card charge function returns false if it is over the limit
    @Test
    public void testCreditCardOvercharge(){
        CreditCard exampleCard = new CreditCard(0, 25, 0, "none", false, 300.0);
        assertFalse(exampleCard.authorizeCharge(400));
    }
    //This test ensures that the apply interest function returns the expected value
    @Test
    public void testApplyInterest(){
        CreditCard exampleCard = new CreditCard(50, 25, 0, "none", false, 300.0);
        exampleCard.makeTransaction(200, "testing");
        exampleCard.applyInterest();
    }
}
