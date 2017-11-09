import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Graphic.DrawRect;
import Graphic.Rect;

public class StartingScreen extends JFrame {

	// JPanel
	JPanel jpanel = new JPanel();
	// Buttons
	JButton jbutton1 = new JButton("Browse");

	public StartingScreen() {

		List<Rect> rects = new ArrayList<>();

		// background
		rects.add(new Rect(0, 1280, 0, 720, new Color(13, 31, 45), true));
		// content
		rects.add(new Rect(100, 1180, 150, 520, new Color(98, 104, 104), true));
		// title
		rects.add(new Rect(100, 300, 45, 105, new Color(98, 104, 104), true));
		// browserRect
		rects.add(new Rect(100, 1030, 595, 625, new Color(89, 131, 146), false));
		// quit
		rects.add(new Rect(1080, 1180, 45, 105, new Color(98, 104, 104), true));

		DrawRect design = new DrawRect(rects);

		this.jbutton1.setBounds(1030, 595, 150, 30);
		this.jbutton1.setBackground(new Color(89, 131, 146));

		this.jpanel.setLayout(new BorderLayout());

		// Adding to JFrame
		this.jpanel.add(this.jbutton1);
		this.jpanel.add(design);
		this.add(this.jpanel);

		// JFrame properties
		this.setSize(1280, 720);
		this.setTitle("Projet Info");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public static void main(final String[] args) {
		new StartingScreen();
	}
}