package src.Utils;

import src.Account_Classes.*;
import src.User_Classes.*;
import java.io.FileWriter;
import java.io.IOException;

public class PrintUtil {

    public static void saveCustomerData(Customer customer, Account account, String extraData) {
        String fileName = "customers.csv";

        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(customer.customerId + "," + customer.firstName + "," + customer.lastName + "," + account.accountNumber + "," + account.getBalance() + "," + extraData + "\n");
            writer.close();
            System.out.println("Customer info saved to file");
        } catch (Exception e) {
            System.out.println("Error saving customer info: " + e.getMessage());
        }
    }
}
