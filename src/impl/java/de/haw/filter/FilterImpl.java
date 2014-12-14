package de.haw.filter;

import de.haw.model.types.Type;
import de.haw.wrapper.Wrapper;
import org.json.JSONObject;

import java.util.Collection;

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
        return wrapper.collect(requests);
    }

    @Override
    public Collection<Type> collectExtended(JSONObject requests, Collection<Type> personsOfInterest) {
        return wrapper.collectExtended(requests, personsOfInterest);
    }
}
