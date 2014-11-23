package de.haw.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphFactory;
import edu.stanford.nlp.semgraph.semgrex.SemgrexMatcher;
import edu.stanford.nlp.semgraph.semgrex.SemgrexPattern;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.ArrayCoreMap;
import edu.stanford.nlp.util.CoreMap;

public class ParserImpl implements Parser{

	public ParserImpl(){
		fillMaps();
	}

	//THIS IS JUST FOR GETTING STARTED...
	//possible values for json:
	String interests="interests";
	String location="location";
	String name ="name";
	String age="age";

	HashMap<String, String> verb_to_key = new HashMap<String, String>();

	private void fillMaps(){
		verb_to_key.put("like", interests);
		verb_to_key.put("have", interests);
		verb_to_key.put("lives", location);
		verb_to_key.put("name", name);
		verb_to_key.put("call", name);
	}


	@Override
	public JSONObject parse(String naturalLanguage) {


		//create parsing toolchain
		Properties props=new Properties();
		props.put("annotators","tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation annotation = new Annotation(naturalLanguage);
		pipeline.annotate(annotation);


		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		ArrayCoreMap sentence = (ArrayCoreMap) sentences.get(0);
		Map<IndexedWord, List<IndexedWord>> verbs= get_full_verb_and_object(sentence);
		JSONObject json=keywordlist_to_json(verbs);


		return json;
	}


	private JSONObject keywordlist_to_json(Map <IndexedWord, List<IndexedWord>> keywords){

		Map<String,List<String>> prevJson=new HashMap<String,List<String>>();

		for(Map.Entry<IndexedWord, List<IndexedWord>> entry:keywords.entrySet()){
			String key=verb_to_key.get((entry.getKey()).lemma());
			// System.out.println("trying to find key: "+entry.getKey()+" with value "+ entry.getValue());
			if(key != null){
				List<String> val = prevJson.get(key);
				List<String> val2=words_to_lemmas(entry.getValue());
				//		System.out.println("lemmas: "+val2);
				if(val != null)val2.addAll(val);
				prevJson.put(key, val2);
			}//TODO: ELSE: resparse for object

		}

		// 	    System.out.println("created map:"+prevJson);
		//create JSON Object
		JSONObject outerJson=new JSONObject();

		if(prevJson.size() > 1){
			ArrayList<JSONObject> jsons=new ArrayList<JSONObject>();
			for (Entry<String,List<String>> entry: prevJson.entrySet()){
				jsons.add(keywords_to_json2D(entry));
			}
			try {
				outerJson.put("and",jsons);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(prevJson.size()==1){
			outerJson=keywords_to_json2D(prevJson.entrySet().iterator().next());
		}
		return outerJson;

	}

	private JSONObject keywords_to_json2D(Map.Entry<String,List<String>> entry){

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




	private List<String> words_to_lemmas(List<IndexedWord> list){
		List<String> lemmas=new ArrayList<String>();
		for(int i=0; i<list.size(); i++) lemmas.add(list.get(i).lemma());
		return lemmas;
	}


	/*
	 * returns main verbs with corresponding object
	 * */
	private Map<IndexedWord, List<IndexedWord>> get_full_verb_and_object(ArrayCoreMap annot_sentence){

		Tree tree = annot_sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
		SemanticGraph graph = SemanticGraphFactory.generateUncollapsedDependencies(tree);
		System.err.println(graph); 

		//		SemgrexPattern semgrex = SemgrexPattern.compile("{tag:/NN.*/}=A [<<dobj {tag:/VB.*/}=B |<<pobj {}=C << {tag:/VB.*/}=B]  ");
		SemgrexPattern semgrex = SemgrexPattern.compile("{tag:/NN.*/}=A <<dobj {tag:/VB.*/}=B ");
		SemgrexMatcher matcher = semgrex.matcher(graph);
		Map<IndexedWord, List<IndexedWord>> verbs=new HashMap<IndexedWord, List<IndexedWord>>();
		while (matcher.find()) {
			// System.err.println(matcher.getNode("A") + " <<dobj " + matcher.getNode("B"));
			IndexedWord nodeA = matcher.getNode("A");
			IndexedWord nodeB = matcher.getNode("B");
			//			System.out.println("Node B:"+nodeB);
			//			System.out.println("Verbalphrase:" + nodeA + " Lemma:"+nodeA.lemma() + " tag:"+nodeA.tag());
			List<IndexedWord>temp=verbs.get(nodeB);
			if(temp!=null){
				temp.add(nodeA);
			}else{
				temp=new ArrayList<IndexedWord>();
				temp.add(nodeA);
			}
			verbs.put(nodeB,temp);
		}

		//	System.out.println("verbs: "+verbs);

		return verbs;
	}

}
