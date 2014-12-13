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
public class EventType extends Type {

	/******************************************************************************
	 * Fields *
	 *****************************************************************************/

	private String name;
	private String start_time;
	private String end_time;
	private String timezone;
	private String location;

	/******************************************************************************
	 * Getter / Setter *
	 *****************************************************************************/

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	/******************************************************************************
	 * Construction & Initialization *
	 *****************************************************************************/

	public EventType(String id) {
		super(id);

	}

    @Override
    public ResultType getType() {
        return ResultType.Event;
    }

    public EventType(String id, String name, String start_time, String end_time,
                     String timezone, String location) {
		super(id);
		this.name = name;
		this.start_time = start_time;
		this.end_time = end_time;
		this.timezone = timezone;
		this.location = location;
	}

	/******************************************************************************
	 * Public Methods *
	 *****************************************************************************/

}
