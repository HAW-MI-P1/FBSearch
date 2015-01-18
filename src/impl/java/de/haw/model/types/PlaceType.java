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
package de.haw.model.types;

/******************************************************************************
 *                                 Imports                                    *
 *****************************************************************************/

import java.util.List;

/******************************************************************************
 * Class Definition *
 *****************************************************************************/
public class PlaceType extends Type {

	/******************************************************************************
	 * Fields *
	 *****************************************************************************/

	private String name;
	private String category;
	private LocationType location;
	private List<CategoryType> category_list;

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

	public LocationType getLocation() {
		return location;
	}

	public void setLocation(LocationType location) {
		this.location = location;
	}

	public List<CategoryType> getCategory_list() {
		return category_list;
	}

	public void setCategory_list(List<CategoryType> category_list) {
		this.category_list = category_list;
	}

	/******************************************************************************
	 * Construction & Initialization *
	 *****************************************************************************/

	public PlaceType(String id) {
		super(id);

	}

    @Override
    public ResultType getType() {
        return ResultType.Place;
    }

    public PlaceType(String id, String name, String category, LocationType location) {
        super(id);
        this.name = name;
        this.category = category;
        this.location = location;
    }

    public PlaceType(String id, String name, String category, LocationType location,
                     List<CategoryType> category_list) {
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
