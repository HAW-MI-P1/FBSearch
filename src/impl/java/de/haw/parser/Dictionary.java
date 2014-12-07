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

		static HashMap<String, String> verb_to_key = new HashMap<String, String>();

		private void fillMaps(){
			verb_to_key.put("like", interests);
			verb_to_key.put("have", interests);
			verb_to_key.put("live", location);
			verb_to_key.put("name", name);
			verb_to_key.put("call", name);
		}

		

}
