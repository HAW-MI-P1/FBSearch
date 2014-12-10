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
		String interests="interests";
		String location="location";
		String name ="name";
		String age="age";
		String person="person";
		static HashMap<String, String> verb_to_key = new HashMap<String, String>();
		static HashMap<String, String> word_to_subject=new HashMap<String,String>();
		private void fillMaps(){
			verb_to_key.put("like", interests);
			verb_to_key.put("have", interests);
			verb_to_key.put("live", location);
			verb_to_key.put("name", name);
			verb_to_key.put("call", name);
			
			word_to_subject.put("who",person);
			word_to_subject.put("where",location);
			word_to_subject.put("city", location);
			word_to_subject.put("land",location);
			word_to_subject.put("when", age);
		}
		
		public String mapSubject(String str){
			return word_to_subject.get(str.toLowerCase());
		}
		

}
