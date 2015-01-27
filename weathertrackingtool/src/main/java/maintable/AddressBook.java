package maintable;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Comparator;

/**
 * Created by webonise on 23-01-2015.
 */
public class AddressBook extends Application {

    private TableView<Person> table = new TableView<Person>();
    private final ObservableList<Person> data =
            FXCollections.observableArrayList(
                    new Person("Jacob", "Smith", "jacob.smith@example.com","Coffee"),
                    new Person("Isabella", "Johnson", "isabella.johnson@example.com","Fruit"),
                    new Person("Ethan", "Williams", "ethan.williams@example.com","Fruit"),
                    new Person("Emma", "Jones", "emma.jones@example.com","Coffee"),
                    new Person("Michael", "Brown", "michael.brown@example.com","Fruit")

            );

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Table View Sample");

        final Label label = new Label("Address Book");
        label.setFont(new Font("Arial", 20));

        final Label actionTaken = new Label();

        table.setEditable(true);

        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("firstName"));

        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setMinWidth(100);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("lastName"));

        TableColumn emailCol = new TableColumn("Email");
        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("email"));

        TableColumn<Person, Person> btnCol = new TableColumn<>("Gifts");
        btnCol.setMinWidth(150);
        btnCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Person, Person>, ObservableValue<Person>>() {
            @Override public ObservableValue<Person> call(TableColumn.CellDataFeatures<Person, Person> features) {
                return new ReadOnlyObjectWrapper(features.getValue());
            }
        });
        btnCol.setComparator(new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                return p1.getLikes().compareTo(p2.getLikes());
            }
        });
        btnCol.setCellFactory(new Callback<TableColumn<Person, Person>, TableCell<Person, Person>>() {
            @Override
            public TableCell<Person, Person> call(TableColumn<Person, Person> btnCol) {
                return new TableCell<Person, Person>() {
                    final ImageView buttonGraphic = new ImageView();
                    final Button button = new Button();

                    {
                        button.setGraphic(buttonGraphic);
                        button.setMinWidth(130);
                    }

                    @Override
                    public void updateItem(final Person person, boolean empty) {
                        super.updateItem(person, empty);
                        if (person != null) {
                            switch (person.getLikes().toLowerCase()) {
                                case "fruit":
                                    button.setText("Buy fruit");
                                    buttonGraphic.setImage(fruitImage);
                                    break;

                                default:
                                    button.setText("Buy coffee");
                                    buttonGraphic.setImage(coffeeImage);
                                    break;
                            }

                            setGraphic(button);
                            button.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    actionTaken.setText("Bought " + person.getLikes().toLowerCase() + " for: " + person.getFirstName() + " " + person.getLastName());
                                }
                            });
                            button.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    int selectdIndex = getTableRow().getIndex();

                                    //delete the selected item in data
                                    data.remove(selectdIndex);
                                }
                            });
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });

        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol, emailCol, btnCol);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(label, table, actionTaken);
        VBox.setVgrow(table, Priority.ALWAYS);

        stage.setScene(new Scene(vbox));
        stage.show();
    }

    public static class Person {

        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty email;
        private final SimpleStringProperty likes;

        private Person(String fName, String lName, String email, String likes) {
            this.firstName = new SimpleStringProperty(fName);
            this.lastName = new SimpleStringProperty(lName);
            this.email = new SimpleStringProperty(email);
            this.likes = new SimpleStringProperty(likes);
        }

        public String getFirstName() {
            return firstName.get();
        }

        public void setFirstName(String fName) {
            firstName.set(fName);
        }

        public String getLastName() {
            return lastName.get();
        }

        public void setLastName(String fName) {
            lastName.set(fName);
        }

        public String getEmail() {
            return email.get();
        }

        public void setEmail(String fName) {
            email.set(fName);
        }

        public String getLikes() {
            return likes.get();
        }

        public void setLikes(String likes) {
            this.likes.set(likes);
        }
    }

    // icons for non-commercial use with attribution from: http://www.iconarchive.com/show/veggies-icons-by-iconicon/bananas-icon.html and http://www.iconarchive.com/show/collection-icons-by-archigraphs.html
    private final Image coffeeImage = new Image(
            "/image/trash-16.png"
    );
    private final Image fruitImage = new Image(
            "/image/trash-16.png"
    );
    }

