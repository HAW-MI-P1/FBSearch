package de.haw.controller;

import de.haw.app.Logger;
import de.haw.app.LoggerImpl;
import de.haw.db.DBImpl;
import de.haw.model.Person;
import de.haw.parser.Parser;
import de.haw.parser.ParserImpl;
import de.haw.wrapper.Wrapper;
import de.haw.wrapper.WrapperImpl;
import org.json.JSONObject;

import java.util.Collection;
import java.util.logging.Level;

/**
 * Created by Fenja on 19.11.2014.
 */
public class ControllerImpl implements Controller{

    private Parser parser;
    private DBImpl db;
    private Wrapper wrapper;

    Logger logger = new LoggerImpl(Level.ALL);

    public ControllerImpl(){
        this.parser = new ParserImpl();
        this.db = new DBImpl();
        this.wrapper = new WrapperImpl();
    }

    public Collection<Person> search(String naturalLanguage){
        logger.Log(Level.INFO, "App", "Search: "+naturalLanguage);
        Collection<Person> personData = collect(parse(naturalLanguage));
        //save(personData);
        return personData;
    }

   public JSONObject parse(String naturalLanguage){
       JSONObject jsonObject = parser.parse(naturalLanguage);
       return jsonObject;
    }

    public Collection<Person> collect(JSONObject requests){
        Collection<Person> results = wrapper.collect(requests);
        return results;
    }

    public Collection<Person> save(int searchID, String naturalLanguage, JSONObject requests, Collection<Person> result){
        return null;
    }

}
