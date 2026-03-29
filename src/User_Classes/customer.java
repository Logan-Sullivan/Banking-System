package User_Classes;
import Account_Classes.*;
import java.util.ArrayList;
import java.util.List;

public class customer {
    public String customerId;
    public String firstName;
    public String lastName;
    public List<Account> accountList = new ArrayList<>();

    public customer(String customerId, String firstName, String lastName){
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}