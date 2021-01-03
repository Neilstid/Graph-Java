package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;

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
	
	/**
	 * Return the matrix of the graph
	 * @param VertexList (List) : List of vertex
	 * @param EdgeList (List) : List of edge
	 * @param isOriented (boolean)
	 * @return The matrix of the graph
	 */
	public static int[][] getMatrix(List<Vertex> VertexList, List<Edge> EdgeList, boolean isOriented){
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
	
	/**
	 * Function to get the degree of vertex
	 * @param p (Pane) : Pane where the graph is display
	 * @return Map of vertex with his degree
	 */
	public static Map<Vertex, Integer> degree(Pane p){
		Map<Vertex, Integer> degree = new HashMap<Vertex, Integer>();
		for(Vertex vertex:getListVertex(p)) {
			degree.put(vertex, vertex.linkToMe.size());
		}
		
		return degree;
	}
	
	/**
	 * Function to get the degree of vertex
	 * @param vList (List) : List of vertex
	 * @return Map of vertex with his degree
	 */
	public static Map<Vertex, Integer> degree(List<Vertex> vList){
		Map<Vertex, Integer> degree = new HashMap<Vertex, Integer>();
		for(Vertex vertex:vList) {
			degree.put(vertex, vertex.linkToMe.size());
		}
		
		return degree;
	}
	
	/**
	 * Function to get list of vertex ordered by degree
	 * @param p (Pane) : Pane where the graph is display
	 * @return Map of vertex with his degree
	 */
	public static List<Vertex> vertexOrderDegree(Pane p){
		Set<Vertex> sourceSet = sortByComparator(degree(p), false).keySet();
		List<Vertex> targetList = new ArrayList<>(sourceSet);
		return targetList;
	}
	
	/**
	 * Function to get list of vertex ordered by degree
	 * @param vList (List) : List of vertex
	 * @return Map of vertex with his degree
	 */
	public static List<Vertex> vertexOrderDegree(List<Vertex> vList){
		Set<Vertex> sourceSet = sortByComparator(degree(vList), false).keySet();
		List<Vertex> targetList = new ArrayList<>(sourceSet);
		return targetList;
	}
	
	//Function to sort hashmap with a comparator
    private static Map<Vertex, Integer> sortByComparator(Map<Vertex, Integer> unsortMap, final boolean order)
    {

        List<Entry<Vertex, Integer>> list = new LinkedList<Entry<Vertex, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<Vertex, Integer>>()
        {
            public int compare(Entry<Vertex, Integer> o1,
                    Entry<Vertex, Integer> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<Vertex, Integer> sortedMap = new LinkedHashMap<Vertex, Integer>();
        for (Entry<Vertex, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}
