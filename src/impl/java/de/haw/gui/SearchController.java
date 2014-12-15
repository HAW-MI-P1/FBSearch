package de.haw.gui;

import java.util.Collection;

import de.haw.controller.Controller;
import de.haw.model.SearchHistory;
import de.haw.model.exception.InternalErrorException;
import de.haw.model.exception.NoSuchEntryException;
import de.haw.model.types.ResultType;
import de.haw.model.types.Type;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

    public int searchID;
    private int parentSearchID;
    private Controller controller;
    private SearchHistory searchHistory;

    // Reference to the main application
    private GUIImpl GUIImpl;

    public SearchController(){
        this.searchHistory = new SearchHistory();
    }

    public void setController (Controller controller){
        this.controller = controller;
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
        ObservableList<Type> resultData = FXCollections.observableArrayList();
        searchStringField.clear();

        searchHistory.newHistory(searchString);
        showSearchHistory();

        try {
            int searchID = newSearchID();
            resultData.addAll(controller.search(searchID, searchString));
            this.parentSearchID = searchID;
            if(!resultData.isEmpty())showResults(resultData);

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
        ObservableList<Type> resultData;

        try{
            searchHistory.addHistoryStep(filterString);
            resultData = (ObservableList<Type>) controller.searchExtended(newSearchID(),parentSearchID,filterString);
            if(!resultData.isEmpty())showResults(resultData);

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


    public int newSearchID(){
        return ++this.searchID;
    }

    public void showSearchHistory(){
        searchHistoryLabel.setText(searchHistory.getLabelFormattedString());
    }

    public void showResults(ObservableList<Type> resultData){
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
        }else if(type.getType().equals(ResultType.User)){
            GUIImpl.showUserOverview();
        }else {
            //something somewhere went terribly wrong...
        }
    }
    
	private void getAndSetRecommendations(String category){
		Collection<String> recData= controller.searchRecs(category);
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
