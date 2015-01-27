package controller;

/**
 * Created by webonise on 22-01-2015.
 */

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Persons;

import java.util.ArrayList;

public class PersonTableController {
    @FXML
    private TextField filterField;
    @FXML
    private TableView<Persons> personTable;
    @FXML
    private TableColumn<Persons, String> firstNameColumn;
    @FXML
    private TableColumn<Persons, String> lastNameColumn;

    private ObservableList<Persons> masterData = FXCollections.observableArrayList();
    private ObservableList<Persons> filteredData = FXCollections.observableArrayList();

    /**
     * The constructor. The constructor is called before the initialize()
     * method.
     */
    public PersonTableController() {
        // Add some sample data to the master data
        masterData.add(new Persons("Hans", "Muster"));
        masterData.add(new Persons("Ruth", "Mueller"));
        masterData.add(new Persons("Heinz", "Kurz"));
        masterData.add(new Persons("Cornelia", "Meier"));
        masterData.add(new Persons("Werner", "Meyer"));
        masterData.add(new Persons("Lydia", "Kunz"));
        masterData.add(new Persons("Anna", "Best"));
        masterData.add(new Persons("Stefan", "Meier"));
        masterData.add(new Persons("Martin", "Mueller"));

        // Initially add all data to filtered data
        filteredData.addAll(masterData);

        // Listen for changes in master data.
        // Whenever the master data changes we must also update the filtered data.
        masterData.addListener(new ListChangeListener<Persons>() {
            @Override
            public void onChanged(Change<? extends Persons> change) {
                updateFilteredData();
            }
        });
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the person table
        firstNameColumn.setCellValueFactory(
                new PropertyValueFactory<Persons, String>("firstName"));
        lastNameColumn.setCellValueFactory(
                new PropertyValueFactory<Persons, String>("lastName"));

        // Add filtered data to the table
        personTable.setItems(filteredData);

        // Listen for text changes in the filter text field
        filterField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                updateFilteredData();
            }
        });
    }

    /**
     * Updates the filteredData to contain all data from the masterData that
     * matches the current filter.
     */
    private void updateFilteredData() {
        filteredData.clear();

        for (Persons p : masterData) {
            if (matchesFilter(p)) {
                filteredData.add(p);
            }
        }

        // Must re-sort table after items changed
        reapplyTableSortOrder();
    }

    /**
     * Returns true if the person matches the current filter. Lower/Upper case
     * is ignored.
     *
     * @param person
     * @return
     */
    private boolean matchesFilter(Persons person) {
        String filterString = filterField.getText();
        if (filterString == null || filterString.isEmpty()) {
            // No filter --> Add all.
            return true;
        }

        String lowerCaseFilterString = filterString.toLowerCase();

        if (person.getFirstName().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
            return true;
        } else if (person.getLastName().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
            return true;
        }

        return false; // Does not match
    }

    private void reapplyTableSortOrder() {
        ArrayList<TableColumn<Persons, ?>> sortOrder = new ArrayList<>(personTable.getSortOrder());
        personTable.getSortOrder().clear();
        personTable.getSortOrder().addAll(sortOrder);
    }
}