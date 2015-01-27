package maintable;

/**
 * Created by webonise on 22-01-2015.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Table Filtering");

        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/PersonTable.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Error loading PersonTable.fxml!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}