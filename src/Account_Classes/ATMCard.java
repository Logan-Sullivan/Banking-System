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
    private int withdraws;
    
    public ATMCard(Customer cus, int withdraws){
        this.cus = cus;
        this.withdraws = withdraws;
    }

    //Check for the best account type available, withdraw, and update the counter.
    public void ATMWithdraw(double amt){
        //Check if customer is over withdraw limit, return if so
        if(withdraws >= 2){
            System.out.printf("Customer %s %s has already made 2 ATM withdraws!\n",
            cus.firstName, cus.lastName);
            return;
        }
        //Check if a valid account is present, return if not
        if (cus.accountList.isEmpty()){
            System.out.printf("Customer %s %s has no accounts!\n",
            cus.firstName, cus.lastName);
            return;
        }

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

        //Make sure we found an account
        if(accToUse == null){
            System.out.printf("Customer %s %s has no compatible account!\n",
            cus.firstName, cus.lastName);
            return;
        }

        //Perform the withdraw
        accToUse.withdraw(amt);
        withdraws++;
        return;
    } //End of ATM withdraw method

    //Very simple, just set withdraws to zero whenever time has passed
    public void updateTime(LocalDate date, int days){
        withdraws = 0;
    }

    //Getter needed for csv methods
    public int getWithdraws(){return withdraws;}
} //End of ATMCard class
