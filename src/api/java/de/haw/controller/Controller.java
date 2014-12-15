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

import de.haw.model.types.Type;

import java.util.Collection;
import java.util.List;

/******************************************************************************
 *                              Class Definition                              *
 *****************************************************************************/

public interface Controller
{
	
/******************************************************************************
 *                              Public Methods                                *
 *****************************************************************************/
	
    public Collection<Type> search        (int searchID,                     String naturalLanguage);
    public Collection<Type> searchExtended(int searchID, int parentSearchID, String naturalLanguage);
    public Collection<String> searchRecs();
}
