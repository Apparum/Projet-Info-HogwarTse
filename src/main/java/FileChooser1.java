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
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileSystemView;

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

public class FileChooser1 {
	public static void main(final String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		final Mat frame = new Mat();
		final JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		final int returnValue = jfc.showOpenDialog(null);
		VideoCapture camera = new VideoCapture();
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			final File selectedFile = jfc.getSelectedFile();
			camera = new VideoCapture(selectedFile.getAbsolutePath());
		}
		final JFrame jframe = new JFrame("Hogwar'TSE");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JLabel vidpanel = new JLabel();
		jframe.setSize(1400, 900);
		jframe.setContentPane(vidpanel);
		jframe.setVisible(true);
		jframe.setResizable(false);

		while (true) {
			if (camera.read(frame)) {
				final ImageIcon image = new ImageIcon(MatToBufferedImage(frame));
				final ImageIcon scaledImage = new ImageIcon(image.getImage().getScaledInstance(image.getIconWidth() * 3,
						image.getIconHeight() * 3, Image.SCALE_SMOOTH));
				vidpanel.setIcon(scaledImage);
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
