package Utils;
import Utils.CsvManager;
import User_Classes.*;
import Account_Classes.*;
public class CheckpointManager {

    public static void EXEC_CHECKPOINT(ArrayListManager<Customer> CustomerList){
        //writes customers to array
        CsvManager.writeCustomersToCsv(CustomerList);
        // CS: just run this whenever you need to exit the program to checkpoint it and grab everything.
        // I may be stupid, so try adding an account, and running this to see
    }

    public void removeCustFromCsv(Customer Customer){

    }


}//end of CheckpointManager