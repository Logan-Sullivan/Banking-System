import User_Classes.*;
import Utils.*;

import java.util.Random;

import Account_Classes.*;

import static Utils.CsvManager.writeCustomersToCsv;

public class Main {
    public static void main(String[] args) {
        createDummyAccounts();
    }//end of main
    public static void createDummyAccounts(){
        String[] names = {"AbdulMajeed","Dhyia","Argyll","Marvellous","Alphonse","Lock","Conli","Conar","Kendall","Diarmaid","Azzedine","Aidian","Jubin","Clayton","Chin","Abdulkarem","Inan","Hassanali","Josiah","Benjamin","Jakub","Mickey","Kaid","Idrees","Jonah","Jostelle","Jaskaran","Marlon","Bradlie","Lorne","Eamonn","Coban","Finlay","Ahoua","Campbell","Brody","Jura","Callahan","Baley","Ayrton","Anmolpreet","Etienne","Nihaal","Denny","Gustav","Jody","Linden","Cal","Edwin","Mirza","Nevan","Meftah","Blyth","Kris","Lawlyn","Jasper","Kenneth","Antony","Daniil","Maddox","Harvie","Daood","Arandeep","Jebadiah","Hcen","Michee","Kadyn","Darrius","Harper","Kadyn","Cameron","Dermot","Nikos","Joash","CoreyJames","Jole","Monty","Mayson","Caethan","Awais","Evan","Bilal","Haroon","Brandon","Avinash","Corey","Kynan","Flint","Del","Caedyn","Forrest","Abu","Meko","Ayaan","Christopher","Jay","Levi","Cade","Dean","AdamJames","Jimbo"};
        String[] Streets = {"kimbrook","walkwood","novak","oldfield","cordie lee","mont blanc","abercrombie","groveshire","kimbro","mont blanc","cape charles","leighton creek","may","great oaks","sweetwood","bow string","pawnee avenue","falling leaf","frontage","dallager","summer fields","mcclellan","cross pike","new england","island grove"};
        String[] cities = {"Savannah","St. Joseph","Marysville","Cameron","Kansas City","Platte City"};
        ArrayListManager<Customer> customers = new ArrayListManager<>();
        int i = 0;
        double balance,interestrate;
        int accountCount,ssn,zipcode,z;
        String state ="MO";
        String firstName,lastName,city,address;
        while(i <25) {
            balance = getRandomNumberUsingNextInt(10000, 1000000)/100.0;
            accountCount = getRandomNumberUsingNextInt(0,4);
            ssn = getRandomNumberUsingNextInt(491000000,491999999);
            firstName = names[getRandomNumberUsingNextInt(0,names.length-1)];
            lastName = names[getRandomNumberUsingNextInt(0,names.length-1)];
            address = getRandomNumberUsingNextInt(1000,5000)+" "+ Streets[getRandomNumberUsingNextInt(0,Streets.length-1)];
            z = getRandomNumberUsingNextInt(0,cities.length-1);
            city = cities[z];
            interestrate = getRandomNumberUsingNextInt(0,10000)/100.0;
            switch (z){
                case 0 ->zipcode = 64485;
                case 1 ->zipcode = 64504;
                case 2 ->zipcode = 64468;
                case 3 ->zipcode = 64429;
                case 4 ->zipcode = 64101;
                default ->zipcode = 64079;
            }
            Customer newcust = new Customer(Integer.toString(ssn),address,city,state,Integer.toString(zipcode),firstName,lastName,0);
            for (int j = 0; j <= accountCount; j++) {
                if(getRandomNumberUsingNextInt(0,1) ==0){
                    if (balance >= 5000){
                        int savingcheck = doeshavesaving(newcust);
                        if( savingcheck != -1){
                            SavingsAccount overdraft = (SavingsAccount) (newcust.accountList.get(savingcheck));
                            newcust.accountList.add(new GDAccount(overdraft,balance,true));
                            if (!overdraft.isOverdraftBackup()){
                                overdraft.toggleOverdraftBackup();
                            }
                        } else {newcust.accountList.add(new GDAccount(null,balance,true));}
                    } else{
                        int savingcheck = doeshavesaving(newcust);
                        if( savingcheck != -1){
                            SavingsAccount overdraft = (SavingsAccount) (newcust.accountList.get(savingcheck));
                            newcust.accountList.add(new TMBAccount(overdraft,balance));
                            if (!overdraft.isOverdraftBackup()){
                                overdraft.toggleOverdraftBackup();
                            }
                        } else newcust.accountList.add(new TMBAccount(null,balance));
                    }

                } else{
                    newcust.accountList.add(new SavingsAccount(interestrate,"14",false,balance));
                }
            }//end of for loop
            customers.addAtFront(newcust);
            i++;
        }//end of while loop
        writeCustomersToCsv(customers,new Timeline());
    }//end of createDummyAccounts
    public static int doeshavesaving(Customer cust){
        for (int i = 0; i <cust.accountList.size() ; i++) {
            if (cust.accountList.get(i) instanceof SavingsAccount){
                return i;
            }
        }
        return -1;
    }
    public static int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
}//end of class


