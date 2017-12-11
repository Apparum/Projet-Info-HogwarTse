package Video;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import Detection.HOGDetection;
import Rectangle.Image_;
import Rectangle.Video;
import Rectangle.rectangle;

import java.awt.Rectangle;

@SuppressWarnings("unused")
public class VideoReader {

	private VideoCapture video;
	private List<Mat> frames = new ArrayList<>();
	private List<Mat> framesClone = new ArrayList<>();
	private String path;
	private HOGDetection hog = new HOGDetection();
	final Scalar rectColor = new Scalar(0, 255, 0);
	private List<List<Rectangle>> rects = new ArrayList<>();

	public VideoReader(String path) {
		this.path = path;
	}

	public void init() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		final Mat frame = new Mat();
		this.video = new VideoCapture(this.path);
		this.video.read(frame);
		double size = this.video.get(Videoio.CAP_PROP_FRAME_COUNT);
		int currentFrame = 0;
		System.out.println("There are " + size + " frames");

		Video vid = new Video();

		Rectangle rect1 = new Rectangle(1, 1, 1, 1);
		Rectangle rect2 = new Rectangle(2, 2, 1, 1);
		Rectangle rect3 = rect1.intersection(rect2);
		System.out.println(rect3);

		while (this.video.read(frame)) {
			System.out.println("Loading : " + (int) ((currentFrame / size) * 100) + "%");

			rects.add(this.hog.detect(frame));
			/*
			 * Image_ img = this.hog.detect(frame); vid.addImage(img);
			 */
			this.framesClone.add(frame.clone());
			currentFrame += 1;
		}
		System.out.println("Fin du chargement");

		// vid.interpolation();
		rects = this.interpolation(rects);

		int nbFrame = 0;
		for (Mat matFrame : framesClone) {
			for (final Rectangle rectangle : rects.get(nbFrame)) {
				Imgproc.rectangle(matFrame, new Point(rectangle.x, rectangle.y),
						new Point(rectangle.x + rectangle.width, rectangle.y + rectangle.height), this.rectColor, 1);
			}
			this.frames.add(matFrame.clone());
			nbFrame += 1;
		}
		/*
		 * int compteur = 0; for (Mat matFrame : framesClone) {
		 * System.out.println("work in progress"); for (final rectangle rect :
		 * vid.getImage(compteur).getRectangles()) {
		 * System.out.println("work in progress"); Imgproc.rectangle(matFrame,
		 * rect.getCoord_hg(), rect.getCoord_bd(), this.rectColor, 1);
		 * System.out.println("work in progress"); Imgproc.putText(matFrame,
		 * String.format("%s", rect.getLabel()), rect.getCoord_hg(), 1, 2, new Scalar(0,
		 * 0, 255)); System.out.println("work in progress"); } compteur += 1;
		 * 
		 * // final ImageIcon image = new ImageIcon(MatToBufferedImage(frame));
		 * this.frames.add(frame.clone()); }
		 */
		///////////////////////// Code Dorian ////////////////////////////////////////
		/*
		 * while (this.video.read(frame)) { System.out.println("Loading : " + (int)
		 * ((currentFrame / size) * 100) + "%");
		 * 
		 * Image_ img = this.hog.detect(frame); System.out.println("ca marche");
		 * vid.addImage(img);
		 * 
		 * //this.rects.add(this.hog.detect(frame));
		 * 
		 * // On fait une interpolation sur rects ici !
		 * 
		 * for(final rectangle rect : vid.getImage(compteur).getRectangles()) {
		 * Imgproc.rectangle(frame, rect.getCoord_hg(), rect.getCoord_bd(),
		 * this.rectColor, 1); //Imgproc.putText(frame, String.format("%s",
		 * rect.getLabel()), // new Point(rectPoint1.x, rectPoint1.y), 1, 2, new
		 * Scalar(0, 0, 255)); } compteur+=1;
		 * 
		 * // final ImageIcon image = new ImageIcon(MatToBufferedImage(frame));
		 * this.frames.add(frame.clone()); currentFrame += 1; }
		 * 
		 */
		///////////////////////////////////////////////////////////////
		System.out.println("Loading completed !");
	}

	private int intersectionArea(Rectangle rect1, Rectangle rect2) {
		Rectangle rect3 = rect1.intersection(rect2);
		if (rect3.isEmpty())
			return -1;
		else {
			return rect3.width * rect3.height;
		}
	}

	private List<List<Rectangle>> interpolation(List<List<Rectangle>> listImg) {
		int compteur = 0;
		int maxArea;
		boolean condition;
		for (List<Rectangle> listRect : listImg) {
			if (compteur > 0 && compteur < listImg.size() - 1) {
				for (Rectangle rectPrevious : listImg.get(compteur - 1)) {
					maxArea = 0;
					Rectangle rectNextIntersect = new Rectangle();
					for (Rectangle rectNext : listImg.get(compteur + 1)) {
						condition = true;
						for (Rectangle actualRect : listImg.get(compteur)) {
							if(actualRect.intersects(rectNext.intersection(rectPrevious))) {
								condition = false;
							}
						}
						if (this.intersectionArea(rectPrevious, rectNext) > maxArea && condition) {
							maxArea = this.intersectionArea(rectPrevious, rectNext);
							rectNextIntersect = rectNext;
						}
					}
					if (rectNextIntersect != null && !rectNextIntersect.isEmpty()) {
						listImg.get(compteur).add(this.rectangleAvg(rectPrevious, rectNextIntersect));
					}
				}
			}
			compteur += 1;
		}
		return listImg;
	}

	private Rectangle rectangleAvg(Rectangle rect1, Rectangle rect2) {
		return new Rectangle(this.avg(rect1.x, rect2.x), this.avg(rect1.y, rect2.y), this.avg(rect1.width, rect2.width),
				this.avg(rect1.height, rect2.height));
	}

	private int avg(int a, int b) {
		return (a + b) / 2;
	}

	public int size() {
		return this.frames.size();
	}

	public Mat getMat(int frame) {
		return this.frames.get(frame);
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

	public VideoCapture getVideo() {
		return this.video;
	}

	public void setVideo(VideoCapture video) {
		this.video = video;
	}
}
