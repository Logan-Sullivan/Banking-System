package Account_Classes;
import User_Classes.*;
import Utils.Services;

import java.util.UUID;

public abstract class Account extends Services{
    public String accountNumber = UUID.randomUUID().toString();
    private double balance;
    Customer Owner;

    public void deposit(double amount){
        if (amount <=0)
            System.out.println("Denied, Non-value");
        else {
            System.out.println("Successfully Deposited " + amount);
            System.out.print(this.balance);
            this.balance += amount;
            System.out.print(" → " + this.balance + "\n");
        }
    }
    public void withdraw(double amount){
        if (amount <=0)
            System.out.println("Denied, Non-value");
        else{
            System.out.println("Successfully Withdrew " + amount);
            System.out.print(this.balance);
            this.balance -= amount;
            System.out.print(" → " + this.balance + "\n");
        }
    }
    public double getBalance(){
        return this.balance;
    }
}