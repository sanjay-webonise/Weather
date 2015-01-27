package maintable;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Created by webonise on 22-01-2015.
 */
public class FileChooserTest  extends Application {
    private model.Person Person;
    private TableView<model.Person> table = new TableView<model.Person>();
    private final ObservableList<model.Person> data =FXCollections.observableArrayList(
                   /* new Person("Jacob", "Smith", "jacob.smith@example.com"),
                    new Person("Isabella", "Johnson", "isabella.johnson@example.com"),
                    new Person("Ethan", "Williams", "ethan.williams@example.com"),
                    new Person("Emma", "Jones", "emma.jones@example.com"),
                    new Person("Michael", "Brown", "michael.brown@example.com")*/
            );
    final HBox hb = new HBox();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(550);
        stage.setHeight(550);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("css/style.css").toExternalForm());
        final Label label = new Label("Address Book");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);

        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellValueFactory(new PropertyValueFactory<model.Person, String>("firstName"));


        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setMinWidth(100);
        lastNameCol.setCellValueFactory( new PropertyValueFactory<model.Person, String>("lastName"));


        TableColumn emailCol = new TableColumn("Email");
        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory( new PropertyValueFactory<model.Person, String>("email"));



        //Insert Button
        TableColumn actionCol = new TableColumn<>("Action");
        actionCol.setSortable(false);

        actionCol.setMinWidth(100);
        actionCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<model.Person, Boolean>,
                        ObservableValue<Boolean>>() {

                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<model.Person, Boolean> p) {
                        return new SimpleBooleanProperty(p.getValue() != null);
                    }
                });

        actionCol.setCellFactory(
                new Callback<TableColumn<model.Person, Boolean>, TableCell<model.Person, Boolean>>() {
                    @Override
                    public TableCell<model.Person, Boolean> call(TableColumn<model.Person, Boolean> p) {
                        return new ButtonCell();
                    }

                });

        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol, emailCol,actionCol);

        final TextField addFirstName = new TextField();
        addFirstName.setPromptText("First Name");
        addFirstName.setMaxWidth(firstNameCol.getPrefWidth());
        final TextField addLastName = new TextField();
        addLastName.setMaxWidth(lastNameCol.getPrefWidth());
        addLastName.setPromptText("Last Name");
        final TextField addEmail = new TextField();
        addEmail.setMaxWidth(emailCol.getPrefWidth());
        addEmail.setPromptText("Email");

        final Button addButton = new Button("Add");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                data.add(new model.Person(
                        addFirstName.getText(),
                        addLastName.getText(),
                        addEmail.getText()));
                addFirstName.clear();
                addLastName.clear();
                addEmail.clear();
            }
        });

        hb.getChildren().addAll(addFirstName, addLastName, addEmail, addButton);
        hb.setSpacing(3);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hb);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();
    }


    //Define the button cell
    private class ButtonCell extends TableCell<model.Person, Boolean> {

        // Adding images
        Image img1 = new Image(getClass().getResourceAsStream("/image/trash-16.png"));
        final Button cellButton = new Button("", new ImageView(img1));
        //cellButton.setId("round-red");

        ButtonCell(){

            cellButton.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent t) {
                    // do something when button clicked
                    int selectdIndex = getTableRow().getIndex();
                    //delete the selected item in data
                    data.remove(selectdIndex);
                }
            });
        }

        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty){
                setGraphic(cellButton);
            }
        }
    }
}