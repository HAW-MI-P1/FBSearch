package de.haw.gui;

import de.haw.controller.Controller;
import de.haw.model.Person;
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
import java.io.InputStream;

public class GUIImpl extends Application implements GUI {
	
	private Stage primaryStage;
	private BorderPane rootLayout;


    /**
     * Note: http://stackoverflow.com/questions/24678021/pass-object-from-main-class-to-javafx-application
     */
    static public Controller controller;
    private SearchController searchController;
	
	/**
	 * The data as an observable list of Persons.
	 */
	private ObservableList<Person> personData = FXCollections.observableArrayList();

    public void setController (Controller controller){
    	GUIImpl.controller = controller;
    }
    private Controller getController (){
        return GUIImpl.controller;
    }

    public void run(){
       launch();
    }

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Search 4 Facebook");
		InputStream is = GUIImpl.class.getResourceAsStream("pic/fb.png");
		Image icon = new Image(is);
		this.primaryStage.getIcons().add(icon);

		try {
			// Load the root layout from the fxml file
			FXMLLoader loader = new FXMLLoader(GUIImpl.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();

            this.searchController = loader.getController();
            searchController.setController(getController());
            searchController.setGUIImpl(this);
		} catch (IOException e) {
			// Exception gets thrown if the fxml file could not be loaded
			e.printStackTrace();
		}

		showResultOverview();
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
	public void showResultOverview() {
		try {
			// Load the fxml file and set into the center of the main layout
			FXMLLoader loader = new FXMLLoader(GUIImpl.class.getResource("view/ResultOverview.fxml"));
			AnchorPane overviewPage = (AnchorPane) loader.load();
			rootLayout.setCenter(overviewPage);
			
			// Give the controller access to the main app
			ResultOverviewController controller = loader.getController();
			controller.setGUIImpl(this);
			
		} catch (IOException e) {
			// Exception gets thrown if the fxml file could not be loaded
			e.printStackTrace();
		}
	}
}
