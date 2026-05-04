package tests.Utils;
import tests.Utils.CsvManager;
import tests.Account_Classes.*;
import tests.User_Classes.*;
public class CheckpointManager {

    public static void EXEC_CHECKPOINT(ArrayListManager<Customer> CustomerList, Timeline timeline){
        //writes customers to array
        CsvManager.writeCustomersToCsv(CustomerList, timeline);
        // CS: just run this whenever you need to exit the program to checkpoint it and grab everything.
        // I may be stupid, so try adding an account, and running this to see
    }

    public void removeCustFromCsv(Customer Customer){

    }


}//end of CheckpointManager