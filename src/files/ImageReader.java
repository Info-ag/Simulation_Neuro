package files;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageReader {
	public static BufferedImage read () {
		try {
			System.out.println(new File("smiley.png").toString());
			File file = new File("C:/Users/Gilges/workspaceJava/NeuroSim/src/files/smiley.png");
			return ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
