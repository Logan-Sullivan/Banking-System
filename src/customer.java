package src;

import java.util.ArrayList;
import java.util.List;

public class customer {
    String customerId;
    String firstName;
    String lastName;
    List<Account> accountList = new ArrayList<>();

    public customer(String customerId, String firstName, String lastName){
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}