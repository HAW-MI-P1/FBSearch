package de.haw.fuzzy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.fail;

public class GetSynonymTest {
	Fuzzy fuzzy;
	
	@Before
	public void setUp() throws Exception {
		this.fuzzy = new FuzzyImpl();
	}

	@Test
	public void testGetSynonym() {
		Collection<String> response = this.fuzzy.getSynonym("lion", 0x0F);

		if(response.size() <= 1){ fail(); }
	}
	
	@Test
	public void testGetSynonymWordNull() {
		try {
			this.fuzzy.getSynonym(null, 0x0F);
			fail();
		} 
		catch (Exception e) {}

	}
	
	@Test
	public void testGetSynonymWordEmpty() {
		try {
			this.fuzzy.getSynonym("", 0x0F);
			fail();
		} 
		catch (Exception e) {}

	}

    @Test
    public void testGetAllSynonymsForLion() {
        Collection<String> synonyms = fuzzy.getSynonym("lion", 0x01);
        Assert.assertEquals(13, synonyms.size());
    }

}
