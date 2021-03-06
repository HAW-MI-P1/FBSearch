/******************************************************************************
 * Modellierung von Informationssystemen - FBSearch
 ******************************************************************************
 * MIP-Group:       1
 * Component:       Model
 *
 * Authors:         Ren�, Hagen
 *
 * Updated:         2014.11.07
 *
 * Version:         0.01
 ******************************************************************************
 * Description:     ----
 *****************************************************************************/

/******************************************************************************
 *                                 Package                                    *
 *****************************************************************************/

package de.haw.app;

/******************************************************************************
 *                                 Imports                                    *
 *****************************************************************************/

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import org.joda.time.DateTime;

import de.haw.model.ComponentID;

/******************************************************************************
 *                              Class Definition                              *
 *****************************************************************************/

public class Logger
{

/******************************************************************************
 *                                  Fields                                    *
 *****************************************************************************/

	private static BufferedWriter writer;
	
/******************************************************************************
 *                              Public Methods                                *
 *****************************************************************************/
	
    public static void log(String msg, ComponentID component)
    {
    	String now = DateTime.now().toString("yyyy_MM_dd_HH_mm_ss");
    	String str = ": " + component.toString() + ": " + msg;
    	
        System.out.println(now + str);
         
        if(writer == null)
        {
        	 try
        	 {
				 writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("FBSearchLog_" + now + ".log"), "utf-8"));
			 }
        	 catch (UnsupportedEncodingException e)
        	 {
				 e.printStackTrace();
			 }
        	 catch (FileNotFoundException e)
        	 {
			 	 e.printStackTrace();
			 }
        }
        
        try
        {
			writer.write(now + str + "\n");
	        writer.flush();
		}
        catch (IOException e)
        {
			e.printStackTrace();
		}
    }
    
    @Override
    protected void finalize() throws Throwable
    {
    	if(writer != null)
    	{
    		writer.close();
    	}
    	
		super.finalize();
    }
}
