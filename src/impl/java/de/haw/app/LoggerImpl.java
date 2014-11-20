package de.haw.app;

import java.util.logging.Level;

/**
 * @author Fenja
 *
 */
public class LoggerImpl implements Logger {
	
	public LoggerImpl(Level p_Level) {
		
	}

	/* (non-Javadoc)
	 * @see de.haw.app.ILogger#Log(java.util.logging.Level, java.lang.String, java.lang.String)
	 */
	
	@Override
	public void Log(Level p_Level, String p_Component, String p_Message) {
        System.out.println(p_Message);
		// TODO Auto-generated method stub

	}

}
