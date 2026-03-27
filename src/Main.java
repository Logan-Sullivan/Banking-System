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
        customer newCustomer = new customer("1001", "John", "Smith");
        SavingsAccount newSavings = new SavingsAccount(10, "Test", false, 1200.01);
        CheckingsAccount newCheckings = new CheckingsAccount(null, 100);
        SavingsAccount blankSavings = new SavingsAccount(1, "Test for Overdraft", false, 0);
        newCustomer.accountList.add(newCheckings);
        newCustomer.accountList.add(newSavings);
        System.out.println("----------Transferring first amount----------");
        newCheckings.transfer(newSavings, 25);
        System.out.println("----------Transfer over---------- \nsetting overdraft account, and overdrafting");
        newCheckings.setOverdraftProtAccount(newSavings);
        newCheckings.transfer(blankSavings, 76);


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