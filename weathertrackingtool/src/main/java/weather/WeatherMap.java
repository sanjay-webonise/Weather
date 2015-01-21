package weather;

import controller.WeatherController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by webonise on 21-01-2015.
 */
public class WeatherMap  extends Application {

    private Scene scene;
    WeatherController weatherController;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Weather");

        weatherController = new WeatherController();
        scene = new Scene(weatherController, 680, 680);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}