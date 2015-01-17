package de.haw.wrapper;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import de.haw.model.WebPicture;
import de.haw.model.types.Type;

public class WrapperTests {

	Wrapper wrapper;

	@Before
	public void setUp() {
		System.out.println("Starting Wrapper Test");
		wrapper = new WrapperImpl();

	}

	@Test
	public void TestEmpty01() {
		List<String> test = new ArrayList<String>();
		Collection<Type> resultData = new ArrayList<Type>();
		Collection<Type> result = wrapper.searchForName("", test);
		assertEquals(result, resultData);
	}

	@Test(expected = de.haw.model.exception.IllegalArgumentException.class)
	public void TestNull01() {
		Collection<Type> result = wrapper.searchForName(null, null);
	}

	@Test
	public void TestEmpty02() {
		Collection<Type> test = new ArrayList<Type>();
		List<WebPicture> res = new ArrayList<WebPicture>();
		List<WebPicture> result = wrapper.getPicturesForPersons(test);
		assertEquals(result, res);
	}

	@Test(expected = de.haw.model.exception.IllegalArgumentException.class)
	public void TestNull02() {
		List<WebPicture> result = wrapper.getPicturesForPersons(null);
	}

	@Test
	public void TestEmpty03() {
		assertFalse(wrapper.idLivesIn("", ""));
	}

	@Test(expected = de.haw.model.exception.IllegalArgumentException.class)
	public void TestNull03() {
		assertFalse(wrapper.idLivesIn(null, null));
	}

	@Test(expected = de.haw.model.exception.IllegalArgumentException.class)
	public void Test01() {
		wrapper.idLivesIn("123 sdf", "Kujh");
	}

	@Test
	public void TestEmpty04() {
		JSONObject test = new JSONObject();
		Collection<Type> test2 = new ArrayList<Type>();
		Collection<Type> res = new ArrayList<Type>();
		Collection<Type> result = wrapper.collectExtended(test, test2);
		assertEquals(result, res);
	}

	@Test(expected = de.haw.model.exception.IllegalArgumentException.class)
	public void TestNull04() {
		Collection<Type> result = wrapper.collectExtended(null, null);
	}

}
