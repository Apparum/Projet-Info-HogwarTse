import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
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

public class myMethod {
	public static void main(final String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		final Mat frame = new Mat();
		final VideoCapture camera = new VideoCapture("metro.mp4");
		final JFrame jframe = new JFrame("Hogwar'TSE");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JLabel vidpanel = new JLabel();
		jframe.setSize(370, 330);
		jframe.setContentPane(vidpanel);
		jframe.setVisible(true);

		final HOGDescriptor hog = new HOGDescriptor();
		// new Size(128, 64), new Size(16, 16), new Size(8, 8), new Size(8, 8),
		// 9, 0, -1, 0, 0.2, false, 64, false
		final MatOfFloat descriptors = HOGDescriptor.getDefaultPeopleDetector();
		hog.setSVMDetector(descriptors);
		final MatOfRect foundLocations = new MatOfRect();
		final MatOfDouble foundWeights = new MatOfDouble();
		final Size winStride = new Size(8, 8);
		final Size padding = new Size(32, 32);
		final Point rectPoint1 = new Point();
		final Point rectPoint2 = new Point();
		final Scalar rectColor = new Scalar(0, 255, 0);

		while (true) {
			if (camera.read(frame)) {
				hog.detectMultiScale(frame, foundLocations, foundWeights, 0.0, winStride, padding, 1.05, 2.0, false);
				if (foundLocations.rows() > 0) {
					final List<Rect> rectList = foundLocations.toList();
					for (final Rect rect : rectList) {
						rectPoint1.x = rect.x;
						rectPoint1.y = rect.y;
						rectPoint2.x = rect.x + rect.width;
						rectPoint2.y = rect.y + rect.height;
						// Draw rectangle around fond object
						Imgproc.rectangle(frame, rectPoint1, rectPoint2, rectColor, 2);
					}
				}
				final ImageIcon image = new ImageIcon(MatToBufferedImage(frame));
				vidpanel.setIcon(image);
				vidpanel.repaint();
			} else {
				jframe.setVisible(false);
				jframe.dispose();
				break;
			}
		}
	}

	public static BufferedImage MatToBufferedImage(final Mat frame) {
		// Mat() to BufferedImage
		int type = 0;
		if (frame.channels() == 1) {
			type = BufferedImage.TYPE_BYTE_GRAY;
		} else if (frame.channels() == 3) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		final BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
		final WritableRaster raster = image.getRaster();
		final DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
		final byte[] data = dataBuffer.getData();
		frame.get(0, 0, data);

		return image;
	}
}
