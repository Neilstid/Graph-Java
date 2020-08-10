package parameter;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;

public class SaveGraph {

	/**
	 * Function to save pane as png file
	 * @param file (File) : File where the pane as png will be saved
	 * @param p (Pane) : pane to save
	 */
	public static void saveAsPng(File file, Pane p) {
		if(file != null){
	        try {
	            WritableImage writableImage = new WritableImage((int) p.getWidth() + 20, (int) p.getHeight() + 20);
	            p.snapshot(null, writableImage);
	            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);

	            ImageIO.write(renderedImage, "png", file);
	        } catch (IOException ex) { ex.printStackTrace(); }
	    }
	}
}
