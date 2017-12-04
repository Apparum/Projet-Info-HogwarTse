package Video;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.List;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import Detection.HOGDetection;

public class VideoReader {

	private VideoCapture video;
	private List<ImageIcon> frames = new ArrayList<>();
	private String path;
	private HOGDetection hog = new HOGDetection();
	final Point rectPoint1 = new Point();
	final Point rectPoint2 = new Point();
	final Scalar rectColor = new Scalar(0, 255, 0);
	private List<List<Rect>> rects = new ArrayList<>();

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
		while (this.video.read(frame)) {
			// faire appel à HOG
			System.out.println("Loading : " + (int) ((currentFrame / size) * 100) + "%");
			this.rects.add(this.hog.detect(frame));

			for (final Rect rect : this.rects.get(currentFrame)) {
				this.rectPoint1.x = rect.x;
				this.rectPoint1.y = rect.y;
				this.rectPoint2.x = rect.x + rect.width;
				this.rectPoint2.y = rect.y + rect.height;
				// Draw rectangle around fond object
				Imgproc.rectangle(frame, this.rectPoint1, this.rectPoint2, this.rectColor, 2);
			}

			final ImageIcon image = new ImageIcon(MatToBufferedImage(frame));
			this.frames.add(image);
			currentFrame += 1;
		}
		System.out.println("Loading completed !");
	}

	public int size() {
		return this.frames.size();
	}

	public ImageIcon getImage(int frame) {
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
