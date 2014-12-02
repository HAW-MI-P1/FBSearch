package de.haw.gui;

import de.haw.controller.Controller;
import de.haw.model.Person;
import de.haw.model.SearchHistory;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIImpl extends Application implements GUI {
	
	private Stage primaryStage;
	private BorderPane rootLayout;

    private SearchHistory searchHistory;

    private Controller controller;
	
	/**
	 * The data as an observable list of Persons.
	 */
	private ObservableList<Person> personData = FXCollections.observableArrayList();

    public void setController (Controller controller){
        this.controller = controller;
    }

    public void run(){
       launch();
    }

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Search 4 Facebook");
		Image icon = new Image(getClass().getResourceAsStream("pic/fb.png"));
		this.primaryStage.getIcons().add(icon);
        searchHistory = new SearchHistory();
		try {
			// Load the root layout from the fxml file
			FXMLLoader loader = new FXMLLoader(GUIImpl.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();

            SearchController searchController = loader.getController();
            searchController.setController(controller);
            searchController.setGUIImpl(this);
            searchController.setSearchHistory(this.searchHistory);
		} catch (IOException e) {
			// Exception gets thrown if the fxml file could not be loaded
			e.printStackTrace();
		}
		
		showPersonOverview();
	}
	
	/**
	 * Returns the main stage.
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

    /**
     * Returns the data as an observable list of Persons.
     * @return
     */
    public ObservableList<Person> getPersonData() {
        return personData;
    }

    public void setPersonData(ObservableList<Person> personData) {
        this.personData = personData;
    }

	/**
	 * Shows the person overview scene.
	 */
	public void showPersonOverview() {
		try {
			// Load the fxml file and set into the center of the main layout
			FXMLLoader loader = new FXMLLoader(GUIImpl.class.getResource("view/PersonOverview.fxml"));
			AnchorPane overviewPage = (AnchorPane) loader.load();
			rootLayout.setCenter(overviewPage);
			
			// Give the controller access to the main app
			PersonOverviewController controller = loader.getController();
			controller.setGUIImpl(this);
            controller.setSearchHistory(this.searchHistory);
			
		} catch (IOException e) {
			// Exception gets thrown if the fxml file could not be loaded
			e.printStackTrace();
		}
	}
}
