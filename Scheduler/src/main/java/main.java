/**
 * main
 */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/interface.fxml"));
        primaryStage.setTitle("CPU Simulator");
        primaryStage.setScene(new Scene(root, 1250, 800));
        primaryStage.show();
    }
        /**
     * Unit tests the {@code Scheduler} class.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        //Scheduler sc = new Scheduler();
        //sc.executeScheduler();
        launch(args);


    }
}
