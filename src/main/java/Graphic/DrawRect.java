package Graphic;

import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

public class DrawRect extends JPanel {

	List<Rect> rects;

	public DrawRect(List<Rect> rects) {

		this.rects = rects;

	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Rect rect : this.rects) {
			rect.drawRect(g);
		}

	}

}
