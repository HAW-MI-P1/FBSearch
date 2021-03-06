package de.haw.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.json.JSONObject;

import de.haw.model.exception.IllegalArgumentException;
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
	
	private StanfordCoreNLP pipeline;
	private Dictionary dict=new Dictionary();
	
	public ParserImpl(){
		//create parsing toolchain
		Properties props=new Properties();
			props.put("pos.model", "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
			props.put("annotators","tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		//props.put("pos.model", "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
		//props.put("annotators","tokenize, ssplit, pos, lemma, parse");
		pipeline = new StanfordCoreNLP(props);
	}

	@Override
	public JSONObject parse(String naturalLanguage) {
		if ((naturalLanguage == null) ||
				(naturalLanguage.equals(""))) {
			throw new de.haw.model.exception.IllegalArgumentException("Please specify a search term");
		}
 		
		//Annotate String and initialize evaluation graph
		Annotation annotation = new Annotation(naturalLanguage);
		pipeline.annotate(annotation);

		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		ArrayCoreMap sentence;
		try {
			sentence = (ArrayCoreMap) sentences.get(0); // Edited by RB & HH: throw correct exception when no sentence is present
		} catch (Exception e) {
			throw new de.haw.model.exception.IllegalArgumentException("Please specify a search term");
		}
		Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);

		SemanticGraph graph = SemanticGraphFactory.generateUncollapsedDependencies(tree);
		//SemanticGraph graph = SemanticGraphFactory.generateCollapsedDependencies(tree);

		//graph.prettyPrint();
		System.out.println(graph);
		//get Information from the Tree
		List<JSONObject> jsons=new ArrayList<>();

		Map<IndexedWord, Set<IndexedWord>> verbs= get_full_verb_and_object(graph);
		Map<IndexedWord, Set<IndexedWord>> adverbs = get_adverb_with_prop(graph);
		
		Set<IndexedWord> question=getQuestionWord(graph);	
		Set<IndexedWord> subjects=getSubject(graph);
		Set<IndexedWord> conjs=getConj(graph);
		
		jsons.add(Serializer.serializeQuestionWords(question));
		
		IndexedWord type = null;
		if ((question.iterator() != null) && question.iterator().hasNext()) {
			type = question.iterator().next();
		}
		
		jsons.add(Serializer.serializeVerbs(verbs, type));
		jsons.add(Serializer.serializeAdverbs(adverbs));
		jsons.add(Serializer.serializeSubject(subjects));
		jsons.add(Serializer.serializeConj(conjs));
		JSONObject finalJSON = Serializer.mergeAll(jsons);
		
		if (!finalJSON.has(Dictionary.type)
				|| !(finalJSON.has(Dictionary.age) || finalJSON.has(Dictionary.interests)
						|| finalJSON.has(Dictionary.location) || finalJSON.has(Dictionary.name))) {
			throw new IllegalArgumentException("incomplete search term");
		}
		
		return finalJSON;
	}
	
	/**
	 * @return all adverbs with and corresponding prepositional object(s)
	 */
	private Map<IndexedWord, Set<IndexedWord>> get_adverb_with_prop(SemanticGraph graph) {
		SemgrexPattern semgrex = SemgrexPattern.compile(
				"{tag:/JJ.*/}=adverb [>>cop {tag:/VB.*/}=verb & ? >>pobj ({tag:/NN.*/}=object)]");
		SemgrexMatcher matcher = semgrex.matcher(graph);
		Map<IndexedWord, Set<IndexedWord>> adverbs = 
				new HashMap<IndexedWord, Set<IndexedWord>>();
		
		while (matcher.find()) {
			//IndexedWord nodeVerb = matcher.getNode("verb");
			IndexedWord nodeAdverb = matcher.getNode("adverb");
			IndexedWord nodeObject = matcher.getNode("object");
			
			System.out.println("Found adverb " + nodeAdverb.lemma());
			if (nodeObject != null) {
				System.out.println("with object " + nodeObject.lemma());
			}
			
			Set<IndexedWord> additions = adverbs.get(nodeAdverb);
			if (additions == null) {
				additions = new HashSet<IndexedWord>();
			}
			if (nodeObject != null) {
				additions.add(nodeObject);
			}
			adverbs.put(nodeAdverb, additions);
		}
		return adverbs;
	}
	
	/*
	 * returns main verbs with corresponding object
	 * */
	/**
	 * @param graph
	 * @return
	 */
	private Map<IndexedWord, Set<IndexedWord>> get_full_verb_and_object(SemanticGraph graph){


		SemgrexPattern semgrex = SemgrexPattern.compile("{tag:/NN.*/}=object [<<pobj ({}=conj < {tag:/VB.*/}=verb)   | 1,2<<dobj {tag:/VB.*/}=verb ]");
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
	
	private Set<IndexedWord> getQuestionWord(SemanticGraph graph){
		Set<IndexedWord> question=new HashSet<IndexedWord>();
		
			SemgrexPattern semgrex = SemgrexPattern.compile("{tag:/W.*/}=A");
			SemgrexMatcher matcher = semgrex.matcher(graph);

			while(matcher.find()) {
				IndexedWord nodeA = matcher.getNode("A");

				System.out.println("Question Word: "+nodeA);
				question.add(nodeA);
			}
		
		return question;
	}

	private Set<IndexedWord> getSubject(SemanticGraph graph){
		//System.out.println("trying to get subject");

		//<<nsubj {tag:/NN.*/}=B 
		Set<IndexedWord> subjects= new HashSet<IndexedWord>();
		//	SemgrexPattern semgrex = SemgrexPattern.compile("{tag:/W.*/}=A  <<nsubj {}=C << {tag:/NN.*/}=B ");
		SemgrexPattern semgrex = SemgrexPattern.compile(" {tag:/WP.*/}=A <</nsubj.*/ {tag:/VB.*/}=C ");
		SemgrexMatcher matcher = semgrex.matcher(graph);

		while(matcher.find()) {
			IndexedWord nodeA = matcher.getNode("A");
			IndexedWord nodeB = matcher.getNode("B");;

			/*			System.out.println("Subject: "+nodeA);
			System.out.println("pradicate: "+nodeB);
			 */
			subjects.add(nodeA);
		}

		if(subjects.isEmpty()){
			//{tag:/WR.*/}=A <<advmod {tag:/VB.*/}=C )
			semgrex = SemgrexPattern.compile(" {tag:/VB.*/}=C >>/auxpass | advmod/ {tag:/WR.*/}=A ?>>/nsubj.*/ {tag:/NN.*/}=B");
			matcher = semgrex.matcher(graph);

			while(matcher.find()){
				IndexedWord nodeA = matcher.getNode("A");
				IndexedWord nodeB = matcher.getNode("B");;

				/*System.out.println("Subject: "+nodeA);
				System.out.println("pradicate: "+nodeB);
				 */
				subjects.add(nodeA);
				if(nodeB!=null) subjects.add(nodeB);
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
		
		if (subjects.isEmpty()) {
			String nounPattern = "{tag:/NN.*/}=S1 ? >>nn {tag:/NN.*/}=S2 ? >>nn {tag:/NN.*/}=S3";
			semgrex = SemgrexPattern.compile(
					"[{tag:/JJ.*/}=adverb | {tag:/VBZ.*/}=verb]"
					+ "[>>nsubj " + nounPattern + " | >>nsubjpass " + nounPattern + "]");
			matcher = semgrex.matcher(graph);
			while (matcher.find()) {
				for (int i = 0; i < 3; i++) {
					IndexedWord subject = matcher.getNode("S" + i);
					if (subject != null) {
						subjects.add(subject);
					}
				}
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
		System.out.println("subjects: " + subjects);
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
