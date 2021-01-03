package parameter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import application.MainInterface_Controller;
import graph.Edge;
import graph.Vertex;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;

import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class LoadFile {
	public static void LoadXML(File f, MainInterface_Controller app) {
		try {
			boolean isOriented = false;
			
			//Import file and parse it
			File inputFile = f;
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			// Check graph properties
			Element root = doc.getDocumentElement();
			if (root.getAttribute("oriented").equals("true")) {
				isOriented = true;
				app.TypeGraph.setValue("Oriented graph");
			}else {
				app.TypeGraph.setValue("Non-oriented graph");
			}

			//Creation of vertex
			NodeList VertexList = doc.getElementsByTagName("Vertex");
			Map<String, Vertex> vertexByName = new HashMap<String, Vertex>();
			for (int vertexNum = 0; vertexNum < VertexList.getLength(); vertexNum++) {
				Node nNode = VertexList.item(vertexNum);
				Element eElement = (Element) nNode;
				Vertex v = new Vertex(new SimpleDoubleProperty(Integer.valueOf(eElement.getAttribute("x"))), new SimpleDoubleProperty(Integer.valueOf(eElement.getAttribute("y"))), app, Color.web(eElement.getAttribute("color")), eElement.getAttribute("name"));
				v.draw();
				vertexByName.put(eElement.getAttribute("name"), v);
			}
			
			//Creation of edge
			NodeList EdgeList = doc.getElementsByTagName("Edge");
			for (int EdgeNum = 0; EdgeNum < EdgeList.getLength(); EdgeNum++) {
				Node nNode = EdgeList.item(EdgeNum);
				Element eElement = (Element) nNode;
				Edge e = new Edge(vertexByName.get(eElement.getAttribute("Vertex1")), vertexByName.get(eElement.getAttribute("Vertex2")), Integer.valueOf(eElement.getAttribute("weight")), app, Integer.valueOf(eElement.getAttribute("type")));
				e.draw();
				e.setOriented(isOriented);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
