package de.haw.gui;

import de.haw.model.types.Type;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class EventOverviewController {
    @FXML
    private TableView<Type> eventTable;
    @FXML
    private TableColumn<Type, String> nameColumn;
    @FXML
    private TableColumn<Type, String> startTimeColumn;
    @FXML
    private TableColumn<Type, String> endTimeColumn;
    @FXML
    private TableColumn<Type, String> timezoneColumn;
    @FXML
    private TableColumn<Type, String> locationColumn;

	// Reference to the main application
	private GUIImpl GUIImpl;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
        // Initialize the person table
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<Type, String>("Name"));
        startTimeColumn.setCellValueFactory(
                new PropertyValueFactory<Type, String>("Start Time"));
        endTimeColumn.setCellValueFactory(
                new PropertyValueFactory<Type, String>("End Time"));
        timezoneColumn.setCellValueFactory(
                new PropertyValueFactory<Type, String>("Timezone"));
        locationColumn.setCellValueFactory(
                new PropertyValueFactory<Type, String>("Location"));
    }

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param GUIImpl
	 */
	public void setGUIImpl(GUIImpl GUIImpl) {
		this.GUIImpl = GUIImpl;

		// Add observable list data to the table
        eventTable.setItems(GUIImpl.getResultData());
	}
}