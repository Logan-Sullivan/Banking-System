package tests;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import  java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.*;
import org.junit.jupiter.api.BeforeEach;
import Account_Classes.CheckingsAccount;
import Account_Classes.SavingsAccount;

public class MainTests {
    //This test checks that the file exists
    @Test 
    public void testCSVFileExists(){
        assertDoesNotThrow(() ->{
            Scanner scanner = new Scanner(new File("src/data.csv"));
        });
    }
    User_Classes.Customer example = new User_Classes.Customer();
    CheckingsAccount exampleChecking=new CheckingsAccount(null, 1000);
    SavingsAccount exampleSavings=new SavingsAccount(0.25, "Daily", false, 2000);
    @BeforeEach
    public void setUpExampleCust(){
        example.accountList = new ArrayList<>();
        example.accountList.add(exampleChecking);
        example.accountList.add(exampleSavings);
    }
    @Test
    public void testDeposit(){
        exampleChecking.deposit(500);
        assertEquals(1500, exampleChecking.getBalance());
    }
    @Test
    public void testWithdraw(){
        exampleChecking.withdraw(1000);
        assertEquals(0, exampleChecking.getBalance());
    }
    @Test
    public void testSetOverdraftProtection(){
        assertDoesNotThrow(() ->{
            exampleChecking.setOverdraftProtAccount(exampleSavings);
        });
    }
    @Test
    public void testAccountFundTransfer(){
        exampleChecking.transfer(exampleSavings, 500);
        assertEquals(2500, exampleSavings.getBalance());
    }
    @Test
    public void testOverdraftPenalty(){
        exampleChecking.withdraw(1100);
        exampleChecking.handleOverdraft();
        assertEquals(-125, exampleChecking.getBalance());
    }
    @Test
    public void testOverdraftFunctionality(){
        exampleChecking.withdraw(1100);
        exampleChecking.setOverdraftProtAccount(exampleSavings);
        exampleChecking.handleOverdraft();
        assertEquals(1900.0, exampleSavings.getBalance());
    }
    @Test
    public void testSavingsInterest(){
        exampleSavings.applyDailyInterest();
        assertEquals(2500, exampleSavings.getBalance());
    }
}
