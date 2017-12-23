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

public class StatScreen {

	private JFrame frame;
	private boolean goToMenu;
	private boolean goToVideo;
	Label frameLabel = new Label("");
	Panel panel = new Panel();
	private int currentFrame;
	private int size;
	private List<Integer> NbPerFrame = new ArrayList<>();
	double[][] data = new double[1][this.NbPerFrame.size()];
	private List<Rect> rects = new ArrayList<>();
	private List<List<Rectangle>> rectPeople = new ArrayList<>();
	boolean global = true;
	boolean local = false;
	int selectedRect = 1;

	/**
	 * Launch the application.
	 */

	public void setVisible(boolean bool) {
		this.frame.setVisible(bool);
	}

	/**
	 * Create the application.
	 */
	public StatScreen() {
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

		this.panel.setBounds(50, 37, 824, 465);
		this.panel.setBackground(new Color(13, 31, 45));
		this.frame.getContentPane().add(this.panel);
		this.panel.setLayout(null);

		this.frameLabel.setForeground(Color.WHITE);
		this.frameLabel.setFont(null);
		this.frameLabel.setAlignment(Label.CENTER);
		this.frameLabel.setBounds(358, 555, 176, 24);
		this.frame.getContentPane().add(this.frameLabel);

		Label menuLabel = new Label("Menu");
		menuLabel.setForeground(new Color(255, 255, 255));
		menuLabel.setFont(new Font("Castellar", Font.PLAIN, 22));
		menuLabel.setBackground(new Color(89, 131, 146));
		menuLabel.setAlignment(Label.CENTER);
		menuLabel.setBounds(740, 522, 134, 40);
		this.frame.getContentPane().add(menuLabel);

		Label videoLabel = new Label("Video");
		videoLabel.setForeground(new Color(255, 255, 255));
		videoLabel.setFont(new Font("Castellar", Font.PLAIN, 22));
		videoLabel.setBackground(new Color(89, 131, 146));
		videoLabel.setAlignment(Label.CENTER);
		videoLabel.setBounds(50, 522, 134, 40);
		this.frame.getContentPane().add(videoLabel);

		Button nextButton = new Button("Local");
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StatScreen.this.local = true;
				StatScreen.this.global = false;
				StatScreen.this.refresh();
			}
		});
		nextButton.setBounds(530, 522, 79, 24);
		this.frame.getContentPane().add(nextButton);

		Button previousButton = new Button("Global");
		previousButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StatScreen.this.global = true;
				StatScreen.this.local = false;
				StatScreen.this.refresh();
			}
		});
		previousButton.setBounds(279, 522, 79, 24);
		this.frame.getContentPane().add(previousButton);

		Button playButton = new Button("TO DO");
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		playButton.setBounds(404, 522, 79, 24);
		this.frame.getContentPane().add(playButton);

		Label rectLabel = new Label("Rectangle n� : ");
		rectLabel.setForeground(Color.WHITE);
		rectLabel.setFont(null);
		rectLabel.setAlignment(Label.RIGHT);
		rectLabel.setBounds(540, 555, 102, 24);
		this.frame.getContentPane().add(rectLabel);

		TextField rectTextField = new TextField();
		rectTextField.addTextListener(new TextListener() {
			public void textValueChanged(TextEvent arg0) {
				if (rectTextField.getText().matches("^[0-9]+$") && (Integer.parseInt(rectTextField.getText()) > 0)
						&& (Integer.parseInt(rectTextField.getText()) < (StatScreen.this.rects.size() + 1))) {
					StatScreen.this.selectedRect = Integer.parseInt(rectTextField.getText());
					StatScreen.this.refresh();
				} else {
					rectTextField.setText("");
				}
			}
		});
		rectTextField.setBounds(641, 555, 24, 24);
		this.frame.getContentPane().add(rectTextField);

		menuLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				StatScreen.this.setGoToMenu(true);
			}
		});

		videoLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				StatScreen.this.setGoToVideo(true);
			}
		});

		this.frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				float xlen = (float) StatScreen.this.frame.getWidth() / 930;
				float ylen = (float) StatScreen.this.frame.getHeight() / 624;
				StatScreen.this.frameLabel.setBounds((int) (358 * xlen), (int) (555 * ylen), (int) (176 * xlen),
						(int) (24 * ylen));
				StatScreen.this.panel.setBounds((int) (50 * xlen), (int) (37 * ylen), (int) (824 * xlen),
						(int) (465 * ylen));
				menuLabel.setBounds((int) (740 * xlen), (int) (522 * ylen), (int) (134 * xlen), (int) (40 * ylen));
				rectLabel.setBounds((int) (540 * xlen), (int) (555 * ylen), (int) (102 * xlen), (int) (24 * ylen));
				rectTextField.setBounds((int) (641 * xlen), (int) (555 * ylen), (int) (24 * xlen), (int) (24 * ylen));
				videoLabel.setBounds((int) (50 * xlen), (int) (522 * ylen), (int) (134 * xlen), (int) (40 * ylen));
				nextButton.setBounds((int) (530 * xlen), (int) (522 * ylen), (int) (79 * xlen), (int) (24 * ylen));
				previousButton.setBounds((int) (279 * xlen), (int) (522 * ylen), (int) (79 * xlen), (int) (24 * ylen));
				playButton.setBounds((int) (404 * xlen), (int) (522 * ylen), (int) (79 * xlen), (int) (24 * ylen));
				StatScreen.this.refresh();
			}
		});
	}

	public double[][] getData() {
		return this.data;
	}

	public void setData(double[][] data) {
		this.data = data;
	}

	public void setNbPerFrameIntoData() {
		double[][] data = new double[1][this.NbPerFrame.size()];
		for (int i = 0; i < this.NbPerFrame.size(); i++) {
			data[0][i] = this.NbPerFrame.get(i);
		}
		this.setData(data);
	}

	public void setNbPerRectIntoData(int rectIndice) {
		rectIndice -= 1;
		double[][] data = new double[1][this.NbPerFrame.size()];
		for (int i = 0; i < this.NbPerFrame.size(); i++) {
			for (int j = 0; j < this.NbPerFrame.get(i); j++) {
				if ((this.rects.size() > 0) && (this.rectPeople.get(i).size() > 0)
						&& this.center(this.rectPeople.get(i).get(j)).inside(this.rects.get(rectIndice))) {
					data[0][i] += 1;
				}
			}
		}
		this.setData(data);
	}

	public Point center(Rectangle rectangle) {
		return new Point(rectangle.getCenterX(), rectangle.getCenterY());
	}

	public void refresh() {
		this.frameLabel.setText("Frame : " + this.currentFrame + "/" + (this.size - 1));
		this.panel.removeAll();
		if (this.global) {
			this.setNbPerFrameIntoData();
			new Histogramme("Nombre de personnes", this.panel, this.data);
		} else if (this.local) {
			this.setNbPerRectIntoData(this.selectedRect);
			new Histogramme("Nombre de personnes dans rectangle n�" + this.selectedRect, this.panel, this.data);
		}
	}

	public boolean isGoToMenu() {
		return this.goToMenu;
	}

	public void setGoToMenu(boolean goToMenu) {
		this.goToMenu = goToMenu;
	}

	public boolean isGoToVideo() {
		return this.goToVideo;
	}

	public void setGoToVideo(boolean goToVideo) {
		this.goToVideo = goToVideo;
	}

	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setNbPerFrame(List<Integer> nbPerFrame) {
		this.NbPerFrame = nbPerFrame;
	}

	public void setRectPerFrame(List<Rect> rectPerFrame) {
		this.rects = rectPerFrame;
	}

	public void setRectPeoplePerFrame(List<List<Rectangle>> rectPeoplePerFrame) {
		this.rectPeople = rectPeoplePerFrame;

	}

}
