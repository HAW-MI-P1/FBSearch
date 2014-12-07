package de.haw.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.haw.model.exception.ConnectionException;
import de.haw.model.exception.IllegalArgumentException;
import de.haw.model.exception.InternalErrorException;
import de.haw.model.exception.NoSuchEntryException;
import de.haw.model.DBRecord;
import de.haw.model.Person;

public class DBImpl implements DB{

	// JDBC driver name and database URL
	private final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	
	private Connection         conn = null;
	private PreparedStatement  stmt = null;
	
	public DBImpl(){}
	
	public void connect(String url, String user, String pass) throws ConnectionException {
		if(url  == null || url.equals("") ) throw new IllegalArgumentException("Illegal url: "  + url);
		if(user == null || user.equals("")) throw new IllegalArgumentException("Illegal user: " + user);
		if(pass == null || pass.equals("")) throw new IllegalArgumentException("Illegal pass: " + pass);
		
		try {
			// Register JDBC driver
			Class.forName(DBImpl.JDBC_DRIVER);
			// Open a connection
			this.conn = DriverManager.getConnection(url, user, pass);

		} 
		catch (ClassNotFoundException | SQLException e) 
		{ e.printStackTrace();throw new ConnectionException(); }
		
	}
	
	public void close(){
		try {
			this.conn.close();
		} catch (Exception se)
		{ throw new InternalErrorException("Connection already closed!"); }// end finally try
		
	}

	@Override
	public void save(int searchID, String naturalLanguage, JSONObject requests, Collection<Person> result){
		
		JSONArray persons = new JSONArray();
		
		for (Person person : result) {
			JSONObject p = new JSONObject();
			try {
				p.put("firstName", person.getFirstName());
				p.put("lastName", person.getLastName());
				p.put("street", person.getStreet());
				p.put("postalCode", person.getPostalCode());
				p.put("city", person.getCity());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
//			p.put("birthday", person.getBirthday());
			
			persons.put(p);
		}
		
		JSONObject json = new JSONObject();
		try {
			json.put("naturalLanguage", naturalLanguage);
			json.put("request", requests);
			json.put("result", persons);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		this.put(searchID, json.toString());
	}
	
	public void put(int searchID, String value){
		if(value == null || value.equals("")) throw new IllegalArgumentException("Illegal value: " + value);
		if(searchID == 0) throw new IllegalArgumentException("Illegal searchID: " + searchID);
		
		try {
			this.stmt = this.conn.prepareStatement("INSERT INTO api_results (k, v) VALUES (?, ?)");
			this.stmt.setInt(1, searchID);
			this.stmt.setString(2, value);
			this.stmt.executeUpdate();
			
			// Clean-up environment
			this.stmt.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			throw new InternalErrorException("Insert operation failed! " + se.getMessage());
		} finally {
			// finally block used to close resources
			try {
				if (this.stmt != null){
					this.stmt.close();
					
				}
			} catch (SQLException se2) 
			{throw new InternalErrorException("Statement cannot be closed! " + se2.getMessage());}// nothing we can do
			
		}// end try
		
	}
	
	public DBRecord load_oldStyle(int parentSearchID) {
		String resultStr = this.get(parentSearchID);
		
		JSONObject jsonResult = null;
		try {
			jsonResult = new JSONObject(resultStr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		LinkedList<Person> persons = new LinkedList<Person>();
		
		JSONArray arr = null;
		try {
			arr = jsonResult.getJSONArray("result");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < arr.length(); i++) {
			JSONObject person = null;
			try {
				person = (JSONObject) arr.get(i);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			Person p = new Person();
			try {
				p.setFirstName(person.getString("firstName"));
				p.setLastName(person.getString("lastName"));
				p.setStreet(person.getString("street"));
				p.setPostalCode(person.getInt("postalCode"));
				p.setCity(person.getString("city"));
//				p.setBirthday(person.get("birthday"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			
			persons.push(p);
		}
		
		DBRecord r = null;
		try {
			r = new DBRecordImpl(parentSearchID,jsonResult.getString("naturalLanguage"),jsonResult.getJSONObject("request"), persons );
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return r;
	}
	
	@Override
    public Collection<Person> load(int parentSearchID)
    {
    	return load_oldStyle(parentSearchID).getResult();
    }
    
	public String get(int searchID) {
		if(searchID   == 0) throw new IllegalArgumentException("Illegal Key: " + searchID);
		
		try {

			// Execute a query
			this.stmt = this.conn.prepareStatement("SELECT v FROM api_results WHERE k=?");
			this.stmt.setInt(1, searchID);
			
			ResultSet rs  = this.stmt.executeQuery();
			String result = null;
			// Extract data from result set
			while (rs.next()) {
				// Retrieve by column name
				result = rs.getString("v");
				
			}
			// Clean-up environment
			rs.close();
			this.stmt.close();
			
			if(result == null || result.equals(""))
			{ throw new NoSuchEntryException(searchID + ""); }
			
			return result;
		} catch (SQLException se) {
			throw new InternalErrorException("Query operation failed!");
			
		} finally {
			// finally block used to close resources
			try {
				if (this.stmt != null){
					this.stmt.close();
				}
			} catch (SQLException se2) 
			{ throw new InternalErrorException("Statement cannot be closed!"); }// nothing we can do
			
		}// end try
		
	}

	

}