package de.haw.gui;

import de.haw.controller.Controller;
import de.haw.model.types.Type;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
	private ObservableList<Type> resultData = FXCollections.observableArrayList();

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
		this.primaryStage.setTitle("FBSearch");
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
    public ObservableList<Type> getResultData() {
        return resultData;
    }

    public void setResultData(ObservableList<Type> resultData) {
        this.resultData = resultData;
    }

    public void showEventOverview() {
        try {
            // Load the fxml file and set into the center of the main layout
            FXMLLoader loader = new FXMLLoader(GUIImpl.class.getResource("view/EventOverview.fxml"));
            AnchorPane overviewPage = (AnchorPane) loader.load();
            rootLayout.setCenter(overviewPage);

            // Give the controller access to the main app
            EventOverviewController controller = loader.getController();
            controller.setGUIImpl(this);

        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }
    }

    /**
     * Shows the group overview scene.
     */
    public void showGroupOverview() {
        try {
            // Load the fxml file and set into the center of the main layout
            FXMLLoader loader = new FXMLLoader(GUIImpl.class.getResource("view/GroupOverview.fxml"));
            AnchorPane overviewPage = (AnchorPane) loader.load();
            rootLayout.setCenter(overviewPage);

            // Give the controller access to the main app
            GroupOverviewController controller = loader.getController();
            controller.setGUIImpl(this);

        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }
    }

    /**
     * Shows the location overview scene.
     */
    public void showLocationOverview() {
        try {
            // Load the fxml file and set into the center of the main layout
            FXMLLoader loader = new FXMLLoader(GUIImpl.class.getResource("view/LocationOverview.fxml"));
            AnchorPane overviewPage = (AnchorPane) loader.load();
            rootLayout.setCenter(overviewPage);

            // Give the controller access to the main app
            LocationOverviewController controller = loader.getController();
            controller.setGUIImpl(this);

        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }
    }

    /**
     * Shows the page overview scene.
     */
    public void showPageOverview() {
        try {
            // Load the fxml file and set into the center of the main layout
            FXMLLoader loader = new FXMLLoader(GUIImpl.class.getResource("view/PageOverview.fxml"));
            AnchorPane overviewPage = (AnchorPane) loader.load();
            rootLayout.setCenter(overviewPage);

            // Give the controller access to the main app
            PageOverviewController controller = loader.getController();
            controller.setGUIImpl(this);

        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }
    }

    /**
     * Shows the place overview scene.
     */
    public void showPlaceOverview() {
        try {
            // Load the fxml file and set into the center of the main layout
            FXMLLoader loader = new FXMLLoader(GUIImpl.class.getResource("view/PlaceOverview.fxml"));
            AnchorPane overviewPage = (AnchorPane) loader.load();
            rootLayout.setCenter(overviewPage);

            // Give the controller access to the main app
            PlaceOverviewController controller = loader.getController();
            controller.setGUIImpl(this);

        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }
    }

    /**
     * Shows the user overview scene.
     */
    public void showUserOverview() {
        try {
            // Load the fxml file and set into the center of the main layout
            FXMLLoader loader = new FXMLLoader(GUIImpl.class.getResource("view/UserOverview.fxml"));
            AnchorPane overviewPage = (AnchorPane) loader.load();
            rootLayout.setCenter(overviewPage);

            // Give the controller access to the main app
            UserOverviewController controller = loader.getController();
            controller.setGUIImpl(this);

        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }
    }

    public void showNoResults() {
        Node node = this.rootLayout.getCenter();
        this.rootLayout.getChildren().remove(node); //<****Remove the node from children****>
        this.rootLayout.setCenter(null);
    }
}
