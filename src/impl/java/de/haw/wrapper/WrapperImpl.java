package de.haw.wrapper;

import de.haw.model.Person;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author lotte
 */
public class WrapperImpl implements Wrapper{

    private  RequestHandler requestHandler;
    private AuthHandler authHandler;

    public WrapperImpl(){
        requestHandler = RequestHandler.getInstance();
        authHandler = new AuthHandler();
        authHandler.login();
        requestHandler.getUserId();
    }

    public Collection<Person> collect(JSONObject requests){
        Collection<Person> personData = new ArrayList<Person>();
        //Mock Up
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
    
    public Collection<Person> collectExtended(JSONObject requests, Collection<Person> personsOfInterest)
    {
    	return collect(requests);
    }
}
