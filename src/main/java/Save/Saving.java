package Save;

import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.opencv.core.Rect;

public class Saving {

	public static void main(String[] s) {
		List<List<Rectangle>> rects = new ArrayList<List<Rectangle>>();

		for (int i = 1; i < 20; i++) {
			List<Rectangle> ligne = new ArrayList<Rectangle>();
			for (int j = 1; j < i; j++) {
				Rectangle r = new Rectangle(i, j, i + j, i + 2 * j);
				ligne.add(r);
			}
			rects.add(ligne);
		}
		sauvegarder("tagada", rects);
		List<List<Rectangle>> rects2 =Loading.charger("tagada");
		System.out.print(rects2.equals(rects));
		sauvegarder("tsouing",rects2);

		
	}
	
	
	
	
	
	

	public static void sauvegarder(String nom, List<List<Rectangle>> video) {

		String line = "";
		Path fichier = Paths.get(nom+".txt");
		Charset charset = Charset.forName("US-ASCII");
		if (!Files.exists(fichier)) {
			try {
				Files.createFile(fichier);
			} catch (IOException e) {
				System.out.println("Erreur à la création du fichier");
				e.printStackTrace();
			}
		}

//		 En-tête
//		for (int i = 0; i < 10; i++) {
//			try (BufferedWriter buffer = Files.newBufferedWriter(fichier, charset)) {
//				buffer.write(line);
//			} catch (IOException ioe) {
//			}
//		}

		for (List<Rectangle> frame : video) {
			for (Rectangle rectangle : frame) {
				int x = rectangle.x;
				int y = rectangle.y;
				int w = rectangle.width;
				int h = rectangle.height;
				line += x + " " + y + " " + w + " " + h + " ";
			}
			line+="\n";
			// On écrit une ligne entière représentant une frame avant de l'écrire dans le
			// fichier.
			try (BufferedWriter buffer = Files.newBufferedWriter(fichier, charset)) {
				buffer.write(line);
			} catch (IOException ioe) {
			}

		}
	}

}
