package de.haw.taxonomy;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

import de.haw.model.exception.IllegalArgumentException;

public class TaxonomyImpl implements Taxonomy{
	
	private Map<String, Model> models = new HashMap<String, Model>();
	
	public TaxonomyImpl(List<String> taxonomys){
		if(taxonomys == null) throw new IllegalArgumentException("Inputparameter list is null");
		LogManager.getRootLogger().setLevel(Level.OFF);
		for(String taxonomy : taxonomys){
			if(taxonomy.isEmpty() || (taxonomy == null)){
				throw new IllegalArgumentException("One or more elements of inputparameter list are empty or null");
			} else {
				File f = new File("taxonomy/"+taxonomy+".rdf");
				if(f.exists()){
					Model model = RDFDataMgr.loadModel("taxonomy/"+taxonomy+".rdf") ;
					models.put(taxonomy,model);
				} else {
					throw new IllegalArgumentException("\""+taxonomy+".rdf\" could not be found");
				}
			}
		}
	}
	
	@Override
	public List<String> search(String taxonomy,String elem) {
		if(taxonomy == null || elem == null || taxonomy.isEmpty() || elem.isEmpty()) throw new IllegalArgumentException("An input parameter is empty or null");
		String queryString = "PREFIX "+taxonomy+": <http://taxonomy.org/"+taxonomy+"s/> " +
				"SELECT ?name " +
				"WHERE {" +
					"{<http://taxonomy/"+elem+"> "+taxonomy+":childOf ?x ." +
					"?x "+taxonomy+":title ?name .}" +
					" UNION "+
					"{ ?x "+taxonomy+":childOf <http://taxonomy/"+elem+"> ." +
					"?x "+taxonomy+":title ?name .}" +
				"}";
	    Query query = QueryFactory.create(queryString);

	    // Execute the query and obtain results
	    Model model = models.get(taxonomy);
	    List<String> result = new ArrayList<String>();
	    if(model != null){
		    QueryExecution qe = QueryExecutionFactory.create(query, model);
		    ResultSet results = qe.execSelect();
		    
		    if (results.hasNext()) {
				while (results.hasNext()) {
					//format name of "elem" from http://xxx/elem style to "elem"
					String currentElem = results.next().getLiteral("name").getString();
					result.add(currentElem);
				}
			}

	    }
		return result;
	}
}