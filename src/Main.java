import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        char[] SSN = new char[9];
        char[] Address = new char[15];
        char[] State = new char[2];
        char[] Zip = new char[5];
        char[] First = new char[10];
        char[] Last = new char[10];

        File file = new File("src/data.csv");
        try (Scanner fileReader = new Scanner(file)){
            while (fileReader.hasNextLine()){
                String text = fileReader.nextLine();
                String[] formattedText = text.split(",");
                SSN = formattedText[0].toCharArray();
                Address = formattedText[1].toCharArray();
                State = formattedText[2].toCharArray();
                Zip = formattedText[3].toCharArray();
                First = formattedText[4].toCharArray();
                Last = formattedText[5].toCharArray();

                /*
                * Here we would create the objects for accounts
                *  with these values and add them to our arraylist
                */
                //arrayListManager.addAccount(Account, new Account(SSN,Address,State,Zip,First,Last));
            }
        } catch (FileNotFoundException e){
            System.out.println("File not found");
        }

    }
}