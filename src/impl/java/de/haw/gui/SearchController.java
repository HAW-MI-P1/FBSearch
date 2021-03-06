package de.haw.gui;

import de.haw.controller.Controller;
import de.haw.model.SearchHistory;
import de.haw.model.exception.InternalErrorException;
import de.haw.model.exception.NoSuchEntryException;
import de.haw.model.types.ResultType;
import de.haw.model.types.Type;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Collection;

public class SearchController {

    @FXML
    private TextField searchStringField;
    @FXML
    private Label recommendationLabel;
    @FXML
    private Label informationLabel;
    @FXML
    private Label searchHistoryLabel;
    @FXML
    private TextField filterTextField;
    @FXML
    private CheckBox checkPicture;

    public int searchID;
    private int parentSearchID;
    private Controller controller;
    private SearchHistory searchHistory;
    private boolean supportsPictureDetection;

    // Reference to the main application
    private GUIImpl GUIImpl;

    public SearchController(){
        this.searchHistory = new SearchHistory();
    }

    public void setController (Controller controller){
        this.controller = controller;
        this.supportsPictureDetection = controller.supportsPictureDetection();
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param GUIImpl
     */
    public void setGUIImpl(GUIImpl GUIImpl) {
        this.GUIImpl = GUIImpl;
    }

        /**
         * Called when the user clicks on the New Search button.
         */
    @FXML
    private void handleNewSearch() {
        informationLabel.setText(" ");
        String searchString = searchStringField.getText();
        filterTextField.setText(" ");
        ObservableList<Type> resultData = FXCollections.observableArrayList();
        searchStringField.clear();

        if(supportsPictureDetection){
            checkPicture.setDisable(false);
        }

        try {
            int searchID = newSearchID();
            resultData.addAll(controller.search(searchID, searchString));
            searchHistory.newHistory(searchString); // Edited by RB & HH: changed order
            showSearchHistory();
            this.parentSearchID = searchID;
            if(!resultData.isEmpty())showResults(resultData);
        }catch(de.haw.model.exception.IllegalArgumentException ex0){
            //Tell user to correct searchString
            System.out.println("SearchController(GUI): Illegal argument: " + ex0.toString());
        }catch(IllegalArgumentException ex1){
            //Tell user to correct searchString
            System.out.println("SearchController(GUI): Illegal argument");
            informationLabel.setText("Please check your typing");
        }catch(NoSuchEntryException ex2){
            //Fill result table with "no results"
            System.out.println("SearchController(GUI): No Results");
            informationLabel.setText("No results were found");
        }catch(InternalErrorException ex3){
            //Internal Error occured
        }catch(Exception ex){
            System.out.println("SearchController(GUI): Only defined Exceptions should be thrown. Please check!");
            ex.printStackTrace();
            informationLabel.setText("Internal Error");
        }
        searchStringField.setText(searchString);
    }

    public void handleFilter() {
        String filterString = filterTextField.getText();
        ObservableList<Type> resultData = FXCollections.observableArrayList();
        boolean searchPicture = false;
        if(supportsPictureDetection){
            searchPicture = checkPicture.isSelected();
        }

        try{
            resultData.addAll(controller.searchExtended(newSearchID(),parentSearchID,filterString,searchPicture));
            searchHistory.addHistoryStep(filterString); // Edited by RB & HH: changed order
            showSearchHistory();
            if(!resultData.isEmpty())showResults(resultData);
            else discardResultTable();
            
        }catch(de.haw.model.exception.IllegalArgumentException ex0){
            //Tell user to correct searchString
            System.out.println("SearchController(GUI): Illegal argument: " + ex0.toString());
        }catch(IllegalArgumentException ex1){
            //Tell user to correct searchString
            System.out.println("SearchController(GUI): Illegal argument");
        }catch(NoSuchEntryException ex2){
            //Fill result table with "no results"
            System.out.println("SearchController(GUI): No Results");
            informationLabel.setText("No results were found");
        }catch(InternalErrorException ex3){
            //Internal Error occured
        }catch(Exception ex){
            System.out.println("SearchController(GUI): Only defined Exceptions should be thrown. Please check!");
            ex.printStackTrace();
            informationLabel.setText("Internal Error");
        }
    }

    private void discardResultTable() {
        GUIImpl.showNoResults();
        showSearchHistory();
        getAndSetRecommendations("place");
    }


    public int newSearchID(){
        return ++this.searchID;
    }

    public void showSearchHistory(){
        searchHistoryLabel.setText(searchHistory.getLabelFormattedString());
    }

    public void showResults(ObservableList<Type> resultData){
        boolean pictures = checkPicture.isSelected();
        Type type = resultData.get(0);
        GUIImpl.setResultData(resultData);
    	getAndSetRecommendations("place");
        if(type.getType().equals(ResultType.Event)){
            GUIImpl.showEventOverview();
        }else if(type.getType().equals(ResultType.Group)){
            GUIImpl.showGroupOverview();
        }else if(type.getType().equals(ResultType.Location)){
            GUIImpl.showLocationOverview();
        }else if(type.getType().equals(ResultType.Page)){
            GUIImpl.showPageOverview();
        }else if(type.getType().equals(ResultType.Place)){
            GUIImpl.showPlaceOverview();
        }else if(type.getType().equals(ResultType.User) && !pictures){
            GUIImpl.showUserOverview();
        }else if(type.getType().equals(ResultType.User) && pictures){
            GUIImpl.showPictureOverview();
        }else {
            //something somewhere went terribly wrong...
        }
    }
    
	private void getAndSetRecommendations(String category){
		Collection<String> recData= controller.getRecommendations(category);
		recommendationLabel.setText("");
		for(String data : recData){
			if(recommendationLabel.getText().isEmpty()){
	    		recommendationLabel.setText("Why don't you try: "+data);
			} else {
	    		recommendationLabel.setText(recommendationLabel.getText()+", "+data);
			}
		}
	}
}
