package de.haw.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class ParserTest {



	Parser parser;
	@Before
	public void setUp() {
		System.out.println("Starting Parser Test");
		parser=new ParserImpl();


	}
	
	@Test
	public void testSimpleSentences() throws JSONException {

		String interests="interests";
		String location="place";
		String thing="page";
		String name ="name";
		String age="age";
		String person="user";
		String op="operation";
		String type="type";


		JSONObject test01=parser.parse("who likes dogs?");
		System.out.println("json:"+test01.toString());

		assertTrue(test01.getJSONArray(interests).getString(0).equals("dog"));
		assertTrue(test01.getString(type).equals(person));


		JSONObject test02=parser.parse("who likes cats and dogs?");
		System.out.println("json:"+test02.toString());
		assertTrue(getListFromJsonArray(test02.getJSONArray(interests)).contains("dog"));
		assertTrue(getListFromJsonArray(test02.getJSONArray(interests)).contains("cat"));
		assertTrue(test02.getString(type).equals(person));

		JSONObject test03=parser.parse("who likes (dogs and cats) or pigs?");
		System.out.println("json:"+test03.toString());
		assertTrue(getListFromJsonArray(test03.getJSONArray(interests)).contains("pig"));
		assertTrue(getListFromJsonArray(test03.getJSONArray(interests)).contains("dog"));
		assertTrue(getListFromJsonArray(test03.getJSONArray(interests)).contains("cat"));
		assertTrue(test03.getString(type).equals(person));


		JSONObject test04=parser.parse("who is called Jane Doe?");
		System.out.println("json:"+test04.toString());
		assertTrue(getListFromJsonArray(test04.getJSONArray(name)).contains("Jane"));
		assertTrue(getListFromJsonArray(test04.getJSONArray(name)).contains("Doe"));
		assertTrue(test04.getString(type).equals(person));



		/*
		 * more dimensions:
		 */
		JSONObject test05=parser.parse("who is called Jane and who likes cats?");
		System.out.println(test05);
		assertTrue(getListFromJsonArray(test05.getJSONArray(name)).contains("Jane"));
		assertTrue(getListFromJsonArray(test05.getJSONArray(interests)).contains("cat"));
		assertTrue(test05.getString(type).equals(person));


		JSONObject test07=parser.parse("Who lives in the Schwarzwald ?");
		System.out.println(test07);
		assertTrue(getListFromJsonArray(test07.getJSONArray(location)).contains("Schwarzwald"));
		assertTrue(test07.getString(type).equals(person));

		
		
		JSONObject test08=parser.parse("Who lives in the Schwarzwald or who likes cats ?");
		System.out.println(test08);
		assertTrue(test08.getString(type).equals(person));
		assertTrue(getListFromJsonArray(test08.getJSONArray(interests)).contains("cat"));
		assertTrue(getListFromJsonArray(test08.getJSONArray(location)).contains("Schwarzwald"));
		assertTrue(test08.getString(type).equals(person));


		JSONObject test09=parser.parse("Who lives in the Schwarzwald or who likes cats or who is called Waldo?");
		System.out.println(test09);
		assertTrue(test09.getString(type).equals(person));
		assertTrue(getListFromJsonArray(test09.getJSONArray(interests)).contains("cat"));
		assertTrue(getListFromJsonArray(test09.getJSONArray(location)).contains("Schwarzwald"));
		assertTrue(getListFromJsonArray(test09.getJSONArray(name)).contains("Waldo"));

		
		JSONObject test10= parser.parse("Which Starbucks is placed in Hamburg and likes dogs?");
		System.out.println(test10);
		assertTrue(test10.getString(type).equals(thing));
		assertTrue(getListFromJsonArray(test10.getJSONArray(interests)).contains("dog"));
		assertTrue(getListFromJsonArray(test10.getJSONArray(name)).contains("Starbucks"));

		//Following cases has Lukas to implement
		JSONObject test11 = parser.parse("Which person is called Jane and lives in Hamburg?");
		System.out.println(test11);
		assertTrue(test11.getString(type).equals(person));
		assertTrue(getListFromJsonArray(test11.getJSONArray(location)).contains("Hamburg"));
		assertTrue(getListFromJsonArray(test11.getJSONArray(name)).contains("Jane"));

		JSONObject test12 = parser.parse("What location is called Hamburg and placed in Europe?");
		System.out.println(test12);
		assertTrue(test12.getString(type).equals(location));
		assertTrue(getListFromJsonArray(test12.getJSONArray(name)).contains("Hamburg"));
		assertTrue(getListFromJsonArray(test12.getJSONArray(location)).contains("Europe"));

		JSONObject test13 = parser.parse("Where lives Jane?");
		System.out.println(test13);
		assertTrue(test13.getString(type).equals(location));
		assertTrue(getListFromJsonArray(test13.getJSONArray(name)).contains("Jane"));
		
		
		
		JSONObject test14 = parser.parse("Where is Jane?");
		
		JSONObject test15 = parser.parse("How old is Jane?");
		
		/*
		 * Here I think the trick is to rescan the objects word for
		 * word and get a match from Festival=>event in the directory,
		 * and further change query type from location (got earlier
		 * from Where) to event.
		 * 
		 * But I still see the problem that in my opinion the end user
		 * explicitly asks for a location with this question.
		 */
		JSONObject test16 = parser.parse("Where is Wacken Festival located in Europe?");
		System.out.println(test16);
		test16 = parser.parse("Where is Wacken Festival located?");
		
		/*
		 * Similar to the question 'Where is Wacken Festival located?'.
		 * But now the dictionary scan of the subject brings type event
		 * from the word demonstration.
		 * 
		 * The problem now is that now a query on any event had to be done.
		 * So we should consider to support that the end user makes a time
		 * constraint, like:
		 * "When starts a demonstration at monday?"
		 */
		JSONObject test17 = parser.parse("When starts a Demonstration?");
		System.out.println(test17);
		
		
		//JSONObject test14 = parser.parse("Where is Jane's home?");
		//System.out.println(test14);
		
		/*
		 * TODO:
		 * Where is Jane's home? (genitiv => name, subject => type)
		 */



	}
	
	List<String> getListFromJsonArray(JSONArray json) {
		List<String> strings=new ArrayList<String>();
		for(int i=0; i<json.length(); i++){
			try {
				strings.add(json.getString(i));	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return strings;
	}

}
