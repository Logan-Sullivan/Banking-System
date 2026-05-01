package Utils;

//This is currently going in utilities to minimize changes to the file structure of the code
//Ideally this should maybe be put into a package that the Loan_Classes and User_Classes are under
import java.time.LocalDate;

abstract public class Services {

    abstract public void updateTime(LocalDate currentDate, int daysPassed);
}
