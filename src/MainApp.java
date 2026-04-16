import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp {

    public static void main(String[] args) {
        Application.launch(MyApp.class, args);
    }

    public static class MyApp extends Application {

        public void start(Stage stage) throws Exception {
            FXMLLoader load = new FXMLLoader(getClass().getResource("/SystemController.fxml"));
            Scene scene = new Scene(load.load());

            stage.setTitle("System Controller");
            stage.setScene(scene);
            stage.show();
        }
    }
}
