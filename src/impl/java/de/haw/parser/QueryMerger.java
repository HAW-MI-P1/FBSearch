package de.haw.parser;

import java.util.List;

import de.haw.parser.query.Attributes;
import de.haw.parser.query.Query;

public class QueryMerger {

	/**
	 * Merge multiple queries if possible. 
	 * @param queries queries to merge
	 * @return merge of queries
	 */
	static Query merge(List<Query> queries) {
		Query resultQuery = new Query();
		
		for (Query query: queries) {
			//merge select into current result
			if (resultQuery.hasSelection()) {
				if (query.hasSelection() 
						&& (!query.getSelection().equals(resultQuery.getSelection()))) {
					//selects for multiple types (e.g. place and user) is not supported
					//TODO: throw exception
				}
			}
			else {
				if (query.hasSelection()) {
					resultQuery.setSelection(query.getSelection());
				}
			}
			
			//merge conjunctions: here the strategy is to take the first one
			if ((!resultQuery.hasConjunction()) && query.hasConjunction()) {
				resultQuery.setConjunction(query.getConjunction());
			}
			
			//merge all attributes
			for (Attributes attribute: query.getAttributes()) {
				for (String entry: query.getAttribute(attribute)) {
					resultQuery.addAttribute(attribute, entry);
				}
			}
		}
		
		return resultQuery;
	}
	
}
