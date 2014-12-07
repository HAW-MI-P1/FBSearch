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
	
	private Dictionary dict=new Dictionary();


	@Override
	public JSONObject parse(String naturalLanguage) {

		//create parsing toolchain
		Properties props=new Properties();
		props.put("pos.model", "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
		props.put("annotators","tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		
		//Annotate String and initialize evaluation graph
		Annotation annotation = new Annotation(naturalLanguage);
		pipeline.annotate(annotation);
		
		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		ArrayCoreMap sentence = (ArrayCoreMap) sentences.get(0);
		Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
		SemanticGraph graph = SemanticGraphFactory.generateUncollapsedDependencies(tree);
		
		//get Information from the Tree
		List<JSONObject> jsons=new ArrayList<>();
		
		Map<IndexedWord, List<IndexedWord>> verbs= get_full_verb_and_object(graph);
		List<IndexedWord> subjects=getSubject(graph);
		JSONObject json=Serializer.keywordlist_to_json(verbs);
		
		jsons.add(Serializer.keywordlist_to_json(verbs));
		jsons.add(Serializer.serializeSubject(subjects));
		System.out.println("subjects: " + subjects);

		return Serializer.mergeAll(jsons);
	}

	
	/*
	 * returns main verbs with corresponding object
	 * */
	private Map<IndexedWord, List<IndexedWord>> get_full_verb_and_object(SemanticGraph graph){

		
		System.err.println(graph); 

		//		SemgrexPattern semgrex = SemgrexPattern.compile("{tag:/NN.*/}=A [<<dobj {tag:/VB.*/}=B |<<pobj {}=C << {tag:/VB.*/}=B]  ");
		//SemgrexPattern semgrex = SemgrexPattern.compile("{tag:/NN.*/}=A <<dobj {tag:/VB.*/}=B ");
		SemgrexPattern semgrex = SemgrexPattern.compile("{tag:/NN.*/}=A [<<pobj {}=C << {tag:/VB.*/}=B | <<dobj {tag:/VB.*/}=B]  ");
		SemgrexMatcher matcher = semgrex.matcher(graph);
		Map<IndexedWord, List<IndexedWord>> verbs=new HashMap<IndexedWord, List<IndexedWord>>();
		while (matcher.find()) {
		//	 System.err.println(matcher.getNode("A") + " <<dobj " + matcher.getNode("B"));
			IndexedWord nodeA = matcher.getNode("A");
			IndexedWord nodeB = matcher.getNode("B");
			IndexedWord nodeC = matcher.getNode("C");
//			System.out.println("Subject: "+nodeA);
//			System.out.println("pradicate: "+nodeB);
			
			//			System.out.println("Node B:"+nodeB);
			//			System.out.println("Verbalphrase:" + nodeB + (nodeC!=null?" Prep: "+ nodeC:"")+ " Lemma:"+nodeA.lemma() + " tag:"+nodeA.tag());
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
	
	private List<IndexedWord> getSubject(SemanticGraph graph){
		System.out.println("trying to get subject");
		
		//<<nsubj {tag:/NN.*/}=B 
		List<IndexedWord> subjects= new ArrayList<IndexedWord>();
		SemgrexPattern semgrex = SemgrexPattern.compile("{tag:/W.*/}=A  <<nsubj {}=C << {tag:/NN.*/}=B  ");
		SemgrexMatcher matcher = semgrex.matcher(graph);
		
		
		if(matcher.matches()){
			while(matcher.find()){
				IndexedWord nodeA = matcher.getNode("A");
				IndexedWord nodeB = matcher.getNode("B");;
				
				System.out.println("Subject: "+nodeA);
				System.out.println("pradicate: "+nodeB);
				subjects.add(nodeA);
			}
		}else{
			semgrex = SemgrexPattern.compile("{tag:/WP.*/}=A");
			matcher = semgrex.matcher(graph);
			
			while(matcher.find()){
				IndexedWord nodeA = matcher.getNode("A");
				System.out.println("Subject: "+nodeA);
				subjects.add(nodeA);
			}
		}
		return subjects;
	}
}
