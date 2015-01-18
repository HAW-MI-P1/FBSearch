package de.haw.db;

import de.haw.model.types.ResultType;
import de.haw.model.types.Type;

import java.util.Collection;
import java.util.List;

/**
 * Created by Fenja on 06.01.2015.
 */
public interface SearchDB {

    public boolean execute(String sql);

    public Collection<Type> getEntries(ResultType type, List<String> names);

}
