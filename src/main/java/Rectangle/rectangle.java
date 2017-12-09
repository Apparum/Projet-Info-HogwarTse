package Rectangle;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Image;
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
public class rectangle {

	private int label_;
	private Point coord_hg, coord_bd, center;
	// On définit une zone autour du centre
	private Zone zone;

	public rectangle(Point coordhg, Point coordbd, int label) {
		this.coord_hg = coordhg;
		this.coord_bd = coordbd;
		this.label_ = label;
		this.center = new Point(this.coord_hg.x - this.coord_bd.x, this.coord_bd.y - this.coord_hg.y);
		this.zone = new Zone(this.center);
	}

	/*
	 * Retourne l'aire de l'intersection de 2 zones si l'intersection existe et -1 si elle n'existe pas
	 */
	public double matchWithZone(rectangle rect) {
		double area = -1;
		if (this.isZoneIncludeIn(rect.getZone())) {
			area = this.getArea(rect.getZone());
		}

		return area;
	}

	/*
	 * Retourne l'aire de l'intersection de 2 zones
	 */
	private double getArea(Zone zone) {
		// Si c'est le point en haut à gauche qui est inclue dans la zone
		double aire = 0, X = 0, Y = 0;

		if (this.zone.getCoord_hg().x - zone.getCoord_hg().x >= 0) {
			X = this.zone.getCoord_hg().x;
			Y = this.zone.getCoord_hg().y;
			aire = (zone.getCoord_bd().x - X) * (zone.getCoord_bd().y - Y);
		}
		// Sinon c'est le point en bas à droite
		else {
			X = this.zone.getCoord_bd().x;
			Y = this.zone.getCoord_bd().y;
			aire = (X - zone.getCoord_hg().x) * (Y - zone.getCoord_hg().y);
		}
		return aire;
	}

	/*
	 * Retourne true si la zone de l'objet a une intersection non nulle avec la zone
	 * zone
	 */
	private boolean isZoneIncludeIn(Zone zone) {
		if (this.zone.isPointInZone(this.coord_bd) || this.zone.isPointInZone(this.coord_hg)) {
			return true;
		}

		return false;
	}

	public void setLabel(int label) {
		this.label_ = label;
	}

	public Point getCoord_hg() {
		return this.coord_hg;
	}

	public Point getCoord_bd() {
		return this.coord_bd;
	}

	public Point getCenter() {
		return this.center;
	}

	public int getLabel() {
		return this.label_;
	}

	public Zone getZone() {
		return this.zone;
	}
}