package weather;

import controller.WeatherInfoController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by webonise on 27-01-2015.
 */
public class WeatherInfo extends Application{

    private Scene scene;
    WeatherInfoController weatherController;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("WeatherInfo");

        weatherController = new WeatherInfoController();
        scene = new Scene(weatherController, 680, 680);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
