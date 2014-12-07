/**
 * 
 */
package de.haw.db;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.haw.db.DBImpl;
import de.haw.model.exception.ConnectionException;
import de.haw.model.exception.IllegalArgumentException;

/**
 * @author Florian
 *
 */
public class ConnectTest {
	private DBImpl db;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.db = new DBImpl();
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
	
	/**
	 * Test method for {@link controller.DB#connect()}.
	 */
	@Test
	public void testConnectSuccess() {
		try 
		{ this.db.connect("jdbc:mysql://localhost/mip", "root", "root"); } 
		catch (ConnectionException e)
		{ fail(); }
	}
	
	/**
	 * Test method for {@link controller.DB#connect()}.
	 */
	@Test
	public void testConnectFailUrlNull() {
		try 
		{ this.db.connect(null, "root", "root"); fail();} 
		catch (ConnectionException e)
		{ fail(); }
		catch (IllegalArgumentException e)
		{} 
		
	}
	
	/**
	 * Test method for {@link controller.DB#connect()}.
	 */
	@Test
	public void testConnectFailUrlEmpty() {
		try 
		{ this.db.connect("", "root", "root"); fail();} 
		catch (ConnectionException e)
		{ fail(); }
		catch (IllegalArgumentException e)
		{} 
		
	}
	
	/**
	 * Test method for {@link controller.DB#connect()}.
	 */
	@Test
	public void testConnectFailUserNull() {
		try 
		{ this.db.connect("jdbc:mysql://localhost/mip", null, "root"); fail();} 
		catch (ConnectionException e)
		{ fail(); }
		catch (IllegalArgumentException e)
		{} 
	}
	
	/**
	 * Test method for {@link controller.DB#connect()}.
	 */
	@Test
	public void testConnectFailUserEmpty() {
		try 
		{ this.db.connect("jdbc:mysql://localhost/mip", "", "root"); fail();} 
		catch (ConnectionException e)
		{ fail(); }
		catch (IllegalArgumentException e)
		{} 
	}
	
	/**
	 * Test method for {@link controller.DB#connect()}.
	 */
	@Test
	public void testConnectFailPassNull() {
		try 
		{ this.db.connect("jdbc:mysql://localhost/mip", "root", null); fail();} 
		catch (ConnectionException e)
		{ fail(); }
		catch (IllegalArgumentException e)
		{} 
	}
	
	/**
	 * Test method for {@link controller.DB#connect()}.
	 */
	@Test
	public void testConnectFailPassEmpty() {
		try 
		{ this.db.connect("jdbc:mysql://localhost/mip", "root", ""); fail();} 
		catch (ConnectionException e)
		{ fail(); }
		catch (IllegalArgumentException e)
		{} 
	}

	/**
	 * Test method for {@link controller.DB#close()}.
	 */
	@Test
	public void testCloseSuccess() {
		try
		{ 	
			this.db.connect("jdbc:mysql://localhost/mip", "root", "root");
			this.db.close(); 
		} 
		catch (Exception e)
		{ e.printStackTrace();fail(); }
	}
	
	/**
	 * Test method for {@link controller.DB#close()}.
	 */
	@Test
	public void testCloseFail() {
		try
		{
			this.db.close();
			fail();
		} 
		catch (Exception e) {}
	}
	
}
