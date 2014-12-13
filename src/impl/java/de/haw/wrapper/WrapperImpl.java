package de.haw.wrapper;

import de.haw.model.types.Type;
import de.haw.model.types.UserType;
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
        testthings();
    }

    private void testthings() {
        System.out.println(requestHandler.searchForUser("Max"));
        System.out.println("-----------------------------------------------------------------\n");
        //System.out.println(requestHandler.searchForPlace("Reeperbahn"));
        System.out.println("-----------------------------------------------------------------\n");
        //System.out.println(requestHandler.searchForPlace("Cafe", 53.5499851056, 9.96649770869));
        System.out.println("-----------------------------------------------------------------\n");
        //System.out.println(requestHandler.searchForPlace("Cafe", 53.5499851056, 9.96649770869, 1.5345612));
    }

    public Collection<Type> collect(JSONObject requests){
        //Mock Up
        Collection<Type> resultData = new ArrayList<Type>();

        //TODO Differ Type caused by JSON Object
        resultData.add(new UserType("1234", "MaxMuster"));
        return resultData;
    }

    @Override
    public Collection<Type> collectExtended(JSONObject requests, Collection<Type> personsOfInterest) {
        return null;
    }
}
