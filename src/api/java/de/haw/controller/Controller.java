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

import de.haw.model.types.Type;

import java.util.Collection;

/******************************************************************************
 *                              Class Definition                              *
 *****************************************************************************/

public interface Controller
{
	
/******************************************************************************
 *                              Public Methods                                *
 *****************************************************************************/
	
    public Collection<Type> search        (int searchID,                     String naturalLanguage);
    public Collection<Type> searchExtended(int searchID, int parentSearchID, String naturalLanguage, boolean searchPicture);
    public Collection<String> getRecommendations(String category);

    boolean supportsPictureDetection();
}
