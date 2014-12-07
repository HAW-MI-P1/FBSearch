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



	@Override
	public JSONObject parse(String naturalLanguage) {


		//create parsing toolchain
		Properties props=new Properties();
		props.put("pos.model", "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
		props.put("annotators","tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation annotation = new Annotation(naturalLanguage);
		pipeline.annotate(annotation);


		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		ArrayCoreMap sentence = (ArrayCoreMap) sentences.get(0);
		Map<IndexedWord, List<IndexedWord>> verbs= get_full_verb_and_object(sentence);
		JSONObject json=Serializer.keywordlist_to_json(verbs);


		return json;
	}




	/*
	 * returns main verbs with corresponding object
	 * */
	private Map<IndexedWord, List<IndexedWord>> get_full_verb_and_object(ArrayCoreMap annot_sentence){

		Tree tree = annot_sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
		SemanticGraph graph = SemanticGraphFactory.generateUncollapsedDependencies(tree);
		//System.err.println(graph); 

		//		SemgrexPattern semgrex = SemgrexPattern.compile("{tag:/NN.*/}=A [<<dobj {tag:/VB.*/}=B |<<pobj {}=C << {tag:/VB.*/}=B]  ");
		//SemgrexPattern semgrex = SemgrexPattern.compile("{tag:/NN.*/}=A <<dobj {tag:/VB.*/}=B ");
		SemgrexPattern semgrex = SemgrexPattern.compile("{tag:/NN.*/}=A [<<pobj {}=C << {tag:/VB.*/}=B | <<dobj {tag:/VB.*/}=B] ");
		SemgrexMatcher matcher = semgrex.matcher(graph);
		Map<IndexedWord, List<IndexedWord>> verbs=new HashMap<IndexedWord, List<IndexedWord>>();
		while (matcher.find()) {
		//	 System.err.println(matcher.getNode("A") + " <<dobj " + matcher.getNode("B"));
			IndexedWord nodeA = matcher.getNode("A");
			IndexedWord nodeB = matcher.getNode("B");
			IndexedWord nodeC = matcher.getNode("C");

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

}
