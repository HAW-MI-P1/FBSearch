package de.haw.wrapper;

import de.haw.model.Person;
import org.json.JSONObject;

import java.util.Collection;

/**
 * Created by Fenja on 19.11.2014.
 */
public interface Wrapper {

    public Collection<Person> collect(JSONObject requests);
}