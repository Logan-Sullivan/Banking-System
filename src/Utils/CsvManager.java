package Utils;

import Account_Classes.CheckingsAccount;
import Account_Classes.SavingsAccount;
import User_Classes.Customer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CsvManager {


    //i think this best belongs in the manger class once thats made as i believe they will be the ones with permissions to close an account
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


    public static void fetchCustsAndAccountsFromCSV(ArrayListManager<Customer> CustomerList){

        File file = new File("src/User_Classes/customers.csv");
        try (Scanner fileReader = new Scanner(file)){
            while (fileReader.hasNextLine()){
                String text = fileReader.nextLine();
                String[] formattedText = text.split(",");
                //struct of csv is
                //SSN,address,city,state,zip,first,last
                CustomerList.addInOrder(new Customer(formattedText[0],formattedText[1],formattedText[2],formattedText[3],formattedText[4],formattedText[5],formattedText[6]));

            }//end of while loop
        } catch (FileNotFoundException e){
            System.out.println("File not found");
        }//end of try-catch 1

        file = new File("src/Account_Classes/SavingsAccounts.csv");
        for (int i = 0; i < CustomerList.getMcount(); i++) {
            Customer cust = CustomerList.getValue(i);
            try (Scanner fileReader = new Scanner(file)){
                while (fileReader.hasNextLine()){
                    String text = fileReader.nextLine();
                    String[] formattedText = text.split(",");

                    if(cust.customerId.equals(formattedText[0])){
                        cust.accountList.add(new SavingsAccount(formattedText[1],(Double.parseDouble(formattedText[2])),formattedText[3],Integer.parseInt(formattedText[4])!=0,Double.parseDouble(formattedText[5])));
                    }

                }//end of while loop
            } catch (FileNotFoundException e){
                System.out.println("File not found");
            }//end of try-catch 1
        }//end of for loop

        //TODO: include overdraft account if applicable prob need also need a check that the overdraft account actually exists
        file = new File("src/Account_Classes/CheckingAccounts.csv");
        for (int i = 0; i < CustomerList.getMcount(); i++) {
            Customer cust = CustomerList.getValue(i);
            try (Scanner fileReader = new Scanner(file)){
                while (fileReader.hasNextLine()){
                    String text = fileReader.nextLine();
                    String[] formattedText = text.split(",");

                    if(cust.customerId.equals(formattedText[0])){
                        cust.accountList.add(new CheckingsAccount(formattedText[1], (Double.parseDouble(formattedText[2])) ));
                    }

                }//end of while loop
            } catch (FileNotFoundException e){
                System.out.println("File not found");
            }//end of try-catch 1
        }//end of for loop


    }//end of fetchCustsFromCSV

    public static void writeCustomerAccountsToCsv(ArrayListManager<Customer> CustomerList){
        try{
            FileWriter Writer = new FileWriter("src/Account_Classes/CheckingAccounts.csv");
            for (int i = 0; i < CustomerList.getMcount(); i++) {
                Writer.write(CustomerList.getValue(i).AccountForCSV());
            }
            Writer.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }//end of try-catch
    }

    public static void writeCustomersToCsv(ArrayListManager<Customer> CustomerList){
        try{
            FileWriter Writer = new FileWriter("src/User_Classes/customers.csv");
            for (int i = 0; i < CustomerList.getMcount(); i++) {
                Writer.write(CustomerList.getValue(i).LineForCSV());
            }
            Writer.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }//end of try-catch

    }//end of writeCustomersToCsv
}
