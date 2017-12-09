package Graphic;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import org.opencv.videoio.VideoCapture;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.awt.Button;
import javax.swing.JLabel;
import java.awt.Label;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Graphic.VideoScreen;
import java.awt.TextArea;

@SuppressWarnings("unused")
public class StartingScreen {

	private JFrame frame;
	private String path = new String("No path yet");
	boolean fileChoosed = false;
	private boolean goToVideo;
	private boolean setupVideoReader = false;

	/**
	 * Launch the application.
	 */

	public void setVisible(boolean bool) {
		this.frame.setVisible(bool);
	}

	/**
	 * Create the application.
	 */
	public StartingScreen() {
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

		Button playButton = new Button("Play");
		playButton.setBackground(new Color(89, 131, 146));
		playButton.setForeground(Color.WHITE);
		playButton.setBounds(337, 372, 200, 65);
		this.frame.getContentPane().add(playButton);

		Label contentLabel = new Label("Welcome on the Hogwar\'Tse software for human detection");
		contentLabel.setFont(new Font("Dialog", Font.PLAIN, 30));
		contentLabel.setBackground(new Color(98, 104, 104));
		contentLabel.setForeground(Color.WHITE);
		contentLabel.setAlignment(Label.CENTER);
		contentLabel.setBounds(50, 95, 824, 275);
		this.frame.getContentPane().add(contentLabel);

		Canvas contentRect = new Canvas();
		contentRect.setBackground(new Color(98, 104, 104));
		contentRect.setBounds(50, 95, 824, 401);
		this.frame.getContentPane().add(contentRect);

		Label titleLabel = new Label("Hogwar'TSE");
		titleLabel.setFont(new Font("Castellar", Font.PLAIN, 27));
		titleLabel.setAlignment(Label.CENTER);
		titleLabel.setBackground(new Color(98, 104, 104));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setBounds(52, 29, 204, 40);
		this.frame.getContentPane().add(titleLabel);

		Canvas titleRect = new Canvas();
		titleRect.setBackground(new Color(0, 0, 0));
		titleRect.setBounds(50, 27, 208, 44);
		this.frame.getContentPane().add(titleRect);

		Label quitLabel = new Label("Quit");
		quitLabel.setForeground(Color.WHITE);
		quitLabel.setFont(new Font("Castellar", Font.PLAIN, 27));
		quitLabel.setBackground(new Color(98, 104, 104));
		quitLabel.setAlignment(Label.CENTER);
		quitLabel.setBounds(668, 29, 204, 40);
		this.frame.getContentPane().add(quitLabel);

		Canvas quitRect = new Canvas();
		quitRect.setBackground(new Color(0, 0, 0));
		quitRect.setBounds(666, 27, 208, 44);
		this.frame.getContentPane().add(quitRect);

		Button browserButton = new Button("Browse File");
		browserButton.setForeground(Color.WHITE);
		browserButton.setBackground(new Color(89, 131, 146));
		browserButton.setBounds(729, 526, 145, 38);
		this.frame.getContentPane().add(browserButton);

		Label browserLabel = new Label("");
		browserLabel.setText(this.path);
		browserLabel.setForeground(new Color(255, 255, 255));
		browserLabel.setBounds(53, 529, 683, 32);
		this.frame.getContentPane().add(browserLabel);

		Canvas browserRectBorder = new Canvas();
		browserRectBorder.setBackground(new Color(89, 131, 146));
		browserRectBorder.setBounds(50, 526, 686, 38);
		this.frame.getContentPane().add(browserRectBorder);

		browserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				final int returnValue = jfc.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					StartingScreen.this.path = jfc.getSelectedFile().getAbsolutePath();
					browserLabel.setText(StartingScreen.this.path);
					StartingScreen.this.fileChoosed = true;
					StartingScreen.this.setupVideoReader = true;
				}
			}
		});

		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (StartingScreen.this.fileChoosed == false) {
					// WARNING NEED TO CHOOSE A FILE FIRST
					System.out.println("no file");
				} else {
					StartingScreen.this.setGoToVideo(true);
				}
			}
		});

		quitLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				StartingScreen.this.frame.dispose();
			}
		});
	}

	public boolean isGoToVideo() {
		return this.goToVideo;
	}

	public void setGoToVideo(boolean goToVideo) {
		this.goToVideo = goToVideo;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isSetupVideoReader() {
		return this.setupVideoReader;
	}

	public void setSetupVideoReader(boolean setupVideoReader) {
		this.setupVideoReader = setupVideoReader;
	}
}
