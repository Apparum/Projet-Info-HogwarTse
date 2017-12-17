package Graphic;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import java.awt.TextField;
import java.awt.event.TextListener;
import java.awt.event.TextEvent;

public class VideoScreen {

	private JFrame frame;
	private String path;
	private int currentFrame = 0;
	private VideoReader video;
	private JLabel contentLabel = new JLabel("");
	private boolean play = false;
	Rect zoneModif = new Rect();
	List<Rect> rects = new ArrayList<>();

	public List<Integer> getNbPerFrame() {
		return this.video.getNbPerFrame();
	}

	final Point rectPoint1 = new Point();
	final Point rectPoint2 = new Point();
	final Scalar rectColor = new Scalar(255, 0, 0);
	private int frameoff = 1;

	public boolean isPlay() {
		return this.play;
	}

	public void setPlay(boolean play) {
		this.play = play;
	}

	Label frameLabel = new Label("");
	private boolean goToStat;
	private boolean goToMenu;
	private boolean hogVisibility = true;

	public void setup() {
		this.video = new VideoReader(this.getPath());
		this.video.setHogVisible(this.isHogVisible());
		this.video.setFrameoff(this.getFrameoff());
		this.video.init();
		this.currentFrame = 0;
		this.refresh();
	}

	public boolean isGoToMenu() {
		return this.goToMenu;
	}

	public void setGoToMenu(boolean goToMenu) {
		this.goToMenu = goToMenu;
	}

	public boolean isGoToStat() {
		return this.goToStat;
	}

	public void setGoToStat(boolean goToStat) {
		this.goToStat = goToStat;
	}

	/**
	 * Launch the application.
	 */

	public void setVisible(boolean bool) {
		this.frame.setVisible(bool);
	}

	/**
	 * Create the application.
	 */
	public VideoScreen() {
		this.initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

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

		Label menuLabel = new Label("Menu");
		menuLabel.setForeground(new Color(255, 255, 255));
		menuLabel.setFont(new Font("Castellar", Font.PLAIN, 22));
		menuLabel.setBackground(new Color(89, 131, 146));
		menuLabel.setAlignment(Label.CENTER);
		menuLabel.setBounds(740, 522, 134, 40);
		this.frame.getContentPane().add(menuLabel);

		Label statLabel = new Label("Stats");
		statLabel.setForeground(new Color(255, 255, 255));
		statLabel.setFont(new Font("Castellar", Font.PLAIN, 22));
		statLabel.setBackground(new Color(89, 131, 146));
		statLabel.setAlignment(Label.CENTER);
		statLabel.setBounds(50, 522, 134, 40);
		this.frame.getContentPane().add(statLabel);

		Button nextButton = new Button("Next");
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if ((VideoScreen.this.currentFrame + 5) < VideoScreen.this.video.size()) {
					VideoScreen.this.moveFrame(5);
				}
			}
		});
		nextButton.setBounds(530, 522, 79, 24);
		this.frame.getContentPane().add(nextButton);

		Button previousButton = new Button("Previous");
		previousButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if ((VideoScreen.this.currentFrame - 5) > 0) {
					VideoScreen.this.moveFrame(-5);
				}
			}
		});
		previousButton.setBounds(279, 522, 79, 24);
		this.frame.getContentPane().add(previousButton);

		Button playButton = new Button("Play/Pause");
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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

		java.awt.List rectList = new java.awt.List();
		rectList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int selectedItem = rectList.getSelectedIndex();
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

		Label gotoLabel = new Label("Go To Frame : ");
		gotoLabel.setForeground(Color.WHITE);
		gotoLabel.setFont(null);
		gotoLabel.setAlignment(Label.RIGHT);
		gotoLabel.setBounds(540, 555, 102, 24);
		this.frame.getContentPane().add(gotoLabel);

		TextField gotoTextField = new TextField();
		gotoTextField.addTextListener(new TextListener() {
			public void textValueChanged(TextEvent arg0) {
				VideoScreen.this.setPlay(false);
				VideoScreen.this.setCurrentFrame(Integer.parseInt(gotoTextField.getText()));
				VideoScreen.this.refresh();
			}
		});
		gotoTextField.setBounds(641, 555, 24, 24);
		this.frame.getContentPane().add(gotoTextField);

		menuLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				VideoScreen.this.setGoToMenu(true);
			}
		});

		statLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				VideoScreen.this.setGoToStat(true);
			}
		});

		this.contentLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				double[] pt = new double[2];
				pt[0] = e.getX();
				pt[1] = e.getY();
				VideoScreen.this.zoneModif.set(pt);
			}

			public void mouseReleased(MouseEvent e) {
				double[] pt = new double[2];
				pt[0] = e.getX();
				pt[1] = e.getY();
				VideoScreen.this.zoneModif.width = e.getX() - VideoScreen.this.zoneModif.x;
				VideoScreen.this.zoneModif.height = e.getY() - VideoScreen.this.zoneModif.y;
				VideoScreen.this.rects.add(VideoScreen.this.zoneModif);
				rectList.add(VideoScreen.this.zoneModif.toString());
				VideoScreen.this.zoneModif = new Rect();
				VideoScreen.this.refresh();
			}
		});

		this.frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				float xlen = (float) VideoScreen.this.frame.getWidth() / 930;
				float ylen = (float) VideoScreen.this.frame.getHeight() / 624;
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

	public void refresh() {
		Mat tmp = this.video.getMat(this.currentFrame).clone();
		for (int i = 0; i < this.rects.size(); i++) {
			Rect rect = this.rects.get(i);
			float yRatio = (float) this.contentLabel.getHeight()
					/ (float) this.video.getMat(this.currentFrame).height();
			float xRatio = (float) this.contentLabel.getWidth() / (float) this.video.getMat(this.currentFrame).width();
			this.rectPoint1.x = rect.x / xRatio;
			this.rectPoint1.y = rect.y / yRatio;
			this.rectPoint2.x = (rect.x / xRatio) + (rect.width / xRatio);
			this.rectPoint2.y = (rect.y / yRatio) + (rect.height / yRatio);
			Imgproc.rectangle(tmp, this.rectPoint1, this.rectPoint2, this.rectColor, 1);
		}
		final ImageIcon image = new ImageIcon(new ImageIcon(MatToBufferedImage(tmp)).getImage()
				.getScaledInstance(this.contentLabel.getWidth(), this.contentLabel.getHeight(), Image.SCALE_SMOOTH));
		this.contentLabel.setIcon(image);
		this.frameLabel.setText("Frame : " + this.currentFrame + "/" + (this.video.size() - 1));
		this.contentLabel.repaint();
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int size() {
		return this.video.size();
	}

	public VideoReader getVideo() {
		return this.video;
	}

	public void setVideo(VideoReader video) {
		this.video = video;
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

	public static Mat bufferedImageToMat(BufferedImage bi) {
		Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
		byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
		mat.put(0, 0, data);
		return mat;
	}

	public boolean moveFrame(int move) {
		if ((this.currentFrame + move) < this.video.size()) {
			VideoScreen.this.currentFrame += move;
			VideoScreen.this.refresh();
			return true;
		}
		return false;
	}

	public int getCurrentFrame() {
		return this.currentFrame;
	}

	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}

	public int getFrameoff() {
		return this.frameoff;
	}

	public void setFrameoff(int frameoff) {
		this.frameoff = frameoff;
	}

	public void setHogVisible(boolean hogVisible) {
		this.hogVisibility = hogVisible;
	}

	public boolean isHogVisible() {
		return this.hogVisibility;
	}
}
