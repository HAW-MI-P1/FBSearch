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


		JSONObject test01=parser.parse("who likes dogs?");
		System.out.println("json:"+test01.toString());

		assertTrue(test01.getJSONArray(interests).getString(0).equals("dog"));
		assertTrue(test01.getString("subject").equals(person));


		JSONObject test02=parser.parse("who likes cats and dogs?");
		System.out.println("json:"+test02.toString());
		assertTrue(getListFromJsonArray(test02.getJSONArray(interests)).contains("dog"));
		assertTrue(getListFromJsonArray(test02.getJSONArray(interests)).contains("cat"));
		assertTrue(test02.getString("subject").equals(person));

		JSONObject test03=parser.parse("who likes (dogs and cats) or pigs?");
		System.out.println("json:"+test03.toString());
		assertTrue(getListFromJsonArray(test03.getJSONArray(interests)).contains("pig"));
		assertTrue(getListFromJsonArray(test03.getJSONArray(interests)).contains("dog"));
		assertTrue(getListFromJsonArray(test03.getJSONArray(interests)).contains("cat"));
		assertTrue(test03.getString("subject").equals(person));


		JSONObject test04=parser.parse("who is called Jane Doe?");
		System.out.println("json:"+test04.toString());
		assertTrue(getListFromJsonArray(test04.getJSONArray(name)).contains("Jane"));
		assertTrue(getListFromJsonArray(test04.getJSONArray(name)).contains("Doe"));
		assertTrue(test04.getString("subject").equals(person));



		/*
		 * more dimensions:
		 */
		JSONObject test05=parser.parse("who is called Jane and who likes cats?");
		System.out.println(test05);
		assertTrue(getListFromJsonArray(test05.getJSONArray(name)).contains("Jane"));
		assertTrue(getListFromJsonArray(test05.getJSONArray(interests)).contains("cat"));
		assertTrue(test05.getString("subject").equals(person));


		JSONObject test07=parser.parse("Who lives in the Schwarzwald ?");
		System.out.println(test07);
		assertTrue(getListFromJsonArray(test07.getJSONArray(location)).contains("Schwarzwald"));
		assertTrue(test07.getString("subject").equals(person));

		
		
		JSONObject test08=parser.parse("Who lives in the Schwarzwald or who likes cats ?");
		System.out.println(test08);
		assertTrue(test08.getString("subject").equals(person));
		assertTrue(getListFromJsonArray(test08.getJSONArray(interests)).contains("cat"));
		assertTrue(getListFromJsonArray(test08.getJSONArray(location)).contains("Schwarzwald"));
		assertTrue(test08.getString("subject").equals(person));


		JSONObject test09=parser.parse("Who lives in the Schwarzwald or who likes cats or who is called Waldo?");
		System.out.println(test09);
		assertTrue(test09.getString("subject").equals(person));
		assertTrue(getListFromJsonArray(test09.getJSONArray(interests)).contains("cat"));
		assertTrue(getListFromJsonArray(test09.getJSONArray(location)).contains("Schwarzwald"));
		assertTrue(getListFromJsonArray(test09.getJSONArray(name)).contains("Waldo"));

		
		JSONObject test10= parser.parse("Which Starbucks is placed in Hamburg and likes dogs?");
		System.out.println(test10);
		assertTrue(test10.getString("subject").equals(thing));
		assertTrue(getListFromJsonArray(test10.getJSONArray(interests)).contains("dog"));
		assertTrue(getListFromJsonArray(test10.getJSONArray(name)).contains("Starbucks"));

		/*
		 * TODO:
		 * Which person is called Jane and lives in Hamburg
		 * Where lives Jane
		 
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
