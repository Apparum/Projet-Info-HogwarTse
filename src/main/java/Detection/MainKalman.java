package Detection;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;

public class MainKalman {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		// System.loadLibrary("opencv_java2410");
	}

	private List<Integer> listNbPeople = new ArrayList<>();
	private List<Mat> listMat = new ArrayList<>();
	static Mat imag = null;
	static Mat orgin = null;
	static Mat kalman = null;
	public static Tracker tracker;
	private List<List<Rectangle>> listRects = new ArrayList<>();
	private List<List<Integer>> listLabel = new ArrayList<>();

	public void process(VideoCapture camera) throws InterruptedException {

		Mat frame = new Mat();
		Mat outbox = new Mat();
		Mat diffFrame = null;
		Vector<Rect> array = new Vector<Rect>();

		// On initialise le background substractor !
		BackgroundSubtractorMOG2 mBGSub = Video.createBackgroundSubtractorMOG2();

		tracker = new Tracker((float) CONFIG._dt, (float) CONFIG._Accel_noise_mag, CONFIG._dist_thres,
				CONFIG._maximum_allowed_skipped_frames, CONFIG._max_trace_length);

		int i = 0;
		while (true) {
			if (!camera.read(frame))
				break;
			Imgproc.resize(frame, frame, new Size(CONFIG.FRAME_WIDTH, CONFIG.FRAME_HEIGHT), 0., 0.,
					Imgproc.INTER_LINEAR);
			imag = frame.clone();
			orgin = frame.clone();
			kalman = frame.clone();
			if (i == 0) {
				diffFrame = new Mat(outbox.size(), CvType.CV_8UC1);
				diffFrame = outbox.clone();
			}

			if (i == 1) {
				diffFrame = new Mat(frame.size(), CvType.CV_8UC1);

				processFrame(camera, frame, diffFrame, mBGSub);
				frame = diffFrame.clone();

				array = detectionContours(diffFrame);
				Vector<Point> detections = new Vector<>();
				Iterator<Rect> it = array.iterator();

				while (it.hasNext()) {
					Rect obj = it.next();

					int ObjectCenterX = (int) ((obj.tl().x + obj.br().x) / 2);
					int ObjectCenterY = (int) ((obj.tl().y + obj.br().y) / 2);

					Point pt = new Point(ObjectCenterX, ObjectCenterY);
					detections.add(pt);
				}
				List<Rectangle> rectFrame = new ArrayList<>();
				if (array.size() > 0) {
					tracker.update(array, detections, imag);
					Iterator<Rect> it3 = array.iterator();
					int compteur = 0;
					while (it3.hasNext()) {
						Rect obj = it3.next();

						int ObjectCenterX = (int) ((obj.tl().x + obj.br().x) / 2);
						int ObjectCenterY = (int) ((obj.tl().y + obj.br().y) / 2);

						Point pt = new Point(ObjectCenterX, ObjectCenterY);
						
						rectFrame.add(new Rectangle(obj.x, obj.y, obj.width, obj.height));

						Imgproc.rectangle(imag, obj.br(), obj.tl(), new Scalar(0, 255, 0), 2);
						Imgproc.circle(imag, pt, 1, new Scalar(0, 0, 255), 2);
						compteur++;
					}
					this.listLabel.add(tracker.getListLabel());
					this.listRects.add(rectFrame);
					this.listNbPeople.add(compteur);
				} else if (array.size() == 0) {
					tracker.updateKalman(imag, detections);
				}

				for (int k = 0; k < tracker.tracks.size(); k++) {
					int traceNum = tracker.tracks.get(k).trace.size();
					if (traceNum > 1) {
						for (int jt = 1; jt < tracker.tracks.get(k).trace.size(); jt++) {
							Imgproc.line(imag, tracker.tracks.get(k).trace.get(jt - 1),
									tracker.tracks.get(k).trace.get(jt),
									CONFIG.Colors[tracker.tracks.get(k).track_id % 9], 2, 4, 0);
						}
					}
				}
			}
			this.listMat.add(imag);
			i = 1;
		}
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
				maxAreaIdx = idx;
				r = Imgproc.boundingRect(contours.get(maxAreaIdx));
				rect_array.add(r);
			}
		}
		v.release();
		return rect_array;
	}

	// GETTERS

	public List<Mat> getListMat() {
		return this.listMat;
	}

	public List<Integer> getListNbPeople() {
		return this.listNbPeople;
	}
	
	public List<List<Rectangle>> getListRects(){
		return this.listRects;
	}
	
	public List<List<Integer>> getListLabel(){
		return this.listLabel;
	}
}
