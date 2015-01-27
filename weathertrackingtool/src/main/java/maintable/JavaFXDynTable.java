package maintable;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Random;

/**
 * Created by webonise on 22-01-2015.
 */
public class JavaFXDynTable  extends Application {

    private TableView tableView = new TableView();
    private Button btnNew = new Button("New Record");
    private model.Record Record;
    static Random random = new Random();

    static final String Day[] = {
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday"};



    ObservableList<model.Record> data = FXCollections.observableArrayList();
    int data_nextId = 0;

    @Override
    public void start(final Stage primaryStage) {
        primaryStage.setTitle("Table View");
        tableView.setEditable(true);
        Callback<TableColumn, TableCell> cellFactory =
                new Callback<TableColumn, TableCell>() {

                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCell();
                    }
                };

        btnNew.setOnAction(btnNewHandler);

        //init table
        //Un-editable column of "id"
        TableColumn col_id = new TableColumn("ID");
        tableView.getColumns().add(col_id);
        col_id.setCellValueFactory(
                new PropertyValueFactory<model.Record, String>("id"));

        //Editable columns
        for(int i=0; i<Day.length; i++){
            TableColumn col = new TableColumn(Day[i]);
            col.setCellValueFactory(
                    new PropertyValueFactory<model.Record, String>(
                            "value_" + String.valueOf(i)));
            tableView.getColumns().add(col);
            col.setCellFactory(cellFactory);
        }

        //Insert Button
        TableColumn col_action = new TableColumn<>("Action");
        col_action.setSortable(false);

        col_action.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<model.Record, Boolean>,
                        ObservableValue<Boolean>>() {

                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<model.Record, Boolean> p) {
                        return new SimpleBooleanProperty(p.getValue() != null);
                    }
                });

        col_action.setCellFactory(
                new Callback<TableColumn<model.Record, Boolean>, TableCell<model.Record, Boolean>>() {

                    @Override
                    public TableCell<model.Record, Boolean> call(TableColumn<model.Record, Boolean> p) {
                        return new ButtonCell(tableView);
                    }

                });
      tableView.getColumns().add(col_action);

        tableView.setItems(data);

        Group root = new Group();
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.getChildren().addAll(btnNew, tableView);
        root.getChildren().add(vBox);
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    //Define the button cell
    private class ButtonCell extends TableCell<model.Record, Boolean> {
        final Button cellButton = new Button("Delete");

        ButtonCell(final TableView tblView){

            cellButton.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent t) {
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

    EventHandler<ActionEvent> btnNewHandler =
            new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent t) {

                    //generate new Record with random number
                    model.Record newRec = new model.Record(
                            data_nextId,
                            random.nextInt(100),
                            random.nextInt(100),
                            random.nextInt(100),
                            random.nextInt(100),
                            random.nextInt(100));
                    data.add(newRec);
                    data_nextId++;

                }
            };

    class EditingCell extends TableCell<XYChart.Data, Number> {

        private TextField textField;

        public EditingCell() {}

        @Override
        public void startEdit() {

            super.startEdit();

            if (textField == null) {
                createTextField();
            }

            setGraphic(textField);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(String.valueOf(getItem()));
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }

        @Override
        public void updateItem(Number item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setGraphic(textField);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                } else {
                    setText(getString());
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()*2);
            textField.setOnKeyPressed(new EventHandler<KeyEvent>() {

                @Override
                public void handle(KeyEvent t) {
                    if (t.getCode() == KeyCode.ENTER) {
                        commitEdit(Integer.parseInt(textField.getText()));
                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }

}