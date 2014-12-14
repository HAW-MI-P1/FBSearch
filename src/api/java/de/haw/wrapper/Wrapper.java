/******************************************************************************
 * Modellierung von Informationssystemen - FBSearch
 ******************************************************************************
 * MIP-Group:       1
 * Component:       Wrapper
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

package de.haw.wrapper;

/******************************************************************************
 *                                 Imports                                    *
 *****************************************************************************/

import de.haw.model.types.Type;
import org.json.JSONObject;

import java.util.Collection;
import java.util.List;

/******************************************************************************
 *                              Class Definition                              *
 *****************************************************************************/

public interface Wrapper
{
	
/******************************************************************************
 *                              Public Methods                                *
 *****************************************************************************/
	
    public Collection<Type> collect        (JSONObject requests);
    public Collection<Type> collectExtended(JSONObject requests, Collection<Type> personsOfInterest);

    public Collection<Type> searchForName (String type, List<String> names);
}
