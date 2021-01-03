package parameter;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import graph.Edge;
import graph.Graph;
import graph.Vertex;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class SaveGraph {

	/**
	 * Function to save pane as png file
	 * 
	 * @param file (File) : File where the pane as png will be saved
	 * @param p    (Pane) : pane to save
	 */
	public static void saveAsPng(File file, Pane p) {
		if (file != null) {
			try {
				WritableImage writableImage = new WritableImage((int) p.getWidth() + 20, (int) p.getHeight() + 20);
				p.snapshot(null, writableImage);
				RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);

				ImageIO.write(renderedImage, "png", file);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Function to get the graph in SVG
	 * @param p (Pane) : Pane where the graph is display
	 * @param isOriented (boolean)
	 * @return The SVG Code of the graph
	 */
	private static String saveAsSVG(Pane p, boolean isOriented) {
		String SVGGraph = "";
		
		//Vertex in SVG
		for (Vertex v : Graph.getListVertex(p)) {
			SVGGraph += "<circle id=\"" + String.valueOf(v.getName()) + "\" cx=\"" + String.valueOf(v.getCenterX())
					+ "\" cy=\"" + String.valueOf(v.getCenterY()) + "\" r=\"" + String.valueOf(v.getRadius())
					+ "\" fill=\"" + String.valueOf(toRGBCode(v.getColor())) + "\" />\n";
			SVGGraph += "<text id=\"" + String.valueOf(v.getName()) + "\" x=\"" + String.valueOf(v.get_Name_Object().getX())
					+ "\" y=\"" + String.valueOf(v.get_Name_Object().getY()) + "\" fill = \""
					+ String.valueOf(v.get_Name_Object().getFill()) + "\" font-size=\""
					+ String.valueOf(v.get_Name_Object().getFont().getSize()) + "px\">" + String.valueOf(v.getName())
					+ "</text>\n";
		}
		
		//Edge in SVG
		if(!isOriented) {
			for(Edge e:Graph.getListEdge(p)) {
				SVGGraph += "<line id = \"" + "n-" + String.valueOf(e.getV1().getName()) + "-" + String.valueOf(e.getV2().getName()) + "\" x1=\"" + String.valueOf(e.GetStartX()) + "\" y1=\"" + String.valueOf(e.GetStartY()) + "\" x2=\"" + String.valueOf(e.GetEndX()) + "\" y2=\"" + String.valueOf(e.GetEndY()) + "\" style=\"stroke:" + String.valueOf(toRGBCode(e.getColor())) + ";stroke-width:" + String.valueOf(e.line.getStrokeWidth()) + "\" />\n";
				SVGGraph += "<text id = \"" + "n-" + String.valueOf(e.getV1().getName()) + "-" + String.valueOf(e.getV2().getName()) + "\" x=\"" + String.valueOf(e.getTextObject().getLayoutX()) + "\" y=\"" + String.valueOf(e.getTextObject().getLayoutY()) + "\" fill = \" BLACK \" + font-size=\"" + String.valueOf(e.getTextObject().getFont().getSize()) + "px\">" + String.valueOf(e.getWeight()) + "</text>\n";
			}
		}else {
			String arrowMarker = "<defs>\n";
			List<String> colorOfEdge = new ArrayList<String>();
			
			for(Edge e:Graph.getListEdge(p)) {
				//Arrow in end of the edge
				if(!colorOfEdge.contains(toRGBCode(e.getColor()))) {
					colorOfEdge.add(toRGBCode(e.getColor()));
					arrowMarker += "<marker id=\"arrow" + toRGBCode(e.getColor()) + "\" markerWidth=\"5\" markerHeight=\"5\" refX=\"4\" refY=\"1.5\" orient=\"auto\" markerUnits=\"strokeWidth\"><path d=\"M0,0 L0,3 L5,1 z\" fill=\""+ toRGBCode(e.getColor()) +"\" stroke = \""+ toRGBCode(e.getColor()) +"\"/></marker>\n";
				}
				
				if(e.typeOfLine == 0) { //Normal line
					SVGGraph += "<line id = \"" + "o-" + String.valueOf(e.getV1().getName()) + "-" + String.valueOf(e.getV2().getName()) + "\" marker-end=\"url(#arrow" + toRGBCode(e.getColor()) + ")\" x1=\"" + String.valueOf(e.GetStartX()) + "\" y1=\"" + String.valueOf(e.GetStartY()) + "\" x2=\"" + String.valueOf(e.GetEndX()) + "\" y2=\"" + String.valueOf(e.GetEndY()) + "\" style=\"stroke:" + String.valueOf(toRGBCode(e.getColor())) + ";stroke-width:" + String.valueOf(e.line.getStrokeWidth()) + "\" />\n";
					SVGGraph += "<text id = \"" + "o-" + String.valueOf(e.getV1().getName()) + "-" + String.valueOf(e.getV2().getName()) + "\" x=\"" + String.valueOf(e.getTextObject().getLayoutX()) + "\" y=\"" + String.valueOf(e.getTextObject().getLayoutY()) + "\" fill = \" BLACK \" + font-size=\"" + String.valueOf(e.getTextObject().getFont().getSize()) + "px\">" + String.valueOf(e.getWeight()) + "</text>\n";
				}else { //Curve line
					SVGGraph += "<path id = \"" + "o-" + String.valueOf(e.getV1().getName()) + "-" + String.valueOf(e.getV2().getName()) + "\" marker-end=\"url(#arrow" + toRGBCode(e.getColor()) + ")\" stroke-width=\"" + String.valueOf(e.line.getStrokeWidth()) + "\" fill=\"none\" stroke=\"" + String.valueOf(toRGBCode(e.getColor())) + "\" d=\"M " + String.valueOf(e.GetStartX()) + " " + String.valueOf(e.GetStartY()) + "C" + String.valueOf(e.getControlX1()) + " " + String.valueOf(e.getControlY1()) + " " +  String.valueOf(e.getControlX2()) + " " + String.valueOf(e.getControlY2()) + " " + String.valueOf(e.GetEndX()) + " " + String.valueOf(e.GetEndY()) + "\"/>\n";
					SVGGraph += "<text id = \"" + "o-" + String.valueOf(e.getV1().getName()) + "-" + String.valueOf(e.getV2().getName()) + "\" x=\"" + String.valueOf(e.getTextObject().getLayoutX() + e.getTextObject().getTranslateX()) + "\" y=\"" + String.valueOf(e.getTextObject().getLayoutY() + e.getTextObject().getTranslateY()) + "\" fill = \" BLACK \" + font-size=\"" + String.valueOf(e.getTextObject().getFont().getSize()) + "px\">" + String.valueOf(e.getWeight()) + "</text>\n";
				}
			}
			arrowMarker += "</defs>\n";
			SVGGraph = arrowMarker + SVGGraph;
		}		
		return SVGGraph;
	}
	
	/**
	 * Funtion to save as html fil
	 * @param file (File): file where the code will be stored
	 * @param p (Pane) : Pane where the graph is display
	 * @param isOriented (boolean)
	 */
	public static void SaveAsHTML(File file, Pane p, boolean isOriented) {
		String html = "<!DOCTYPE html> <html> <body> <h1></ h1> <svg width = \""+ p.getWidth() +"\" height = \""+ p.getHeight() +"\">\n";
		html += saveAsSVG(p, isOriented);
		html += "</svg> </body> </html>";
		
	    try {
	        FileWriter myWriter = new FileWriter(file);
	        myWriter.write(html);
	        myWriter.close();
	        System.out.println("Successfully wrote to the file.");
	      } catch (IOException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	      }
	}

	public static String toRGBCode(Color color) {
		return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
				(int) (color.getBlue() * 255));
	}
	
	public static void SaveAsXML(File file, Pane p, boolean isOriented) {
		String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		xmlString += "<graph oriented=\"" + String.valueOf(isOriented) + "\" height=\"" + String.valueOf(p.getHeight()) + "\" width=\"" + String.valueOf(p.getWidth()) + "\">\n";
		
		for (Vertex v : Graph.getListVertex(p)) {
			xmlString += "	<Vertex name=\"" + v.getName() + "\" x=\"" + String.valueOf((int) v.getX().doubleValue()) + "\" y=\"" + String.valueOf((int) v.getY().doubleValue()) + "\" color=\"" + toRGBCode(v.getColor()) + "\"/>\n";
		}
		
		for(Edge e:Graph.getListEdge(p)) {
			xmlString += "	<Edge weight=\"" + String.valueOf((int) e.getWeight()) + "\" Vertex1=\"" + String.valueOf(e.getV1().getName()) + "\" Vertex2=\"" + String.valueOf(e.getV2().getName()) + "\" color=\"" + toRGBCode(e.getColor()) + "\" type=\"" + String.valueOf(e.typeOfLine) + "\"/>\n";
		}
		
		xmlString += "</graph>";
		
	    try {
	        FileWriter myWriter = new FileWriter(file);
	        myWriter.write(xmlString);
	        myWriter.close();
	        System.out.println("Successfully wrote to the file.");
	      } catch (IOException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	      }
	}
}
