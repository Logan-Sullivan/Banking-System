import Account_Classes.*;
import User_Classes.*;
import Utils.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayListManager<Customer> CustomerList = new ArrayListManager<>();

        fetchCustsFromCSV(CustomerList,"src/data.csv");
        removeCustFromArrBySSN(CustomerList,"28733578");
        // example of customer object to save the data
        writeToArrayToCsv(CustomerList,"src/data.csv");
        Customer newCustomer = new Customer();
        SavingsAccount newSavings = new SavingsAccount();
        TMBAccount newCheckings = new TMBAccount(null, 100);
        SavingsAccount blankSavings = new SavingsAccount(1, "Test for Overdraft", false, 0);
        newCustomer.accountList.add(newCheckings);
        newCustomer.accountList.add(newSavings);
        String extraData = "";
        PrintUtil.saveCustomerData(newCustomer, newSavings, extraData);
        System.out.println("----------Transferring first amount----------");
        newCheckings.transfer(newSavings, 25);
        System.out.println("----------Transfer over---------- \nsetting overdraft account, and overdrafting");
        newCheckings.setOverdraftProtAccount(newSavings);
        newCheckings.transfer(blankSavings, 76);

    }
    public static void writeToArrayToCsv(ArrayListManager<Customer> CustomerList, String fileName){
        try{
            FileWriter Writer = new FileWriter(fileName);
            for (int i = 0; i < CustomerList.getMcount(); i++) {
                 Writer.write(CustomerList.getValue(i).LineForCSV());
            }
            Writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    public static void removeCustFromArrBySSN(ArrayListManager<Customer> CustomerList, String SSN){
        try{
            for(int i = 0; i < CustomerList.getMcount(); i++){
                if (CustomerList.getValue(i).customerId.equals(SSN)) {
                    System.out.println("Removing " + CustomerList.getValue(i).firstName + " " + CustomerList.getValue(i).lastName);
                    CustomerList.removeM(i);
                    return;
                }

            }
        } catch (Exception e){
            System.out.println("An error occurred.");
        }
        System.out.println("cust not found");
    }

    public static void fetchCustsFromCSV(ArrayListManager<Customer> CustomerList, String fileName){
        String SSN,Address,City,State,Zip,First,Last;

        File file = new File(fileName);
        try (Scanner fileReader = new Scanner(file)){
            while (fileReader.hasNextLine()){
                String text = fileReader.nextLine();
                String[] formattedText = text.split(",");
                SSN = formattedText[0];
                Address = formattedText[1];
                City = formattedText[2];
                State = formattedText[3];
                Zip = formattedText[4];
                First = formattedText[5];
                Last = formattedText[6];
                CustomerList.addInOrder(new Customer(SSN,Address,City,State,Zip,First,Last));

            }
        } catch (FileNotFoundException e){
            System.out.println("File not found");
        }
    }

}
