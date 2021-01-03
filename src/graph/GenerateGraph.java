package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import GestionGraph.OrderGraph;
import application.MainInterface_Controller;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;

/**
 * Class to create a graph automatically
 * 
 * @author Neil Farmer
 *
 */
public class GenerateGraph {

	/**
	 * Function to create a new complete graph
	 * @param app (MainInterface_Controller) : Link to the main application
	 * @param numberOfVertex (int) : Number of vertex in the graph
	 * @param col (Color) : Color of the vertex
	 * @param MaxWeight (int) : the max weight of edge
	 */
	public static void CreateCompleteGraph(MainInterface_Controller app, int numberOfVertex, Color col, int MaxWeight) {
		app.Overlay.getChildren().clear();
		List<Vertex> ListVertex = new ArrayList<Vertex>();
		DoubleProperty x = new SimpleDoubleProperty(10);
		DoubleProperty y = new SimpleDoubleProperty(15);

		//Create the vertex
		for (int vertexNum = 0; vertexNum < numberOfVertex; vertexNum++) {
			Vertex v = new Vertex(x, y, app, col, name(vertexNum));
			ListVertex.add(v);
			v.draw();
		}

		//Create the edge
		Random rand = new Random();
		//For each vertex
		for (int EdgeVertex1 = 0; EdgeVertex1 < numberOfVertex - 1; EdgeVertex1++) {
			//For each vertex non linked to the vertex selected
			for (int EdgeVertex2 = EdgeVertex1 + 1; EdgeVertex2 < numberOfVertex; EdgeVertex2++) {
				//create the edge
				Edge e = new Edge(ListVertex.get(EdgeVertex1), ListVertex.get(EdgeVertex2), rand.nextInt(MaxWeight) + 1, app, 0);
				e.draw();
			}
		}
		//Put order on the graph
		OrderGraph.order(app.Overlay);
	}

	/**
	 * 
	 * @param app (MainInterface_Controller) : Link to the main application
	 * @param numberOfVertex (int) : Number of vertex
	 * @param col (Color) : Default color of the edge
	 * @param numberOfEdge (int) : Number of of edge
	 * @param MaxWeight (int) : Maximum weight of edge
	 */
	public static void CreateRandomGraph(MainInterface_Controller app, int numberOfVertex, Color col,
			int numberOfEdge, int MaxWeight) {
		app.Overlay.getChildren().clear();
		List<Vertex> ListVertex = new ArrayList<Vertex>();

		//Create vertex
		Random rand = new Random();
		for (int vertexNum = 0; vertexNum < numberOfVertex; vertexNum++) {
			Vertex v = new Vertex(new SimpleDoubleProperty(rand.nextInt((int) app.Overlay.getWidth()-50)), new SimpleDoubleProperty(rand.nextInt((int) app.Overlay.getHeight()-50)), app, col, name(vertexNum));
			ListVertex.add(v);
			v.draw();
		}

		Vertex v1 = null;
		Vertex v2 = null;
		int ActualNumOfEdge = 0;
		ArrayList<Edge> EdgeListVertex1 = new ArrayList<Edge>();
		ArrayList<Edge> EdgeListVertex2 = new ArrayList<Edge>();

		//create edge
		while (ActualNumOfEdge < numberOfEdge) {
			//Get two vertex
			v1 = ListVertex.get(rand.nextInt(numberOfVertex));
			v2 = ListVertex.get(rand.nextInt(numberOfVertex));

			//Check if vertex are different and if they don't already have edge between them
			
			int sameOriented = 0;
			int diferentOriented = 0;

			EdgeListVertex1 = new ArrayList<Edge>(v1.linkToMe);
			EdgeListVertex2 = new ArrayList<Edge>(v2.linkToMe);
			EdgeListVertex1.retainAll(EdgeListVertex2);

			for (Edge e : EdgeListVertex1) {
				if (e.getV1() == v1 && e.getV2() == v2) {
					sameOriented++;
					break;
				} else {
					diferentOriented++;
				}
			}
			
			if (v1 == v2) {
				// If the two vertex selected are the same
				continue;
			} else if (sameOriented > 0) {
				continue;
			} else if (diferentOriented > 0 && !app.isOriented) {
				continue;
			} else {
				// Create the edge
				Edge e = new Edge(v1, v2, rand.nextInt(MaxWeight) + 1, app, diferentOriented);
				e.draw();
				ActualNumOfEdge++;
			}
		}
		OrderGraph.order(app.Overlay);
	}
	
	//Function to find a name to the vertex
	private static String name(int number) {
		String to_return = String.valueOf((char) (65 + number % 26));
		if(number > 26) {
			to_return =  String.valueOf((char) (64 + number / 26)) + to_return;
		}
		return to_return;
	}
}
