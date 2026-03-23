package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        char[] SSN = new char[9];
        char[] Address = new char[15];
        char[] State = new char[2];
        char[] Zip = new char[5];
        char[] First = new char[10];
        char[] Last = new char[10];

        File file = new File("src/data.csv");
        try (Scanner fileReader = new Scanner(file)){
            while (fileReader.hasNextLine()){
                String text = fileReader.nextLine();
                String[] formattedText = text.split(",");
                SSN = formattedText[0].toCharArray();
                Address = formattedText[1].toCharArray();
                State = formattedText[2].toCharArray();
                Zip = formattedText[3].toCharArray();
                First = formattedText[4].toCharArray();
                Last = formattedText[5].toCharArray();

                /*
                * Here we would create the objects for accounts
                *  with these values and add them to our arraylist
                */
                //arrayListManager.addAccount(Account, new Account(SSN,Address,State,Zip,First,Last));
            }
        } catch (FileNotFoundException e){
            System.out.println("File not found");
        }

        // example of customer object to save the data
        customer newCustomer = new customer();
        newCustomer.customerId = "1001";
        newCustomer.firstName = "John";
        newCustomer.lastName = "Smith";

        saveCustomerData(newCustomer);


    }

    static void saveCustomerData(customer customerToSave){
        String fileName = "customers.csv";
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(customerToSave.customerId + ","+ customerToSave.firstName+"," +customerToSave.lastName);
            fileWriter.close();
            System.out.println("Customer saved to file.");
        } catch (Exception e) {
            System.out.println("Error saving customer." + e.getMessage());
        }
    }


}

abstract class Account{
    String accountNumber;
    private double balance;
    customer Owner;

    public void deposit(double amount){
        System.out.println("Successfully Deposited "+ amount);
        System.out.print(this.balance);
        this.balance += amount;
        System.out.print(" → "+ this.balance+"\n");
    }
    public void withdraw(double amount){}
    public double getBalance(){
        return this.balance;
    }
}

class SavingsAccount extends Account{
    double intrestRate;
    String compoundFreq;
    boolean overdraftBackup;

    public SavingsAccount(double intrestRate, String compoundFreq, boolean overdraftBackup,double balance){
        this.intrestRate = intrestRate;
        this.compoundFreq = compoundFreq;
        this.overdraftBackup = overdraftBackup;
        this.deposit(balance);
    }

    public void applyDailyInterest(){
        System.out.println("Daily interest applied: " + this.getBalance());
        this.deposit(this.getBalance()*intrestRate);
        System.out.print(" → "+ this.getBalance() +"\n");
    }

    public void toggleOverdraftBackup(){
        this.overdraftBackup = !this.overdraftBackup; // toggles between true and false
        if (this.overdraftBackup) {
            System.out.println("Account set as backup");
        } else {
            System.out.println("Account set as non-backup");
        }
    }

}

class customer{
    String customerId;
    String firstName;
    String lastName;

}