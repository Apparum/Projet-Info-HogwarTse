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

public class Loading {

	public static List<List<Rectangle>> charger(String nom) {
		/* Fonction qui renvoit une liste de liste de Rectangles à partir d'un fichier dont le nom
		 * est passé en argument.
		 * */

		Path fichier = Paths.get(nom+".txt");
		Charset charset = Charset.forName("US-ASCII");
		List<List<Rectangle>> video=new ArrayList<List<Rectangle>>();

		try (BufferedReader reader = Files.newBufferedReader(fichier, charset)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				//System.out.println(line);
				List<Rectangle> frame=new ArrayList<Rectangle>();
				String[] parts=line.split(" ");
				for(int i=0;i<(parts.length)-3;i=i+4) {
					int x=Integer.parseInt(parts[i]);
					int y=Integer.parseInt(parts[i+1]);
					int w=Integer.parseInt(parts[i+2]);
					int h=Integer.parseInt(parts[i+3]);
					Rectangle r=new Rectangle(x,y,w,h);
					frame.add(r);
				}
				video.add(frame);
			}
		} catch (IOException ioe) {
		}
		return video;
		
		
	}
}
