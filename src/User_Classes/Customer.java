package User_Classes;
import Account_Classes.*;
import java.util.ArrayList;
import java.util.List;

public class Customer implements Comparable {
    public String customerId,address,city,state,zipcode,firstName,lastName;
    public List<SavingsAccount> savingsAccountList = new ArrayList<>();
    public List<CheckingsAccount> checkingsAccountList = new ArrayList<>();

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
    public String AccountForCSV(List list){
        String accounts="";
        for (Account account : list) {
            accounts += this.customerId + "," + account.toString() + "\n";
        }
        return accounts;
    }
    //for arraylist sorts customers by last name
    @Override
    public int compareTo(Object o){
        return this.lastName.compareTo(((Customer)o).lastName);
    }
}