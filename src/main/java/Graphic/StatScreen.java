package Graphic;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
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

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("unused")
public class StatScreen {

	private JFrame frame;
	private boolean goToMenu;
	private boolean goToVideo;
	Label frameLabel = new Label("");
	private int currentFrame;
	private int size;

	private JLabel contentLabel = new JLabel("");

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
		this.frame.setResizable(false);
		this.frame.getContentPane().setBackground(new Color(13, 31, 45));
		this.frame.setBounds(0, 0, 930, 624);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.getContentPane().setLayout(null);
		this.frame.setLocation((dim.width / 2) - (this.frame.getSize().width / 2),
				(dim.height / 2) - (this.frame.getSize().height / 2));

		this.frameLabel.setForeground(Color.WHITE);
		this.frameLabel.setFont(null);
		this.frameLabel.setAlignment(Label.CENTER);

		this.frameLabel.setBounds(358, 555, 176, 24);
		this.frame.getContentPane().add(this.frameLabel);

		this.contentLabel.setBackground(new Color(98, 104, 104));
		this.contentLabel.setBounds(50, 31, 824, 465);
		this.frame.getContentPane().add(this.contentLabel);

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

		Button nextButton = new Button("TO DO");
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		nextButton.setBounds(530, 522, 79, 24);
		this.frame.getContentPane().add(nextButton);

		Button previousButton = new Button("TO DO");
		previousButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

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
				StatScreen.this.contentLabel.setBounds((int) (50 * xlen), (int) (31 * ylen), (int) (824 * xlen),
						(int) (465 * ylen));
				menuLabel.setBounds((int) (740 * xlen), (int) (522 * ylen), (int) (134 * xlen), (int) (40 * ylen));
				videoLabel.setBounds((int) (50 * xlen), (int) (522 * ylen), (int) (134 * xlen), (int) (40 * ylen));
				nextButton.setBounds((int) (530 * xlen), (int) (522 * ylen), (int) (79 * xlen), (int) (24 * ylen));
				previousButton.setBounds((int) (279 * xlen), (int) (522 * ylen), (int) (79 * xlen), (int) (24 * ylen));
				playButton.setBounds((int) (404 * xlen), (int) (522 * ylen), (int) (79 * xlen), (int) (24 * ylen));
			}
		});

		this.contentLabel.setIcon(new ImageIcon("png.png"));
		this.refresh();
	}

	public void refresh() {
		this.frameLabel.setText("Frame : " + this.currentFrame + "/" + (this.size - 1));
		this.contentLabel.repaint();
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

}
