package Video;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import Detection.HOGDetection;
import Detection.MainKalman;
import Save.Loading;
import Save.Saving;

/**
 *
 * 
 * 
 * Lecteur de vidéo.
 *
 * 
 * 
 */

public class VideoReader {
	private VideoCapture video;
	private String nomVideo;
	private final String path;
	private final HOGDetection hog = new HOGDetection();
	
	private final Scalar rectColor = new Scalar(0, 255, 0);
	private int frameoff = 1;
	private boolean hogVisibility = true;
	private boolean hogOrKalman = true; // 1 = hog, 0 = kalman
	private List<Mat> frames = new ArrayList<>();
	private final List<Mat> framesClone = new ArrayList<>();
	private List<List<Rectangle>> rects = new ArrayList<>();
	private List<Integer> nbPerFrame = new ArrayList<>();
	private final List<List<Integer>> listLabel = new ArrayList<>();

	/**
	 * 
	 * Constructeur du lecture de la vidéo.
	 *
	 * 
	 * 
	 * @param path
	 * 
	 *            : Chemin menant à la vidéo considérée.
	 * 
	 */
	public VideoReader(final String path) {
		this.path = path;
		final String[] pathSplit = path.split("\\\\");
		this.nomVideo = pathSplit[pathSplit.length - 1];
		this.nomVideo = this.nomVideo.substring(0, this.nomVideo.lastIndexOf("."));
	}

	public void init() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		this.video = new VideoCapture(this.path);
		final double size = this.video.get(Videoio.CAP_PROP_FRAME_COUNT);
		System.out.println("There are " + size + " frames");

		// Jusqu'à maintenant vous avez pas vu Kalman, donc false pour ce qui était avant 
		// et true pour le nouveau truc
		boolean vraiKalman = true;
		///////////// HOG ////////////////
		if (this.hogOrKalman) {
			this.initHOG();
			vraiKalman = false;
		}

		////////// KALMAN /////////////
		else {
			if(vraiKalman) {
				this.initKalman2();
			}
			else {
				this.initKalman();
			}
		}
		
		if(!vraiKalman) {
			this.labellisation();
			this.affichageMat();
		}

