/******************************************************************************
 * Modellierung von Informationssystemen - FBSearch
 ******************************************************************************
 * MIP-Group:       1
 * Component:       Model
 *
 * Authors:         Raimund, Lotte, Vitalij
 *
 * Updated:         2014.11.07
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

import de.haw.model.types.ResultType;
import de.haw.model.types.Type;

import java.util.Calendar;

/******************************************************************************
 *                              Class Definition                              *
 *****************************************************************************/

public class Person extends Type
{
    
/******************************************************************************
 *                                  Fields                                    *
 *****************************************************************************/
    
    private String   userID;
    private String   firstname;
    private String   lastname;
	private Calendar birthday;
	private String   street;
	private int      postalCode;
    private String   city;
    
/******************************************************************************
 *                              Getter / Setter                               *
 *****************************************************************************/

    public String   getUserID     ()                  { return userID;                  }
    public void     setUserID     (String userID)     {   this.userID     = userID;     }
    
    public String   getFirstName  ()                  { return firstname;               }
    public void     setFirstName  (String firstname)  {   this.firstname  = firstname;  }
    
    public String   getLastName   ()                  { return lastname;                }
    public void     setLastName   (String lastname)   {   this.lastname   = lastname;   }
    
    public Calendar getBirthday   ()                  { return birthday;                }
    public void     setBirthday   (Calendar birthday) {   this.birthday   = birthday;   }
    
    public String   getStreet     ()                  { return street;                  }
    public void     setStreet     (String street)     {   this.street     = street;     }
    
    public int      getPostalCode ()                  { return postalCode;              }
    public void     setPostalCode (int postalCode)    {   this.postalCode = postalCode; }
    
    public String   getCity       ()                  { return city;                    }
    public void     setCity       (String city)       {   this.city        = city;      }
    
/******************************************************************************
 *                         Construction & Initialization                      *
 *****************************************************************************/
    
	public Person()
	{
		
	}
	
    /**
	 * Constructor with some initial data.
	 */
	public Person(String firstname, String lastname)
	{
		this.firstname = firstname;
		this.lastname = lastname;
		
		// some initial dummy data
		this.street = "some street";
		this.postalCode = 1234;
		this.city = "some city";
		this.birthday = Calendar.getInstance();
	}
	
    public Person(String userID, String firstname, String lastname, String city, Calendar birthday)
    {
        this.userID    = userID;
        this.firstname = firstname;
        this.lastname  = lastname;
        this.city      = city;
        this.birthday  = birthday;
    }
    
    public Person(String userID, String firstname, String lastname, Calendar birthday, String street, int postalCode, String city)
    {
		this.userID     = userID;
		this.firstname  = firstname;
		this.lastname   = lastname;
		this.birthday   = birthday;
		this.street     = street;
		this.postalCode = postalCode;
		this.city       = city;
	}
    
/******************************************************************************
 *                              Public Methods                                *
 *****************************************************************************/

	@Override
	public String toString()
	{
		return "Person [userID=" + userID + ", firstname=" + firstname
				+ ", lastname=" + lastname + ", birthday=" + birthday
				+ ", street=" + street + ", postalCode=" + postalCode
				+ ", city=" + city + "]";
	}

    @Override
    public ResultType getType() {
        return ResultType.User;
    }
}
