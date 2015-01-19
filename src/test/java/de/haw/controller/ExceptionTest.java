package de.haw.controller;

import de.haw.db.DB;
import de.haw.db.DBImpl;
import de.haw.detector.Detector;
import de.haw.detector.DetectorImpl;
import de.haw.fuzzy.Fuzzy;
import de.haw.fuzzy.FuzzyImpl;
import de.haw.parser.Parser;
import de.haw.parser.ParserImpl;
import de.haw.taxonomy.Taxonomy;
import de.haw.taxonomy.TaxonomyImpl;
import de.haw.wrapper.Wrapper;
import de.haw.wrapper.WrapperImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ExceptionTest
{
    Wrapper wrapper;
    DB dbcontrol;
    Parser parser;
    Detector detector;
    Taxonomy taxonomy;
    Controller controller;
    Fuzzy fuzzy;

	@Before
	public void setUp()
	{
        wrapper = new WrapperImpl();
        dbcontrol = new DBImpl();
        parser = new ParserImpl();
        detector = new DetectorImpl();
        taxonomy = new TaxonomyImpl(Arrays.asList("place"));
        fuzzy = new FuzzyImpl();
        controller = new ControllerImpl(parser, wrapper, dbcontrol, detector, taxonomy, fuzzy);
	}
	
	@After
	public void tearDorw()
	{
	
	}

	@Test
	public void testSearch()
	{
        try
		{
            controller.search(0, "lorem ipsum");
		} 
		catch (Exception e)
        {
			fail();
        }
	}
	
	@Test
	public void testSearchExtended()
	{
        try
		{
            controller.searchExtended(1, 0, "lorem ipsum", false);
		} 
		catch (Exception e)
        {
			fail();
        }
	}
	
	@Test
	public void testSearchRecs()
	{
        Collection<String> expResult = Arrays.asList("Deutschland", "Altona");
        controller.search(0, "who is called guido westerwell and lives in Hamburg?");
        Collection<String> result = controller.searchRecs("place");
        assertEquals(result,expResult);
	}
}
