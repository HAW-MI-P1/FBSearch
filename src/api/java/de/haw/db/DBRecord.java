package de.haw.db;

import java.util.Collection;

import org.json.JSONObject;

import de.haw.model.Person;

public interface DBRecord {
	
	public int getSearchId();
	public String getNaturalLanguage();
	public JSONObject getRequests();
	public Collection<Person> getResult();
	
}
