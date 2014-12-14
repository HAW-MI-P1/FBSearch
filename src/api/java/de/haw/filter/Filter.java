package de.haw.filter;

import de.haw.model.types.Type;
import org.json.JSONObject;

import java.util.Collection;

/**
 * Created by Fenja on 14.12.2014.
 */
public interface Filter {
    public Collection<Type> collect        (JSONObject requests);
    public Collection<Type> collectExtended(JSONObject requests, Collection<Type> personsOfInterest);
}
