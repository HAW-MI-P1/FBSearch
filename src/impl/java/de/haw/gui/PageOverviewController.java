package de.haw.gui;

import de.haw.model.types.Type;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class PageOverviewController {
    @FXML
    private TableView<Type> pageTable;
    @FXML
    private TableColumn<Type, String> nameColumn;
    @FXML
    private TableColumn<Type, String> categoryColumn;

	// Reference to the main application
	private GUIImpl GUIImpl;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<Type, String>("Name"));
        categoryColumn.setCellValueFactory(
                new PropertyValueFactory<Type, String>("ID"));
    }

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param GUIImpl
	 */
	public void setGUIImpl(GUIImpl GUIImpl) {
		this.GUIImpl = GUIImpl;
        pageTable.setItems(GUIImpl.getResultData());
	}
}