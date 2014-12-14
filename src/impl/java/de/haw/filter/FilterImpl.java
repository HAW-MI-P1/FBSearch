package de.haw.filter;

import de.haw.model.types.LocationType;
import de.haw.model.types.Type;
import de.haw.wrapper.Wrapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Fenja on 14.12.2014.
 */
public class FilterImpl implements Filter {
    public Wrapper wrapper;

    public FilterImpl (Wrapper wrapper){
        this.wrapper = wrapper;
    }

    @Override
    public Collection<Type> collect(JSONObject requests) {
        Collection<Type> resultData = new ArrayList<Type>();
        // Get subject from requests
        String subject = "-";
        try {
            subject = requests.getString("subject");
            System.out.println("subject is: " + subject);
        } catch (JSONException e) {
            System.out.println("no subject");
        }
        // Get names from request
        List<String> names = getValues(requests, "name");
        // Get places from request
        List<String> places = getValues(requests, "place");//TODO should be "location" in the actual context
        // Get interests from request
        //List<String> interests = getValues(requests, "interests"); //interests won't work

        // Get operation from request
        List<String> operationList = getValues(requests, "operation"); //should only be one operation
        String operation = "";
        if(operationList.size()>0) operation = operationList.get(0);

        resultData = wrapper.searchForName(subject, names);
        System.out.println("Results by name: "+resultData.size());

        if(places.size() > 0){
            resultData = resultsWithLocation(resultData, places, operation);
        }

        System.out.println("Results by name and location: "+resultData.size());

        return resultData;
    }

    @Override
    public Collection<Type> collectExtended(JSONObject requests, Collection<Type> personsOfInterest) {
        return wrapper.collectExtended(requests, personsOfInterest);
    }

    private List<String> getValues(JSONObject requests, String valueType) {
        List<String> result = new ArrayList<String>();
        try {
            JSONArray p = requests.getJSONArray(valueType);
            for (int i = 0; i < p.length(); i++) {
                result.add(p.getString(i));
            }
        } catch (JSONException e) {
            System.out.println("no " + valueType + "s");
        }
        return result;
    }

    private Collection<Type> resultsWithLocation(Collection<Type> data, List<String> places, String operation){
        Collection<Type> resultData = new ArrayList<Type>();
        /*for(String place : places){
            System.out.println("Place: "+place.toString());
            for(Type result : data){
                LocationType loc = wrapper.searchLocation(result.getID());

                if(loc!=null && compareLocation(loc, place) ){
                    resultData.add(result);
                    System.out.println("Added: "+result);
                }
            }
        }*/

        return resultData;
    }

    private boolean compareLocation (LocationType location, String locName){
        System.out.println("compare "+
        location.toString()+" and "+locName);
        return(location.getCity().equals(locName) || location.getCountry().equals(locName) || location.getState().equals(locName));
    }
}
