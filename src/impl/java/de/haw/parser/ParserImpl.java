package de.haw.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.Set;

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
		//	props.put("pos.model", "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
		//	props.put("annotators","tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		props.put("pos.model", "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
		props.put("annotators","tokenize, ssplit, pos, lemma, parse");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		//Annotate String and initialize evaluation graph
		Annotation annotation = new Annotation(naturalLanguage);
		pipeline.annotate(annotation);

		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		ArrayCoreMap sentence = (ArrayCoreMap) sentences.get(0);
		Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);

		SemanticGraph graph = SemanticGraphFactory.generateUncollapsedDependencies(tree);
		//SemanticGraph graph = SemanticGraphFactory.generateCollapsedDependencies(tree);

		graph.prettyPrint();
		
		//get Information from the Tree
		List<JSONObject> jsons=new ArrayList<>();

		Map<IndexedWord, Set<IndexedWord>> verbs= get_full_verb_and_object(graph);
		Set<IndexedWord> subjects=getSubject(graph);
		Set<IndexedWord> conjs=getConj(graph);

		jsons.add(Serializer.keywordlist_to_json(verbs, containsWRB(subjects)));
		System.out.println("verbs: "+verbs);
		jsons.add(Serializer.serializeSubject(subjects));
		System.out.println("subjects: " + subjects);
		jsons.add(Serializer.serializeConj(conjs));
		return Serializer.mergeAll(jsons);
	}
	
	private boolean containsWRB(Set<IndexedWord> subjects) 
	{
		boolean contains = false;
		
		for (IndexedWord subject: subjects) {
			if (subject.tag().equals("WRB")) {
				contains = true;
			}
		}
		
		return contains;
	}
	
	/*
	 * returns main verbs with corresponding object
	 * */
	/**
	 * @param graph
	 * @return
	 */
	private Map<IndexedWord, Set<IndexedWord>> get_full_verb_and_object(SemanticGraph graph){


		SemgrexPattern semgrex = SemgrexPattern.compile("{tag:/NN.*/}=object [<<pobj ({}=conj < {tag:/VB.*/}=verb)   | <<dobj {tag:/VB.*/}=verb ]")	;
		//SemgrexPattern semgrex = SemgrexPattern.compile("{tag:/NN.*/}=object [<pobj ({}< {tag:/VB.*/}=verb)  | <dobj ({tag:/VB.*/}=verb)  | <nn ({} <dobj {tag:/VB.*/}=verb) | <conj ({} <dobj {tag:/VB.*/}=verb) | <conj ( {#} > {tag:/VB.*/}=verb ) ]");

		SemgrexMatcher matcher = semgrex.matcher(graph);
		Map<IndexedWord, Set<IndexedWord>> verbs=new HashMap<IndexedWord, Set<IndexedWord>>();


		while (matcher.find()) {

			IndexedWord nodeMainVerb = matcher.getNode("mainverb");

			IndexedWord nodeVerb = matcher.getNode("verb");

			if(nodeVerb==null) nodeVerb=nodeMainVerb;

			IndexedWord nodePrep = matcher.getNode("prep");
			IndexedWord nodeConj = matcher.getNode("conj");
			IndexedWord nodeObject = matcher.getNode("object");

			/*
			System.out.println("verb:"+nodeVerb);
			System.out.println("prep:"+nodePrep);
			System.out.println("conj:"+nodeConj);
			System.out.println("Object:"+nodeObject);
			 */


			//		System.out.println("found:"+(nodeConj!=null?" nodeConj="+nodeConj:"")+"; verb="+nodeVerb+(nodePrep!=null?"; nodePrep="+nodePrep:"")+";nodeObject="+nodeObject);
			//		System.out.println("Verbalphrase:" + nodeB + (nodeC!=null?" Prep: "+ nodeC:"")+ " Lemma:"+nodeA.lemma() + " tag:"+nodeA.tag());
			Set<IndexedWord>temp=verbs.get(nodeVerb);
			if(temp!=null){
				temp.add(nodeObject);

				//	temp.add(nodeObject);
			}else{
				temp=new HashSet<IndexedWord>();
				temp.add(nodeObject);
			}
			
			verbs.put(nodeVerb, temp);
		}

		return verbs;
	}

	private Set<IndexedWord> getSubject(SemanticGraph graph){
		//System.out.println("trying to get subject");

		//<<nsubj {tag:/NN.*/}=B 
		Set<IndexedWord> subjects= new HashSet<IndexedWord>();
		//	SemgrexPattern semgrex = SemgrexPattern.compile("{tag:/W.*/}=A  <<nsubj {}=C << {tag:/NN.*/}=B ");
		SemgrexPattern semgrex = SemgrexPattern.compile(" {tag:/WP.*/}=A <</nsubj.*/ {tag:/VB.*/}=C ");
		SemgrexMatcher matcher = semgrex.matcher(graph);

		while(matcher.find()){
			IndexedWord nodeA = matcher.getNode("A");
			IndexedWord nodeB = matcher.getNode("B");;

			/*			System.out.println("Subject: "+nodeA);
			System.out.println("pradicate: "+nodeB);
			 */
			subjects.add(nodeA);
		}

		if(subjects.isEmpty()){
			//{tag:/WR.*/}=A <<advmod {tag:/VB.*/}=C )
			semgrex = SemgrexPattern.compile(" {tag:/WR.*/}=A <<advmod {tag:/VB.*/}=C ");
			matcher = semgrex.matcher(graph);

			while(matcher.find()){
				IndexedWord nodeA = matcher.getNode("A");
				IndexedWord nodeB = matcher.getNode("B");;

				/*System.out.println("Subject: "+nodeA);
				System.out.println("pradicate: "+nodeB);
				 */
				subjects.add(nodeA);
			}
		}

		if( subjects.isEmpty()){
			semgrex = SemgrexPattern.compile("{tag:/NN.*/}=A [<< nsubj {tag:/VB.*/}=B | << nsubjpass {tag:/VB.*/}=B]");
			matcher = semgrex.matcher(graph);
			while(matcher.find()){
				IndexedWord nodeA = matcher.getNode("A");
				//	System.out.println("Subject: "+nodeA);
				subjects.add(nodeA);
			}
		}
		if( subjects.isEmpty()){
			semgrex = SemgrexPattern.compile("{tag:/WP.*/}=A");
			matcher = semgrex.matcher(graph);

			while(matcher.find()){
				IndexedWord nodeA = matcher.getNode("A");
				//System.out.println("Subject: "+nodeA);
				subjects.add(nodeA);
			}
		}			
		return subjects;
	}


	private Set<IndexedWord> getConj(SemanticGraph graph){
		//System.out.println("trying to find conjunction");
		Set<IndexedWord> conjs= new HashSet<IndexedWord>();
		SemgrexPattern semgrex = SemgrexPattern.compile("{tag:CC}=conj ");
		SemgrexMatcher matcher = semgrex.matcher(graph);

		while(matcher.find()){
			conjs.add(matcher.getNode("conj"));
		}
		//System.out.println("found conjunctions: "+ conjs);
		return conjs;
	}
}
