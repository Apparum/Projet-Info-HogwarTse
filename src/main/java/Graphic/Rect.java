package Graphic;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class Rect {

	private int x1;
	private int x2;
	private int y1;
	private int y2;
	private Color color;
	private boolean fill;

	List<Rect> rects;

	public Rect(int x1, int x2, int y1, int y2, Color color, boolean fill) {
		super();
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.color = color;
		this.fill = fill;
	}

	public void drawRect(Graphics g) {
		g.setColor(this.color);
		g.drawRect(this.x1, this.y1, this.x2 - this.x1, this.y2 - this.y1);
		if (this.fill) {
			g.fillRect(this.x1, this.y1, this.x2 - this.x1, this.y2 - this.y1);
		}
	}
}