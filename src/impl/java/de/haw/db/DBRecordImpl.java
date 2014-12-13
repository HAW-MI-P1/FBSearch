package de.haw.db;

import de.haw.model.types.Type;
import org.json.JSONObject;

import java.util.Collection;

public class DBRecordImpl implements DBRecord {

	private int searchID;
	private String naturalLanguage;
	private JSONObject requests;
	private Collection<Type> result;
	
	public DBRecordImpl(int searchID, String naturalLanguage, JSONObject requests, Collection<Type> result) {
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
	public Collection<Type> getResult(){
		return this.result;
	}

}
