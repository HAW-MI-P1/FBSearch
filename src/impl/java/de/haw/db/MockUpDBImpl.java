package de.haw.db;

import de.haw.model.types.Type;
import org.json.JSONObject;

import java.util.Collection;

/**
 * Created by Fenja on 13.12.2014.
 */
public class MockUpDBImpl implements DB {
    @Override
    public void save(int searchID, String naturalLanguage, JSONObject requests, Collection<Type> result) {

    }

    @Override
    public Collection<Type> load(int parentSearchID) {
        return null;
    }
}
