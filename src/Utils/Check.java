package Utils;

import Account_Classes.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/*
* TODO related to checks:
* Ability to get the accounts from our arraylist by the accounts accountnumber (i think this should be simple but need new parser to be done to do so)
* Show all checks by a specific Sender / Receiver (allows searching by either user or manager)
* Check that account has funds to withdrawal before allowing check to be made
* */

public class Check implements Comparable {
    int checkID;
    double amount;
    Account sender,receiver;
    String Status;

    //constructor for creating from csv file for the arraylist
    public Check(int ID, String senderID,String receiverID,double amount,String Status){
        this.checkID = ID;
        //this.sender = getaccountbyid(senderID) // will make in future
        //this.receiver = getaccountbyid(receiver) // will make in future
        this.amount = amount;
        this.Status = Status;
    }

    //constructor for creating from scratch
    public Check(double amount, Account Sender, Account Receiver) {
        Sender.withdraw(amount);
        //need a check that account has enough funds before creating check object
        this.checkID = getLineCountOfCSV() +1;
        this.amount = amount;
        this.sender = Sender;
        this.receiver = Receiver;
        this.Status = "Pending";
    }

    public void cancelCheck(){
        if (this.Status.equals("Pending")){
            this.Status = "Cancelled";
            System.out.println("Successfully cancelled check #"+this.checkID);
            //should this update line in csv or in the arraylist?
            this.sender.deposit(this.amount);
        } else {
            System.out.println("Unable to cancel check with a status of "+this.Status);
        }

    }
    public void processCheck(){
        if (this.Status.equals("Pending")){
            this.Status = "Received";
            System.out.println("Successfully deposited check #"+this.checkID);
            //should this update line in csv or in the arraylist?
            this.receiver.deposit(this.amount);
        } else {
            System.out.println("Unable to process check with a status of "+this.Status);
        }
    }

    public String CheckCSVString(){
        return this.checkID+","+this.sender.accountNumber+","+this.receiver.accountNumber+","+this.amount+","+this.Status+"\n";
    }

    public static int getLineCountOfCSV(){
        int count =0;
        File file = new File("checks.csv");
        try (Scanner fileReader = new Scanner(file)){
            while (fileReader.hasNextLine()){
                count++;
            }
        } catch (FileNotFoundException e){
            System.out.println("File not found");
        }
        return count;
    }
    //for testing
    public static void clearFileContents(String filename){
        try{
            FileWriter Writer = new FileWriter(filename);
            Writer.write("");
            Writer.close();
            System.out.println("Cleared "+ filename);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    @Override
    public int compareTo(Object o){
        if (this.checkID > ((Check)o).checkID){
            return 1;
        } else if (this.checkID < ((Check)o).checkID) {
            return -1;
        }
        else return 0;
    }
}
