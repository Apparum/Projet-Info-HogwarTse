package Graphic;

import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileSystemView;
import java.awt.List;
import javax.swing.JPanel;

public class VideoScreen {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VideoScreen window = new VideoScreen();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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

		JLabel contentLabel = new JLabel("");
		contentLabel.setBackground(new Color(98, 104, 104));
		contentLabel.setBounds(50, 31, 824, 465);
		this.frame.getContentPane().add(contentLabel);

		final ImageIcon image = new ImageIcon("png.png");
		contentLabel.setIcon(image);
		contentLabel.repaint();

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

	}
}
