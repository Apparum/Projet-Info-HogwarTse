import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class ZoomMolette extends JComponent implements MouseWheelListener {

	private static final long serialVersionUID = 1L;
	public int width, height;
	public BufferedImage img;
	public float zoom = 1f;

	public ZoomMolette(final int width, final int height, final InputStream is) {
		this.width = width;
		this.height = height;
		this.setPreferredSize(new Dimension(width, height));
		try {
			this.img = ImageIO.read(is);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		this.addMouseWheelListener(this);
	}

	@Override
	protected void paintComponent(final Graphics gd) {
		final Graphics2D g = (Graphics2D) gd;
		final float z = this.zoom * this.zoom;
		final AffineTransform t = new AffineTransform();
		final float currentImgWidth = this.img.getWidth() * z, currentImgHeight = this.img.getHeight() * z;
		t.translate((this.width / 2) - (currentImgWidth / 2), (this.height / 2) - (currentImgHeight / 2));
		t.scale(z, z);
		g.drawImage(this.img, t, null);
		g.dispose();
	}

	public void mouseWheelMoved(final MouseWheelEvent e) {
		this.zoom = Math.max(0, this.zoom - (0.03f * e.getWheelRotation()));
		this.repaint();
	}

	public static void main(final String[] args) {
		final ZoomMolette m = new ZoomMolette(640, 480, ZoomMolette.class.getResourceAsStream("png.png"));
		final JFrame f = new JFrame();
		f.setResizable(false);
		f.add(m);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}