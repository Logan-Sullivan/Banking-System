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
}