package de.haw.db;

import de.haw.model.Person;
import org.json.JSONObject;

import java.util.Collection;

/**
 * Created by Fenja on 19.11.2014.
 */
public interface DB {

    public Collection<Person> save(int searchID, String naturalLanguage, JSONObject requests, Collection<Person> result);

}
