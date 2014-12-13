package de.haw.gui;

import de.haw.controller.Controller;
import de.haw.model.Person;
import de.haw.model.SearchHistory;
import de.haw.model.exception.InternalErrorException;
import de.haw.model.exception.NoSuchEntryException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SearchController {

    @FXML
    private TextField searchStringField;
    @FXML
    private Button newSearchButton;
    @FXML
    private Label informationLabel;

    public int searchID;
    @FXML
    private Label searchHistoryLabel;

    @FXML
    private TextField filterTextField;
    @FXML
    private Button addFilterButton;

    private int parentSearchID;
    private Controller controller;
    private ResultOverviewController resultOverviewController;

    // Reference to the main application
    private GUIImpl GUIImpl;
    private SearchHistory searchHistory;

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

    public void setResultOverviewController(ResultOverviewController resultOverviewController){
        this.resultOverviewController = resultOverviewController;
    }

        /**
         * Called when the user clicks on the New Search button.
         */
    @FXML
    private void handleNewSearch() {
        informationLabel.setText(" ");
        String searchString = searchStringField.getText();
        ObservableList<Person> personData = FXCollections.observableArrayList();
        searchStringField.clear();

        searchHistory.newHistory(searchString);
        showSearchHistory();

        try {
            int searchID = newSearchID();
            personData.addAll(controller.search(searchID, searchString));
            this.parentSearchID = searchID;
            GUIImpl.setPersonData(personData);
            GUIImpl.showResultOverview();
        }catch(IllegalArgumentException ex1){
            //Tell user to correct searchString
            System.out.println("SearchController(GUI): Illegal argument");
            informationLabel.setText("Please check your typing");
        }catch(NoSuchEntryException ex2){
            //Fill result table with "no results"
            System.out.println("SearchController(GUI): No Results");
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
        ObservableList<Person> personData = FXCollections.observableArrayList();

        try{
            searchHistory.addHistoryStep(filterString);
            personData = (ObservableList<Person>) controller.searchExtended(newSearchID(),parentSearchID,filterString);
            GUIImpl.setPersonData(personData);
            GUIImpl.showResultOverview();
        }catch(IllegalArgumentException ex1){
            //Tell user to correct searchString
            System.out.println("SearchController(GUI): Illegal argument");
        }catch(NoSuchEntryException ex2){
            //Fill result table with "no results"
            System.out.println("SearchController(GUI): No Results");
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
        System.out.println(searchHistory.getLabelFormattedString());
        searchHistoryLabel.setText(searchHistory.getLabelFormattedString());
    }
}
