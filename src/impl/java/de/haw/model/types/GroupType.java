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

/******************************************************************************
 * Class Definition *
 *****************************************************************************/
public class GroupType extends Type {

	/******************************************************************************
	 * Fields *
	 *****************************************************************************/

	private String name;

	private LocationType venue;
	private UserType owner_user;
	private PageType owner_page;

	/******************************************************************************
	 * Getter / Setter *
	 *****************************************************************************/

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocationType getVenue() {
		return venue;
	}

	public void setVenue(LocationType venue) {
		this.venue = venue;
	}

	public UserType getOwner_user() {
		return owner_user;
	}

	public void setOwner_user(UserType owner_user) {
		this.owner_user = owner_user;
	}

	public PageType getOwner_page() {
		return owner_page;
	}

	public void setOwner_page(PageType owner_page) {
		this.owner_page = owner_page;
	}

	/******************************************************************************
	 * Construction & Initialization *
	 *****************************************************************************/

	public GroupType(String id) {
		super(id);

	}

	@Override
	public ResultType getType() {
		return ResultType.Group;
	}

	public GroupType(String id, String name) {
		super(id);
		this.name = name;
	}

	public GroupType(String name, LocationType venue, UserType owner_user) {
		super();
		this.name = name;
		this.venue = venue;
		this.owner_user = owner_user;
	}

	public GroupType(String name, LocationType venue, PageType owner_page) {
		super();
		this.name = name;
		this.venue = venue;
		this.owner_page = owner_page;
	}

	public GroupType(String name, UserType owner_user) {
		super();
		this.name = name;
		this.owner_user = owner_user;
	}

	public GroupType(String name, PageType owner_page) {
		super();
		this.name = name;
		this.owner_page = owner_page;
	}

	/******************************************************************************
	 * Public Methods *
	 *****************************************************************************/

}
