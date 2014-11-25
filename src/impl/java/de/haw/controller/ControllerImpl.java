/******************************************************************************
 * Modellierung von Informationssystemen - FBSearch
 ******************************************************************************
 * MIP-Group:       1
 * Component:       Controller
 *
 * Authors:         René, Hagen
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

import de.haw.model.*;
import de.haw.parser.Parser;
import de.haw.app.Logger;
import de.haw.db.DB;
import de.haw.wrapper.Wrapper;
import java.util.Collection;
import org.json.JSONObject;

/******************************************************************************
 *                              Class Definition                              *
 *****************************************************************************/

public class ControllerImpl implements Controller
{
	
/******************************************************************************
 *                                  Fields                                    *
 *****************************************************************************/

    public Parser  parser;
    public Wrapper wrapper;
    public DB      db;
    
/******************************************************************************
 *                         Construction & Initialization                      *
 *****************************************************************************/

	public ControllerImpl(Parser parser, Wrapper wrapper, DB db)
	{
		this.parser  = parser;
		this.wrapper = wrapper;
		this.db      = db;
	}

/******************************************************************************
 *                              Public Methods                                *
 *****************************************************************************/

	@Override
    public Collection<Person> search(int searchID, String naturalLanguage)
    {
        Logger.log("<search()>", ComponentID.Controller);
        
        JSONObject         requests = parser .parse  (naturalLanguage);
        Collection<Person> result   = wrapper.collect(requests);
                                      db     .save   (searchID, naturalLanguage, requests, result);
        
		return result;
    }

	@Override
	public Collection<Person> searchExtended(int searchID, int parentSearchID, String naturalLanguage)
    {
        Logger.log("<searchExtended()>", ComponentID.Controller);
        
        JSONObject         requests          = parser .parse          (naturalLanguage);
        Collection<Person> personsOfInterest = db     .load           (parentSearchID);
        Collection<Person> result            = wrapper.collectExtended(requests, personsOfInterest);
                                               db     .save           (searchID, naturalLanguage, requests, result);
        
		return result;
	}
}