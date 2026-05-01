package User_Classes;
import java.util.ArrayList;
import java.util.List;

import Account_Classes.*;

public class Customer implements Comparable {
    public String customerId,address,city,state,zipcode,firstName,lastName;
    public List<Account> accountList = new ArrayList<>();

    public Customer(){
        this.customerId = (int)(Math.random()*100000000)+"";
        this.address = "Test Address";
        this.city = "Test City";
        this.state = "Test State";
        this.zipcode = (int)(Math.random()*10000)+"";
        this.firstName = "Test First Name";
        this.lastName = "Test Last Name";
    }


    public Customer(String customerId, String address, String city, String state, String zipcode, String firstName, String lastName) {
        this.customerId = customerId;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String LineForCSV(){
        return this.customerId+","+this.address+","+this.city+","+this.state+","+this.zipcode+","+this.firstName+","+this.lastName+"\n";
    }
    //for arraylist sorts customers by last name
    @Override
    public int compareTo(Object o){
        return this.lastName.compareTo(((Customer)o).lastName);
    }
}