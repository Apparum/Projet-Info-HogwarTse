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
 * Sauvegarde d'une d�tection de personne.
 *
 */
public class Saving {
	/**
	 * Fonction pour enregistrer les rectangles dans un fichier .txt.
	 *
	 * @param nom
	 *            : nom du fichier (nom.txt)
	 * @param video
	 *            : liste de liste des rectangles d�tect�s � enregistrer.
	 */
	public static void sauvegarder(final String nom, final List<List<Rectangle>> video) {
		String line = "";
		final Path fichier = Paths.get(nom + ".txt");
		final Charset charset = Charset.forName("US-ASCII");
		if (!Files.exists(fichier)) {
			try {
				Files.createFile(fichier);
			} catch (final IOException e) {
				System.out.println("Erreur � la cr�ation du fichier");
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

			// On �crit une ligne enti�re repr�sentant une frame avant de l'�crire dans le
			// fichier.
			try (BufferedWriter buffer = Files.newBufferedWriter(fichier, charset)) {
				buffer.write(line);
			} catch (final IOException ioe) {
			}

		}
	}

}
