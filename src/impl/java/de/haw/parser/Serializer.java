package de.haw.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import de.haw.parser.query.Attributes;
import de.haw.parser.query.Conjunctions;
import de.haw.parser.query.Query;
import de.haw.parser.query.Selections;
import edu.stanford.nlp.ling.IndexedWord;

public class Serializer {


	private static Map<Object,String> translateTable = new HashMap<Object, String>();
	
	static Dictionary dict=new Dictionary();

	static {
		createTranslateTable();
	}
	
	private static void createTranslateTable() {
		translateTable.put(Attributes.AGES, "age");
		translateTable.put(Attributes.INTERESTS, "interests");
		translateTable.put(Attributes.LOCATIONS, "location");
		translateTable.put(Attributes.NAMES, "name");
		translateTable.put(Conjunctions.AND, "and");
		translateTable.put(Conjunctions.OR, "or");
		translateTable.put(Selections.EVENT, "event");
		translateTable.put(Selections.GROUP, "group");
		translateTable.put(Selections.PAGE, "page");
		translateTable.put(Selections.PLACE, "place");
		translateTable.put(Selections.USER, "user");
	}
	
	private static String translate(Object object) {
		//TODO: throw exception if query part can't be translated
		return translateTable.get(object);
	}
	
	static JSONObject serializeQuery(Query query) {
		JSONObject json = new JSONObject();
		Attributes[] attributes = new Attributes[]{Attributes.AGES,
				Attributes.INTERESTS, Attributes.LOCATIONS, Attributes.NAMES};
		
		try {
			json.put("type", translate(query.getConjunction()));
			json.put("operation", translate(query.getConjunction()));
			for (Attributes attribute: attributes) {
				String attrName = translate(attribute);
				List<String> entries = query.getAttribute(attribute);
				for (String entry: entries) {
					json.append(attrName, entries);
				}
			}
		}
		catch(JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return json;
	}
	
	static JSONObject serializeVerbs(Map <IndexedWord, Set<IndexedWord>> keywords,
			boolean wrb){
		Map<String,Set<String>> prevJson=new HashMap<String,Set<String>>();

		for(Map.Entry<IndexedWord, Set<IndexedWord>> entry:keywords.entrySet()){
			//TODO: There are for sure cases for which this solution is to simple
			//FOR EXAMPLE: What about multiselects? e.g. Where lives Jane and who lives in Hamburg?!
			String key = wrb ? dict.name
					: dict.verb_to_key.get((entry.getKey()).lemma());
			//		System.out.println("trying to find key: "+entry.getKey().lemma()+" with value "+ entry.getValue());
			if(key != null){
				Set<String> val = prevJson.get(key);
				Set<String> val2=words_to_lemmas(entry.getValue());
				//		System.out.println("lemmas: "+val2);
				if(val != null)val2.addAll(val);
				prevJson.put(key, val2);
			}//TODO: ELSE: resparse for object

		}

		//	  System.out.println("created map:"+prevJson);
		//create JSON Object
		JSONObject outerJson=new JSONObject();

		//if(prevJson.size() > 1){
		//TODO: createJson object:
		JSONObject json=new JSONObject();
		for(Entry<String,Set<String>> entry: prevJson.entrySet()){
			try {
				for(String val:entry.getValue()){
					json.append(entry.getKey(), val);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return json; 

	}



	static JSONObject serializeSubject(Set<IndexedWord> set){
		JSONObject json=new JSONObject();
		//	Set<String> words=words_to_lemmas(set);


		for(IndexedWord word: set){
			try {
				//json.append("subject", dict.mapSubject(word));
				json.put("type", dict.mapSubject(word.lemma()));
				if(word.tag().startsWith("NN") && (!word.tag().equals("NN"))){
					json.append("name", word.value());
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return json;
	}

	static Set<String> words_to_lemmas(Set<IndexedWord> set){
		Set<String> lemmas=new HashSet<String>();
		for(IndexedWord word:set)
			lemmas.add(word.lemma());
		//for(int i=0; i<list.size(); i++) lemmas.add(list.get(i).lemma());
		return lemmas;
	}

	static JSONObject serializeConj(Set<IndexedWord> conjs) {
		JSONObject json=new JSONObject();

		//get only the first conjunction, because we don't give lists!
		if(conjs.size()>0){
			for(IndexedWord word: conjs){
				try {
					json.put(dict.mapOperation(word.lemma()) , word.lemma());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}



		return json;
	}



	static JSONObject mergeAll(List<JSONObject> list){
		JSONObject json=new JSONObject();
		//System.out.println("merge all json list: " + list);
		for(JSONObject json01:list){
			//json01.names()==null wenn es ein leeres json ist, zB wenn es keine konjunktion im satz gab
			if(json01.names()!=null){
				//		System.out.println("working with json: "+ json01);
				for(String str: JSONObject.getNames(json01)){
					try {

						/*
						 * TODO: merge objects with the same keys
						 */

						//	if(!(json.has(str))){
						json.put(str,  json01.get(str));
						//	}else{
						//		JSONArray temp = json.getJSONArray(str);
						//		temp.put(temp.length(), )
						//	}


					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		return json;
	}



}
