package Graphic;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import Detection.HOGDetection;
import Video.VideoReader;

public class VideoScreen {

	private JFrame frame;
	private String path;
	private HOGDetection hog1;
	private int currentFrame = 0;
	private VideoReader video;
	private JLabel contentLabel = new JLabel("");
	private boolean play = false;

	public boolean isPlay() {
		return this.play;
	}

	public void setPlay(boolean play) {
		this.play = play;
	}

	Label frameLabel = new Label("");
	private boolean goToStat;
	private boolean goToMenu;

	// Deplacer le main dans un global à la fin !!!

	public void setup() {
		this.video = new VideoReader(this.getPath());
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

		this.frame = new JFrame();
		this.frame.setResizable(false);
		this.frame.getContentPane().setBackground(new Color(13, 31, 45));
		this.frame.setBounds(100, 100, 930, 624);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.getContentPane().setLayout(null);

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
	}

	public void refresh() {
		final ImageIcon image = new ImageIcon(this.video.getImage(this.currentFrame).getImage()
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
		return video;
	}

	public void setVideo(VideoReader video) {
		this.video = video;
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
}
