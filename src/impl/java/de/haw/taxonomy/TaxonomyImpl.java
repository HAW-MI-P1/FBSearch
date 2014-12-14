package de.haw.taxonomy;

import java.util.ArrayList;
import java.util.List;

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
	
	private Model model;
	
	public TaxonomyImpl(){
		LogManager.getRootLogger().setLevel(Level.OFF);
		model = RDFDataMgr.loadModel("de/haw/taxonomy/default.rdf") ;
	}
	
	@Override
	public List<String> search(String item) {
		String queryString = "PREFIX rela: <http://taxonomy.org/relationship/> " +
				"SELECT ?x " +
				"WHERE {" +
					"{<http://taxonomy/"+item+"> rela:childOf ?x }" +
					" UNION "+
					"{ ?x rela:childOf <http://taxonomy/"+item+"> }" +
				"}";
		
	    Query query = QueryFactory.create(queryString);

	    // Execute the query and obtain results
	    QueryExecution qe = QueryExecutionFactory.create(query, model);
	    ResultSet results = qe.execSelect();
	    
	    List<String> result = new ArrayList<String>();
	    if (results.hasNext()) {
			while (results.hasNext()) {
				//format name of "elem" from http://xxx/elem style to "elem"
				String currentElem = results.next().getResource("x").toString().split("/")[3];
				result.add(currentElem);
			}
		}
		return result;
	}
}