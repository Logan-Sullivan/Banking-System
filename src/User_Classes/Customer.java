package src.User_Classes;
import src.Account_Classes.*;
import java.util.ArrayList;
import java.util.List;

public class Customer {
    public String customerId;
    public String firstName;
    public String lastName;
    public List<Account> accountList = new ArrayList<>();

    public Customer(String customerId, String firstName, String lastName){
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}