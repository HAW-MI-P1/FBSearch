package de.haw.gui;

import de.haw.model.SearchHistory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import de.haw.model.Person;

public class PersonOverviewController {
	@FXML
	private TableView<Person> personTable;
	@FXML
	private TableColumn<Person, String> firstNameColumn;
	@FXML
	private TableColumn<Person, String> lastNameColumn;
    @FXML
    private TableColumn<Person, String> languageColumn;
    @FXML
    private TableColumn<Person, String> birthdayColumn;

    @FXML
    private ListView filterListView;

	@FXML
	private TextField filterTextField;
	@FXML
	private Button addFilterButton;

    private SearchHistory searchHistory;

	// Reference to the main application
	private MainApp mainApp;

	/**
	 * The constructor. The constructor is called before the initialize()
	 * method.
	 */
	public void setSearchHistory(SearchHistory searchHistory) {
        this.searchHistory = searchHistory;
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		// Initialize the person table
		firstNameColumn.setCellValueFactory(
				new PropertyValueFactory<Person, String>("firstName"));
		lastNameColumn.setCellValueFactory(
				new PropertyValueFactory<Person, String>("lastName"));

        //filterListView.setItems(FXCollections.observableArrayList());
        //filterListView.setItems(searchHistory.getSearchStringList());
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

		// Add observable list data to the table
		personTable.setItems(mainApp.getPersonData());
	}

	public void handleFilter() {
		String filterString = filterTextField.getText();
		 ObservableList<Person> personData = FXCollections.observableArrayList();

        searchHistory.addHistoryStep(filterString);
        filterListView.setItems(searchHistory.getSearchStringList());

        personData = showMockUpData(filterString);
        //personData = Controller.getDataByFilter(filterString);
        mainApp.setPersonData(personData);
        mainApp.showPersonOverview();
	}

    private ObservableList<Person> showMockUpData(String searchString) {
        ObservableList<Person> personData = FXCollections.observableArrayList();
        personData.add(new Person("Ruth", "Mueller"));
        personData.add(new Person("Cornelia", "Meier"));
        personData.add(new Person("Lydia", "Kunz"));
        personData.add(new Person("Anna", "Best"));
        return personData;
    }
}