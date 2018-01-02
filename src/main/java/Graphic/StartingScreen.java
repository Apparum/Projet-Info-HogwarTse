package Graphic;

import java.awt.Button;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * Ecran de d�marrage.
 *
 */
public class StartingScreen {

	private JFrame frame;
	private String path = new String("No path yet");
	private boolean fileChoosed = false;
	private boolean goToVideo;
	private boolean setupVideoReader = false;
	private boolean quit = false;
	private boolean hogVisibility = true;
	private int frameoff = 1;

	/**
	 * Constructeur de l'�cran de d�marrage.
	 */
	public StartingScreen() {
		this.initialize();
	}

	/**
	 * Initialise la fen�tre.
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

		final Button playButton = new Button("Play");
		playButton.setBackground(new Color(89, 131, 146));
		playButton.setForeground(Color.WHITE);
		playButton.setBounds(337, 372, 200, 65);
		this.frame.getContentPane().add(playButton);

		final Label HOGVisibilityLabel = new Label("Is HOG visible");
		HOGVisibilityLabel.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		HOGVisibilityLabel.setBackground(new Color(98, 104, 104));
		HOGVisibilityLabel.setAlignment(Label.RIGHT);
		HOGVisibilityLabel.setBounds(60, 398, 125, 24);
		this.frame.getContentPane().add(HOGVisibilityLabel);

		final Checkbox HOGVisibilityCheckbox = new Checkbox("");
		HOGVisibilityCheckbox.setState(true);
		HOGVisibilityCheckbox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				e.getStateChange();
				if (e.getStateChange() == 1) {
					StartingScreen.this.setHogVisible(true);
				} else {
					StartingScreen.this.setHogVisible(false);
				}
			}
		});
		HOGVisibilityCheckbox.setBackground(new Color(98, 104, 104));
		HOGVisibilityCheckbox.setBounds(191, 398, 24, 24);
		this.frame.getContentPane().add(HOGVisibilityCheckbox);

		final TextField indiceTextField = new TextField();
		indiceTextField.addTextListener(new TextListener() {
			@Override
			public void textValueChanged(final TextEvent e) {
				if (indiceTextField.getText().matches("^[0-9]+$")) {
					StartingScreen.this.setFrameoff(Integer.parseInt(indiceTextField.getText()));
				}
			}
		});
		indiceTextField.setBackground(new Color(98, 104, 104));
		indiceTextField.setText("1");
		indiceTextField.setBounds(191, 372, 24, 24);
		this.frame.getContentPane().add(indiceTextField);

		final Label indiceLabel = new Label("Take 1 frame out of");
		indiceLabel.setBackground(new Color(98, 104, 104));
		indiceLabel.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		indiceLabel.setAlignment(Label.RIGHT);
		indiceLabel.setBounds(60, 369, 125, 24);
		this.frame.getContentPane().add(indiceLabel);

		final Label contentLabel = new Label("Welcome on the Hogwar\'Tse software for human detection");
		contentLabel.setFont(new Font("Dialog", Font.PLAIN, 30));
		contentLabel.setBackground(new Color(98, 104, 104));
		contentLabel.setForeground(Color.WHITE);
		contentLabel.setAlignment(Label.CENTER);
		contentLabel.setBounds(50, 95, 824, 275);
		this.frame.getContentPane().add(contentLabel);

		final Canvas contentRect = new Canvas();
		contentRect.setBackground(new Color(98, 104, 104));
		this.frame.getContentPane().add(contentRect);

		final Label titleLabel = new Label("Hogwar'TSE");
		titleLabel.setFont(new Font("Castellar", Font.PLAIN, 27));
		titleLabel.setAlignment(Label.CENTER);
		titleLabel.setBackground(new Color(98, 104, 104));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setBounds(52, 29, 204, 40);
		this.frame.getContentPane().add(titleLabel);

		final Canvas titleRect = new Canvas();
		titleRect.setBackground(new Color(0, 0, 0));
		titleRect.setBounds(50, 27, 208, 44);
		this.frame.getContentPane().add(titleRect);

		final Label quitLabel = new Label("Quit");
		quitLabel.setForeground(Color.WHITE);
		quitLabel.setFont(new Font("Castellar", Font.PLAIN, 27));
		quitLabel.setBackground(new Color(98, 104, 104));
		quitLabel.setAlignment(Label.CENTER);
		quitLabel.setBounds(668, 29, 204, 40);
		this.frame.getContentPane().add(quitLabel);

		final Canvas quitRect = new Canvas();
		quitRect.setBackground(new Color(0, 0, 0));
		quitRect.setBounds(666, 27, 208, 44);
		this.frame.getContentPane().add(quitRect);

		final Button browserButton = new Button("Browse File");
		browserButton.setForeground(Color.WHITE);
		browserButton.setBackground(new Color(89, 131, 146));
		browserButton.setBounds(729, 526, 145, 38);
		this.frame.getContentPane().add(browserButton);

		final Label browserLabel = new Label("");
		browserLabel.setText(this.path);
		browserLabel.setForeground(new Color(255, 255, 255));
		browserLabel.setBounds(53, 529, 683, 32);
		this.frame.getContentPane().add(browserLabel);

		final Canvas browserRectBorder = new Canvas();
		browserRectBorder.setBackground(new Color(89, 131, 146));
		browserRectBorder.setBounds(50, 526, 686, 38);
		this.frame.getContentPane().add(browserRectBorder);

		browserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				final int returnValue = jfc.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					StartingScreen.this.path = jfc.getSelectedFile().getAbsolutePath();
					browserLabel.setText(StartingScreen.this.path);
					StartingScreen.this.fileChoosed = true;
					StartingScreen.this.setupVideoReader = true;
					browserLabel.setBackground(null);
				}
			}
		});

		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				if (StartingScreen.this.fileChoosed == false) {
					browserLabel.setBackground(Color.RED);
				} else {
					StartingScreen.this.setGoToVideo(true);
				}
			}
		});

		quitLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent arg0) {
				StartingScreen.this.setQuit(true);
				StartingScreen.this.frame.dispose();
			}
		});

		this.frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent arg0) {
				final float xlen = (float) StartingScreen.this.frame.getWidth() / 930;
				final float ylen = (float) StartingScreen.this.frame.getHeight() / 624;
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
				HOGVisibilityLabel.setBounds((int) (60 * xlen), (int) (398 * ylen), (int) (125 * xlen),
						(int) (24 * ylen));
				HOGVisibilityCheckbox.setBounds((int) (191 * xlen), (int) (398 * ylen), (int) (24 * xlen),
						(int) (24 * ylen));
			}
		});
	}

	// Getters

	public int getFrameoff() {
		return this.frameoff;
	}

	public Dimension getFrameSize() {
		return this.frame.getSize();
	}

	public String getPath() {
		return this.path;
	}

	public int getX() {
		return this.frame.getX();
	}

	public int getY() {
		return this.frame.getY();
	}

	public boolean isGoToVideo() {
		return this.goToVideo;
	}

	public boolean isHogVisible() {
		return this.hogVisibility;
	}

	public boolean isQuit() {
		return this.quit;
	}

	public boolean isSetupVideoReader() {
		return this.setupVideoReader;
	}

	// Setters

	public void setFrameoff(final int frameoff) {
		this.frameoff = frameoff;
	}

	public void setFrameSize(final Dimension dim) {
		this.frame.setSize(dim.width, dim.height);
	}

	public void setGoToVideo(final boolean goToVideo) {
		this.goToVideo = goToVideo;
	}

	public void setHogVisible(final boolean b) {
		this.hogVisibility = b;
	}

	public void setQuit(final boolean quit) {
		this.quit = quit;
	}

	public void setSetupVideoReader(final boolean setupVideoReader) {
		this.setupVideoReader = setupVideoReader;
	}

	public void setVisible(final boolean bool) {
		this.frame.setVisible(bool);
	}
}
