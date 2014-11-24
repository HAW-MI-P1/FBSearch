package de.haw.db;

import static org.junit.Assert.fail;

import org.json.JSONObject;
import org.junit.Test;

public class SaveLoadTest {

	@Test
	public void testPut() {
		DBImpl db = new DBImpl();
		try {
			int key = (int)(Math.random()*10000);
			db.connect("jdbc:mysql://localhost/mip", "root", "root");
			db.save(key, "naturalLanguageasdasd", new JSONObject(), de.haw.testData.Person.getCollection());
			db.load(key);

		} catch (Exception e) {
			fail();
		}
		
	}

}
