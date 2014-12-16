package de.haw.taxonomy;

import java.util.ArrayList;
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

public class TaxonomyImpl implements Taxonomy{
	
	private Map<String, Model> models = new HashMap<String, Model>();
	
	public TaxonomyImpl(List<String> taxonomys){
		LogManager.getRootLogger().setLevel(Level.OFF);
		for(String taxonomy : taxonomys){
			Model model = RDFDataMgr.loadModel("taxonomy/"+taxonomy+".rdf") ;
			models.put(taxonomy,model);
		}
	}
	
	@Override
	public List<String> search(String taxonomy,String elem) {
		String queryString = "PREFIX rela: <http://taxonomy.org/relationship/> " +
				"SELECT ?x " +
				"WHERE {" +
					"{<http://taxonomy/"+elem+"> rela:childOf ?x }" +
					" UNION "+
					"{ ?x rela:childOf <http://taxonomy/"+elem+"> }" +
				"} LIMIT 3";
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
					String currentElem = results.next().getResource("x").toString().split("/")[3];
					result.add(currentElem);
				}
			}
	    }
	    System.out.println(">>>"+result);
		return result;
	}
}