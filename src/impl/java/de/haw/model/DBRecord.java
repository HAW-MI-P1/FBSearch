package de.haw.model;

import java.util.Collection;

import org.json.JSONObject;

public interface DBRecord {
	
	public int getSearchId();
	public String getNaturalLanguage();
	public JSONObject getRequests();
	public Collection<Person> getResult();
	
}
