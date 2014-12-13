package de.haw.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by Fenja on 26.10.2014.
 */
public class SearchHistory {

    private ObservableList<String> searchStringList;

    public SearchHistory(){
        this.searchStringList = FXCollections.observableArrayList();
    }

    public SearchHistory(ObservableList<String> list){
        this.searchStringList = list;
    }

    public ObservableList<String> getSearchStringList(){
        System.out.println("getSearchStringList "+searchStringList.toString());
        return this.searchStringList;
    }

    public void newHistory(String searchString) {
        searchStringList.clear();
        searchStringList.add(searchString);
        System.out.println("newHistory "+searchStringList.toString());
    }

    public void addHistoryStep(String filterString) {
        searchStringList.add(filterString);
        System.out.println("addHistoryStep "+searchStringList.toString());
    }

    public String getLabelFormattedString() {
        StringBuffer listString = new StringBuffer();
        for(String step : searchStringList){
            listString.append(step+'\n');
        }
        return listString.toString();
    }
}
