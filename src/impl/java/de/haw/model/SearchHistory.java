package de.haw.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import de.haw.model.exception.IllegalArgumentException;


/**
 * Created by Fenja on 26.10.2014.
 */
public class SearchHistory {

    private ObservableList<String> searchStringList;

    public SearchHistory(){
        this.searchStringList = FXCollections.observableArrayList();
    }

    public SearchHistory(ObservableList<String> list){
    	if(list == null) throw new IllegalArgumentException("list null");
        this.searchStringList = list;
    }

    public ObservableList<String> getSearchStringList(){
        return this.searchStringList;
    }

    public void newHistory(String searchString) {
        searchStringList.clear();
        if(searchString != null && !searchString.isEmpty()){
        	searchStringList.add(searchString);
        } else {
        	throw new IllegalArgumentException("search string parameter empty or null");
        }
    }

    public void addHistoryStep(String filterString) {
        if(filterString != null && !filterString.isEmpty()){
        	searchStringList.add(filterString);
        } else {
        	throw new IllegalArgumentException("search string parameter empty or null");
        }
    }

    public String getLabelFormattedString() {
        StringBuffer listString = new StringBuffer();
        for(String step : searchStringList){
            listString.append(step+'\n');
        }
        return listString.toString();
    }
}
