import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScreenNavigator {

    public static void switchScreen(Stage stage, String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(ScreenNavigator.class.getResource(fxmlPath));
            Scene scene = new Scene(root);
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
