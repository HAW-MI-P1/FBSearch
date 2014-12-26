package de.haw.taxonomy;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TaxonomyTests {

	Taxonomy taxonomy;
	
	@Before
	public void setup(){
		taxonomy = new TaxonomyImpl(Arrays.asList("place"));
	}
	
	@Test
	public void testSearch(){
		List<String> expResult = Arrays.asList("Deutschland", "Altona");
		List<String> result = taxonomy.search("place","Hamburg");
		assertEquals(result,expResult);
	}
}
