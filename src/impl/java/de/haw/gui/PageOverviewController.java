package de.haw.gui;

import javafx.fxml.FXML;

public class PageOverviewController {

	// Reference to the main application
	private GUIImpl GUIImpl;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param GUIImpl
	 */
	public void setGUIImpl(GUIImpl GUIImpl) {
		this.GUIImpl = GUIImpl;
	}
}