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
public class Video {
	private List<Image_> listImage = new ArrayList<>();

	public void addImage(Image_ img) {
		boolean firstImg = false;
		if (listImage.size() == 0)
			firstImg = true;
		listImage.add(img);
		this.labellisation(firstImg);
	}

	public Image_ getImage(int indice) {
		return listImage.get(indice);
	}
	
	public List<Image_> getImages(){
		return listImage;
	}

	public void labellisation(boolean firstImg) {
		// Pour la premiere image on les numerotes dans l'ordre d'apparition
		if(firstImg) {
			int compteur = 1;
			for(rectangle rect : this.listImage.get(0).getRectangles())
			{
				rect.setLabel(compteur);
				compteur +=1;
			}
		}
		else {
			for(rectangle rectActuels : this.listImage.get(listImage.size()-1).getRectangles()) {
				Point centerActuel = rectActuels.getCenter();
				for(rectangle rectPast : this.listImage.get(listImage.size()-2).getRectangles()) {
					if(this.isCloseTo(centerActuel, rectPast.getCenter())) {
						rectActuels.setLabel(rectPast.getLabel());
						//break;
					}
				}
			}
		}
	}
	
	/*
	 * Retourne true si le point p1 est proche du point p2
	 */
	public boolean isCloseTo(Point p1, Point p2) {
		if(p1.x<p2.x + 10 && p1.x>p2.x -10) {
			if(p1.y<p2.y + 10 && p1.y>p2.y -10) {
				return true;
			}
		}
		return false;
	}

	public void interpolation() {
		// On parcourt les images de la deuxième à l'avant-dernière et on fait une interpolation de chaque image avec sa précédente et sa suivante
		int compteur = 0;
		
		for(Image_ img : this.listImage) {
			if(compteur >0 && compteur <this.listImage.size()-2) {
				for(rectangle rectActuel : img.getRectangles()) {
					// On parcourt tous les rectangle de la frame precedente
					double max=0;
					rectangle rectInterpPrevious=null;
					for(rectangle rectPrevious : this.listImage.get(compteur-1).getRectangles()) {
						if(rectActuel.matchWithZone(rectPrevious)>=max) {
							max = rectActuel.matchWithZone(rectPrevious);
							rectInterpPrevious = rectPrevious;
						}
					}
					max=0;
					rectangle rectInterpNext=null;
					for(rectangle rectNext : this.listImage.get(compteur+1).getRectangles()) {
						if(rectActuel.matchWithZone(rectNext)>=max) {
							max = rectActuel.matchWithZone(rectNext);
							rectInterpNext = rectNext;
						}
					}
					if(rectInterpNext != null && rectInterpPrevious != null) {
						img.addMid(rectInterpPrevious, rectInterpNext);
					}
				}
			}
		}
	}
	
	
}