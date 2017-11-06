import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class StartingScreen extends JFrame {

	// JPanel
	JPanel jpanel = new JPanel();
	// Buttons
	JButton jbutton1 = new JButton("Browse");

	private static class RectDraw extends JPanel {
		protected void paintComponent(final Graphics g) {
			super.paintComponent(g);
			g.drawRect(230, 80, 10, 10);
			g.setColor(Color.RED);
			g.fillRect(230, 80, 10, 10);
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(100, 100); // appropriate constants
		}
	}

	public StartingScreen() {
		// FlightInfo setbounds
		this.jbutton1.setBounds(1080, 600, 150, 30);
		this.jpanel.setLayout(null);
		this.jpanel.setBackground(Color.GRAY);
		this.jbutton1.setBackground(Color.WHITE);
		// Adding to JFrame
		this.jpanel.add(this.jbutton1);
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