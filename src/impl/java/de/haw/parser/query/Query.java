package de.haw.parser.query;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Describes a query.
 */
public class Query {

	private Conjunctions conjunction = null;
	
	private Selections selection = null;
	
	private Map<Attributes, List<String>> attributeLists = 
			new HashMap<Attributes, List<String>>();
	
	public Query() {
		
	}
	
	public boolean hasConjunction() {
		return (conjunction != null);
	}
	
	public void setConjunction(Conjunctions conjunction) {
		this.conjunction = conjunction;
	}
	
	public Conjunctions getConjunction() {
		return conjunction;
	}
	
	public boolean hasSelection() {
		return (selection != null);
	}
	
	public void setSelection(Selections selection) {
		this.selection = selection;
	}
	
	public void addAttribute(Attributes attribute, String value) {
		List<String> values = attributeLists.get(attribute);
		if (values == null) {
			values = new LinkedList<String>();
			attributeLists.put(attribute, values);
		}
		values.add(value);
	}
	
	public boolean hasAttributes(Set<Attributes> attributes) {
		boolean result = true;
		for (Attributes a: attributes) {
			if (!attributeLists.containsKey(a)) result = false;
		}
		
		return result;
	}
	
	public boolean hasAnyAttribute(Set<Attributes> attributes) {
		boolean result = false;
		for (Attributes a: attributes) {
			if (attributeLists.containsKey(a)) result = true;
		}
		
		return result;
	}
	
	public boolean isComplete() {
		Attributes as[] = new Attributes[]{Attributes.INTERESTS, Attributes.NAMES,
				Attributes.LOCATIONS, Attributes.AGES};
		Set<Attributes> attrSet = new HashSet<Attributes>();
		for (Attributes a: as) {
			attrSet.add(a);
		}
		
		return (hasSelection() && hasConjunction() && hasAnyAttribute(attrSet));
	}
	
}
