package weather;

import controller.MyBrowser;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by webonise on 20-01-2015.
 */
public class Map extends Application {

    private Scene scene;
    MyBrowser myBrowser;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Weather");

        myBrowser = new MyBrowser();
        scene = new Scene(myBrowser, 640, 480);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}