package Utils;

//This is currently going in utilities to minimize changes to the file structure of the code. Can be moved if preferred
import java.time.LocalDate;

//This is the class that the loans and accounts inherit from, and from this they inherit the a general function that states that they change as time increments
//This is helpful because it allows them to be stored into one large list that increments the time and updates the whole list, one item at a time.
abstract public class Services {
    abstract public void updateTime(LocalDate currentDate, int daysPassed);
}
