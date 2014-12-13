/******************************************************************************
 * Modellierung von Informationssystemen - FBSearch
 ******************************************************************************
 * MIP-Group:       1
 * Component:       Model
 *
 * Authors:         Vitalij
 *
 * Updated:         2014.12.11
 *
 * Version:         0.01
 ******************************************************************************
 * Description:     ----
 *****************************************************************************/

/******************************************************************************
 *                                 Package                                    *
 *****************************************************************************/
package de.haw.model;

/******************************************************************************
 *                                 Imports                                    *
 *****************************************************************************/

import java.util.List;

/******************************************************************************
 * Class Definition *
 *****************************************************************************/
public class Place extends Type {

	/******************************************************************************
	 * Fields *
	 *****************************************************************************/

	private String name;
	private String category;
	private Location location;
	private List<Category> category_list;

	/******************************************************************************
	 * Getter / Setter *
	 *****************************************************************************/

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public List<Category> getCategory_list() {
		return category_list;
	}

	public void setCategory_list(List<Category> category_list) {
		this.category_list = category_list;
	}

	/******************************************************************************
	 * Construction & Initialization *
	 *****************************************************************************/

	public Place(String id) {
		super(id);

	}

	public Place(String id, String name, String category, Location location,
			List<Category> category_list) {
		super(id);
		this.name = name;
		this.category = category;
		this.location = location;
		this.category_list = category_list;
	}

	/******************************************************************************
	 * Public Methods *
	 *****************************************************************************/

}
