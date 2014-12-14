package de.haw.controller;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.haw.db.DB;
import de.haw.db.MockUpDBImpl;
import de.haw.detector.Detector;
import de.haw.detector.DetectorImpl;
import de.haw.filter.Filter;
import de.haw.filter.FilterImpl;
import de.haw.parser.Parser;
import de.haw.parser.ParserImpl;
import de.haw.wrapper.Wrapper;
import de.haw.wrapper.WrapperImpl;

public class ExceptionTest
{
	@Before
	public void setUp()
	{
		
	}
	
	@After
	public void tearDorw()
	{
	
	}

	@Test
	public void testSearch()
	{
        Wrapper wrapper = new WrapperImpl();
        Filter filter = new FilterImpl(wrapper);
        DB dbcontrol = new MockUpDBImpl();
        Parser parser = new ParserImpl();
        Detector detector = new DetectorImpl();
        Controller controller = new ControllerImpl(parser, filter, dbcontrol, detector);
        
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
		Wrapper wrapper = new WrapperImpl();
        Filter filter = new FilterImpl(wrapper);
        DB dbcontrol = new MockUpDBImpl();
        Parser parser = new ParserImpl();
        Detector detector = new DetectorImpl();
        Controller controller = new ControllerImpl(parser, filter, dbcontrol, detector);
        
        try
		{
            controller.searchExtended(1, 0, "lorem ipsum");
		} 
		catch (Exception e)
        {
			fail();
        }
	}
}
