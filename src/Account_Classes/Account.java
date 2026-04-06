package src.Account_Classes;
import src.User_Classes.*;

public abstract class Account{
    public String accountNumber;
    private double balance;
    Customer Owner;

    public void deposit(double amount){
        System.out.println("Successfully Deposited "+ amount);
        System.out.print(this.balance);
        this.balance += amount;
        System.out.print(" → "+ this.balance+"\n");
    }
    public void withdraw(double amount){
        this.balance -= amount; // CS: just put this in temporarily to test transfer cause I didn't realize this didn't do anyhting
    }
    public double getBalance(){
        return this.balance;
    }
}