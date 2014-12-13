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

    private Controller controller;

    // Reference to the main application
    private GUIImpl GUIImpl;
    private SearchHistory searchHistory;

    private int searchID;

    public SearchController(){
        this.searchID = 1;
    }

    public void setController (Controller controller){
        this.controller = controller;
    }

    public void setSearchHistory(SearchHistory searchHistory){
        this.searchHistory = searchHistory;
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
        ObservableList<Person> personData = FXCollections.observableArrayList();
        searchStringField.clear();

        searchHistory.newHistory(searchString);

        try {
            personData.addAll(controller.search(searchID, searchString));
            searchID++;
            GUIImpl.setPersonData(personData);
            GUIImpl.showPersonOverview();
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

    private ObservableList<Person> showMockUpData(String searchString) {
        ObservableList<Person> personData = FXCollections.observableArrayList();
        personData.add(new Person("Hans", "Muster"));
        personData.add(new Person("Ruth", "Mueller"));
        personData.add(new Person("Heinz", "Kurz"));
        personData.add(new Person("Cornelia", "Meier"));
        personData.add(new Person("Werner", "Meyer"));
        personData.add(new Person("Lydia", "Kunz"));
        personData.add(new Person("Anna", "Best"));
        personData.add(new Person("Stefan", "Meier"));
        personData.add(new Person("Martin", "Mueller"));
        return personData;
    }
}
