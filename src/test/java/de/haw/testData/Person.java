package de.haw.testData;

import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;

public class Person {
	public static Collection<de.haw.model.Person> getCollection()
	{
		LinkedList<de.haw.model.Person> collection = new LinkedList<de.haw.model.Person>();
		
		collection.add(Person.getNewPerson("Peter",   "Striker", 12345, "Yellepit",         "1754 Sleepy Pond Forest",  Calendar.getInstance()));
		collection.add(Person.getNewPerson("Timithy", "Schmith", 6543,  "Pomonkey Landing", "4603 Shady Mews",          Calendar.getInstance()));
		collection.add(Person.getNewPerson("Cordon",  "Bleu",    23456, "Why",              "6661 Velvet Zephyr Jetty", Calendar.getInstance()));
		collection.add(Person.getNewPerson("Peter",   "Striker", 7654,  "Mermaid Run",      "7064 Rocky Park",          Calendar.getInstance()));
		
		return collection;
		
	}
	
	private static de.haw.model.Person getNewPerson(String firstName, String lastName, int postalCode, String city, String street, Calendar birthday){
		de.haw.model.Person person = new de.haw.model.Person();
		person.setFirstName(firstName);
		person.setLastName(lastName);
		person.setPostalCode(postalCode);
		person.setCity(city);
		person.setStreet(street);
		person.setBirthday(birthday);
		
		return person;
	}
}
