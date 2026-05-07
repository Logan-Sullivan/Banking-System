package tests;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.*;
import org.junit.jupiter.api.BeforeEach;

import Account_Classes.CheckingsAccount;
import Account_Classes.SavingsAccount;
import Loan_Classes.MortgageLoan;
import User_Classes.Customer;


public class MainTests {
    //This test checks that the file exists
    @Test 
    public void testCSVFileExists(){
        assertDoesNotThrow(() ->{
            Scanner scanner = new Scanner(new File("src/data.csv"));
        });
    }
    @Test
    public void testCreateExampleCustomer(){
        assertDoesNotThrow(() ->{
            ArrayList<Customer> custList = new ArrayList<Customer>();
            custList.add(new Customer());
            String[] args = {};
        });
    }
    @Test
    public void testCreateExampleAccounts(){
        
    }
    @Test
    public void testClosingAccount(){
        ArrayList<Customer>custList = new ArrayList<Customer>();
        custList.add(new Customer());
        custList.get(0).accountList.add(new SavingsAccount(5, "30", false, 1000));
        Double expected = 1000.0;
        Double gotten = custList.get(0).closeOneAccount(0);
        
        assertEquals(expected, gotten);
    }
    @Test
    public void testClosingLoan(){
        ArrayList<Customer>custList = new ArrayList<Customer>();
        custList.add(new Customer());
        custList.get(0).payoffList.add(new MortgageLoan(15, 5, 5000));
        Double expected = -5000.0;
        Double gotten = custList.get(0).closeOneLoan(0);
        
        assertEquals(expected, gotten);
    }
}