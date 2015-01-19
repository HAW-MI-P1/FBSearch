package de.haw.controller;

import de.haw.db.DB;
import de.haw.db.DBImpl;
import de.haw.detector.Detector;
import de.haw.detector.DetectorImpl;
import de.haw.fuzzy.Fuzzy;
import de.haw.fuzzy.FuzzyImpl;
import de.haw.model.types.Type;
import de.haw.parser.Parser;
import de.haw.parser.ParserImpl;
import de.haw.taxonomy.Taxonomy;
import de.haw.taxonomy.TaxonomyImpl;
import de.haw.wrapper.Wrapper;
import de.haw.wrapper.WrapperImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class ControllerTests {

	Controller controller;

	Parser parser;
	Wrapper wrapper;
	DB db;
	Detector detector;
	Taxonomy taxonomy;
	Fuzzy fuzzy;

	@Before
	public void setUp() {
		System.out.println("Starting Controller Test");
		parser = new ParserImpl();
		wrapper = new WrapperImpl();
		db = new DBImpl();
		detector = new DetectorImpl();
		taxonomy = new TaxonomyImpl(Arrays.asList("place"));
		fuzzy = new FuzzyImpl();
		controller = new ControllerImpl(parser, wrapper, db, detector,
				taxonomy, fuzzy);

	}

	@Test
	public void TestSearchEmpty() {
		Collection<Type> resultData = new ArrayList<Type>();
		Collection<Type> result = controller.search(0, "");
		assertEquals(result, resultData);
	}

	@Test(expected = de.haw.model.exception.IllegalArgumentException.class)
	public void TestSearchNull() {
		int i = new Integer(null);
		Collection<Type> result = controller.search(i, null);
	}

	@Test(expected = de.haw.model.exception.IllegalArgumentException.class)
	public void TestSearch01() {
		int i = -1;
		Collection<Type> result = controller.search(i, "sd");
	}

	@Test(expected = de.haw.model.exception.IllegalArgumentException.class)
	public void TestSearchExtendedNull() {
		int i = new Integer(null);
		int j = new Integer(null);
		Collection<Type> resultData = new ArrayList<Type>();
		Collection<Type> result = controller.searchExtended(i, j, "", false);
	}

	@Test(expected = de.haw.model.exception.IllegalArgumentException.class)
	public void TestSearchExtended01() {
		int i = -1;
		int j = -1;
		Collection<Type> result = controller.searchExtended(i, j, "sd", true);
	}

	@Test
	public void TestSearchRecsEmpty() {
		Collection<String> resultData = new ArrayList<String>();
		Collection<String> result = controller.searchRecs("");
		assertEquals(result, resultData);
	}

	@Test(expected = de.haw.model.exception.IllegalArgumentException.class)
	public void TestSearchRecsNull() {
		Collection<String> result = controller.searchRecs(null);
	}

	@Test(expected = de.haw.model.exception.IllegalArgumentException.class)
	public void TestSearchRecs01() {
		Collection<String> result = controller.searchRecs("sadfdsd asdf");
	}

}
