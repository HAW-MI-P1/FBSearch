package de.haw.db;

import java.util.Collection;

import org.json.JSONObject;

import de.haw.model.Person;

public class DBRecordImpl implements DBRecord {

	private int searchID;
	private String naturalLanguage;
	private JSONObject requests;
	private Collection<Person> result;
	
	public DBRecordImpl(int searchID, String naturalLanguage, JSONObject requests, Collection<Person> result) {
		this.searchID = searchID;
		this.naturalLanguage = naturalLanguage;
		this.requests = requests;
		this.result = result;
	}
	
	@Override
	public int getSearchId(){
		return this.searchID;
	}

	@Override
	public String getNaturalLanguage(){
		return this.naturalLanguage;
	}

	@Override
	public JSONObject getRequests(){
		return this.requests;
	}

	@Override
	public Collection<Person> getResult(){
		return this.result;
	}

}
