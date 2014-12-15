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

import java.util.List;

/******************************************************************************
 *                                 Imports                                    *
 *****************************************************************************/

/******************************************************************************
 * Class Definition *
 *****************************************************************************/
public class PageType extends Type {

	/******************************************************************************
	 * Fields *
	 *****************************************************************************/
	private String category;
	private String name;
	private List<CategoryType> category_list;

	private String about;
	private LocationType location;

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

	public List<CategoryType> getCategory_list() {
		return category_list;
	}

	public void setCategory_list(List<CategoryType> category_list) {
		this.category_list = category_list;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public LocationType getLocation() {
		return location;
	}

	public void setLocation(LocationType location) {
		this.location = location;
	}

	/******************************************************************************
	 * Construction & Initialization *
	 *****************************************************************************/

	public PageType(String id) {
		super(id);

	}

	@Override
	public ResultType getType() {
		return ResultType.Page;
	}

	public PageType(String id, String name, String category) {
		super(id);
		this.name = name;
		this.category = category;
	}

	public PageType(String id, String name, String category,
			List<CategoryType> category_list) {
		super(id);
		this.name = name;
		this.category = category;
		this.category_list = category_list;
	}

	public PageType(String id, String category, String name,
			List<CategoryType> category_list, String about,
			LocationType location) {
		super(id);
		this.category = category;
		this.name = name;
		this.category_list = category_list;
		this.about = about;
		this.location = location;
	}

	/******************************************************************************
	 * Public Methods *
	 *****************************************************************************/

}
