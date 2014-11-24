package de.haw.db;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.haw.db.DBImpl;
import de.haw.db.exception.IllegalArgumentException;
import de.haw.db.exception.NoSuchEntryException;

public class GetTest {
	private DBImpl db;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.db = new DBImpl();
		this.db.connect("jdbc:mysql://localhost/mip", "root", "root");
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDorw() throws Exception {
		try
		{ this.db.close(); } 
		catch (Exception e) {}
	}

	@Test
	public void testGetSuccess() {
		try {
			int keyHash = (int) (Math.random() * 1000000);
			db.put(keyHash, "value");
			
			String value = db.get(keyHash);
			assertTrue( value.equals("value") );
		} 
		catch (Exception e)
		{ fail(); }
	}
	
	@Test
	public void testGetFailKeyNull() {
		try {
			db.get(0);
			fail();
		} 
		catch (IllegalArgumentException e) {};
	}
	
	@Test
	public void testGetFailNoSuchEntry() {
		try {
			db.get(1);
			fail();
		} 
		catch (NoSuchEntryException e) {};
	}

}
