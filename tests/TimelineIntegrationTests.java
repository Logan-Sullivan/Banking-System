package tests;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Period;

import org.junit.Test;

import Account_Classes.SavingsAccount;
import Loan_Classes.CreditCard;
import Loan_Classes.MortgageLoan;
import Loan_Classes.ShortTermLoan;
import Utils.Timeline;

public class TimelineIntegrationTests {
    //This test checks that the interest increments correctly to a savings account
    @Test
    public void testIncrementingInterest(){
        Timeline test = new Timeline(LocalDate.of(2026, 4, 30));
        SavingsAccount testAccount = new SavingsAccount(5.0, "30", false, 1000);
        test.addServices(testAccount);
        test.advanceTime(30);
        Double expectedValue = 1004.1095890410959;
        assertEquals(expectedValue, (Double)testAccount.getBalance());
    }
    //This test checks that loans increment correctly for the payments
    @Test
    public void testLoanDateIncrement(){
        Timeline test = new Timeline(LocalDate.of(2028, 5, 10));
        LocalDate time = LocalDate.of(2028, 5, 10);
        ShortTermLoan testLoan = new ShortTermLoan(5, 1000, Period.ofDays(10), test.getLastUpdatedDate());
        test.addServices(testLoan);
        test.advanceTime(30);
        Double expectedValue = 100.0;
        //Need to add assert
    }
    //This test checks that the credit card is billed for the correct amount
    @Test
    public void testCreditCardBill(){
        Timeline test = new Timeline(LocalDate.of(2028, 4, 21));
        CreditCard testCard = new CreditCard(100, 10, "none", false, 450.0, test.getLastUpdatedDate());
        test.addServices(testCard);
        test.advanceTime(14);
        Double expectedValue = 33.333333333333333;
        assertEquals(expectedValue, testCard.getFinanceCharge());
    }
    //This test checks that loans go overdue when they are supposed to
    @Test
    public void testOverdueLoan(){
        Timeline test = new Timeline(LocalDate.of(2015, 6, 10));
        MortgageLoan testLoan = new MortgageLoan(15, 8, 5000, test.getLastUpdatedDate());
        test.addServices(testLoan);
        test.advanceTime(31);
        assertTrue(testLoan.getIsProblemAccount());
    }
}
