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

import de.haw.model.WebPicture;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/******************************************************************************
 *                                 Imports                                    *
 *****************************************************************************/

/******************************************************************************
 * Class Definition *
 *****************************************************************************/
public class UserType extends Type {

	/******************************************************************************
	 * Fields *
	 *****************************************************************************/
	private String name;
	private String first_name;
	private String last_name;
    private String city;
	private String link;
	private String updated_time;
	private boolean is_silhouette;
	private String uri;
	private List<WebPicture> pictures = new ArrayList<WebPicture>();

	/******************************************************************************
	 * Getter / Setter *
	 *****************************************************************************/

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getUpdated_time() {
		return updated_time;
	}

	public void setUpdated_time(String updated_time) {
		this.updated_time = updated_time;
	}

	public boolean isIs_silhouette() {
		return is_silhouette;
	}

	public void setIs_silhouette(boolean is_silhouette) {
		this.is_silhouette = is_silhouette;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public List<WebPicture> getPictures() {
		return pictures;
	}

	public void setPictures(List<WebPicture> pictures) {
		this.pictures = pictures;
	}

    public String getPicture(){
        String picture = null;
        if(this.pictures.size() > 0){
            picture = this.pictures.get(0).getUrl().toString();
        }
        return picture;
    }

    public void setPicture(String picture){
        try {
            this.pictures.add(new WebPicture(new URL(picture)));
        } catch (MalformedURLException e) {
        }
    }

	/******************************************************************************
	 * Construction & Initialization *
	 *****************************************************************************/

	public UserType(String id) {
		super(id);

	}

	@Override
	public ResultType getType() {
		return ResultType.User;
	}

	public UserType(String id, String name) {
		super(id);
		this.name = name;
	}

	public UserType(String id, String name, String first_name, String last_name) {
		super(id);
		this.name = name;
		this.first_name = first_name;
		this.last_name = last_name;
	}

    public UserType(String id, String name, String first_name, String last_name, String city) {
        super(id);
        this.name = name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.city = city;
    }

	public UserType(String id, String name, String first_name,
			String last_name, String link, String updated_time) {
		super(id);
		this.name = name;
		this.first_name = first_name;
		this.last_name = last_name;
		this.link = link;
		this.updated_time = updated_time;
	}

	/******************************************************************************
	 * Public Methods *
	 *****************************************************************************/
	
	public void addPicture(WebPicture picture)
	{
		pictures.add(picture);
	}
	
}
