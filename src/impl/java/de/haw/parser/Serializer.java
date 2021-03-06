package de.haw.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import edu.stanford.nlp.ling.IndexedWord;

public class Serializer {

	private static Map<Object,String> translateTable = new HashMap<Object, String>();
	
	static Dictionary dict=new Dictionary();

	static JSONObject serializeAdverbs(Map<IndexedWord, Set<IndexedWord>> adverbs) {
		JSONObject json = new JSONObject();
		
		for (IndexedWord adverb: adverbs.keySet()) {
			String key = dict.mapVerb(adverb.lemma());
			
			if (key != null) {
				for (IndexedWord prop: adverbs.get(adverb)) {
					try {
						json.append(key, prop.lemma());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		return json;
	}
	
	static JSONObject serializeVerbs(Map <IndexedWord, Set<IndexedWord>> keywords, IndexedWord type) {
		Map<String,Set<String>> prevJson=new HashMap<String,Set<String>>();
		System.out.println("keywords: "+ keywords);
		for(Map.Entry<IndexedWord, Set<IndexedWord>> entry :keywords.entrySet()){
		
		// key = dict.mapQuestionWord(type.lemma()) ? dict.name :dict.mapVerb(entry.getKey().lemma());
			System.out.println("type tag: "+type.tag());
			
	//		String key =  dict.mapVerb(entry.getKey().lemma())!=null ? dict.mapVerb(entry.getKey().lemma()) : dict.mapNer(entry.getValue().iterator().next().ner());
			String key =  dict.mapNer(entry.getValue().iterator().next().ner())!=null ? dict.mapNer(entry.getValue().iterator().next().ner()): dict.mapVerb(entry.getKey().lemma());

			System.out.println("ner: "+entry.getValue().iterator().next().ner());
			
			String mappedVerb=dict.mapVerb(entry.getKey().lemma());
			String mappedNer=dict.mapNer(entry.getValue().iterator().next().ner());
			String mappedW=dict.mapQuestionWord(type.lemma());
			
			if(mappedVerb!=null && mappedNer!=null){
				if(mappedVerb.equals(mappedNer)){
					key=mappedVerb;
				}
				else if(mappedVerb.equals(dict.name) && mappedNer.equals(dict.location)){
					key=dict.name;
				}
				else if(mappedVerb.equals(dict.location)&& mappedNer.equals(dict.name)){
					key=dict.name;
				}
				else if(mappedVerb.equals(dict.interests)){
					key=dict.interests;
				}
			}

			Set<String> val = prevJson.get(key);
			Set<String> val2=words_to_lemmas(entry.getValue());
			System.out.println("adding criterias: "+val + " : "+val2);
			if(val != null)val2.addAll(val);
			prevJson.put(key, val2);

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

	static JSONObject serializeSubject(Set<IndexedWord> set) {
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
	
	
	public static JSONObject serializeQuestionWords(Set<IndexedWord> question) {
		JSONObject json=new JSONObject();
		for(IndexedWord word: question){
			try {
				json.put(dict.type,dict.mapQuestionWord(word.lemma()));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// TODO Auto-generated method stub
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
