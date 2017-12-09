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
public class Image_ {
	private List<rectangle> listRectangle = new ArrayList<>();
	
	public Image_(){
		
	}
	
	public void addrectangle(rectangle rect) {
		listRectangle.add(rect);
	}
	
	public rectangle getRectangle(int indice) {
		return listRectangle.get(indice);
	}
	
	public List<rectangle> getRectangles(){
		return listRectangle;
	}
	
	/*
	 * Ajoute un rectangle par interpolation linéaire entre 2 rectangles
	 */
	public void addMid(rectangle rect1, rectangle rect2) {
		Point newPoint_hg = this.avg(rect1.getCoord_hg(), rect2.getCoord_hg());
		Point newPoint_bd = this.avg(rect1.getCoord_bd(), rect2.getCoord_bd());
		rectangle newRect = new rectangle(newPoint_hg, newPoint_bd, 1);
		this.addrectangle(newRect);
	}
	
	/*
	 * Retourne un point obtenu par interpolation niéaire entre les points p1 et p2
	 */
	private Point avg(Point p1, Point p2) {
		Point newPoint = new Point();
		newPoint.x = (p1.x+p2.x)/2;
		newPoint.y = (p1.y+p2.y)/2;
		return newPoint;
	}
}
