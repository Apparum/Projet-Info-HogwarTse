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
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.objdetect.HOGDescriptor;

public class HOGDetection {

	public List<Rect> detect(Mat frame) {
		HOGDescriptor hog = new HOGDescriptor(new Size(32, 64), new Size(8, 8), new Size(4, 4), new Size(4, 4), 9, 1,
				-1, 1, 0.2, true, 16, false);
		MatOfFloat descriptors = HOGDescriptor.getDefaultPeopleDetector();
		MatOfRect foundLocations = new MatOfRect();
		MatOfDouble foundWeights = new MatOfDouble();
		Size winStride = new Size(0, 0);
		Size padding = new Size(4, 4);
		hog.setSVMDetector(descriptors);
		List<Rect> rectList = new ArrayList<>();
		hog.detectMultiScale(frame, foundLocations, foundWeights, 0, winStride, padding, 1.05, 1, false);
		if (foundLocations.rows() > 0) {
			rectList = foundLocations.toList();
		}
		return rectList;
	}

	public static Mat bufferedImageToMat(BufferedImage bi) {
		Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
		byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
		mat.put(0, 0, data);
		return mat;
	}
}
