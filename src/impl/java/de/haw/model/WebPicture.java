/******************************************************************************
 * Modellierung von Informationssystemen - FBSearch
 ******************************************************************************
 * MIP-Group:       1
 * Component:       Controller
 *
 * Authors:         Ren�, Hagen
 *
 * Updated:         2015.01.20
 *
 * Version:         0.02
 ******************************************************************************
 * Description:     ----
 *****************************************************************************/

/******************************************************************************
 *                                 Package                                    *
 *****************************************************************************/

package de.haw.model;

/******************************************************************************
 *                                 Imports                                    *
 *****************************************************************************/

import java.io.*;
import java.net.*;

import de.haw.app.Logger;

/******************************************************************************
 *                              Class Definition                              *
 *****************************************************************************/

public class WebPicture 
{

/******************************************************************************
 *                                  Fields                                    *
 *****************************************************************************/

	private static final String TEMP_PATH = "temp.dat";
	
	private URL     url;
	private byte[]  picture;
	private boolean isDownloaded;

/******************************************************************************
 *                         Construction & Initialization                      
 *****************************************************************************/

	public WebPicture(URL url)
	{
		this.url          = url;
		this.isDownloaded = false;
		this.picture      = null;
	}

/******************************************************************************
 *                                Methods                                     *
 *****************************************************************************/

	public URL getUrl()
	{
		return this.url;
	}
	
	public byte[] getPicture() throws IOException
	{
		if(!this.isDownloaded)
		{
			downloadImage();
		}
		
		return this.picture;
	}
	
	public boolean isDownloaded()
	{
		return this.isDownloaded;
	}
	
	public String pictureToFile() throws IOException
	{
		FileOutputStream fileStream = new FileOutputStream(WebPicture.TEMP_PATH);
		
		// byte array to file
		fileStream.write(this.getPicture());
		fileStream.close();
		
		return TEMP_PATH;
	}
	
	private void downloadImage() throws IOException
	{
		InputStream           inputStream  = new BufferedInputStream(this.url.openStream());
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

		for (int i = 0; (i = inputStream.read()) != -1;) 
		{
			byteStream.write(i);
		}
		
		inputStream.close();
		byteStream.close();
		
		Logger.log("Download image from " + this.url.toString() + " completed.", ComponentID.Model);
		
		// download ok!
		this.isDownloaded = true;
		this.picture = byteStream.toByteArray();
	}
}
