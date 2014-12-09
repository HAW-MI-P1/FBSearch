package de.haw.wrapper;

import de.haw.model.Person;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author lotte
 */
public class WrapperImpl implements Wrapper{

    private RequestHandler requestHandler;
    private AuthHandler authHandler;

    public WrapperImpl(){
        requestHandler = RequestHandler.getInstance();
        authHandler = new AuthHandler();
        authHandler.login();
        //testthings();
    }

    private void testthings() {
        System.out.println(requestHandler.searchForUser("Max"));
        System.out.println("-----------------------------------------------------------------\n");
        System.out.println(requestHandler.searchForPlace("Reeperbahn"));
        System.out.println("-----------------------------------------------------------------\n");
        System.out.println(requestHandler.searchForPlace("Cafe", 53.5499851056, 9.96649770869));
        System.out.println("-----------------------------------------------------------------\n");
        System.out.println(requestHandler.searchForPlace("Cafe", 53.5499851056, 9.96649770869, 1.5345612));
    }

    public Collection<Person> collect(JSONObject requests){
        //Mock Up
        Collection<Person> personData = new ArrayList<Person>();
        personData.add(new Person("Hans", "Muster"));
        return personData;
    }
    
    public Collection<Person> collectExtended(JSONObject requests, Collection<Person> personsOfInterest)
    {
    	return collect(requests);
    }
}
