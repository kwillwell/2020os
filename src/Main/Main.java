package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import process.ProcessDispatcher;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage)  {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ProcessSchedulUI.fxml"));
            Parent root = (Parent) loader.load();
            Scene scene = new Scene(root);
            //scene.getStylesheets().add("view/UIcss.css");
            primaryStage.setScene(scene);
            primaryStage.setTitle("进程调度");
            primaryStage.getIcons().add(new Image("/steam.png"));
            primaryStage.centerOnScreen();
            primaryStage.show();
            ProcessDispatcher.dispatchProcess();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        launch(args);
    }

}