		System.out.println("Loading completed !");
	}
	
	
	private void initKalman2() {
		MainKalman kalman = new MainKalman();
		try {
			kalman.process2(this.video);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.frames = kalman.getListMat();
		this.nbPerFrame = kalman.getListNbPeople();
	}
	

	/*
	 * Fonction qui contient le code de traitement de la vidéo en utilisant la
	 * méthode HOG
	 */
	private void initHOG() {
		final Mat frame = new Mat();
		final double size = this.video.get(Videoio.CAP_PROP_FRAME_COUNT);
		int currentFrame = 0;
		Path fichier = Paths.get(this.nomVideo + ".txt");
		boolean dejaVu = Files.exists(fichier);

		while (this.video.read(frame)) {
			System.out.println("Loading : " + (int) ((currentFrame / size) * 100) + "%");
			// On ne fait la détection que si on n'a pas le fichier correspondant.
			if (!dejaVu) {
				if ((currentFrame % this.frameoff) == 0) {
					final List<Rectangle> detected = this.hog.detect(frame);
					this.rects.add(detected);
					this.nbPerFrame.add(detected.size());
				} else {
					this.rects.add(new ArrayList<Rectangle>());
				}
			}
			this.framesClone.add(frame.clone());
			currentFrame += 1;
		}
		// On charge les rectangles si le fichier existe(déjà interpollé).
		if (dejaVu) {
			System.out.println("The video was already in memory");
			this.rects = Loading.charger(this.nomVideo);
			for (final List<Rectangle> image : this.rects) {
				this.nbPerFrame.add(image.size());
			}
		}
		// Sinon on interpole ceux calculés dans la boucle while.
		else {
			this.rects = this.interpolation(this.rects);
			Saving.sauvegarder(this.nomVideo, this.rects);
		}
	}

	/*
	 * Fonction qui contient le code de traitement de la vidéo avec la méthode de
	 * supression du fond et avec la méthode d'interpolation du filtre de Kalman
	 */
	private void initKalman() {
		final Mat frame = new Mat();
		
		try {
			this.rects = MainKalman.process(this.video);
			System.out.println("Fin du chargement de Kalman !");
		} catch (final InterruptedException e) {
			e.printStackTrace();
			System.out.println("Erreur dans Kalman");
		}
		this.video = new VideoCapture(this.path);
		while (this.video.read(frame)) {
			this.framesClone.add(frame.clone());
		}
		for (int k = 0; k < this.rects.size(); k++) {
			this.nbPerFrame.add(this.rects.get(k).size());
		}
	}

	private void affichageMat() {
		int nbFrame = 0, compteurRect = 0;
		for (final Mat matFrame : this.framesClone) {
			if (this.hogVisibility) {
				for (final Rectangle rectangle : this.rects.get(nbFrame)) {
					Imgproc.rectangle(matFrame, new Point(rectangle.x, rectangle.y),
							new Point(rectangle.x + rectangle.width, rectangle.y + rectangle.height), this.rectColor,
							1);
					Imgproc.putText(matFrame, "" + this.listLabel.get(nbFrame).get(compteurRect),
							new Point(rectangle.x + rectangle.width, rectangle.y + rectangle.height),
							Core.FONT_HERSHEY_PLAIN, 1, new Scalar(255, 255, 255), 1);
					compteurRect++;
				}
				compteurRect = 0;
			}
			this.frames.add(matFrame.clone());
			nbFrame += 1;
		}
	}

	// Getters
	private double getArea(final Rectangle rect) {
		return rect.getWidth() * rect.getHeight();
	}

	public Mat getMat(final int frame) {
		return this.frames.get(frame);
	}

	public List<Integer> getNbPerFrame() {
		return this.nbPerFrame;
	}

	public List<List<Rectangle>> getRectPeoplePerFrame() {
		return this.rects;
	}

	public VideoCapture getVideo() {
		return this.video;
	}

	public int size() {
		return this.frames.size();
	}

	// Setters
	public void setFrameoff(final int frameoff) {
		this.frameoff = frameoff;
	}

	public void setHogVisible(final boolean hogVisible) {
		this.hogVisibility = hogVisible;
	}

	public void setHogOrKalman(boolean hogOrKalman) {
		this.hogOrKalman = hogOrKalman;
	}

	public void setVideo(final VideoCapture video) {
		this.video = video;
	}

	// Méthodes

	/**
	 * 
	 * Calcule une moyenne entre deux nombres entiers.
	 *
	 * 
	 * 
	 * @param a
	 * 
	 *            : 1er nombre.
	 * 
	 * @param b
	 * 
	 *            : 2eme nombre.
	 * 
	 * @return La moyenne.
	 * 
	 */

	private int avg(final int a, final int b) {
		return (a + b) / 2;
	}

	/**
	 * 
	 * Calcule les rectangles interpolés entre des images successives.
	 *
	 * 
	 * 
	 * @param listImg
	 * 
	 *            : La liste des rectangles de chaque frame.
	 * 
	 * @return La liste des rectangles de chaque frame après interpolation.
	 * 
	 */

	private List<List<Rectangle>> interpolation(final List<List<Rectangle>> listImg) {
		int compteur = 0;
		int maxArea;
		boolean condition;

		for (int k = 0; k < listImg.size(); k++) {
			if ((compteur > 0) && (compteur < (listImg.size() - 1))) {
				for (final Rectangle rectPrevious : listImg.get(compteur - 1)) {
					maxArea = 0;
					Rectangle rectNextIntersect = new Rectangle();
					for (final Rectangle rectNext : listImg.get(compteur + 1)) {
						condition = true;
						for (final Rectangle actualRect : listImg.get(compteur)) {
							if (actualRect.intersects(rectNext.intersection(rectPrevious))) {
								condition = false;
							}
						}
						if ((this.intersectionArea(rectPrevious, rectNext) > maxArea) && condition) {
							maxArea = this.intersectionArea(rectPrevious, rectNext);
							rectNextIntersect = rectNext;
						}
					}
					if ((rectNextIntersect != null) && !rectNextIntersect.isEmpty()) {
						listImg.get(compteur).add(this.rectangleAvg(rectPrevious, rectNextIntersect));
					}
				}
			}
			compteur += 1;
		}
		return listImg;
	}

	/**
	 * 
	 * Détermine la surface d'intersection de deux rectangles.
	 *
	 * 
	 * 
	 * @param rect1
	 * 
	 *            : 1er rectangle.
	 * 
	 * @param rect2
	 * 
	 *            : 2eme rectangle.
	 * 
	 * @return L'aire de l'intersection.
	 * 
	 */

	private int intersectionArea(final Rectangle rect1, final Rectangle rect2) {
		final Rectangle rect3 = rect1.intersection(rect2);
		if (rect3.isEmpty()) {
			return -1;
		} else {
			return rect3.width * rect3.height;
		}
	}

	/**
	 * 
	 * Inscrit dans listLabel les labels des personnes présentes dans l'image.
	 * 
	 */

	private void labellisation() {
		int maxLabel = 0, indiceMax = 0, compteur = 0;
		double intersectArea;
		double maxArea = 0;
		List<Integer> listInter = new ArrayList<>();

		for (int j = 0; j < this.rects.get(0).size(); j++) {
			listInter.add(maxLabel);
			maxLabel++;
		}

		Rectangle previous = new Rectangle();
		this.listLabel.add(listInter);

		for (int k = 1; k < this.rects.size(); k++) {
			List<Integer> listInter1 = new ArrayList<>();
			for (Rectangle actualRect : this.rects.get(k)) {
				for (Rectangle previousRect : this.rects.get(k - 1)) {
					if (actualRect.intersects(previousRect)) {
						intersectArea = this.getArea(actualRect.intersection(previousRect));
					} else {
						intersectArea = 0;
					}
					// System.out.println(actualRect.intersects(previousRect));
					if (intersectArea > maxArea) {
						maxArea = intersectArea;
						indiceMax = compteur;
						previous = previousRect;
					}
					compteur = compteur + 1;
				}

				if (maxArea > this.getArea(previous) / 3 || maxArea > this.getArea(actualRect) / 3) {
					listInter1.add(this.listLabel.get(k - 1).get(indiceMax));
				} else {
					listInter1.add(maxLabel);
					maxLabel++;
				}
				compteur = 0;
				maxArea = 0;
				indiceMax = 0;
			}
			this.listLabel.add(listInter1);
		}
	}

	/**
	 * 
	 * Crée un rectangle résultant de la moyenne des coordonnées de deux rectangles.
	 *
	 * 
	 * 
	 * @param rect1
	 * 
	 *            : 1er rectangle.
	 * 
	 * @param rect2
	 * 
	 *            : 2eme rectangle.
	 * 
	 * @return Un rectangle moyen.
	 * 
	 */

	private Rectangle rectangleAvg(final Rectangle rect1, final Rectangle rect2) {
		return new Rectangle(this.avg(rect1.x, rect2.x), this.avg(rect1.y, rect2.y), this.avg(rect1.width, rect2.width),
				this.avg(rect1.height, rect2.height));
	}

	/**
	 * 
	 * Convertit une Mat(rice) en BufferedImage.
	 *
	 * 
	 * 
	 * @param frame
	 * 
	 *            : La matrice à convertir.
	 * 
	 * @return image : La BufferedImage associée.
	 * 
	 */

	public static BufferedImage MatToBufferedImage(final Mat frame) {

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