package controller;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;

/**
 * Created by webonise on 21-01-2015.
 */
public class WeatherController  extends Region {

    HBox toolbar;
    WebView webView = new WebView();
    WebEngine webEngine = webView.getEngine();

    public WeatherController() {

        final URL urlGoogleMaps = getClass().getResource("/html/weather.html");
        webEngine.load(urlGoogleMaps.toExternalForm());

        getChildren().add(webView);

    }
}