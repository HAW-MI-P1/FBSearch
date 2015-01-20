/******************************************************************************
 * Modellierung von Informationssystemen - FBSearch
 ******************************************************************************
 * MIP-Group:       1
 * Component:       Controller
 *
 * Authors:         Ren�, Hagen
 *
 * Updated:         2015.01.20
 *
 * Version:         0.02
 ******************************************************************************
 * Description:     ----
 *****************************************************************************/

/******************************************************************************
 *                                 Package                                    *
 *****************************************************************************/

package de.haw.controller;

/******************************************************************************
 *                                 Imports                                    *
 *****************************************************************************/

import de.haw.app.Logger;
import de.haw.db.DB;
import de.haw.detector.Detector;
import de.haw.fuzzy.Fuzzy;
import de.haw.model.ComponentID;
import de.haw.model.types.Type;
import de.haw.parser.Parser;
import de.haw.taxonomy.Taxonomy;
import de.haw.wrapper.Wrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/******************************************************************************
 *                              Class Definition                              *
 *****************************************************************************/

public class ControllerImpl implements Controller
{

/******************************************************************************
 *                                  Fields                                    *
 *****************************************************************************/

    public  Parser     parser;
    public  Wrapper    wrapper;
    public  DB         db;
    public  Detector   detector;
    public  Taxonomy   taxonomy;
    public  Fuzzy      fuzzy;
    private JSONObject lastRequestResult = null;
    
/******************************************************************************
 *                         Construction & Initialization                      *
 *****************************************************************************/

	public ControllerImpl(Parser parser, Wrapper wrapper, DB db, Detector detector, Taxonomy taxonomy, Fuzzy fuzzy)
	{
		this.parser   = parser;
		this.wrapper  = wrapper;
		this.db       = db;
		this.detector = detector;
		this.taxonomy = taxonomy;
        this.fuzzy    = fuzzy;
	}

/******************************************************************************
 *                                   Search                                   *
 *****************************************************************************/

	@Override
    public Collection<Type> search(int searchID, String naturalLanguage)
    {
        Logger.log("<search()>", ComponentID.Controller);
        
        // parse the natural language string
        JSONObject requests = parser.parse(naturalLanguage);
        
        // save the requests to give the user taxonomy / recommendations
        lastRequestResult = requests;
        
        // log the requests for better debugging
        Logger.log("Requests: " + requests, ComponentID.Controller);
	    
        // collect and merge the results from the social network api
        Collection<Type> result = wrapper.collect(requests);
        
        // save the input string, requests and the results in the database (enables filtering)
        db.save(searchID, naturalLanguage, requests, result);
        
        // return the resulted list of users
		return result;
    }

	@Override
	public Collection<Type> searchExtended(int searchID, int parentSearchID, String naturalLanguage, boolean searchPicture)
    {
        Logger.log("<searchExtended()>", ComponentID.Controller);
        
        // load results from the last search from the database
        Collection<Type> personsOfInterest = db.load(parentSearchID);
        Collection<Type> result = personsOfInterest;
        
		// parse the natural language string
		JSONObject requests = parser.parse(naturalLanguage);
		
		// save the requests to give the user taxonomy / recommendations
		lastRequestResult = requests;
		
        // log the requests for better debugging
        Logger.log("Requests: " + requests, ComponentID.Controller);
		
		// collect and merge the results from the social network api
		result = wrapper.collectExtended(requests, personsOfInterest);
		
        if(searchPicture)
        // only search for pictures when this flag is true
        {
            // search for pictures / detect objects on pictures
            result = detectObject(result, requests);
        }
        
        // save the input string, requests and the results in the database (enables filtering)
        db.save(searchID, naturalLanguage, requests, result);

        // return the resulted list of users
		return result;
	}

/******************************************************************************
 *                                Image Detection                             *
 *****************************************************************************/

	private Collection<Type> detectObject(Collection<Type> result, JSONObject requests)
	{
		
		// illegal input / no need to detect an empty collection
		if (result == null || result.size() == 0)
		{
			Logger.log("No results, image detection not required", ComponentID.Controller);
			return result;
		}
		
		try
		{
			Collection<Type> tempResult;
			HashSet<Type> listOfUsersWithMathedImages = new HashSet<Type>();
			
			// extract the objects to detect from the JSON requests 
			List<String> objectsToDetect = extractObjectsToDetect(requests);
            
			// loop through objects
			for (int i = 0; i < objectsToDetect.size(); i++)
			{
				try
				{
					String object = objectsToDetect.get(i);
					
					// detect object on all pictures in the result set
					tempResult = detector.detectObject(result, object);
					
					// add the matched users to the result list
					listOfUsersWithMathedImages.addAll(tempResult);
				}
				catch (Exception ex)
				{
					// ignore ex
				}
			}
			
			Logger.log("Users with images detected: " + listOfUsersWithMathedImages.size(), ComponentID.Controller);
			
			// returns a set with users that have matching pictures
			return listOfUsersWithMathedImages;
		}
		catch (Exception e)
		{
			throw new de.haw.model.exception.IllegalArgumentException("JSON object can't be interpreted");
		}
	}
	
	private List<String> extractObjectsToDetect(JSONObject requests) throws JSONException
	{
		List<String> objects = new ArrayList<String>();
		
		// interests
		if(requests.has("interests"))
		{
			String interestsSingleString = requests.getString("interests");
			interestsSingleString = interestsSingleString.substring(1, interestsSingleString.length() - 1);
			String[] interrests = interestsSingleString.split(",");
			for (String string : interrests)
			{
				string = string.substring(1, string.length() - 1);
				objects.add(string);
			}
		}
		
		// places
		if(requests.has("place"))
		{
			String placesSingleString = requests.getString("place");
			placesSingleString = placesSingleString.substring(1, placesSingleString.length() - 1);
			String[] places = placesSingleString.split(",");
			for (String string : places)
			{
				string = string.substring(1, string.length() - 1);
				objects.add(string);
			}
		}
		
		return objects;
	}

    @Override
    public boolean supportsPictureDetection()
    {
        return this.detector.supportsPictureDetection();
    }

/******************************************************************************
 *                   Taxonomy / Fuzzy Search / Recommendation                 *
 *****************************************************************************/

	@Override
	public Collection<String> getRecommendations(String category)
	{
        Collection<String> result= new ArrayList<String>();
        
        if(lastRequestResult != null)
        {
			try
			{
				String item = lastRequestResult.getString(category);
                item = item.substring(2, item.length() - 2);
                result.addAll(taxonomy.search(category, item));
			}
			catch (JSONException e)
			{
				
			}
			
            try
            {
                String name = lastRequestResult.getString("name");
                name = name.substring(2, name.length()-2);
                Collection<String> synonyms = fuzzy.getSynonym(name, 0x01);
                synonyms = clearSynonyms(synonyms);
                result.addAll(synonyms);
            }
            catch (JSONException e2)
            {
            	
            }
            catch (NullPointerException n)
            {
            	
            }
        }
		return result;
	}
	
    private Collection<String> clearSynonyms(Collection<String> synonyms)
    {
        Set<String> results = new HashSet<String>();
        for(String s : synonyms)
        {
            s.toLowerCase();
            results.add(s);
        }
        
        List<String> syn = new ArrayList<String>();
        Iterator<String> it = results.iterator();
        
        for(int i = 3; i > 0; i--)
        {
            if (it.hasNext())
            {
                syn.add(it.next().toString());
            }
        }
        
        return syn;
    }
}