package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * 
 * @author Neil Farmer
 *
 */
public class Graph {
	static Predicate<Node> GetEdge = node -> node.getClass().equals(Edge.class);
	
	/**
	 * Function to get the list of all edge in the graph
	 * @param p (pane) : Pane where the graph is display
	 * @return List of edge
	 */
	public static List<Edge> getListEdge(Pane p){
		List<Edge> EdgeList = new ArrayList<Edge>();
		List<Vertex> VertexList = Graph.getListVertex(p);
		
		for(Vertex v:VertexList) {
			for(Edge e:v.linkToMe) {
				if(!EdgeList.contains(e)) {
					EdgeList.add(e);
				}
			}
		}
		
		return EdgeList;
	}
	
	static Predicate<Node> GetVertex = node -> node.getClass().equals(Vertex.class);
	
	/**
	 * Function to get the list of all edge in the graph
	 * @param p (pane) : Pane where the graph is display
	 * @return List of vertex
	 */
	public static List<Vertex> getListVertex(Pane p){
		return (List<Vertex>)(List<?>)p.getChildren().stream().filter(GetVertex).collect(Collectors.toList());
	}
	
	/**
	 * Function to get the list of all edge in the graph
	 * @param p (pane) : Pane where the graph is display
	 * @param isOriented (boolean) : True if the graph is oriented, else False
	 * @return An 2d array representing the matrix
	 */
	public static int[][] getMatrix(Pane p, boolean isOriented){
		List<Edge> EdgeList = getListEdge(p);
		List<Vertex> VertexList = getListVertex(p);
		int sizeMatrix = VertexList.size();
		int[][] matrix = new int [sizeMatrix][sizeMatrix];
		
		for(Edge e:EdgeList) {
			matrix[VertexList.indexOf(e.getV1())][VertexList.indexOf(e.getV2())] = e.getWeight();
			if(!isOriented) {
				matrix[VertexList.indexOf(e.getV2())][VertexList.indexOf(e.getV1())] = e.getWeight();
			}
		}
		
		return matrix;
	}
}
