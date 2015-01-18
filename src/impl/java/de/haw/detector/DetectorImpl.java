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

import de.haw.model.WebPicture;
import de.haw.model.types.Type;
import de.haw.model.types.UserType;
import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/******************************************************************************
 *                              Class Definition                              *
 *****************************************************************************/

public class DetectorImpl implements Detector
{
	private static final double MAGNITUDE                         = 30.0;
	private static final double MINIMAL_MATCH_PERCENTAGE          = 50.0;
	private static final double MINIMAL_NEEDED_MATCHES_PERCENTAGE = 60.0;
	
	private boolean supported;
	
	public DetectorImpl()
	{
		if(this.isWindows64())
		{
			this.supported = true;
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		}
		else
		{
			this.supported = false;
            System.out.println("System does not support picture detection");
			//throw new RuntimeException("Your systeme doesnt support OpenCV Beta version.");
		}
	}
	
/******************************************************************************
 *                              Public Methods                                
 * @throws Exception *
 *****************************************************************************/

    @Override
    public boolean supportsPictureDetection(){
        return this.supported;
    }

	@Override
	public Collection<Type> detectObject(Collection<Type> result, String objectName)
	{
		if(!supported)
		{
			return result;
		}
		
		Collection<Type> personsWithMatchedObjects = new ArrayList<Type>();
		try
		{
			for (Type personType : result) 
			{
				UserType person = (UserType) personType;
				
				boolean foundObjectForPerson = false;
				
				// loop through all pictures
				for (WebPicture picture : person.getPictures()) 
				{
					boolean objectFound = this.findObjectInImage(picture, objectName);
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
	public boolean findObjectInImage(WebPicture picture, String object) throws IOException
	{
		if(!this.supported)
		{
			//throw new RuntimeException("Your systeme doesnt support OpenCV Beta version.");
			return false;
		}
		
		Point[]  locations;
		File     folder    = new File("images/" + object);
		String   file      = picture.pictureToFile();
		Mat      source    = Imgcodecs.imread(file);
		
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
		System.out.println("[IMAGE_DECODER] " + matchCountInPercent + " % matches (count: " + matchCount + ")");
		
		if(matchCountInPercent >= MINIMAL_NEEDED_MATCHES_PERCENTAGE)
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
		
		Mat sourcePolar                = new Mat(source.rows(), source.cols(), source.type());
		Mat templatePolar              = new Mat(template.rows(), template.cols(), template.type());
		Mat matchResult                = new Mat();
		Mat threshold                  = new Mat();
		MinMaxLocResult minMaxLocation = new MinMaxLocResult();
		Point location                 = new Point();
		
		
		// transform data to polar coordinates
		Imgproc.logPolar(source, sourcePolar,  new Point(source.cols() * 0.5, source.rows() * 0.5), 
				MAGNITUDE, Imgproc.INTER_LINEAR + Imgproc.CV_WARP_FILL_OUTLIERS);

        Imgproc.logPolar(template, templatePolar, new Point(template.cols() * 0.5, template.rows() * 0.5), 
        		MAGNITUDE, Imgproc.INTER_LINEAR + Imgproc.CV_WARP_FILL_OUTLIERS);
		
		// matching, normalize and create binary image
		Imgproc.matchTemplate(sourcePolar, templatePolar, matchResult, Imgproc.TM_CCOEFF);
		Core.normalize(matchResult, matchResult, 0, 1, Core.NORM_MINMAX, -1, new Mat());
		Imgproc.threshold(matchResult, threshold, 0.5, 255, 0);

		// find best match
		minMaxLocation = Core.minMaxLoc(matchResult);
		
		// method = Imgproc.TM_SQDIFF || Imgproc.TM_SQDIFF_NORMED
		// location = minMaxLocation.minLoc;
		// else
		location = minMaxLocation.maxLoc;

		// cut out relevant position, calculate width/height
		int    matchedPoints        = 0;
		double matchedPointsPercent = 0;
		int    locationWidth        = ((int)location.x + template.cols() > threshold.cols() ? 
				threshold.cols() - (int)location.x : template.cols());
		int    locationheight       = ((int)location.y + template.rows() > threshold.rows() ? 
				threshold.rows() - (int)location.y : template.rows());
		Mat    locationImage        = new Mat(locationheight, locationWidth, threshold.type());
		Rect   locationRect         = new Rect((int)location.x, (int)location.y, locationWidth, locationheight);
		
		locationImage = threshold.submat(locationRect);
		
		// rate match
		for (int i = 0; i < locationImage.cols(); i++) 
		{
			for (int j = 0; j < locationImage.rows(); j++) 
			{
				if((int)locationImage.get(j, i)[0] == 255)
				{
					matchedPoints++;
				}
			}
		}
		
		matchedPointsPercent = (100.0 / ((double)locationImage.cols() * (double)locationImage.rows())) * 
				(double)matchedPoints;
		
		// Complete image dump?
		/*
		Imgcodecs.imwrite("dump/source.jpg",        source);
		Imgcodecs.imwrite("dump/template.jpg",      template);
		Imgcodecs.imwrite("dump/sourcePolar.jpg",   sourcePolar);
		Imgcodecs.imwrite("dump/templatePolar.jpg", templatePolar);
		Imgcodecs.imwrite("dump/threshold.jpg",     threshold);
		Imgcodecs.imwrite("dump/locationImage.jpg", locationImage);
		System.out.println(matchedPointsPercent);
		*/
		
		if(matchedPointsPercent < MINIMAL_MATCH_PERCENTAGE)
		{
			return null;
		}
		
		return location;
	}
	
	public boolean isSupported()
	{
		return this.supported;
	}
	
	private boolean isWindows64()
	{
		if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0 &&
		   System.getProperty("os.arch").toLowerCase().indexOf("64") >= 0)
		{
			return true;
		}
		else
		{
			return false;
		}	
	}
}
