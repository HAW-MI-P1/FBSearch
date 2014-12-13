package de.haw.gui;

import de.haw.model.types.Type;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class LocationOverviewController {
    @FXML
    private TableView<Type> locationTable;
    @FXML
    private TableColumn<Type, String> streetColumn;
    @FXML
    private TableColumn<Type, String> cityColumn;
    @FXML
    private TableColumn<Type, String> stateColumn;
    @FXML
    private TableColumn<Type, String> countryColumn;

	// Reference to the main application
	private GUIImpl GUIImpl;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
        streetColumn.setCellValueFactory(
                new PropertyValueFactory<Type, String>("Street"));
        cityColumn.setCellValueFactory(
                new PropertyValueFactory<Type, String>("City"));
        stateColumn.setCellValueFactory(
                new PropertyValueFactory<Type, String>("State"));
        countryColumn.setCellValueFactory(
                new PropertyValueFactory<Type, String>("Country"));
    }

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param GUIImpl
	 */
	public void setGUIImpl(GUIImpl GUIImpl) {
		this.GUIImpl = GUIImpl;
	}
}