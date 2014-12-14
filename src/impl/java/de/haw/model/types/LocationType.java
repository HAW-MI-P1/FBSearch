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
public class LocationType extends Type {

	/******************************************************************************
	 * Fields *
	 *****************************************************************************/
	private String street;
	private String city;
	private String state;
	private String country;
	private String zip;
	private String latitude;
	private String longtitude;

	/******************************************************************************
	 * Getter / Setter *
	 *****************************************************************************/

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(String longtitude) {
		this.longtitude = longtitude;
	}

	/******************************************************************************
	 * Construction & Initialization *
	 *****************************************************************************/

	public LocationType(String street, String city, String state, String country,
                        String zip, String latitude, String longtitude) {
		this.street = street;
		this.city = city;
		this.state = state;
		this.country = country;
		this.zip = zip;
		this.latitude = latitude;
		this.longtitude = longtitude;
	}

    public LocationType(String street, String city, String state, String country) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    @Override
    public ResultType getType() {
        return ResultType.Location;
    }

    /******************************************************************************
	 * Public Methods *
	 *****************************************************************************/

}
