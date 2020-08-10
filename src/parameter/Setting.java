package parameter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javafx.scene.paint.Color;

/**
 * 
 * @author Neil Farmer
 *
 */
public class Setting{

		private Color ColorVertexDefault;
		
		private Color ColorWrittenVertexName;
		
		private Color ColorEdge;
	
		/**
		 * Constructor
		 */
		public Setting() {
			Properties properties = new Properties();
			
			try {
				properties.loadFromXML(new FileInputStream("config.xml")); 
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			ColorVertexDefault = Color.valueOf(properties.getProperty("ColorVertexDefault", "Orange"));
			ColorWrittenVertexName = Color.valueOf(properties.getProperty("ColorWrittenVertexName", "Black")); 
			ColorEdge = Color.valueOf(properties.getProperty("ColorEdge", "Gray"));
		}
		
		/**
		 * Function to save a parameter
		 * @param reference  (String) : Reference to the parameter
		 * @param value (String) : The value of this parameter
		 */
		private void saveSetting(String reference, String value) {
			Properties props = new Properties();
			
			try {
				props.loadFromXML(new FileInputStream("config.xml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Properties propClone = new Properties(); 
			propClone = (Properties) props.clone();
			
			propClone.setProperty(reference, value); 

			try {
				File configFile = new File("config.xml"); 
				FileOutputStream out = new FileOutputStream(configFile);
				propClone.storeToXML(out, "Configuration");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Function to set the color of the text of a vertex
		 * @param value (Color)
		 */
		public void setColorWrittenVertexName(Color value) {
			this.ColorWrittenVertexName = value;
			saveSetting("ColorWrittenVertexName", value.toString());
		}
		
		/**
		 * 
		 * @return The default color of vertex
		 */
		public Color getColorWrittenVertexName() {
			return this.ColorWrittenVertexName;
		}

		/**
		 * Function to set the color of the text of a vertex
		 * @param value (Color)
		 */
		public void setColorVertexDefault(Color value) {
			this.ColorVertexDefault = value;
			saveSetting("ColorVertexDefault", value.toString());
		}
		
		/**
		 * 
		 * @return The default color of vertex
		 */
		public Color getColorVertexDefault() {
			return this.ColorVertexDefault;
		}
		
		/**
		 * Function to set the color of the text of a vertex
		 * @param value (Color)
		 */
		public void setColorEdge(Color value) {
			this.ColorEdge = value;
			saveSetting("ColorEdge", value.toString());
		}
		
		/**
		 * 
		 * @return The default color of vertex
		 */
		public Color getColorEdge() {
			return this.ColorEdge;
		}
}
