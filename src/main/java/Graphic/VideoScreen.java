package Graphic;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import Video.VideoReader;

/**
 *
 * Ecran de vidéo.
 *
 */
public class VideoScreen {

	private JFrame frame;
	private Label frameLabel = new Label("");
	private final JLabel contentLabel = new JLabel("");
	private String path;
	private VideoReader video;
	private boolean goToStat;
	private boolean goToMenu;
	private boolean hogVisibility = true;
	private boolean play = false;
	private boolean hogOrKalman = true;
	private int currentFrame = 0;
	private int frameoff = 1;
	private Rect zoneModif = new Rect();
	private List<Rect> rects = new ArrayList<>();
	private final Point rectPoint1 = new Point();
	private final Point rectPoint2 = new Point();
	private final Scalar rectColor = new Scalar(255, 0, 0);

	/**
	 * Constructeur de l'écran vidéo.
	 */
	public VideoScreen() {
		this.initialize();
	}

	/**
	 * Initialise la fenêtre.
	 */
	private void initialize() {
		final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		this.frame = new JFrame();
		this.frame.setResizable(true);
		this.frame.getContentPane().setBackground(new Color(13, 31, 45));
		this.frame.setBounds(0, 0, 930, 624);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.getContentPane().setLayout(null);
		this.frame.setLocation((dim.width / 2) - (this.frame.getSize().width / 2),
				(dim.height / 2) - (this.frame.getSize().height / 2));

		this.contentLabel.setBackground(new Color(98, 104, 104));
		this.contentLabel.setBounds(50, 31, 750, 465);
		this.frame.getContentPane().add(this.contentLabel);

		final Label menuLabel = new Label("Menu");
		menuLabel.setForeground(new Color(255, 255, 255));
		menuLabel.setFont(new Font("Castellar", Font.PLAIN, 22));
		menuLabel.setBackground(new Color(89, 131, 146));
		menuLabel.setAlignment(Label.CENTER);
		menuLabel.setBounds(740, 522, 134, 40);
		this.frame.getContentPane().add(menuLabel);

		final Label statLabel = new Label("Stats");
		statLabel.setForeground(new Color(255, 255, 255));
		statLabel.setFont(new Font("Castellar", Font.PLAIN, 22));
		statLabel.setBackground(new Color(89, 131, 146));
		statLabel.setAlignment(Label.CENTER);
		statLabel.setBounds(50, 522, 134, 40);
		this.frame.getContentPane().add(statLabel);

		final Button nextButton = new Button("Next");
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				if ((VideoScreen.this.currentFrame + 1) < VideoScreen.this.video.size()) {
					VideoScreen.this.moveFrame(1);
				}
			}
		});
		nextButton.setBounds(530, 522, 79, 24);
		this.frame.getContentPane().add(nextButton);

		final Button previousButton = new Button("Previous");
		previousButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				if ((VideoScreen.this.currentFrame - 1) > 0) {
					VideoScreen.this.moveFrame(-1);
				}
			}
		});
		previousButton.setBounds(279, 522, 79, 24);
		this.frame.getContentPane().add(previousButton);

		final Button playButton = new Button("Play/Pause");
		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				if (VideoScreen.this.isPlay()) {
					VideoScreen.this.setPlay(false);
				} else {
					VideoScreen.this.setPlay(true);
				}
			}
		});
		playButton.setBounds(404, 522, 79, 24);
		this.frame.getContentPane().add(playButton);

		this.frameLabel.setForeground(Color.WHITE);
		this.frameLabel.setFont(null);
		this.frameLabel.setAlignment(Label.CENTER);
		this.frameLabel.setBounds(358, 555, 176, 24);
		this.frame.getContentPane().add(this.frameLabel);

		final java.awt.List rectList = new java.awt.List();
		rectList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				if (e.getClickCount() == 2) {
					final int selectedItem = rectList.getSelectedIndex();
					rectList.remove(selectedItem);
					VideoScreen.this.rects.remove(selectedItem);
					VideoScreen.this.refresh();
				}
			}
		});
		rectList.setForeground(Color.WHITE);
		rectList.setFont(new Font("Arial", Font.PLAIN, 12));
		rectList.setBackground(Color.DARK_GRAY);
		rectList.setMultipleMode(false);
		rectList.setBounds(806, 31, 108, 465);
		this.frame.getContentPane().add(rectList);

		final Label gotoLabel = new Label("Go To Frame : ");
		gotoLabel.setForeground(Color.WHITE);
		gotoLabel.setFont(null);
		gotoLabel.setAlignment(Label.RIGHT);
		gotoLabel.setBounds(540, 555, 102, 24);
		this.frame.getContentPane().add(gotoLabel);

		final TextField gotoTextField = new TextField();
		gotoTextField.addTextListener(new TextListener() {
			@Override
			public void textValueChanged(final TextEvent arg0) {
				if (gotoTextField.getText().matches("^[0-9]+$")
						&& (Integer.parseInt(gotoTextField.getText()) < VideoScreen.this.video.size())) {
					VideoScreen.this.setPlay(false);
					VideoScreen.this.setCurrentFrame(Integer.parseInt(gotoTextField.getText()));
					VideoScreen.this.refresh();
				} else {
					gotoTextField.setText("");
				}
			}
		});
		gotoTextField.setBounds(641, 555, 24, 24);
		this.frame.getContentPane().add(gotoTextField);

		menuLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent arg0) {
				VideoScreen.this.setGoToMenu(true);
			}
		});

		statLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent arg0) {
				VideoScreen.this.setGoToStat(true);
			}
		});

		this.contentLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(final MouseEvent e) {
				final float xlen = (float) VideoScreen.this.frame.getWidth() / 930;
				final float ylen = (float) VideoScreen.this.frame.getHeight() / 624;
				VideoScreen.this.zoneModif.x = (int) (e.getX() / xlen);
				VideoScreen.this.zoneModif.y = (int) (e.getY() / ylen);
			}

			@Override
			public void mouseReleased(final MouseEvent e) {
				final float xlen = (float) VideoScreen.this.frame.getWidth() / 930;
				final float ylen = (float) VideoScreen.this.frame.getHeight() / 624;
				final double[] pt = new double[2];
				pt[0] = e.getX() / xlen;
				pt[1] = e.getY() / ylen;
				if (pt[0] < VideoScreen.this.zoneModif.x) {
					final double tmp = VideoScreen.this.zoneModif.x;
					VideoScreen.this.zoneModif.x = (int) pt[0];
					pt[0] = tmp;
				}
				if (pt[1] < VideoScreen.this.zoneModif.y) {
					final double tmp = VideoScreen.this.zoneModif.y;
					VideoScreen.this.zoneModif.y = (int) pt[1];
					pt[1] = tmp;
				}
				VideoScreen.this.zoneModif.width = (int) ((pt[0]) - VideoScreen.this.zoneModif.x);
				VideoScreen.this.zoneModif.height = (int) ((pt[1]) - VideoScreen.this.zoneModif.y);
				VideoScreen.this.rects.add(VideoScreen.this.zoneModif);
				rectList.add(VideoScreen.this.zoneModif.toString());
				VideoScreen.this.zoneModif = new Rect();
				VideoScreen.this.refresh();
			}
		});

		this.frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent arg0) {
				final float xlen = (float) VideoScreen.this.frame.getWidth() / 930;
				final float ylen = (float) VideoScreen.this.frame.getHeight() / 624;
				VideoScreen.this.contentLabel.setBounds((int) (50 * xlen), (int) (31 * ylen), (int) (750 * xlen),
						(int) (465 * ylen));
				menuLabel.setBounds((int) (740 * xlen), (int) (522 * ylen), (int) (134 * xlen), (int) (40 * ylen));
				statLabel.setBounds((int) (50 * xlen), (int) (522 * ylen), (int) (134 * xlen), (int) (40 * ylen));
				nextButton.setBounds((int) (530 * xlen), (int) (522 * ylen), (int) (79 * xlen), (int) (24 * ylen));
				previousButton.setBounds((int) (279 * xlen), (int) (522 * ylen), (int) (79 * xlen), (int) (24 * ylen));
				playButton.setBounds((int) (404 * xlen), (int) (522 * ylen), (int) (79 * xlen), (int) (24 * ylen));
				VideoScreen.this.frameLabel.setBounds((int) (358 * xlen), (int) (555 * ylen), (int) (176 * xlen),
						(int) (24 * ylen));
				rectList.setBounds((int) (806 * xlen), (int) (31 * ylen), (int) (108 * xlen), (int) (465 * ylen));
				gotoLabel.setBounds((int) (540 * xlen), (int) (555 * ylen), (int) (102 * xlen), (int) (24 * ylen));
				gotoTextField.setBounds((int) (641 * xlen), (int) (555 * ylen), (int) (24 * xlen), (int) (24 * ylen));
				VideoScreen.this.refresh();
			}
		});
	}

	// Getters

	public int getCurrentFrame() {
		return this.currentFrame;
	}

	public Dimension getFrameSize() {
		return this.frame.getSize();
	}

	public List<Integer> getNbPerFrame() {
		return this.video.getNbPerFrame();
	}

	public List<List<Rectangle>> getRectPeoplePerFrame() {
		return this.video.getRectPeoplePerFrame();
	}

	public List<Rect> getRectPerFrame() {
		return this.rects;
	}

	public boolean isGoToMenu() {
		return this.goToMenu;
	}

	public boolean isGoToStat() {
		return this.goToStat;
	}

	public boolean isPlay() {
		return this.play;
	}

	public int size() {
		return this.video.size();
	}

	// Setters

	public void setCurrentFrame(final int currentFrame) {
		this.currentFrame = currentFrame;
	}

	public void setFrameoff(final int frameoff) {
		this.frameoff = frameoff;
	}

	public void setFrameSize(final Dimension dim) {
		this.frame.setSize(dim.width, dim.height);
	}

	public void setGoToMenu(final boolean goToMenu) {
		this.goToMenu = goToMenu;
	}

	public void setGoToStat(final boolean goToStat) {
		this.goToStat = goToStat;
	}

	public void setHogVisible(final boolean hogVisible) {
		this.hogVisibility = hogVisible;
	}

	public void setHogOrKalman(boolean hogOrKalman) {
		this.hogOrKalman = hogOrKalman;
	}

	public void setLocation(final int x, final int y) {
		this.frame.setLocation(x, y);
	}

	public void setPath(final String path) {
		this.path = path;
	}

	public void setPlay(final boolean play) {
		this.play = play;
	}

	public void setVisible(final boolean bool) {
		this.frame.setVisible(bool);
	}

	// Methodes

	/**
	 * Prépare la fenêtre video avant son affichage afin qu'elle puisse traiter la
	 * vidéo en amont.
	 */
	public void setup() {
		this.video = new VideoReader(this.path);
		this.video.setHogVisible(this.hogVisibility);
		this.video.setFrameoff(this.frameoff);
		this.video.setHogOrKalman(this.hogOrKalman);
		this.video.init();
		this.currentFrame = 0;
		this.refresh();
	}

	/**
	 * Déplace le frame active d'une certaine quantité.
	 *
	 * @param move
	 *            : La quantité de frame à décaler (entier relatif).
	 * @return true si le décalage est possible (et réalisé), false sinon.
	 */
	public boolean moveFrame(final int move) {
		if ((this.currentFrame + move) < this.video.size()) {
			VideoScreen.this.currentFrame += move;
			VideoScreen.this.refresh();
			return true;
		}
		return false;
	}

	/**
	 * Mise à jour graphique de la fenêtre
	 */
	public void refresh() {
		final Mat tmp = this.video.getMat(this.currentFrame).clone();
		for (int i = 0; i < this.rects.size(); i++) {
			final Rect rect = this.rects.get(i);
			final float xlen = (float) VideoScreen.this.frame.getWidth() / 930;
			final float ylen = (float) VideoScreen.this.frame.getHeight() / 624;
			final float yRatio = (float) this.contentLabel.getHeight()
					/ (float) this.video.getMat(this.currentFrame).height();
			final float xRatio = (float) this.contentLabel.getWidth()
					/ (float) this.video.getMat(this.currentFrame).width();
			this.rectPoint1.x = (rect.x / xRatio) * xlen;
			this.rectPoint1.y = (rect.y / yRatio) * ylen;
			this.rectPoint2.x = ((rect.x / xRatio) * xlen) + ((rect.width / xRatio) * xlen);
			this.rectPoint2.y = ((rect.y / yRatio) * ylen) + ((rect.height / yRatio) * ylen);
			Imgproc.rectangle(tmp, this.rectPoint1, this.rectPoint2, this.rectColor, 1);
			Imgproc.putText(tmp, "Rectangle " + (i + 1), this.rectPoint1, 0, 0.3, new Scalar(0, 0, 0), 1);
		}
		final ImageIcon image = new ImageIcon(new ImageIcon(MatToBufferedImage(tmp)).getImage()
				.getScaledInstance(this.contentLabel.getWidth(), this.contentLabel.getHeight(), Image.SCALE_SMOOTH));
		this.contentLabel.setIcon(image);
		this.frameLabel.setText("Frame : " + this.currentFrame + "/" + (this.video.size() - 1));
		this.contentLabel.repaint();
	}

	/**
	 * Convertit une BufferedImage en Mat(rice).
	 *
	 * @param bi
	 *            : La BufferedImage à convertir.
	 * @return mat : La matrice associée.
	 */
	public static Mat bufferedImageToMat(final BufferedImage bi) {
		final Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
		final byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
		mat.put(0, 0, data);
		return mat;
	}

	/**
	 * Convertit une Mat(rice) en BufferedImage.
	 *
	 * @param frame
	 *            : La matrice à convertir.
	 * @return image : La BufferedImage associée.
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
