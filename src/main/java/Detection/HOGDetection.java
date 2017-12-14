package Detection;

import java.awt.image.BufferedImage;

import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.objdetect.HOGDescriptor;

//import Rectangle.Image_;
//import Rectangle.Video;
//import Rectangle.rectangle;

import java.awt.Rectangle;

@SuppressWarnings("unused")
public class HOGDetection {

	public List<Rectangle> detect(Mat frame) {
		HOGDescriptor hog = new HOGDescriptor(new Size(32, 64), new Size(8, 8), new Size(4, 4), new Size(4, 4), 9, 1,
				-1, 1, 0.2, true, 16, false);
		MatOfFloat descriptors = HOGDescriptor.getDefaultPeopleDetector();
		MatOfRect foundLocations = new MatOfRect();
		MatOfDouble foundWeights = new MatOfDouble();
		Size winStride = new Size(0, 0);
		Size padding = new Size(4, 4);
		hog.setSVMDetector(descriptors);
		List<Rectangle> rectList = new ArrayList<>();
		hog.detectMultiScale(frame, foundLocations, foundWeights, 0, winStride, padding, 1.05, 1, false);
		
		if (foundLocations.rows() > 0) {
			for(Rect rect : foundLocations.toList()) {
				rectList.add(new Rectangle(rect.x, rect.y, rect.width, rect.height));
			}
		}
		
		/*
		Image_ img = new Image_();
		//List<rectangle> list_point = new ArrayList<>();
		for (final Rect rect : rectList) { // On stocke les coordonnées des rectangles dans list_point
			
			Point rectPoint1 = new Point(); // On declare les 2 points qui nous serviront pour situer les rectangles
			// pour visualiser les personnes detectées
			Point rectPoint2 = new Point();
			rectPoint1.x = rect.x;
			rectPoint1.y = rect.y;
			rectPoint2.x = rect.x + rect.width;
			rectPoint2.y = rect.y + rect.height;
			
			rectangle ephemere = new rectangle(rectPoint1, rectPoint2, 1);
			img.addrectangle(ephemere);
			//list_point.add(ephemere);

			// System.out.println(String.format("%s : (%s, %s) (%s, %s)", compteur,
			// rectPoint1.x, rectPoint1.y, rectPoint2.x, rectPoint2.y));
			// System.out.println(list_point);
		}
		*/
		
		// On affiche le nombre de personnes detectées sur la console
		System.out.println(String.format("%s FACES detected.", foundLocations.toArray().length));
		
		return rectList;
		//return list_point;
	}

	public static Mat bufferedImageToMat(BufferedImage bi) {
		Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
		byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
		mat.put(0, 0, data);
		return mat;
	}
}
