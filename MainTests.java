import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import  java.io.File;
import java.util.Scanner;

import org.junit.*;

public class MainTests {
    @Test 
    public void testFileExists(){
        assertDoesNotThrow(() ->{
            Scanner scanner = new Scanner(new File("src/data.csv"));
        });
    }
}
