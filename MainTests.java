import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import  java.io.File;
import java.util.Scanner;

import org.junit.*;

public class MainTests {
    //This test checks that the file exists
    @Test 
    public void testFileExists(){
        assertDoesNotThrow(() ->{
            Scanner scanner = new Scanner(new File("src/data.csv"));
        });
    }
}
