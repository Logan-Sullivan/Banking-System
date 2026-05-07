package Account_Classes;

import java.time.LocalDate;

import User_Classes.Customer;
import Utils.TimeService;

/*
@author: Macalistair Clark
@since: May 5, 2026

This class represents the ATM card handling for this simulation. Rather than tracking
a card, each user will simply have a counter that is incremented whenever a withdraw
is performed. The withdraws occur from savings, GD checking, and TMB checking accounts
in that order. ATM withdraws are rejected if no account is present,k or if a withdraw
cannot be covered.
 */
public class ATMCard extends TimeService {
    private Customer cus;
    private Account acc = null;
    private int withdraws;
    
    public ATMCard(Customer cus, int withdraws){
        this.cus = cus;
        this.withdraws = withdraws;
    }

    //Check for the best account type available, withdraw, and update the counter.
    //0 is success, 1 is withdraw excess, 2 is no accounts, 3 is no valid accounts
    public int ATMWithdraw(double amt){
        //Check if customer is over withdraw limit, return if so
        if(withdraws >= 2){
            System.out.printf("Customer %s %s has already made 2 ATM withdraws!\n",
            cus.firstName, cus.lastName);
            return 1;
        }

        //Make sure we found an account
        if(acc == null){
            System.out.printf("Customer %s %s has not found an account!\n",
            cus.firstName, cus.lastName);
            return 2;
        }

        //Perform the withdraw
        acc.withdraw(amt);
        withdraws++;
        return 0;
    } //End of ATM withdraw method

    //Method to find a viable account
    public int findATMAccount (double amt) {
        //Determine account to use, prioritize savings if available
        Account accToUse = null;
        for (Account acc : cus.accountList){
            if (acc.getClass() == SavingsAccount.class && acc.getBalance() > amt){ //Stop processing if we find a valid savings account
                accToUse = acc;
                break;

            } else if (acc.getClass() == GDAccount.class && acc.getBalance() > amt){ //Next is GD accounts
                accToUse = acc;

            } else if (acc.getClass() == TMBAccount.class && acc.getBalance() > amt){ //Finally TMB accounts
                if (accToUse == null){
                    accToUse = acc;
                }
            } //End of account type check
        } //End of account list scan

        //Return 1 if we found no account, 0 otherwise
        if (accToUse == null){
            return 1;
        } else {
            this.acc = accToUse;
            return 0;
        }
    }

    //Very simple, just set withdraws to zero whenever time has passed
    public void updateTime(LocalDate date, int days){
        withdraws = 0;
    }

    //Getters
    public int getWithdraws(){return withdraws;}
    public Account getAccount(){return acc;}

} //End of ATMCard class
