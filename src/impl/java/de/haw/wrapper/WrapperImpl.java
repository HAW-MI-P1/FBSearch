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
