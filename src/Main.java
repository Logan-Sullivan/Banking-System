import User_Classes.*;
import Utils.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import Account_Classes.*;

public class Main {
    public static void main(String[] args) {
        //ATM Card testing
        Customer atmTestCus = new Customer();
        atmTestCus.accountList.add(new SavingsAccount(10.0, "daily", false, 1000.0));
        System.out.println("Created test customer: " + atmTestCus.customerId + " with a savings account balance 1000.");

        System.out.println("Attempting ATM withdraw with customer: " + atmTestCus.customerId);
        atmTestCus.atm.ATMWithdraw(100);
        System.out.println("New balance is: " + atmTestCus.accountList.get(0).getBalance());

        System.out.println("Attempting excessive withdraws...");
        atmTestCus.atm.ATMWithdraw(10);
        atmTestCus.atm.ATMWithdraw(10);

        System.out.println("Try to exceed account balance...");
        atmTestCus.atm.updateTime(LocalDate.now(), 1);
        atmTestCus.atm.ATMWithdraw(1500);

        System.out.println("Now test that account search is working");
        atmTestCus.accountList.add(new TMBAccount(null, 2000));
        atmTestCus.atm.ATMWithdraw(1500);

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
                CustomerList.addInOrder(new Customer(SSN,Address,City,State,Zip,First,Last,2));

            }
        } catch (FileNotFoundException e){
            System.out.println("File not found");
        }
    }

}
