package Main;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import static javafx.application.Application.launch;
import View_Controller.MainScreenController;

public class App extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource("/View_Controller/MainScreen.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        MainScreenController controller = (MainScreenController)loader.getController();
        controller.addWindowEvent(stage);
        
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }

}