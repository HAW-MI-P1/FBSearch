/******************************************************************************
 * Modellierung von Informationssystemen - FBSearch
 ******************************************************************************
 * MIP-Group:       1
 * Component:       Controller
 *
 * Authors:         Ren�, Hagen
 *
 * Updated:         2014.11.07
 *
 * Version:         0.01
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
import de.haw.model.ComponentID;
import de.haw.model.types.Type;
import de.haw.parser.Parser;
import de.haw.taxonomy.Taxonomy;
import de.haw.wrapper.Wrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/******************************************************************************
 *                              Class Definition                              *
 *****************************************************************************/

public class ControllerImpl implements Controller
{

/******************************************************************************
 *                                  Fields                                    *
 *****************************************************************************/

    public Parser   parser;
    public Wrapper wrapper;
    public DB       db;
    public Detector detector;
    public Taxonomy taxonomy;
    private JSONObject lastRequestResult = null;
    
/******************************************************************************
 *                         Construction & Initialization                      
 *****************************************************************************/

	public ControllerImpl(Parser parser, Wrapper wrapper, DB db, Detector detector, Taxonomy taxonomy)
	{
		this.parser   = parser;
		this.wrapper = wrapper;
		this.db       = db;
		this.detector = detector;
		this.taxonomy = taxonomy;
	}

/******************************************************************************
 *                              Public Methods                                *
 *****************************************************************************/

	@Override
    public Collection<Type> search(int searchID, String naturalLanguage)
    {
        Logger.log("<search()>", ComponentID.Controller);
        
        JSONObject       requests = parser.parse  (naturalLanguage);
        lastRequestResult = requests;
        Logger.log(">>" + requests, ComponentID.Controller);
	    
        Collection<Type> result   = wrapper.collect(requests);
                                    db    .save   (searchID, naturalLanguage, requests, result);
        
		return result;
    }

	@Override
	public Collection<Type> searchExtended(int searchID, int parentSearchID, String naturalLanguage)
    {
        Logger.log("<searchExtended()>", ComponentID.Controller);
        
        JSONObject         requests        = parser  .parse         (naturalLanguage);
        lastRequestResult = requests;
        Collection<Type> personsOfInterest = db      .load          (parentSearchID);
        Collection<Type> result            = wrapper.collectExtended(requests, personsOfInterest);
                         result            = detectObject           (result, requests);
                                             db      .save          (searchID, naturalLanguage, requests, result);
        
		return result;
	}

	private Collection<Type> detectObject(Collection<Type> result, JSONObject requests)
	{
		if (result == null)
		{
			return result;
		}
		
		try
		{
			Collection<Type> tempResult;
			HashSet<Type> set = new HashSet<Type>();
			
			// TODO give me an object to search for
			tempResult = detector.detectObject(result, "lena");     set.addAll(tempResult);
			tempResult = detector.detectObject(result, "elephant"); set.addAll(tempResult);
			tempResult = detector.detectObject(result, "lion");     set.addAll(tempResult);
			
			return new ArrayList<Type>(set);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			return result;
		}
	}

	@Override
	public Collection<String> searchRecs(String category)
	{
        Collection<String> result= new ArrayList<String>();
        if(lastRequestResult != null){
			try {
				String item = lastRequestResult.getString(category);
				item = item.substring(2, item.length() -2);
				result = taxonomy.search(item);
			} catch (JSONException e) {}
        }
		return result;
	}
}