package de.haw.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.stanford.nlp.ling.IndexedWord;

public class Serializer {
	

	static Dictionary dict=new Dictionary();
	
	
	public static JSONObject keywordlist_to_json(Map <IndexedWord, List<IndexedWord>> keywords){
		Map<String,List<String>> prevJson=new HashMap<String,List<String>>();

		for(Map.Entry<IndexedWord, List<IndexedWord>> entry:keywords.entrySet()){
			String key=dict.verb_to_key.get((entry.getKey()).lemma());
			// System.out.println("trying to find key: "+entry.getKey()+" with value "+ entry.getValue());
			if(key != null){
				List<String> val = prevJson.get(key);
				List<String> val2=words_to_lemmas(entry.getValue());
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
			for(Entry<String,List<String>> entry: prevJson.entrySet()){
				try {
					for(String val:entry.getValue()){
					json.append(entry.getKey(), val);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			/*ArrayList<JSONObject> jsons=new ArrayList<JSONObject>();
			for (Entry<String,List<String>> entry: prevJson.entrySet()){
				jsons.add(keywords_to_json2D(entry));
			}
			try {
				outerJson.put("and",jsons);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
	//	}else if(prevJson.size()==1){
	//		json=keywords_to_json2D(prevJson.entrySet().iterator().next());
	//	}
		return json; 
		 
	}
	
	
	public static JSONObject keywords_to_json2D(Map.Entry<String,List<String>> entry){

		JSONObject json=new JSONObject();
		
		
		
		// 	   for (Entry<String,List<String>> entry: prevJson.entrySet()){
		try {
			json.put("weight", 1);
			json.put("attribute", entry.getKey());
			json.put("value_type", "string_list");
			JSONArray list=new JSONArray();
			list.put(entry.getValue());
			//	list.addAll(entry.getValue());
			json.put("value", entry.getValue());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}


		static JSONObject serializeSubject(List<IndexedWord> list){
			JSONObject json=new JSONObject();
			List<String> words=words_to_lemmas(list);
			for(String word: words){
				try {
					json.append("subject", dict.mapSubject(word));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return json;
		}

		public static List<String> words_to_lemmas(List<IndexedWord> list){
		List<String> lemmas=new ArrayList<String>();
		for(int i=0; i<list.size(); i++) lemmas.add(list.get(i).lemma());
		return lemmas;
	}
	
	
		
	static JSONObject mergeAll(List<JSONObject> list){
		JSONObject json=new JSONObject();
		
		for(JSONObject json01:list){

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
		
		return json;
	}
	
}
