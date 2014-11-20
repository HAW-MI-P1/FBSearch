/**
 * 
 */
package de.haw.app;

import java.util.logging.Level;

/**
 * @author Fenja
 *
 */
public interface Logger {
	
	/***
	 * Method to log debug messages from all components
	 * 
	 * @param p_Level Level of severity
	 * @param p_Component Component of origin
	 * @param p_Message Debug Message
	 */
	void Log(Level p_Level, String p_Component, String p_Message);
	
}
