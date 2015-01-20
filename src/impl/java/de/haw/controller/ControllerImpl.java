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

    public Parser   parser;
    public Wrapper wrapper;
    public DB       db;
    public Detector detector;
    public Taxonomy taxonomy;
    public Fuzzy fuzzy;
    private JSONObject lastRequestResult = null;
    
/******************************************************************************
 *                         Construction & Initialization                      *
 *****************************************************************************/

	public ControllerImpl(Parser parser, Wrapper wrapper, DB db, Detector detector, Taxonomy taxonomy, Fuzzy fuzzy)
	{
		this.parser   = parser;
		this.wrapper = wrapper;
		this.db       = db;
		this.detector = detector;
		this.taxonomy = taxonomy;
        this.fuzzy = fuzzy;
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
	public Collection<Type> searchExtended(int searchID, int parentSearchID, String naturalLanguage, boolean searchPicture)
    {
        Logger.log("<searchExtended()>", ComponentID.Controller);
        Collection<Type> personsOfInterest = db.load(parentSearchID);
        Collection<Type> result = personsOfInterest;
        JSONObject requests = null;
        //this makes it possible to only filter for pictures
        if((naturalLanguage != null) && (!naturalLanguage.isEmpty()) && (naturalLanguage.length() > 1)) {
            requests = parser.parse(naturalLanguage);
            lastRequestResult = requests;
            result = wrapper.collectExtended(requests, personsOfInterest);
        }
        if(searchPicture) {
            result = detectObject(result);
        }
        db.save(searchID, naturalLanguage, requests, result);
        
		return result;
	}

	private Collection<Type> detectObject(Collection<Type> result)
	{
		if (result == null){
			return result;
		}
		
		if(result.size() == 0)
		{
			Logger.log("No results, image detection not required.", ComponentID.Controller);	
			return result;
		}
		
		try{
			Collection<Type> tempResult;
			HashSet<Type> set = new HashSet<Type>();
			
			// TODO give me an object to search for
			tempResult = detector.detectObject(result, "lena");
            set.addAll(tempResult);
			tempResult = detector.detectObject(result, "elephant");
            set.addAll(tempResult);
			tempResult = detector.detectObject(result, "lion");
            set.addAll(tempResult);
			Logger.log("Images detected: "+set.size(), ComponentID.Controller);
			
			return set;
		}catch (Exception e){
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
                item = item.substring(2, item.length() - 2);
                result.addAll(taxonomy.search(category, item));
			} catch (JSONException e) {}
            try {
                String name = lastRequestResult.getString("name");
                name = name.substring(2, name.length()-2);
                Collection<String> synonyms = fuzzy.getSynonym(name, 0x01);
                synonyms = clearSynonyms(synonyms);
                result.addAll(synonyms);
            } catch (JSONException e2) {}
            catch (NullPointerException n) {}
        }
		return result;
	}

    @Override
    public boolean supportsPictureDetection() {
        return this.detector.supportsPictureDetection();
    }

    private Collection<String> clearSynonyms(Collection<String> synonyms){
        Set<String> results = new HashSet<String>();
        for(String s : synonyms){
            s.toLowerCase();
            results.add(s);
        }
        List<String> syn = new ArrayList<String>();
        Iterator it = results.iterator();
        for(int i = 3; i > 0; i--) {
            if (it.hasNext()){
                syn.add(it.next().toString());
            }
        }
        return syn;
    }
}