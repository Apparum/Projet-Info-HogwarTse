package Detection;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileSystemView;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.HOGDescriptor;
import org.opencv.videoio.VideoCapture;

public class HOGDetection {

	public List<Rect> detect(Mat frame) {
		HOGDescriptor hog = new HOGDescriptor(new Size(32, 64), new Size(8, 8), new Size(4, 4), new Size(4, 4), 9, 1,
				-1, 1, 0.2, true, 16, false);
		MatOfFloat descriptors = HOGDescriptor.getDefaultPeopleDetector();
		MatOfRect foundLocations = new MatOfRect();
		MatOfDouble foundWeights = new MatOfDouble();
		Size winStride = new Size(4, 4);
		Size padding = new Size(0, 0);
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

	public static void main(String[] args) {

	}
}
