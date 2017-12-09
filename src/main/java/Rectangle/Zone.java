package Rectangle;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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

@SuppressWarnings("unused")
public class Zone {
	private Point coord_hg, coord_bd;
	private int precision = 10;

	Zone(Point center) {
		this.coord_hg.x = center.x - precision;
		this.coord_hg.y = center.y - precision;
		this.coord_bd.x = center.x + precision;
		this.coord_bd.y = center.y + precision;
	}

	public Point getCoord_hg() {
		return this.coord_hg;
	}

	public Point getCoord_bd() {
		return this.coord_bd;
	}

	public boolean isPointInZone(Point p) {
		if (p.x <= this.coord_bd.x && p.x >= this.getCoord_hg().x) {
			if (p.y <= this.getCoord_bd().y && p.y >= this.getCoord_hg().y) {
				return true;
			}
		}

		return false;
	}
}
