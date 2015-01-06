/******************************************************************************
 * Modellierung von Informationssystemen - FBSearch
 ******************************************************************************
 * MIP-Group:       1
 * Component:       Fuzzy
 *
 * Authors:         Florian
 *
 * Updated:         2014.11.25
 *
 * Version:         0.01
 ******************************************************************************
 * Description:     ----
 *****************************************************************************/

/******************************************************************************
 *                                 Package                                    *
 *****************************************************************************/

package de.haw.fuzzy;

import java.util.Collection;

/******************************************************************************
 *                              Class Definition                              *
 *****************************************************************************/

public interface Fuzzy
{
	
/******************************************************************************
 *                              Public Methods                                *
 *****************************************************************************/
	
	/**
	 * 
	 * @param word to find synonyms for.
	 * @param opt set the type of fuzzy search. <br>
	 * 0x01: Search for synonym sets<br>
	 * 0x02: Search for similar words<br>
	 * 0x04: Search for substrings containing this words<br>
	 * 0x08: Search for the words at the beginning<br>
	 * 0x0F: Enable all search options<br>
	 * @return Array of all matches.<br>
	 */
	public Collection<String> getSynonym(String word, int opt);
}
