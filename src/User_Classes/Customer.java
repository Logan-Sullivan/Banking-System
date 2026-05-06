package User_Classes;
import Account_Classes.*;
import Loan_Classes.*;
import java.util.ArrayList;
import java.util.List;

public class Customer implements Comparable {
    public String customerId,address,city,state,zipcode,firstName,lastName;
    public List<Account> accountList = new ArrayList<>();
    public List<Loan> payoffList = new ArrayList<>();
    public ATMCard atm;

    public Customer(){
        this.customerId = (int)(Math.random()*100000000)+"";
        this.address = "Test Address";
        this.city = "Test City";
        this.state = "Test State";
        this.zipcode = (int)(Math.random()*10000)+"";
        this.firstName = "Test First Name";
        this.lastName = "Test Last Name";
        this.atm = new ATMCard(this, 0);
    }


    public Customer(String customerId, String address, String city, String state, String zipcode, String firstName, String lastName, int withdraws) {
        this.customerId = customerId;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.atm = new ATMCard(this, withdraws);
    }

    public String LineForCSV(){
        return this.customerId+","+this.address+","+this.city+","+this.state+","+this.zipcode+","+this.firstName+","+this.lastName+","+this.atm.getWithdraws()+"\n";
    }
    //for arraylist sorts customers by last name
    @Override
    public int compareTo(Object o){
        return this.lastName.compareTo(((Customer)o).lastName);
    }
}