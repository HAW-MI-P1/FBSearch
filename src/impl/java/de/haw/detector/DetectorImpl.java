/******************************************************************************
 * Modellierung von Informationssystemen - FBSearch
 ******************************************************************************
 * MIP-Group:       1
 * Component:       Controller
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

package de.haw.detector;

/******************************************************************************
 *                                 Imports                                    *
 *****************************************************************************/

import java.io.*;
import java.net.URL;
import java.util.*;

import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;
import org.opencv.core.*;
import org.opencv.core.Core.*;

import de.haw.model.Person;
import de.haw.model.types.Type;

/******************************************************************************
 *                              Class Definition                              *
 *****************************************************************************/

public class DetectorImpl implements Detector
{
	private boolean initialized;
	private boolean initializeFailed;
	
	public DetectorImpl()
	{
		initialized = false;
	}
	
/******************************************************************************
 *                              Public Methods                                
 * @throws Exception *
 *****************************************************************************/

	@Override
	public Collection<Type> detectObject(Collection<Type> result, String objectName)
	{
		if(!initialized)
		{
			try
			{
				System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
				initialized = true;
			}
			catch (Exception e)
			{
				initializeFailed = true;
				e.printStackTrace();
			}
		}
		
		if(initializeFailed)
		{
			return result;
		}
		
		Collection<Type> personsWithMatchedObjects = new ArrayList<Type>();
		try
		{
			for (Type personType : result) 
			{
				Person person = (Person) personType;
				
				boolean foundObjectForPerson = false;
				
				// image loop
				for (URL url : person.getPictures()) 
				{
					boolean objectFound = this.findObjectInImage(downloadImage(url), objectName);
					if(objectFound)
					{
						foundObjectForPerson = true;
						break;
					}
				}
				
				if(foundObjectForPerson)
				{
					personsWithMatchedObjects.add(person);					
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		
		return personsWithMatchedObjects;
	}
	
/******************************************************************************
 *                              Private Methods                               *
 *****************************************************************************/

	// check for matches
	private boolean findObjectInImage(String path, String object)
	{
		Point[]  locations;
		File     folder    = new File("images/" + object);
		Mat      source    = Imgcodecs.imread(path);
		
		if(source.empty())
		{
			System.out.println("[IMAGE_DECODER] Empty source image.");
			throw new IllegalArgumentException("[IMAGE_DECODER] Empty source image.");
		}
		
		if(!folder.exists())
		{
			System.out.println("[IMAGE_DECODER] No images for the search string available.");
			throw new IllegalArgumentException("[IMAGE_DECODER] No images for the search string available.");
		}
		
		if(folder.listFiles().length <= 0)
		{
			System.out.println("[IMAGE_DECODER] No images for the search string available.");
			throw new IllegalArgumentException("[IMAGE_DECODER] No images for the search string available.");
		}
		
		locations = new Point[folder.listFiles().length];
		
		for(int i = 0; i < folder.listFiles().length; i++)
		{
			System.out.println("[IMAGE_DECODER] " + folder.listFiles()[i].getPath());
			Mat template = Imgcodecs.imread(folder.listFiles()[i].getPath());
			
			if(template.empty())
			{
				System.out.println("[IMAGE_DECODER] Empty template image.");
				throw new IllegalArgumentException("[IMAGE_DECODER] Empty template image.");
			}
			
			locations[i] = imageMatching(source, template);
		}
		
		// check match count
		int    matchCount          = 0;
		double matchCountInPercent = 0.0;
		for (int i = 0; i < locations.length; i++) 
		{
			if(locations[i] != null)
			{
				matchCount++;
			}
		}
		
		matchCountInPercent = (100.0 / (double)folder.listFiles().length) * matchCount;
		System.out.println("[IMAGE_DECODER] " + matchCountInPercent + " % matches (as count: " + matchCount + ")");
		
		if(matchCountInPercent >= 70)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	// return location when something was found and it was a good match, else null
	private Point imageMatching(Mat source, Mat template)
	{
		//TODO source greater than template -> resize?
		if(source.cols() < template.cols())
		{}
		
		if(source.rows() < template.rows())
		{}
		
		int             cols             = source.cols() - template.cols() + 1;
		int             rows             = source.rows() - template.rows() + 1;
		Mat             result           = new Mat(cols, rows, CvType.CV_32FC1);
		Point           location         = null;
		MinMaxLocResult minMaxLocation   = null;
		Mat             resultThreshold  = new Mat(result.rows(),   result.cols(),   CvType.CV_32FC1);
		Mat             logPolarSource   = new Mat(source.rows(),   source.cols(),   CvType.CV_32FC1);
		Mat             logPolarTemplate = new Mat(template.rows(), template.cols(), CvType.CV_32FC1);
		
		// transform data to polar coordinates
		Imgproc.logPolar(source, logPolarSource, 
		                 new Point(logPolarSource.cols() / 2, logPolarSource.rows() / 2), 
		                 0.1, Imgproc.CV_WARP_INVERSE_MAP + Imgproc.CV_WARP_FILL_OUTLIERS);

        Imgproc.logPolar(template, logPolarTemplate, 
                         new Point(logPolarTemplate.cols() / 2, logPolarTemplate.rows() / 2), 
                         0.1, Imgproc.CV_WARP_INVERSE_MAP + Imgproc.CV_WARP_FILL_OUTLIERS);
		
		// matching, normalize
		Imgproc.matchTemplate(logPolarSource, logPolarTemplate, result, Imgproc.TM_CCOEFF);
		Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
		Imgproc.threshold(result, resultThreshold, 0.8, 255, 0);

		// find best match
		minMaxLocation = Core.minMaxLoc(result);
		
		// method = Imgproc.TM_SQDIFF || Imgproc.TM_SQDIFF_NORMED
		// location = minMaxLocation.minLoc;
		// else
		location = minMaxLocation.maxLoc;

		// rate match
		int white = 0;
		double percentWhite = 100;
		
		for (int i = 0; i < resultThreshold.cols(); i++) 
		{
			for (int j = 0; j < resultThreshold.rows(); j++) 
			{
				if((int)resultThreshold.get(j, i)[0] == 255)
				{
					white++;
				}
			}
		}
		
		percentWhite = (100.0 / ((double)resultThreshold.cols() * (double)resultThreshold.rows())) * (double)white;

		// Complete image dump?
		//Imgcodecs.imwrite("source.jpg",           source);
		//Imgcodecs.imwrite("template.jpg",         template);
		//Imgcodecs.imwrite("logPolarSource.jpg",   logPolarSource);
		//Imgcodecs.imwrite("logPolarTemplate.jpg", logPolarTemplate);
		//Imgcodecs.imwrite("resultThreshold.jpg",  resultThreshold);
		
		//System.out.println(percentWhite);
		
		if(percentWhite > 5 || percentWhite <= 0.0001)
		{
			return null;
		}
		
		/*// Marked image
		Imgproc.rectangle(source, location, new Point(location.x + template.cols(), 
					      location.y + template.rows()), new Scalar(0, 255, 0));
		Imgcodecs.imwrite("markedMatch.jpg",  source);
		*/
		
		return location;
	}
	
	//Download an image an save the image as file on your harddisk
	private String downloadImage(URL url) throws IOException
	{
		InputStream  inputStream  = new BufferedInputStream(url.openStream());
		OutputStream outputStream = new BufferedOutputStream(new FileOutputStream("temp.dat"));

		for (int i = 0; (i = inputStream.read()) != -1;) 
		{
			outputStream.write(i);
		}
		
		inputStream.close();
		outputStream.close();
		
		return "temp.dat";
	}
}
