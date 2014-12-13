package de.haw.db;

import de.haw.model.types.Type;
import org.json.JSONObject;

import java.util.Collection;

public interface DBRecord {
	
	public int getSearchId();
	public String getNaturalLanguage();
	public JSONObject getRequests();
	public Collection<Type> getResult();
	
}
