package Utils;

import Account_Classes.*;
import User_Classes.Customer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

/*
* TODO related to checks:
* Ability to get the accounts from our arraylist by the accounts accountnumber (i think this should be simple but need new parser to be done to do so)
* Show all checks by a specific Sender / Receiver (allows searching by either user or manager)
* Check that account has funds to withdrawal before allowing check to be made
* */

/*
* UI reqs
* a check screen for customers
*   allows creation of new checks
*   allows canceling of checks
*   allows depositing of checks
*   shows all checks related to their owned accounts (sent and received)
*
*
* */

/* overview of check
*
*   Sender logic
*   to create a check
*       sender enters
 *          the account they want to take from
*           the name of the person they want to send the check to
*           the amount of the check
*   on creation we set the status of the check to "pending"
*   after creation of the check the owner of the check is able to cancel it at any point during the checks "pending" state setting the status to "canceled"
*   after the check is processed the check still appears for the owner showing the status as recieved
*
* sender does not need to have the check owned within their object as they can just call from the check array list to find checks owned by them at run time
* i dont remember why i decided this but to be fair we dont need another reason to rework our parser at this point
*
*   receiver logic
*       after a check is sent by a sender the check holds the receiver and therefore the receiver can now view the check on their ui
*       receivers are able to either deposit or reject a check canceling it
*       on deposit
*           receiver picks the account to load the funds into
*           then we check if the sender has enough in their account to give to the receiver
*           if the sender doesn't have enough the check is rejected and status is set to "canceled"
*           (it was decided to not use backup account logic here as it is a direct transfer from account to account though if we did want to add it implementation would not be difficult)
*           if the sender does have enough funds the funds are withdrawn from the sender account and deposited into the reciever account with the status of the check moving to "Processed"
*
*   to store our checks we use a csv file once again which
* from the logic laid out before the variables needed by our check object is
* Account Sender - account object (checking or saving) of the sender
* Customer receiver - within the constructor we find the Customer object from first and last name so that the user doesn't have to find and write the account id of the receiver and so that the receiver can choose the account to deposit the check
* double amount - amount that the check is worth
* */

public class Check {
    double amount;
    Customer receiver;
    Account sender;
    String Status;


    public Check(double amount, Account Sender, Customer receiver,String Status) {
        this.amount = amount;
        this.sender = Sender;
        this.receiver = receiver;
        this.Status = Status;
        System.out.println("Check successfully created from csv!");
        System.out.println(CheckCSVString());
    }

    //constructor
    public Check(double amount, Account Sender, Customer receiver) {
        this.amount = amount;
        this.sender = Sender;
        this.receiver = receiver;
        this.Status = "Pending";
        System.out.println("New Check successfully created!");
        System.out.println(CheckCSVString());
    }

    public void cancelCheck(){
        if (this.Status.equals("Pending")){
            this.Status = "Cancelled";
            System.out.println("Successfully cancelled check ");
        } else {
            System.out.println("Unable to cancel check with a status of "+this.Status);
        }
    }

    //verifies that sender has enough funds to process the check
    // and that the check has not already been canceled
    public void processCheck(Account receiver){
        if (this.sender.getBalance() < this.amount){
            this.Status = "Cancelled";
            System.out.println("Insufficient funds, Check bounces");
        } else {
            if (this.Status.equals("Pending")){
                this.Status = "Received";
                System.out.println("Successfully deposited check ");
                this.sender.withdraw(this.amount);
                receiver.deposit(this.amount);
            } else {
                System.out.println("Unable to process check with a status of "+this.Status);
            }
        }

    }

    public String CheckCSVString(){
        return this.sender.accountNumber+","+this.receiver.firstName+","+this.receiver.lastName+","+this.amount+","+this.Status+"\n";
    }

}
