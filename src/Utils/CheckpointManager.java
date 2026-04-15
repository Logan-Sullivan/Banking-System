package Utils;
import Utils.CsvManager;
import User_Classes.*;
import Account_Classes.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CheckpointManager {

    public void EXEC_CHECKPOINT(ArrayListManager<Customer> CustomerList){
        //writes customers to array
        CsvManager.writeCustomersToCsv(CustomerList);
        //writes customer accounts to array
        CsvManager.writeCustomerAccountsToCsv(CustomerList);
    }

    public void removeCustFromCsv(Customer Customer){

    }


}//end of CheckpointManager
