package Save;

import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * Sauvegarde d'une détection de personne.
 *
 */
public class Saving {
	/**
	 * Fonction pour enregistrer les rectangles dans un fichier .txt.
	 *
	 * @param nom
	 *            : nom du fichier (nom.txt)
	 * @param video
	 *            : liste de liste des rectangles détectés à enregistrer.
	 */
	public static void sauvegarder(final String nom, final List<List<Rectangle>> video) {
		String line = "";
		final Path fichier = Paths.get("Saves/" + nom + ".txt");
		final Charset charset = Charset.forName("US-ASCII");
		if (!Files.exists(fichier)) {
			try {
				Files.createFile(fichier);
			} catch (final IOException e) {
				System.out.println("Erreur à la création du fichier");
				e.printStackTrace();
			}
		}

		for (final List<Rectangle> frame : video) {
			for (final Rectangle rectangle : frame) {
				final int x = rectangle.x;
				final int y = rectangle.y;
				final int w = rectangle.width;
				final int h = rectangle.height;
				line += x + " " + y + " " + w + " " + h + " ";
			}
			line += "\n";
		}
		try (BufferedWriter buffer = Files.newBufferedWriter(fichier, charset)) {
			buffer.write(line);
		} catch (final IOException ioe) {
		}
	}

	/**
	 * 
	 * Cette fonction crée un fichier texte donnant la position de chaque personne sur chaque image.
	 * 
	 * @param nomVideo : nom du fichier (nom.txt)
	 * @param rects : liste de liste des rectangles détectés.
	 * @param listLabel : liste de liste des labels des rectangles détectés.
	 */
	public static void infoText(String nomVideo, List<List<Rectangle>> rects, List<List<Integer>> listLabel) {
		String line = "";
		final Path fichier = Paths.get("Saves/StatsOf_" + nomVideo + ".txt");
		final Charset charset = Charset.forName("US-ASCII");
		if (!Files.exists(fichier)) {
			try {
				Files.createFile(fichier);
			} catch (final IOException e) {
				System.out.println("Erreur à la création du fichier");
				e.printStackTrace();
			}
		}
		int i = 0;
		for (final List<Rectangle> frame : rects) {
			i++;
			line += "Frame number " + (i-1) +"\n";
			int j = 0;
			for (final Rectangle rectangle : frame) {
				j++;
				final int x = rectangle.x;
				final int y = rectangle.y;
				final int w = rectangle.width;
				final int h = rectangle.height;
				line += "\tPerson number " + listLabel.get(i-1).get(j-1) + " detected at ("+ (x + 0.5*w)+"," + (y + 0.5*h) +")";
			line += "\n";
			}
		}
		try (BufferedWriter buffer = Files.newBufferedWriter(fichier, charset)) {
			buffer.write(line);
		} catch (final IOException ioe) {
		}
	}
}
