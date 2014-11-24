package de.haw.parser;

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
		JSONObject test01=parser.parse("who likes dogs?");
		System.out.println("json:"+test01.toString());
		assertTrue(test01.getString("attribute").equals("interests"));
		assertTrue(test01.getJSONArray("value").getString(0).equals("dog"));


		JSONObject test02=parser.parse("who likes cats and dogs?");
		System.out.println("json:"+test02.toString());
		assertTrue(test02.getString("attribute").equals("interests"));
		assertTrue(test02.getJSONArray("value").getString(0).equals("cat"));
		assertTrue(test02.getJSONArray("value").getString(1).equals("dog"));

		JSONObject test03=parser.parse("who likes (dogs and cats) or pigs?");
		System.out.println("json:"+test03.toString());
		assertTrue(test03.getString("attribute").equals("interests"));
		assertTrue(test03.getJSONArray("value").getString(0).equals("dog"));
		assertTrue(test03.getJSONArray("value").getString(1).equals("pig"));
		assertTrue(test03.getJSONArray("value").getString(2).equals("cat"));


		JSONObject test04=parser.parse("who is called Jane?");
		System.out.println("json:"+test04.toString());
		assertTrue(test04.getString("attribute").equals("name"));
		assertTrue(test04.getJSONArray("value").getString(0).equals("Jane"));



		/*
		 * more dimensions:
		 */
		JSONObject test05=parser.parse("who is called Jane and likes cats?");
		System.out.println(test05);
		JSONArray list05=test05.getJSONArray("and");
		assertTrue(list05.getJSONObject(0).getString("attribute").equals("name"));		
		assertTrue(list05.getJSONObject(0).getJSONArray("value").get(0).equals("Jane"));
		assertTrue(list05.getJSONObject(1).getString("attribute").equals("interests"));		
		assertTrue(list05.getJSONObject(1).getJSONArray("value").get(0).equals("cat"));

	}

	@Ignore
	@Test
	public void testMoreComplexSentences() {
		//fail("Not yet implemented");
		//assertTrue
	}

}
