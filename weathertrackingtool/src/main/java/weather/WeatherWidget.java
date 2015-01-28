package weather;

import java.io.*;
import java.net.*;
import java.util.*;
import javafx.application.*;
import javafx.concurrent.*;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.*;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

/** 
 * The WeatherWidget application demonstrates the
 * use of the following:
 * <pre>
 *  1) Communications from Java to JavaScript
 *  2) Communications from JavaScript to Java
 *  3) RESTful GET Web service end point 
 *  4) JSON Objects
 *  5) Handling HTML/JavaScript WebEvents
 *  6) Debugging using Firebug lite
 * </pre>
 * Weather APIs are from Open Weather Map http://openweathermap.org
 * 
 * @author Sanjay
 */
public class WeatherWidget extends Application {

   private WebView webView;
   public static final String WEATHER_URL ="http://api.openweathermap.org/data/2.5/weather";
    
   @Override 
   public void start(Stage stage) {
      // create the scene
      stage.setTitle("Weather Widget");
      BorderPane root = new BorderPane();
      Scene scene = new Scene(root, 300, 300);
       scene.getStylesheets().add(getClass().getClassLoader().getResource("css/map.css").toExternalForm());
       scene.getStylesheets().add(getClass().getClassLoader().getResource("css/flatred.css").toExternalForm());
      stage.setScene(scene);

      // WebView node to display local html content
      webView = new WebView();
      root.setCenter(webView);
      
      // Turns on Firebug lite for debugging 
      // html,css, javascript
      //enableFirebug(webView);
        
      // The web view's web engine
      webView.getEngine().getLoadWorker().stateProperty().addListener( (obs, oldValue, newValue) -> {
                if (newValue == Worker.State.SUCCEEDED) {
                   // Let JavaScript make upcalls to this (Java) class
                   JSObject jsobj = (JSObject) 
                         webView.getEngine().executeScript("window");
                   jsobj.setMember("WeatherWidget2", this);
                   // default city's weather (a sunny place)
                   queryWeatherByLocationAndUnit("Miami,FL", "c");
                }                     
             });
      
      webView.getEngine().setOnAlert((WebEvent<String> t) -> {
         System.out.println("Alert Event - Message: " + t.getData());
      });


      // load weather_template.html
       final URL urlGoogleMaps = getClass().getResource("/html/weather_template.html");

      //String htmlFile = streamToString( getClass().getClassLoader().getResourceAsStream("html/weather_template.html"));
      webView.getEngine().load(urlGoogleMaps.toExternalForm());
      stage.show();
   }

   /**
    * Quick one liner that delimits on the end of file character and 
    * returning the whole input stream as a String. Use for small files.
    * @param inputStream byte input stream.
    * @return STring a file or JSON text
    */
   public String streamToString(InputStream inputStream) {
      String text = new Scanner(inputStream, "UTF-8")
                           .useDelimiter("\\Z")
                           .next();
      return text;
   }
   
   /** -- C --
    * Called from the JavaScript function findWeatherByLocation()
    * inside of the weather_template.html file.
    * @param cityRegion
    * @param unitType 
    */
   public void queryWeatherByLocationAndUnit(String cityRegion,
         String unitType) {
      
      String queryString = null; 
      queryString = cityRegion;
      String units = "f".equalsIgnoreCase(unitType) ? "imperial": "metric";

      String weatherRequest = WEATHER_URL +
            "?q=" + queryString +
            "&" + "units=" + units +
            "&" + "mode=json";

      // Spawn background thread to fetch weather info.
      new Thread(createWeatherQueryWorker(weatherRequest, 
            unitType)).start();
   }
   /** -- D --
    * Returns a Task responsible for fetching and populating 
    * the html display area.
    * @param weatherRequest the json request
    * @param unitType f is for degrees in fahrenheit and 
    *                 c for degrees celsius. 
    * @return Task 
    */
   private Task createWeatherQueryWorker(String weatherRequest, 
         String unitType) {
      
      return new Task() {
         @Override
         protected Object call() throws Exception {
            // On worker thread... blocking call to jsonGetRequest()
            String json = jsonGetRequest(weatherRequest); // 
            
            Platform.runLater(() -> 
               // On the JavaFX Application Thread....
               webView.getEngine()
                      .executeScript(
                        "populateWeatherData('" + json + "', " +
                        "'" + unitType +"' );")
            ); // runLater()
            return true;
         } // call()
      }; // return
   }

   /**
    * JSON retrieval using a RESTful GET request.
    * @param urlQueryString
    * @return 
    */
   public String jsonGetRequest(String urlQueryString) {
      String json = null; 
      try {
          System.out.println("Request: " + urlQueryString);
          URL url = new URL(urlQueryString);
          HttpURLConnection connection = (HttpURLConnection) url.openConnection();
          connection.setDoOutput(true);
          connection.setInstanceFollowRedirects(false);
          connection.setRequestMethod("GET");
          connection.setRequestProperty("Content-Type", "application/json");
          connection.setRequestProperty("charset", "UTF-8");
          connection.connect();
          InputStream inStream = connection.getInputStream();
          json = streamToString(inStream);
          System.out.println("Response: " + json);
       } catch (IOException ex ) {
          ex.printStackTrace();
       }
       return json;
   }

  private static void enableFirebug(WebView webView) {
     WebEngine webEngine = webView.getEngine();
     webEngine.documentProperty().addListener((prop, oldDoc, newDoc) ->
                     webEngine.executeScript("if (!document.getElementById('FirebugLite')){"
                             + "E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;"
                             + "E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');"
                             + "E['setAttribute']('id', 'FirebugLite');"
                             + "E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened');"
                             + "E['setAttribute']('FirebugLite', '4');"
                             + "(document['getElementsByTagName']('head')[0] || "
                             + " document['getElementsByTagName']('body')[0]).appendChild(E);"
                             + "E = new Image;"
                             + "E['setAttribute']('src', 'https://getfirebug.com/' + '#startOpened');"
                             + "}")
     );
  }
  
  public static void main(String[] args){
        Application.launch(args);
  }
}
