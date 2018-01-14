package Save;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Chargement d'une détection de personne.
 *
 */
public class Loading {

	/**
	 * Fonction qui renvoit une liste de liste de Rectangles à partir d'un fichier.
	 *
	 * @param nom
	 *            : Nom du fichier.
	 * @return La liste des rectangle de détection.
	 */
	public static List<List<Rectangle>> charger(final String nom, final String methode) {
		final Path fichier;
		switch(methode){
		case "Kalman":
			fichier = Paths.get("K-" + nom + ".txt");
			break;
		case "HOG":
			fichier = Paths.get("H-" + nom + ".txt");
			break;
		default:
			fichier = Paths.get(nom + ".txt");
		}
		final Charset charset = Charset.forName("US-ASCII");
		final List<List<Rectangle>> video = new ArrayList<>();

		try (BufferedReader reader = Files.newBufferedReader(fichier, charset)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				final List<Rectangle> frame = new ArrayList<>();
				final String[] parts = line.split(" ");
				for (int i = 0; i < ((parts.length) - 3); i = i + 4) {
					final int x = Integer.parseInt(parts[i]);
					final int y = Integer.parseInt(parts[i + 1]);
					final int w = Integer.parseInt(parts[i + 2]);
					final int h = Integer.parseInt(parts[i + 3]);
					final Rectangle r = new Rectangle(x, y, w, h);
					frame.add(r);
				}
				video.add(frame);
			}
		} catch (final IOException ioe) {
		}
		return video;

	}
}
