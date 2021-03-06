package de.haw.parser;

import java.util.HashMap;

public class Dictionary {
	
	public Dictionary(){
		fillMaps();
	}
	
	/* TODO:
	 * - add better dictionary
	 */
	
	//THIS IS JUST FOR GETTING STARTED...
		//possible values for json:
		public final static String type="type";
		public final static String interests="interests";
		public final static String location="place";
		public final static String thing="page";
		public final static String name ="name";
		public final static String age="age";
		public final static String person="user";
		public final static String op="operation";
		public final static String event="event";
		
		static HashMap<String, String> verb_to_key = new HashMap<String, String>();
		static HashMap<String, String> word_to_subject=new HashMap<String,String>();
		static HashMap<String, String> word_to_Operation=new HashMap<String,String>();
		static HashMap<String, String> qword_to_type=new HashMap<String,String>();
		static HashMap<String, String> ners_to_description=new HashMap<String,String>();
		private void fillMaps(){
			verb_to_key.put("like", interests);
			verb_to_key.put("have", interests);
			verb_to_key.put("live", location);
			verb_to_key.put("situate", location);
			verb_to_key.put("place", location);
			verb_to_key.put("name", name);
			verb_to_key.put("call", name);
			//Adverben
			verb_to_key.put("located", location);
			
			word_to_subject.put("who",person);
			word_to_subject.put("person",person);
			word_to_subject.put("band",person);
			word_to_subject.put("metalband",person);
			word_to_subject.put("musician",person);
			word_to_subject.put("actor",person);
			word_to_subject.put("who",person);
			
			word_to_subject.put("where",location);
			
			word_to_subject.put("city", location);
			word_to_subject.put("location", location);
			
			word_to_subject.put("restaurant", thing);
			word_to_subject.put("university", thing);
			word_to_subject.put("starbucks", thing);
			word_to_subject.put("bakery", thing);
			word_to_subject.put("super market", thing);
			word_to_subject.put("town", thing);
			word_to_subject.put("country", thing);
			word_to_subject.put("county", thing);
			word_to_subject.put("island", thing);
			word_to_subject.put("school", thing);
			word_to_subject.put("disco", thing);
			word_to_subject.put("cafe", thing);
			word_to_subject.put("cafeteria", thing);
			word_to_subject.put("district", thing);
			word_to_subject.put("when", age);
			
			word_to_Operation.put("and", op);
			word_to_Operation.put("or", op);
			
			word_to_subject.put("demonstration", event);
			word_to_subject.put("festival", event);
			word_to_subject.put("party", event);
			word_to_subject.put("birthday", event);
			word_to_subject.put("conference", event);
			word_to_subject.put("ceremony", event);
			word_to_subject.put("event", event);
			word_to_subject.put("meeting", event);
			word_to_subject.put("wedding", event);
			word_to_subject.put("confirmation", event);
			word_to_subject.put("show", event);
			
			qword_to_type.put("who", person);
			qword_to_type.put("where", location);
			qword_to_type.put("which", thing);
			
			ners_to_description.put("location", location);
			ners_to_description.put("person", name);
			ners_to_description.put("organisation", thing);
			ners_to_description.put("date", age);
			
			
		}
		
		public String mapVerb(String str) {
			return verb_to_key.get(str.toLowerCase());
		}
		
		public String mapSubject(String str) {
			return word_to_subject.get(str.toLowerCase());
		}
		
		public String mapOperation(String str) {
			return word_to_Operation.get(str.toLowerCase());
		}
		
		public String mapQuestionWord(String str) {
			return qword_to_type.get(str.toLowerCase());
		}		
		public String mapNer(String str) {
			return ners_to_description.get(str.toLowerCase());
		}
		


}
