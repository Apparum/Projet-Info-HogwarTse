package Graphic;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.opencv.core.Point;
import org.opencv.core.Rect;

import Stats.Histogramme;

/**
 *
 * Ecran de statistique.
 *
 */
public class StatScreen {

	private JFrame frame;
	private Label frameLabel = new Label("");
	private Panel panel = new Panel();
	private boolean goToMenu;
	private boolean goToVideo;
	private boolean global = true;
	private boolean local = false;
	private int currentFrame;
	private int size;
	private int selectedRect = 1;
	private int frameLow = 0;
	private int frameUp = Integer.MAX_VALUE;
	private List<Integer> NbPerFrame = new ArrayList<>();
	private List<Rect> rects = new ArrayList<>();
	private List<List<Rectangle>> rectPeople = new ArrayList<>();
	private double[][] data = new double[1][this.NbPerFrame.size()];

	/**
	 * Constructeur de l'écran de statistique.
	 */
	public StatScreen() {
		this.initialize();
	}

	/**
	 * Initialise la fenêtre
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

		this.panel.setBounds(50, 37, 824, 465);
		this.panel.setBackground(new Color(13, 31, 45));
		this.frame.getContentPane().add(this.panel);
		this.panel.setLayout(null);

		this.frameLabel.setForeground(Color.WHITE);
		this.frameLabel.setFont(null);
		this.frameLabel.setAlignment(Label.CENTER);
		this.frameLabel.setBounds(358, 555, 176, 24);
		this.frame.getContentPane().add(this.frameLabel);

		final Label menuLabel = new Label("Menu");
		menuLabel.setForeground(new Color(255, 255, 255));
		menuLabel.setFont(new Font("Castellar", Font.PLAIN, 22));
		menuLabel.setBackground(new Color(89, 131, 146));
		menuLabel.setAlignment(Label.CENTER);
		menuLabel.setBounds(740, 522, 134, 40);
		this.frame.getContentPane().add(menuLabel);

		final Label videoLabel = new Label("Video");
		videoLabel.setForeground(new Color(255, 255, 255));
		videoLabel.setFont(new Font("Castellar", Font.PLAIN, 22));
		videoLabel.setBackground(new Color(89, 131, 146));
		videoLabel.setAlignment(Label.CENTER);
		videoLabel.setBounds(50, 522, 134, 40);
		this.frame.getContentPane().add(videoLabel);

		final Button nextButton = new Button("Local");
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				if (StatScreen.this.rects.size() > 0) {
					StatScreen.this.local = true;
					StatScreen.this.global = false;
					StatScreen.this.refresh();
				}
			}
		});
		nextButton.setBounds(530, 522, 79, 24);
		this.frame.getContentPane().add(nextButton);

		final Button previousButton = new Button("Global");
		previousButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				StatScreen.this.global = true;
				StatScreen.this.local = false;
				StatScreen.this.refresh();
			}
		});
		previousButton.setBounds(279, 522, 79, 24);
		this.frame.getContentPane().add(previousButton);

		final TextField rectTextField = new TextField();
		rectTextField.addTextListener(new TextListener() {
			@Override
			public void textValueChanged(final TextEvent arg0) {
				if (rectTextField.getText().matches("^[0-9]+$") && (Integer.parseInt(rectTextField.getText()) > 0)
						&& (Integer.parseInt(rectTextField.getText()) < (StatScreen.this.rects.size() + 1))) {
					StatScreen.this.selectedRect = Integer.parseInt(rectTextField.getText());
					StatScreen.this.refresh();
				}
			}
		});

		final Label rectLabel = new Label("Rectangle n° : ");
		rectLabel.setForeground(Color.WHITE);
		rectLabel.setFont(null);
		rectLabel.setAlignment(Label.RIGHT);
		rectLabel.setBounds(540, 555, 102, 24);
		this.frame.getContentPane().add(rectLabel);
		rectTextField.setBounds(641, 555, 24, 24);
		this.frame.getContentPane().add(rectTextField);

		final Label frameLowLabel = new Label("From frame n°");
		frameLowLabel.setBounds(190, 555, 102, 24);
		this.frame.getContentPane().add(frameLowLabel);
		frameLowLabel.setForeground(Color.WHITE);
		frameLowLabel.setFont(null);
		frameLowLabel.setAlignment(Label.RIGHT);

		final TextField frameLowTextField = new TextField();
		frameLowTextField.addTextListener(new TextListener() {
			@Override
			public void textValueChanged(final TextEvent arg0) {
				if (frameLowTextField.getText().matches("^[0-9]+$")
						&& (Integer.parseInt(frameLowTextField.getText()) > 0)
						&& (Integer.parseInt(frameLowTextField.getText()) <= (StatScreen.this.frameUp))) {
					StatScreen.this.frameLow = Integer.parseInt(frameLowTextField.getText());
					StatScreen.this.refresh();
				}
			}
		});
		frameLowTextField.setBounds(289, 555, 24, 24);
		this.frame.getContentPane().add(frameLowTextField);

		final Label frameUpLabel = new Label("to");
		frameUpLabel.setForeground(Color.WHITE);
		frameUpLabel.setFont(null);
		frameUpLabel.setAlignment(Label.RIGHT);
		frameUpLabel.setBounds(303, 555, 24, 24);
		this.frame.getContentPane().add(frameUpLabel);

		final TextField frameUpTextField = new TextField();
		frameUpTextField.addTextListener(new TextListener() {
			@Override
			public void textValueChanged(final TextEvent arg0) {
				if (frameUpTextField.getText().matches("^[0-9]+$")
						&& (Integer.parseInt(frameUpTextField.getText()) >= StatScreen.this.frameLow)
						&& (Integer.parseInt(frameUpTextField.getText()) < (StatScreen.this.size))) {
					StatScreen.this.frameUp = Integer.parseInt(frameUpTextField.getText()) + 1;
					StatScreen.this.refresh();
				}
			}
		});
		frameUpTextField.setBounds(328, 555, 24, 24);
		this.frame.getContentPane().add(frameUpTextField);

		menuLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent arg0) {
				StatScreen.this.setGoToMenu(true);
			}
		});

		videoLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent arg0) {
				StatScreen.this.setGoToVideo(true);
			}
		});

		this.frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent arg0) {
				final float xlen = (float) StatScreen.this.frame.getWidth() / 930;
				final float ylen = (float) StatScreen.this.frame.getHeight() / 624;
				StatScreen.this.frameLabel.setBounds((int) (358 * xlen), (int) (555 * ylen), (int) (176 * xlen),
						(int) (24 * ylen));
				StatScreen.this.panel.setBounds((int) (50 * xlen), (int) (37 * ylen), (int) (824 * xlen),
						(int) (465 * ylen));
				menuLabel.setBounds((int) (740 * xlen), (int) (522 * ylen), (int) (134 * xlen), (int) (40 * ylen));
				rectLabel.setBounds((int) (540 * xlen), (int) (555 * ylen), (int) (102 * xlen), (int) (24 * ylen));
				rectTextField.setBounds((int) (641 * xlen), (int) (555 * ylen), (int) (24 * xlen), (int) (24 * ylen));
				frameLowLabel.setBounds((int) (190 * xlen), (int) (555 * ylen), (int) (102 * xlen), (int) (24 * ylen));
				frameLowTextField.setBounds((int) (289 * xlen), (int) (555 * ylen), (int) (24 * xlen),
						(int) (24 * ylen));
				frameUpLabel.setBounds((int) (303 * xlen), (int) (555 * ylen), (int) (24 * xlen), (int) (24 * ylen));
				frameUpTextField.setBounds((int) (328 * xlen), (int) (555 * ylen), (int) (24 * xlen),
						(int) (24 * ylen));
				videoLabel.setBounds((int) (50 * xlen), (int) (522 * ylen), (int) (134 * xlen), (int) (40 * ylen));
				nextButton.setBounds((int) (530 * xlen), (int) (522 * ylen), (int) (79 * xlen), (int) (24 * ylen));
				previousButton.setBounds((int) (279 * xlen), (int) (522 * ylen), (int) (79 * xlen), (int) (24 * ylen));
				StatScreen.this.refresh();
			}
		});
	}

	// SETTERS

	public boolean isGoToMenu() {
		return this.goToMenu;
	}

	public boolean isGoToVideo() {
		return this.goToVideo;
	}

	public void setCurrentFrame(final int currentFrame) {
		this.currentFrame = currentFrame;
	}

	public void setData(final double[][] data) {
		this.data = data;
	}

	public void setFrameSize(final Dimension dim) {
		this.frame.setSize(dim.width, dim.height);
	}

	public void setGoToMenu(final boolean goToMenu) {
		this.goToMenu = goToMenu;
	}

	public void setGoToVideo(final boolean goToVideo) {
		this.goToVideo = goToVideo;
	}

	public void setLocation(final int x, final int y) {
		this.frame.setLocation(x, y);
	}

	public void setNbPerFrame(final List<Integer> nbPerFrame) {
		this.NbPerFrame = nbPerFrame;
	}

	public void setNbPerFrameIntoData(final int Ilow, final int Iup) {
		final int range = Iup - Ilow;
		final double[][] data = new double[1][range];
		for (int i = 0; i < range; i++) {
			data[0][i] = this.NbPerFrame.get(Ilow + i);
		}
		this.setData(data);
	}

	public void setNbPerRectIntoData(int rectIndice, final int Ilow, final int Iup) {
		rectIndice -= 1;
		final int range = Iup - Ilow;
		final double[][] data = new double[1][range];
		for (int i = 0; i < range; i++) {
			for (int j = 0; j < this.NbPerFrame.get(Ilow + i); j++) {
				if ((this.rects.size() > 0) && (this.rectPeople.get(Ilow + i).size() > 0)
						&& this.center(this.rectPeople.get(Ilow + i).get(j)).inside(this.rects.get(rectIndice))) {
					data[0][i] += 1;
				}
			}
		}
		this.setData(data);
	}

	public void setRectPeoplePerFrame(final List<List<Rectangle>> rectPeoplePerFrame) {
		this.rectPeople = rectPeoplePerFrame;
	}

	public void setRectPerFrame(final List<Rect> rectPerFrame) {
		this.rects = rectPerFrame;
	}

	public void setSize(final int size) {
		if (this.frameUp > size) {
			this.frameUp = size;
		}
		this.size = size;
	}

	public void setVisible(final boolean bool) {
		this.frame.setVisible(bool);
	}

	// Méthodes

	public void refresh() {
		this.frameLabel.setText("Frame : " + this.currentFrame + "/" + (this.size - 1));
		this.panel.removeAll();
		if (this.global) {
			this.setNbPerFrameIntoData(this.frameLow, this.frameUp);
			new Histogramme("Nombre de personnes", this.panel, this.data);
		} else if (this.local) {
			this.setNbPerRectIntoData(this.selectedRect, this.frameLow, this.frameUp);
			new Histogramme("Nombre de personnes dans rectangle n°" + this.selectedRect, this.panel, this.data);
		}
	}

	public Point center(final Rectangle rectangle) {
		return new Point(rectangle.getCenterX(), rectangle.getCenterY());
	}
}
