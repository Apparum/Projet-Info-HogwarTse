package Graphic;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

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
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		this.frame = new JFrame();
		this.frame.setResizable(true);
		this.frame.getContentPane().setBackground(new Color(13, 31, 45));
		this.frame.setBounds(0, 0, 930, 624);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.getContentPane().setLayout(null);
		this.frame.setLocation((dim.width / 2) - (this.frame.getSize().width / 2),
				(dim.height / 2) - (this.frame.getSize().height / 2));

		Button playButton = new Button("Play");
		playButton.setBackground(new Color(89, 131, 146));
		playButton.setForeground(Color.WHITE);
		playButton.setBounds(337, 372, 200, 65);
		this.frame.getContentPane().add(playButton);

		TextField indiceTextField = new TextField();
		indiceTextField.setBackground(new Color(98, 104, 104));
		indiceTextField.setText("1");
		indiceTextField.setBounds(191, 372, 24, 24);
		this.frame.getContentPane().add(indiceTextField);

		Label indiceLabel = new Label("Take 1 frame out of");
		indiceLabel.setBackground(new Color(98, 104, 104));
		indiceLabel.setAlignment(Label.RIGHT);
		indiceLabel.setBounds(60, 369, 125, 24);
		this.frame.getContentPane().add(indiceLabel);

		Label contentLabel = new Label("Welcome on the Hogwar\'Tse software for human detection");
		contentLabel.setFont(new Font("Dialog", Font.PLAIN, 30));
		contentLabel.setBackground(new Color(98, 104, 104));
		contentLabel.setForeground(Color.WHITE);
		contentLabel.setAlignment(Label.CENTER);
		contentLabel.setBounds(50, 95, 824, 275);
		this.frame.getContentPane().add(contentLabel);

		Canvas contentRect = new Canvas();
		contentRect.setBackground(new Color(98, 104, 104));
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

		this.frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				float xlen = (float) StartingScreen.this.frame.getWidth() / 930;
				float ylen = (float) StartingScreen.this.frame.getHeight() / 624;
				playButton.setBounds((int) (337 * xlen), (int) (372 * ylen), (int) (200 * xlen), (int) (65 * ylen));
				indiceTextField.setBounds((int) (191 * xlen), (int) (372 * ylen), (int) (24 * xlen), (int) (24 * ylen));
				indiceLabel.setBounds((int) (60 * xlen), (int) (369 * ylen), (int) (125 * xlen), (int) (24 * ylen));
				contentLabel.setBounds((int) (50 * xlen), (int) (95 * ylen), (int) (824 * xlen), (int) (275 * ylen));
				contentRect.setBounds((int) (50 * xlen), (int) (95 * ylen), (int) (824 * xlen), (int) (401 * ylen));
				titleLabel.setBounds((int) (52 * xlen), (int) (29 * ylen), (int) (204 * xlen), (int) (40 * ylen));
				titleRect.setBounds((int) (50 * xlen), (int) (27 * ylen), (int) (208 * xlen), (int) (44 * ylen));
				quitLabel.setBounds((int) (668 * xlen), (int) (29 * ylen), (int) (204 * xlen), (int) (40 * ylen));
				quitRect.setBounds((int) (666 * xlen), (int) (27 * ylen), (int) (208 * xlen), (int) (44 * ylen));
				browserButton.setBounds((int) (729 * xlen), (int) (526 * ylen), (int) (145 * xlen), (int) (38 * ylen));
				browserLabel.setBounds((int) (53 * xlen), (int) (529 * ylen), (int) (683 * xlen), (int) (32 * ylen));
				browserRectBorder.setBounds((int) (50 * xlen), (int) (526 * ylen), (int) (686 * xlen),
						(int) (38 * ylen));
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
