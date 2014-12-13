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

import java.util.List;

/******************************************************************************
 *                                 Imports                                    *
 *****************************************************************************/

/******************************************************************************
 * Class Definition *
 *****************************************************************************/
public class Page extends Type {

	/******************************************************************************
	 * Fields *
	 *****************************************************************************/
	private String category;
	private String name;
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

	public List<Category> getCategory_list() {
		return category_list;
	}

	public void setCategory_list(List<Category> category_list) {
		this.category_list = category_list;
	}

	/******************************************************************************
	 * Construction & Initialization *
	 *****************************************************************************/

	public Page(String id) {
		super(id);

	}

	public Page(String id, String name, String category,
			List<Category> category_list) {
		super(id);
		this.name = name;
		this.category = category;
		this.category_list = category_list;
	}

	/******************************************************************************
	 * Public Methods *
	 *****************************************************************************/

}
