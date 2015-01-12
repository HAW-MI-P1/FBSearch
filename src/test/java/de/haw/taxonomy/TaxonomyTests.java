package de.haw.taxonomy;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
		assertEquals(expResult, result);

        expResult = Arrays.asList("Hamburg");
        result = taxonomy.search("place","Altona");
        assertEquals(expResult, result);

        expResult = new ArrayList<String>();
        result = taxonomy.search(null, "Altona");
        assertEquals(expResult, result);

        expResult = new ArrayList<String>();
        result = taxonomy.search("place", null);
        assertEquals(expResult, result);

        expResult = new ArrayList<String>();
        result = taxonomy.search(null, null);
        assertEquals(expResult, result);

        expResult = new ArrayList<String>();
        result = taxonomy.search("", "Altona");
        assertEquals(expResult, result);

        expResult = new ArrayList<String>();
        result = taxonomy.search("place", "");
        assertEquals(expResult, result);

        expResult = new ArrayList<String>();
        result = taxonomy.search("", "");
        assertEquals(expResult, result);

        expResult = new ArrayList<String>();
        result = taxonomy.search("place", "a");
        assertEquals(expResult, result);
    }

    @Test(expected = org.apache.jena.riot.RiotNotFoundException.class)
    public void fileMissing() throws org.apache.jena.riot.RiotNotFoundException {
        taxonomy = new TaxonomyImpl(Arrays.asList("fasdf"));
    }
}
