package Detection;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.HOGDescriptor;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

//@SuppressWarnings("unused")
@SuppressWarnings("unused")
public class MainKalman {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		// System.loadLibrary("opencv_java2410");
	}

	static Mat imag = null;
	static Mat orgin = null;
	static Mat kalman = null;
	public static Tracker tracker;
	private static boolean vraiKalman = false;

	// Il faudra ouvrir la camera avant
	public static List<List<Rectangle>> process(VideoCapture camera) throws InterruptedException {
		System.out.println("Let's go processing");
		
		List<List<Rectangle>> listListRect = new ArrayList<>();
		
		Mat frame = new Mat();
		Mat outbox = new Mat();
		Mat diffFrame = null;
		Vector<Rect> array = new Vector<Rect>();
		

		// On initialise le background substractor !
		BackgroundSubtractorMOG2 mBGSub = Video.createBackgroundSubtractorMOG2();

		tracker = new Tracker((float) CONFIG._dt, (float) CONFIG._Accel_noise_mag, CONFIG._dist_thres,
				CONFIG._maximum_allowed_skipped_frames, CONFIG._max_trace_length);

		int i = 0, currentFrame = 1;
		double size = camera.get(Videoio.CAP_PROP_FRAME_COUNT);
		System.out.println("On a initialisé tout le bazar de kalman");
		while (true) {
			if (!vraiKalman) {
				List<Rectangle> listRect = new ArrayList<>();
				System.out.println("Loading : " + (int) ((currentFrame / size) * 100) + "%");
				if (!camera.read(frame))
					break;
				//Imgproc.resize(frame, frame, new Size(CONFIG.FRAME_WIDTH, CONFIG.FRAME_HEIGHT), 0., 0.,
				//		Imgproc.INTER_LINEAR);
				imag = frame.clone();
				orgin = frame.clone();
				kalman = frame.clone();
				if (i == 0) {
					// jFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
					diffFrame = new Mat(outbox.size(), CvType.CV_8UC1);
					diffFrame = outbox.clone();
				}
				if (i == 1) {
					diffFrame = new Mat(frame.size(), CvType.CV_8UC1);

					//////////////// Pour la soustraction de contour ///////////////
					// Permet d'extraire les objets dynamique du fond et modifie diffFrame en
					// binaire
					processFrame(camera, frame, diffFrame, mBGSub);
					frame = diffFrame.clone();

					array = detectionContours(diffFrame);
					//////////////////////////////////////////////////////////////
					Vector<Point> detections = new Vector<>();
					// detections.clear();
					Iterator<Rect> it = array.iterator();

					// Boucle pour faire une liste de tous les centres des objets
					while (it.hasNext()) {
						Rect obj = it.next();

						int ObjectCenterX = (int) ((obj.tl().x + obj.br().x) / 2);
						int ObjectCenterY = (int) ((obj.tl().y + obj.br().y) / 2);

						Point pt = new Point(ObjectCenterX, ObjectCenterY);
						detections.add(pt);
					}
					// ///////

					if (array.size() > 0) {
						tracker.update(array, detections, imag);
						Iterator<Rect> it3 = array.iterator();
						while (it3.hasNext()) {
							Rect obj = it3.next();
							listRect.add(new Rectangle(obj.x, obj.y, obj.width, obj.height));
						}
					} else if (array.size() == 0) {
						tracker.updateKalman(imag, detections);
					}
				}
				i = 1;
				currentFrame++;
				listListRect.add(listRect);
			}
		}
		return listListRect;
	}

	// background substractionMOG2
	protected static void processFrame(VideoCapture capture, Mat mRgba, Mat mFGMask, BackgroundSubtractorMOG2 mBGSub) {
		// GREY_FRAME also works and exhibits better performance
		mBGSub.apply(mRgba, mFGMask, CONFIG.learningRate);
		Imgproc.cvtColor(mFGMask, mRgba, Imgproc.COLOR_GRAY2BGRA, 0);
		Mat erode = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(8, 8));
		Mat dilate = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(8, 8));

		Mat openElem = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3), new Point(1, 1));
		Mat closeElem = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(7, 7), new Point(3, 3));

		Imgproc.threshold(mFGMask, mFGMask, 127, 255, Imgproc.THRESH_BINARY);
		Imgproc.morphologyEx(mFGMask, mFGMask, Imgproc.MORPH_OPEN, erode);
		Imgproc.morphologyEx(mFGMask, mFGMask, Imgproc.MORPH_OPEN, dilate);
		Imgproc.morphologyEx(mFGMask, mFGMask, Imgproc.MORPH_OPEN, openElem);
		Imgproc.morphologyEx(mFGMask, mFGMask, Imgproc.MORPH_CLOSE, closeElem);
	}

	private static BufferedImage Mat2bufferedImage(Mat image) {
		MatOfByte bytemat = new MatOfByte();
		Imgcodecs.imencode(".jpg", image, bytemat);
		byte[] bytes = bytemat.toArray();
		InputStream in = new ByteArrayInputStream(bytes);
		BufferedImage img = null;
		try {
			img = ImageIO.read(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}

	public static Vector<Rect> detectionContours(Mat outmat) {
		Mat v = new Mat();
		Mat vv = outmat.clone();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(vv, contours, v, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

		int maxAreaIdx = -1;
		Rect r = null;
		Vector<Rect> rect_array = new Vector<Rect>();

		for (int idx = 0; idx < contours.size(); idx++) {
			Mat contour = contours.get(idx);
			double contourarea = Imgproc.contourArea(contour);
			if (contourarea > CONFIG.MIN_BLOB_AREA && contourarea < CONFIG.MAX_BLOB_AREA) {
				// MIN_BLOB_AREA = contourarea;
				maxAreaIdx = idx;
				r = Imgproc.boundingRect(contours.get(maxAreaIdx));
				rect_array.add(r);
				// Imgproc.drawContours(imag, contours, maxAreaIdx, new
				// Scalar(255, 255, 255));
			}

		}

		v.release();
		return rect_array;
	}

}
