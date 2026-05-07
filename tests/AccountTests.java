package tests;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.*;
import org.junit.jupiter.api.BeforeEach;

import Account_Classes.CheckingsAccount;
import Account_Classes.SavingsAccount;
import Account_Classes.TMBAccount;
public class AccountTests {
    //Now we create some example accounts for an example customer for the tests
    User_Classes.Customer example = new User_Classes.Customer();
    TMBAccount exampleChecking=new TMBAccount(null, 1000);
    SavingsAccount exampleSavings=new SavingsAccount(0.25, "Daily", false, 2000);
    @BeforeEach
    public void setUpExampleCust(){
        example.accountList = new ArrayList<>();
        example.accountList.add(exampleChecking);
        example.accountList.add(exampleSavings);
    }
    //This test checks that the amount deposited into the account is correct
    @Test
    public void testDeposit(){
        exampleChecking.deposit(500);
        assertEquals(1500, exampleChecking.getBalance());
    }
    //This test checks that the amount withdrawn from the account is correct
    @Test
    public void testWithdraw(){
        exampleChecking.withdraw(1000);
        assertEquals(0, exampleChecking.getBalance());
    }
    //This test checks that setting an account as the overdraft protection for another occurs without errors
    @Test
    public void testSetOverdraftProtection(){
        assertDoesNotThrow(() ->{
            exampleChecking.setOverdraftProtAccount(exampleSavings);
        });
    }
    //This test checks that the transfer method transfers the correct amount to the correct account
    @Test
    public void testAccountFundTransfer(){
        exampleChecking.transfer(exampleSavings, 500);
        assertEquals(2500, exampleSavings.getBalance());
    }
    //This test ensures that the overdraft penalty is applied correctly from a withdraw into negative values
    @Test
    public void testOverdraftPenalty(){
        exampleChecking.withdraw(1100);
        exampleChecking.handleOverdraft();
        assertEquals(-125, exampleChecking.getBalance());
    }
    //This test checks that the overdraft protection moves the overdraft to the correct account
    @Test
    public void testOverdraftFunctionality(){
        exampleChecking.withdraw(1100);
        exampleChecking.setOverdraftProtAccount(exampleSavings);
        exampleChecking.handleOverdraft();
        assertEquals(1900.0, exampleSavings.getBalance());
    }
    //This test checks that the interest is applied correctly to the savings account
    @Test
    public void testSavingsInterest(){
        exampleSavings.applyDailyInterest();
        assertEquals(2000.013698630137, exampleSavings.getBalance());
    }
}