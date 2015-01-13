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


	static String interests="interests";
	static String location="place";
	static String thing="page";
	static String name ="name";
	static String age="age";
	static String person="user";
	static String op="operation";
	static String type="type";
	
	

	Parser parser;
	@Before
	public void setUp() {
		System.out.println("Starting Parser Test");
		parser=new ParserImpl();


	}

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestEmpty() {
        parser.parse("");
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestNullPointer() {
        JSONObject test01=parser.parse(null);
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestTypoInput() {
        JSONObject test01=parser.parse("fasdfhaksdjfnlkjadfg");
    }

    @Test(expected = de.haw.model.exception.IllegalArgumentException.class)
    public void TestTypoInput2() {
        JSONObject test01=parser.parse("fasdfhaks djfnlkjadfg");
    }

    @Test
	public void Test01() throws JSONException{
		JSONObject test01=parser.parse("Who likes dogs?");
		System.out.println("json:"+test01.toString());

		assertTrue(test01.getJSONArray(interests).getString(0).equals("dog"));
		assertTrue(test01.getString(type).equals(person));
	}
	
	@Test
	public void Test02() throws JSONException{
		System.out.println();
		System.out.println("Who likes cats and dogs?");
		JSONObject test02=parser.parse("who likes cats and dogs?");
		System.out.println("json:"+test02.toString());
		assertTrue(getListFromJsonArray(test02.getJSONArray(interests)).contains("dog"));
		assertTrue(getListFromJsonArray(test02.getJSONArray(interests)).contains("cat"));
		assertTrue(test02.getString(type).equals(person));
	}
	
	
	@Test
	public void Test03() throws JSONException{
		System.out.println("who likes (dogs and cats) or pigs?");
		JSONObject test03=parser.parse("who likes (dogs and cats) or pigs?");
		System.out.println("json:"+test03.toString());
		assertTrue(getListFromJsonArray(test03.getJSONArray(interests)).contains("pig"));
		assertTrue(getListFromJsonArray(test03.getJSONArray(interests)).contains("dog"));
		assertTrue(getListFromJsonArray(test03.getJSONArray(interests)).contains("cat"));
		assertTrue(test03.getString(type).equals(person));

	}
	
	@Test
	public void Test04() throws JSONException{
		System.out.println("Who is called Jane Doe?");
		JSONObject test04=parser.parse("Who is called Jane Doe?");
		System.out.println("json:"+test04.toString());
		assertTrue(getListFromJsonArray(test04.getJSONArray(name)).contains("Jane"));
		assertTrue(getListFromJsonArray(test04.getJSONArray(name)).contains("Doe"));
		assertTrue(test04.getString(type).equals(person));

	}
	
	@Test
	public void Test05() throws JSONException{
		System.out.println("Who is called Jane and who likes cats?");
		JSONObject test05=parser.parse("who is called Jane and who likes cats?");
		System.out.println(test05);
		assertTrue(getListFromJsonArray(test05.getJSONArray(name)).contains("Jane"));
		assertTrue(getListFromJsonArray(test05.getJSONArray(interests)).contains("cat"));
		assertTrue(test05.getString(type).equals(person));
	}
	
	@Test
	public void Test07() throws JSONException{
		System.out.println("Who lives in the Schwarzwald ?");
		JSONObject test07=parser.parse("Who lives in the Schwarzwald ?");
		System.out.println(test07);
		assertTrue(getListFromJsonArray(test07.getJSONArray(location)).contains("Schwarzwald"));
		assertTrue(test07.getString(type).equals(person));

	}
	
	
	@Test
	public void Test08() throws JSONException{
		System.out.println("Who lives in the Schwarzwald or who likes cats ?");

		JSONObject test08=parser.parse("Who lives in the Schwarzwald or who likes cats ?");
		System.out.println(test08);
		assertTrue(test08.getString(type).equals(person));
		assertTrue(getListFromJsonArray(test08.getJSONArray(interests)).contains("cat"));
		assertTrue(getListFromJsonArray(test08.getJSONArray(location)).contains("Schwarzwald"));
		assertTrue(test08.getString(type).equals(person));



	}
	
	@Test
	public void Test09() throws JSONException{
		System.out.println("Who lives in the Schwarzwald or who likes cats or who is called Waldo?");
		
		JSONObject test09=parser.parse("Who lives in the Schwarzwald or who likes cats or who is called Waldo?");
		System.out.println(test09);
		assertTrue(test09.getString(type).equals(person));
		assertTrue(getListFromJsonArray(test09.getJSONArray(interests)).contains("cat"));
		assertTrue(getListFromJsonArray(test09.getJSONArray(location)).contains("Schwarzwald"));
		assertTrue(getListFromJsonArray(test09.getJSONArray(name)).contains("Waldo"));
	}
	
	@Test
	public void Test10() throws JSONException{
		System.out.println("Which Starbucks is placed in Hamburg and likes dogs?");
		
		JSONObject test10= parser.parse("Which Starbucks is placed in Hamburg and likes dogs?");
		System.out.println(test10);
		assertTrue(test10.getString(type).equals(thing));
		assertTrue(getListFromJsonArray(test10.getJSONArray(interests)).contains("dog"));
		assertTrue(getListFromJsonArray(test10.getJSONArray(name)).contains("Starbucks"));
	}
	
	@Test
	public void Test11() throws JSONException{
		System.out.println("Which person is called Jane and lives in Hamburg?");
		JSONObject test11 = parser.parse("Which person is called Jane and lives in Hamburg?");
		System.out.println(test11);
		assertTrue(test11.getString(type).equals(person));
		assertTrue(getListFromJsonArray(test11.getJSONArray(location)).contains("Hamburg"));
		assertTrue(getListFromJsonArray(test11.getJSONArray(name)).contains("Jane"));
	}
	
	
	@Test
	public void Test12() throws JSONException{
		System.out.println("What location is called Hamburg and placed in Europe?");
		JSONObject test12 = parser.parse("What location is called Hamburg and placed in Europe?");
		System.out.println(test12);
		assertTrue(test12.getString(type).equals(location));
		assertTrue(getListFromJsonArray(test12.getJSONArray(name)).contains("Hamburg"));
		assertTrue(getListFromJsonArray(test12.getJSONArray(location)).contains("Europe"));
	}
	
	
	
	/**
	 * Wird erstmal noch ignoriert, da wir bisher user nur als type spezifiert haben und nicht als 
	 * Suchkriterium. -> Muss noch abgesprochen werden
	 * 
	 */
	@Test
	@Ignore
	public void Test13() throws JSONException{
		System.out.println("Where lives Jane?");
		JSONObject test13 = parser.parse("Where lives Jane?");
		System.out.println(test13);
		assertTrue(test13.getString(type).equals(location));
		assertTrue(getListFromJsonArray(test13.getJSONArray(person)).contains("Jane"));
				
		
	}
	
	@Test
	public void Test14() throws JSONException{
		System.out.println("Who lives in the Schwarzwald and who likes cats and dogs ?");

		JSONObject test08=parser.parse("Who lives in the Schwarzwald and who likes cats and dogs ?");
		System.out.println(test08);
		assertTrue(test08.getString(type).equals(person));
		assertTrue(getListFromJsonArray(test08.getJSONArray(interests)).contains("cat"));
		assertTrue(getListFromJsonArray(test08.getJSONArray(interests)).contains("dog"));
		assertTrue(getListFromJsonArray(test08.getJSONArray(location)).contains("Schwarzwald"));


	}
	
	@Test
	public void Test15() throws JSONException {
		JSONObject test15 = parser.parse("Where is Jane?");
		System.out.println(test15);
		assertTrue(test15.getString(type).equals(location));
		assertTrue(getListFromJsonArray(test15.getJSONArray(name)).contains("Jane"));
	}
	
	/**
	 * TODO: FUER LUKAS
	 * Hier koennte dann die Adverbsuche doch noch zum Einsatz kommen.
	 * Naemlich dass das Adverb located dazu fuehrt, dass in Europe als Suchkriterium
	 * benutzt wird.
	 * @throws JSONException
	 */
	@Test
	public void Test16() throws JSONException {
		JSONObject test16 = parser.parse("Where is Wacken Festival located in Europe?");
		System.out.println(test16);
		assertTrue(test16.getString(type).equals("event"));
		assertTrue(getListFromJsonArray(test16.getJSONArray(name)).contains("Wacken"));
		assertTrue(getListFromJsonArray(test16.getJSONArray(name)).contains("Festival"));
		assertTrue(getListFromJsonArray(test16.getJSONArray(location)).contains("Europe"));
	}
	
	@Test
	public void Test17() throws JSONException {
		JSONObject test17 = parser.parse("Where is Wacken Festival located?");
		System.out.println(test17);
		assertTrue(test17.getString(type).equals("event"));
		assertTrue(getListFromJsonArray(test17.getJSONArray(name)).contains("Wacken"));
		assertTrue(getListFromJsonArray(test17.getJSONArray(name)).contains("Festival"));
	}

	@Test
	public void Test18() throws JSONException {
		JSONObject test18 = parser.parse("Where is Jane placed?");
		System.out.println(test18);
		assertTrue(test18.getString(type).equals(location));
		assertTrue(getListFromJsonArray(test18.getJSONArray(name)).contains("Jane"));
	}
	
	@Test
	public void Test19() throws JSONException {
		JSONObject test19 = parser.parse("Where is Jane's home?");
		System.out.println(test19);
		assertTrue(test19.getString(type).equals(location));
		assertTrue(getListFromJsonArray(test19.getJSONArray(name)).contains("Jane"));
	}	

	@Test
	public void Test20() throws JSONException {
		JSONObject test20 = parser.parse("Which festival is named Wacken?");
		System.out.println(test20);
		assertTrue(test20.getString(type).equals("event"));
		assertTrue(getListFromJsonArray(test20.getJSONArray(name)).contains("Wacken"));
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
