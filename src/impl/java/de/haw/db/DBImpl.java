package de.haw.db;

import de.haw.model.types.Type;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Fenja on 13.12.2014.
 */
public class DBImpl implements DB {

    private Collection<Type> db;

    public DBImpl() {
        this.db = new ArrayList<Type>();
    }
    @Override
    public void save(int searchID, String naturalLanguage, JSONObject requests, Collection<Type> result) {
        this.db = result;
    }

    @Override
    public Collection<Type> load(int parentSearchID) {
        return this.db;
    }
}
