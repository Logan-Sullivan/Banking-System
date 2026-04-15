package Utils;
import Account_Classes.*;
import User_Classes.*;
import java.io.FileWriter;

public class PrintUtil {

    public static void saveCustomerData(Customer customer, Account account, String extraData) {
        String fileName = "User_Classes/customers.csv";

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
